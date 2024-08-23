package com.reb.file.feature.file;

import com.reb.file.comon.dto.ResponseMessage;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    ResponseMessage save(MultipartFile file);

    FileDto getFile(ObjectId id, String uuid);

    ResponseMessage deleteById(ObjectId id);
}
