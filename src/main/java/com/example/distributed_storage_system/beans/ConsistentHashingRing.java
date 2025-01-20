package com.example.distributed_storage_system.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsistentHashingRing {

    private TreeMap<Integer, String> serverIndexMap;
    private Map<String, List<String>> serverVirtualIdListMap;
}
