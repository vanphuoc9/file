package com.reb.file.feature.file;

import com.reb.file.comon.dto.ResponseMessage;
import com.reb.file.comon.utils.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Operation(summary = "Upload multiple files")
    @PostMapping("/--multiple")
    public ResponseEntity<?> uploadFiles(@RequestPart("files") MultipartFile[] files) {
        List<ResponseMessage> res = fileService.saveFiles(files);
        return ResponseEntity.ok(res);
    }


    @Operation(summary = "Upload file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload file", content = @Content(schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @PostMapping
    public ResponseEntity<?>  uploadFile(@RequestPart("file") MultipartFile file) {
        ResponseMessage res = fileService.save(file);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Download file",
            parameters = {
                    @Parameter(name = "id", description = "ID of the file", required = true, example = "66cdc8bc83cca2286649c18f"),
                    @Parameter(name = "uuid", description = "UUID of the file", required = true, example = "b35ac76c-936e-4f69-8cc4-9344ca5e1202")
            }

    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found and returned successfully", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
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

    @Operation(summary = "View file",
            parameters = {
                    @Parameter(name = "id", description = "ID of the file", required = true, example = "66cdc8bc83cca2286649c18f"),
                    @Parameter(name = "uuid", description = "UUID of the file", required = true, example = "b35ac76c-936e-4f69-8cc4-9344ca5e1202")
            }

    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found and returned successfully", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
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


    @Operation(summary = "Delete file")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ObjectId id) {
        ResponseMessage res = fileService.deleteById(id);
        return ResponseEntity.ok(res);
    }



}
