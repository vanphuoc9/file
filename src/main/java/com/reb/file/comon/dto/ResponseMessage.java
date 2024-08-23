package com.reb.file.comon.dto;

import lombok.Data;

@Data
public class ResponseMessage {
    private Integer code;
    private String message;
    private Object data;

    public ResponseMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseMessage(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
