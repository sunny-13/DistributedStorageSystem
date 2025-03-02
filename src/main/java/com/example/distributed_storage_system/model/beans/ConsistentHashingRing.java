package com.example.distributed_storage_system.model.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import static com.example.distributed_storage_system.constant.Constants.CONSISTENT_RING_SECTIONS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsistentHashingRing {

    /* This map contains all serverVirtualIds mapped to the real id */
    private Map<String, String> serverIdsMap;
    private List<String> serverIndexList;

    public static ConsistentHashingRing initializeConsistentRing() {
        List<String> serverIndexList = new ArrayList<>(CONSISTENT_RING_SECTIONS);
        for (int i = 0; i < CONSISTENT_RING_SECTIONS; i++) {
            serverIndexList.add(null); // Initialize with default values
        }

        return ConsistentHashingRing.builder()
                .serverIdsMap(new HashMap<>())
                .serverIndexList(serverIndexList)
                .build();
    }
}
