package com.example.distributed_storage_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChunkStorageService {

    @Autowired
    private MongoTemplate metaDataDBMongoTemplate;
    @Autowired
    private Map<String, MongoTemplate> chunkDBMongoTemplateList;
}
