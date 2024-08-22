package com.reb.file.feature.file;

import com.reb.file.audit.Audit;
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
    private String path;

    private Audit audit = new Audit();
}
