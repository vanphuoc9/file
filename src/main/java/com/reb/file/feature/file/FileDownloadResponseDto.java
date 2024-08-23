package com.reb.file.feature.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;

import java.time.LocalDateTime;

@Data
public class FileDownloadResponseDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String uuid;
    private String name;
    @JsonIgnore
    private String uniqueFilename;
    private Long size;
    private String type;
    private LocalDateTime updatedAt;

    @JsonIgnore
    private InputStreamResource stream;
}
