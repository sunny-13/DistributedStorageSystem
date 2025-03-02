package com.example.distributed_storage_system.constant;

import java.util.List;

public class Constants {

    public static final int DEFAULT_CHUNK_SIZE = 32 * 1024; /* 32kb */
    public static final String HASHING_ALGORITHM = "SHA-256";
    public static final Integer CONSISTENT_RING_SECTIONS = 1000;
    public static final Integer VIRTUAL_SERVER_NODES_NUMBER = 5;
    public static final String MONGO_METADATA_DB_NAME = "metadata_db";
    public static final Integer MONGO_CHUNK_DB_COUNT = 4;
    public static final List<String> MONGO_CHUNK_SERVER_ID_LIST =
            List.of("MONGO_CHUNK_SERVER_ID_1", "MONGO_CHUNK_SERVER_ID_2", "MONGO_CHUNK_SERVER_ID_3", "MONGO_CHUNK_SERVER_ID_4");
    public static final String MONGO_CHUNK_DB_NAME = "chunk_db";
}
