package com.example.distributed_storage_system.controller;

import com.example.distributed_storage_system.beans.ConsistentHashingRing;
import com.example.distributed_storage_system.services.ConsistentHashingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consistent")
public class ConsistentHashingController {

    @Autowired
    private ConsistentHashingService consistentHashingService;

    @PostMapping("/initialize")
    public ResponseEntity<ConsistentHashingRing> initializeConsistentRing(@RequestBody List<String> serverIds) {
        ConsistentHashingRing consistentHashingRing = consistentHashingService.buildConsistentRing(serverIds);
        return ResponseEntity.ok(consistentHashingRing);
    }
}
