package com.example.distributed_storage_system.repo;

import com.example.distributed_storage_system.model.beans.ChunkMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChunkMetaDataRepo {

    private static final String FILE_NAME = "fileName";
    private static final String CHUNK_SEQUENCE = "chunkSequence";

    @Autowired
    private MongoTemplate metaDataDBMongoTemplate;

    public synchronized void createAndSaveChunkMetaData(String fileName, String chunkId, Integer chunkSequence, String mongoServerId) {
        System.out.println("createAndSaveChunkMetaData chunkId " + chunkId + "serverId " + mongoServerId);
        ChunkMetaData chunkMetaData = ChunkMetaData.builder()
                .fileName(fileName)
                .chunkId(chunkId)
                .chunkSequence(chunkSequence)
                .mongoServerId(mongoServerId)
                .build();
        metaDataDBMongoTemplate.save(chunkMetaData);
        System.out.println("Successfully stored metadata for chunk: " + chunkId);
    }

    public List<ChunkMetaData> getChunkListByFileNameSorted(String fileName) {
        Query query = new Query();
        query.addCriteria(Criteria.where(FILE_NAME).is(fileName));
        query.with(Sort.by(Sort.Direction.ASC, CHUNK_SEQUENCE)); /* Sorting by chunkSequence in ascending order */

        return metaDataDBMongoTemplate.find(query, ChunkMetaData.class);
    }
}
