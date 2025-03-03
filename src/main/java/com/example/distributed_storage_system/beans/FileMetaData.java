package com.example.distributed_storage_system.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "chunk_metadata")
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaData {

    private String fileName;
    private List<ChunkMetaData> chunkMetaDataList;

    @Data
    @AllArgsConstructor
    public static class ChunkMetaData {
        private String chunkId;
        private Integer chunkSequence;
        private String mongoServerId;
    }
}

