package com.example.distributed_storage_system.controller;

import com.example.distributed_storage_system.services.DistributedStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.distributed_storage_system.utils.CommonUtil.determineContentType;

@RestController
@RequestMapping("/dss")
public class DistributedStorageController {

    @Autowired
    private DistributedStorageService distributedStorageService;

    @PostMapping("/")
    public String healthCheck() {
        return "Service up";
    }

    @PostMapping("/store")
    public ResponseEntity<String> storeFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("Request received");
        distributedStorageService.chunkAndStoreFile(file);
        return ResponseEntity.ok()
                .body("File stored");
    }

    @GetMapping("/fetch")
    public ResponseEntity<ByteArrayResource> fetchFile(@PathVariable String fileName) throws FileNotFoundException {
        System.out.println("Request received");
        ByteArrayResource resource = distributedStorageService.fetchFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(determineContentType(fileName))
                .body(resource);
    }
}
