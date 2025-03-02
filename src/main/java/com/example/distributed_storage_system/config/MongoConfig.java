package com.example.distributed_storage_system.config;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.example.distributed_storage_system.constant.Constants.*;
import static java.util.Objects.nonNull;

@Configuration
public class MongoConfig {

    private final Environment env;

    public MongoConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public MongoTemplate createMetadataMongoTemplate() {
        String dbKey = "mongodb://localhost:27017/metadata_db";
        return new MongoTemplate(MongoClients.create(dbKey), MONGO_METADATA_DB_NAME);
    }

    @Bean
    public Map<String, MongoTemplate> createChunkMongoServerMap() {
        Map<String, MongoTemplate> chunkMongoServerMap = new HashMap<>();
        for (int index = 0; index < MONGO_CHUNK_DB_COUNT; index++) {
            String dbKey = "chunk.db" + index + ".uri";
            String uri = env.getProperty(dbKey);
            System.out.println("uri:");
            System.out.println(uri);
            if (nonNull(uri)) {
                MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(uri), MONGO_CHUNK_DB_NAME);
                chunkMongoServerMap.put(MONGO_CHUNK_SERVER_ID_LIST.get(index), mongoTemplate);
            }
        }
        return chunkMongoServerMap;
    }
}