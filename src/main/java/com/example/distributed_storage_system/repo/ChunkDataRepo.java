package com.example.distributed_storage_system.repo;

import com.example.distributed_storage_system.model.beans.ChunkData;
import com.example.distributed_storage_system.model.beans.FileMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.distributed_storage_system.utils.CommonUtil.nullSafeMap;

@Repository
public class ChunkDataRepo {

    private static final int BATCH_SIZE = 10;
    private static final String CHUNK_ID = "chunkId";

    @Autowired
    @Qualifier("createChunkMongoServerMap")
    private Map<String, MongoTemplate> chunkMongoServerIdMap;

    public void bulkSaveData(Map<String, List<ChunkData>> serverIdChunkDataListMap) {
        nullSafeMap(serverIdChunkDataListMap).forEach(
                (serverId, chunkDataList) -> {
                    MongoTemplate chunkMongoServer = chunkMongoServerIdMap.get(serverId);
                    chunkMongoServer.insertAll(chunkDataList);
                }
        );
    }

    public List<ChunkData> getChunkDataForFile(List<FileMetaData.ChunkMetaData> chunkMetaDataList) {
        List<ChunkData> chunkDataList = new ArrayList<>();
        Map<String, List<String>> serverIdToChunkIdsMap = getServerIdToChunkIdListMap(chunkMetaDataList);

        /* Fetch chunks in batches of 10 per server */
        for (Map.Entry<String, List<String>> entry : serverIdToChunkIdsMap.entrySet()) {
            String serverId = entry.getKey();
            List<String> chunkIds = entry.getValue();
            MongoTemplate mongoTemplate = chunkMongoServerIdMap.get(serverId);

            if (mongoTemplate != null) {
                for (int i = 0; i < chunkIds.size(); i += BATCH_SIZE) {
                    int end = Math.min(i + BATCH_SIZE, chunkIds.size());
                    List<String> batchChunkIds = chunkIds.subList(i, end);
                    Query query = new Query(Criteria.where(CHUNK_ID).in(batchChunkIds));
                    List<ChunkData> batchData = mongoTemplate.find(query, ChunkData.class);
                    chunkDataList.addAll(batchData);
                }
            }
        }
        return chunkDataList;
    }

    public Map<String, List<String>> getServerIdToChunkIdListMap(List<FileMetaData.ChunkMetaData> chunkMetaDataList) {
        Map<String, List<String>> serverIdToChunkIdsMap = new HashMap<>();

        /* Group chunkIds by serverId */
        for (FileMetaData.ChunkMetaData chunkMetaData : chunkMetaDataList) {
            serverIdToChunkIdsMap.computeIfAbsent(chunkMetaData.getMongoServerId(), k -> new ArrayList<>())
                    .add(chunkMetaData.getChunkId());
        }
        return serverIdToChunkIdsMap;
    }
}
