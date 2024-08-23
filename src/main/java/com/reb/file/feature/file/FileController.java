package com.reb.file.feature.file;

import com.reb.file.comon.dto.ResponseMessage;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/{file}")
    public ResponseEntity<?> downloadFile(@PathVariable ObjectId id,@RequestParam(defaultValue = "uuid") String uuid) {
        FileDto source = fileService.getFile(id, uuid);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(source.getSize())
                .header("Content-disposition", "attachment; filename=" + source.getName())
                .body(source.getStream());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ObjectId id) {
        ResponseMessage res = fileService.deleteById(id);
        return ResponseEntity.ok(res);
    }



}
