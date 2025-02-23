package com.example.distributed_storage_system.services;

import com.example.distributed_storage_system.model.beans.ConsistentHashingRing;
import com.example.distributed_storage_system.constant.Constants;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.distributed_storage_system.constant.Constants.CONSISTENT_RING_SECTIONS;
import static com.example.distributed_storage_system.utils.CommonUtil.getRingIndex;
import static com.example.distributed_storage_system.utils.CommonUtil.nullSafeMap;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
public class ConsistentHashingService {

    @Autowired
    private Map<String, MongoTemplate> chunkMongoServerMap;
    private ConsistentHashingRing consistentHashingRing;

    @PostConstruct
    public void init() {
        this.consistentHashingRing = ConsistentHashingRing.initializeConsistentRing();
        addServerNodesInConsistentRing(this.consistentHashingRing, chunkMongoServerMap);
    }

    private void addServerNodesInConsistentRing(ConsistentHashingRing consistentHashingRing, Map<String, MongoTemplate> chunkMongoServerMap) {
        if(isNull(consistentHashingRing) || isNull(chunkMongoServerMap)) {
            return;
        }
        Map<String, String> serverIdsMap = consistentHashingRing.getServerIdsMap();
        List<String> serverIndexList = consistentHashingRing.getServerIndexList();
        nullSafeMap(chunkMongoServerMap).keySet()
                        .forEach(mongoServerId -> {
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
