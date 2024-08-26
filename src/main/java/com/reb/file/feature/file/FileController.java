package com.reb.file.feature.file;

import com.reb.file.comon.dto.ResponseMessage;
import com.reb.file.comon.utils.FileUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    public ResponseEntity<?> create(@RequestPart("file") MultipartFile file) {
        ResponseMessage res = fileService.save(file);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable ObjectId id, @RequestParam String uuid) {
        ResponseMessage source = fileService.getFile(id, uuid);

        if (Objects.nonNull(source) && Objects.nonNull(source.getData())) {
            try {
                Integer code = source.getCode();
                if (code == 0) {
                    return ResponseEntity.ok(new ResponseMessage(0, source.getMessage()));
                }

                FileDownloadResponseDto dto = (FileDownloadResponseDto) source.getData();
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(dto.getSize())
                        .header("Content-disposition", "attachment; filename=" + dto.getName())
                        .body(dto.getStream());
            }catch (Exception e){
                return ResponseEntity.ok(new ResponseMessage(0, "Failed to download file"));
            }

        }else{
            return ResponseEntity.ok(new ResponseMessage(0, "File not found"));
        }


    }

    @GetMapping("/view/{id}")
    public ResponseEntity<?> viewFile(@PathVariable ObjectId id, @RequestParam String uuid) {
        ResponseMessage source = fileService.getFile(id, uuid);

        if (Objects.nonNull(source) && Objects.nonNull(source.getData())) {
            try {
                Integer code = source.getCode();
                if (code == 0) {
                    return ResponseEntity.ok(new ResponseMessage(0, source.getMessage()));
                }
                
                FileDownloadResponseDto dto = (FileDownloadResponseDto) source.getData();
                return ResponseEntity
                        .ok()
                        .contentType(FileUtils.getMediaType(dto.getType()))
                        .contentLength(dto.getSize())
                        .header("Content-disposition", FileUtils.getContentDisposition(dto.getType(), dto.getName()))
                        .body(dto.getStream());
            }catch (Exception e){
                return ResponseEntity.ok(new ResponseMessage(0, "Failed to download file"));
            }

        }else{
            return ResponseEntity.ok(new ResponseMessage(0, "File not found"));
        }


    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ObjectId id) {
        ResponseMessage res = fileService.deleteById(id);
        return ResponseEntity.ok(res);
    }



}
