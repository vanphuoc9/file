package com.reb.file.feature.file;

import com.reb.file.comon.dto.ResponseMessage;
import com.reb.file.comon.minio.MinioService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private MinioService minioService;

    @Override
    public ResponseMessage save(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String uniqueFilename = uuid + "_" + file.getOriginalFilename();

        Path path = Path.of(uniqueFilename);
        try {
            minioService.uploadFile(path, file.getInputStream(), file.getContentType());

            // Check if the file was uploaded successfully
            try {
                minioService.getMetadata(path.toString());
            } catch (Exception e) {
                return new ResponseMessage(0, "Failed to upload file to Minio: " + e.getMessage());
            }

            File fileEntity = new File();
            fileEntity.setName(file.getOriginalFilename());
            fileEntity.setSize(file.getSize());
            fileEntity.setPath(path.toString());
            fileEntity.setType(file.getContentType());
            fileEntity.setUniqueFilename(uniqueFilename);
            fileEntity.setUuid(uuid);
            fileRepository.save(fileEntity);

            ResponseMessage res = new ResponseMessage(1, "Save file success");
            FileDto dto = new FileDto();
            dto.setName(file.getOriginalFilename());
            dto.setSize(file.getSize());
            dto.setType(file.getContentType());

            res.setData(dto);
            return res;
        }  catch (Exception e) {
            e.printStackTrace();
            return new ResponseMessage(0, "Failed to save file");
        }


    }

    @Override
    public FileDto getFile(ObjectId id, String uuid) {
        return null;
    }

    @Override
    public ResponseMessage deleteById(ObjectId id) {
        return null;
    }
}
