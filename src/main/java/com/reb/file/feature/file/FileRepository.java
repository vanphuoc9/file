package com.reb.file.feature.file;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<File, ObjectId> {
    @Query("{ '_id': ?0, 'audit.isActive': 1 }")
    Optional<File> findActiveById(ObjectId id);

    @Query("{ '_id': :#{#id}, 'uuid': :#{#uuid}, 'audit.isActive': 1 }")
    Optional<File> findActiveByIdAndUuid(@Param("id") ObjectId id, @Param("uuid") String uuid);
}
