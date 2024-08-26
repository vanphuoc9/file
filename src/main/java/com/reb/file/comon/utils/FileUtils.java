package com.reb.file.comon.utils;

import org.springframework.http.MediaType;

import java.util.Objects;

public class FileUtils {
    public static MediaType getMediaType(String type) {
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if(Objects.nonNull(type)){
            if (type.equals("image/jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else if (type.equals("image/png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (type.equals("image/gif")) {
                mediaType = MediaType.IMAGE_GIF;
            }else if (type.equals("application/pdf")) {
                mediaType = MediaType.APPLICATION_PDF;
            }else if (type.equals("application/msword")) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }
        }

        return mediaType;
    }

    public static String getContentDisposition(String type, String name) {
        String contentDisposition = "attachment; filename=" + name;
        if(Objects.nonNull(type)){
            if (type.equals("image/jpeg")) {
                contentDisposition = "inline; filename=" + name;
            } else if (type.equals("image/png")) {
                contentDisposition = "inline; filename=" + name;
            } else if (type.equals("image/gif")) {
                contentDisposition = "inline; filename=" + name;
            }else if (type.equals("application/pdf")) {
                contentDisposition = "inline; filename=" + name;
            }
        }
        return contentDisposition;
    }
}
