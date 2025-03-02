package com.example.distributed_storage_system.model.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "chunk_data")
@NoArgsConstructor
@AllArgsConstructor
public class ChunkData {

    private Integer chunkSequence;
    private String chunkId;
    private byte[] data;
}
