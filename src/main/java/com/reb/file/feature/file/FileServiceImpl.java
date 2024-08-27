package com.reb.file.feature.file;

import com.reb.file.comon.audit.Audit;
import com.reb.file.comon.dto.ResponseMessage;
import com.reb.file.comon.minio.MinioService;
import io.minio.StatObjectResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private MinioService minioService;

    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public ResponseMessage save(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String uniqueFilename = uuid + "_" + file.getOriginalFilename();

        String currentDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        // Append date to the file path
        String path = currentDate + "/" + uniqueFilename;
        //Path path = Path.of(datedPath);
        try {
            minioService.uploadFile(path, file.getInputStream(), file.getContentType());

            // Check if the file was uploaded successfully
            try {
                minioService.getMetadata(path);
            } catch (Exception e) {
                return new ResponseMessage(0, "Failed to upload file to Minio: " + e.getMessage());
            }

            File fileEntity = new File();
            fileEntity.setName(file.getOriginalFilename());
            fileEntity.setSize(file.getSize());
            fileEntity.setPath(path);
            fileEntity.setType(file.getContentType());
            fileEntity.setUniqueFilename(uniqueFilename);
            fileEntity.setUuid(uuid);
            fileEntity.setBucket(bucket);
            fileRepository.save(fileEntity);

            ResponseMessage res = new ResponseMessage(1, "Save file success");
            FileUploadResponseDto dto = new FileUploadResponseDto();
            dto.setName(file.getOriginalFilename());
            dto.setSize(file.getSize());
            dto.setType(file.getContentType());
            dto.setUuid(uuid);
            dto.setId(fileEntity.getId());

            res.setData(dto);
            return res;
        }  catch (Exception e) {
            e.printStackTrace();
            return new ResponseMessage(0, "Failed to save file");
        }


    }

    @Override
    public ResponseMessage getFile(ObjectId id, String uuid) {
        File file = fileRepository.findActiveByIdAndUuid(id, uuid).orElse(null);
        if(Objects.nonNull(file)){
            try {
                String path = file.getPath();
                String bucket = file.getBucket();

                try {
                    StatObjectResponse metadata =  minioService.getMetadata(path, bucket);
                    InputStream inputStream = minioService.get(path, bucket);
                    InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
                    ResponseMessage res = new ResponseMessage(1, "File found");
                    FileDownloadResponseDto fileDownloadResponseDto = new FileDownloadResponseDto();
                    fileDownloadResponseDto.setName(file.getName());
                    fileDownloadResponseDto.setSize(file.getSize());
                    fileDownloadResponseDto.setStream(inputStreamResource);
                    fileDownloadResponseDto.setType(file.getType());
                    res.setData(fileDownloadResponseDto);
                    return res;

                } catch (Exception e) {
                    return new ResponseMessage(0, "Failed to upload file to Minio: " + e.getMessage());
                }


            } catch (Exception e) {
                return new ResponseMessage(0, "Failed to download file");
            }
        }
        return new ResponseMessage(0, "File not found");
    }

    @Override
    public ResponseMessage deleteById(ObjectId id) {
        File entity = fileRepository.findById(id).orElse(null);
        if (Objects.nonNull(entity)) {
            Audit audit = entity.getAudit();
            audit.setIsActive(0);
            entity.setAudit(audit);
            fileRepository.save(entity);
            return new ResponseMessage(1, "File deleted successfully");
        }
        return new ResponseMessage(0, "File not found");
    }
}
