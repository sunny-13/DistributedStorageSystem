package com.example.distributed_storage_system.services;

import com.example.distributed_storage_system.constant.Constants;
import com.example.distributed_storage_system.beans.ConsistentHashingRing;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.distributed_storage_system.constant.Constants.CONSISTENT_RING_SECTIONS;
import static com.example.distributed_storage_system.constant.Constants.MONGO_CHUNK_SERVER_ID_LIST;
import static com.example.distributed_storage_system.utils.CommonUtil.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
public class ConsistentHashingService {

    private ConsistentHashingRing consistentHashingRing;

    @PostConstruct
    public void init() {
        this.consistentHashingRing = ConsistentHashingRing.initializeConsistentRing();
        addServerNodesInConsistentRing(this.consistentHashingRing);
    }

    public String getChunkMongoServerId(String chunkId) {
        if (isBlank(chunkId)) {
            throw new  RuntimeException("ChunkId is blank");
        }
        Integer ringIndex = getRingIndex(chunkId);
        String virtualServeId = consistentHashingRing.getServerIndexList().get(ringIndex);
        return consistentHashingRing.getServerIdsMap().get(virtualServeId);
    }

    private void addServerNodesInConsistentRing(ConsistentHashingRing consistentHashingRing) {
        if(isNull(consistentHashingRing)) {
            return;
        }
        Map<String, String> serverIdsMap = consistentHashingRing.getServerIdsMap();
        List<String> serverIndexList = consistentHashingRing.getServerIndexList();
        nullSafeList(MONGO_CHUNK_SERVER_ID_LIST).forEach(mongoServerId -> {
                            for (int i = 0; i < Constants.VIRTUAL_SERVER_NODES_NUMBER; i++) {
                                String serverVirtualId = mongoServerId + UUID.randomUUID();
                                serverIdsMap.put(serverVirtualId, mongoServerId);
                                Integer ringIndex = getRingIndex(serverVirtualId);
                                if(nonNull(ringIndex)) {
                                    serverIndexList.set(ringIndex, serverVirtualId);
                                }
                            }
                        });
        performSlidingWindowOnServerIndexList(serverIndexList);
    }

    private void performSlidingWindowOnServerIndexList(List<String> serverIndexList) {
        if(isEmpty(serverIndexList)) {
            return;
        }
        String prevValue = null;
        for (int i = 0 ; i < 2; i++) {
            for (int index = 0; index < CONSISTENT_RING_SECTIONS; index++) {
                if(nonNull(serverIndexList.get(index))) {
                    prevValue = serverIndexList.get(index);
                    continue;
                }
                if(nonNull(prevValue)) {
                    serverIndexList.set(index, prevValue);
                }
            }
        }
    }
}
