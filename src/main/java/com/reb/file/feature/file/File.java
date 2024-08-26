package com.reb.file.feature.file;

import com.reb.file.comon.audit.Audit;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "file")
public class File {
    @Id
    private ObjectId id;
    private String name;
    private String uniqueFilename;
    private String path;
    private Long size;
    private String type;
    private String bucket;

    private String uuid;
    private Audit audit = new Audit();
}
