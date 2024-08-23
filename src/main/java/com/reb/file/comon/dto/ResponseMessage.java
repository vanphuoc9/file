package com.reb.file.comon.dto;

import com.reb.file.feature.file.FileDto;
import lombok.Data;

@Data
public class ResponseMessage {
    private Integer code;
    private String message;
    private FileDto data;

    public ResponseMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseMessage(Integer code, String message, FileDto data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
