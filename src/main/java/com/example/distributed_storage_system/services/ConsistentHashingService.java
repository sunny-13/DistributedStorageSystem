package com.example.distributed_storage_system.services;

import com.example.distributed_storage_system.beans.ConsistentHashingRing;
import com.example.distributed_storage_system.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.example.distributed_storage_system.constant.Constants.CONSISTENT_RING_SECTIONS;
import static com.example.distributed_storage_system.utils.CommonUtil.nullSafeList;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@Component
public class ConsistentHashingService {

    private static final String HASHING_ALGORITHM = "SHA-256";

    public ConsistentHashingRing buildConsistentRing(List<String> serverIdList) {
        ConsistentHashingRing consistentHashingRing = initializeConsistentRing();
        nullSafeList(serverIdList).forEach(serverId -> {
            if(isNotBlank(serverId)) {
                addServerNodesInConsistentRing(consistentHashingRing.getServerIndexMap(), consistentHashingRing.getServerVirtualIdListMap(), serverId);
            }
        });
        return consistentHashingRing;
    }

    private void addServerNodesInConsistentRing(TreeMap<Integer, String> serverIndexMap, Map<String, List<String>> serverVirtualIdListMap,
                                                String serverId) {
        if(isBlank(serverId)) {
            return;
        }
        List<String> serverVirtualIdList = new ArrayList<>();
        for (int i = 0; i< Constants.VIRTUAL_SERVER_NODES_NUMBER; i++) {
            String serverVirtualId = serverId + UUID.randomUUID();
            serverVirtualIdList.add(serverVirtualId);
            Integer serverVirtualIdHash = getHash(serverVirtualId);
            if(nonNull(serverVirtualIdHash)) {
                serverIndexMap.put(serverVirtualIdHash, serverVirtualId);
            }
        }
        serverVirtualIdListMap.put(serverId, serverVirtualIdList);
    }

    private static Integer getHash(String serverVirtualId) {
        Integer ringIndex = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASHING_ALGORITHM);
            byte[] hashBytes = messageDigest.digest(serverVirtualId.getBytes());
            ringIndex = Math.abs(Arrays.hashCode(hashBytes)) % CONSISTENT_RING_SECTIONS;

        } catch (NoSuchAlgorithmException exception) {
            log.error("NoSuchAlgorithmException : exception : {}", exception.getMessage());
        }
        return ringIndex;
    }

    private static ConsistentHashingRing initializeConsistentRing() {
        return ConsistentHashingRing.builder()
                .serverIndexMap(new TreeMap<>())
                .serverVirtualIdListMap(new HashMap<>())
                .build();
    }
}
