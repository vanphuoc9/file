package com.reb.file.feature.file;

import com.reb.file.comon.dto.ResponseMessage;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    ResponseMessage save(MultipartFile file);

    ResponseMessage getFile(ObjectId id, String uuid);

    ResponseMessage deleteById(ObjectId id);

    List<ResponseMessage> saveFiles(MultipartFile[] files);
}
