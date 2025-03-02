package com.example.distributed_storage_system.repo;

import com.example.distributed_storage_system.model.beans.ChunkData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChunkDataRepo {

    @Autowired
    @Qualifier("createChunkMongoServerMap")
    private Map<String, MongoTemplate> chunkMongoServerIdMap;

    public synchronized void saveChunkData(Integer chunkSequence, String chunkId, byte[] data, String serverId) {
        System.out.println("saveChunkData chunkId " + chunkId + "serverId " + serverId);
        MongoTemplate chunkMongoServer = chunkMongoServerIdMap.get(serverId);
        ChunkData chunkData = ChunkData.builder()
                .chunkSequence(chunkSequence)
                .chunkId(chunkId)
                .data(data)
                .build();
        chunkMongoServer.save(chunkData);
        System.out.println("Successfully stored chunk: " + chunkId);
    }

    public List<ChunkData> getChunksByIds(List<String> chunkIds, String serverId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("chunkId").in(chunkIds));

        return chunkMongoServerIdMap.get(serverId).find(query, ChunkData.class);
    }
}
