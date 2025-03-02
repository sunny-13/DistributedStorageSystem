package com.example.distributed_storage_system.controller;

import com.example.distributed_storage_system.services.DistributedStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("")
public class DistributedStorageController {

    @Autowired
    private DistributedStorageService distributedStorageService;

    @PostMapping("/")
    public String healthCheck() {
        return "Service up";
    }

    @PostMapping("/store")
    public String storeFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("Request received");
        distributedStorageService.chunkAndStoreFile(file);
        return "File Stored";
    }
}
