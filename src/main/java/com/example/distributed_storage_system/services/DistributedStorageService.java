package com.example.distributed_storage_system.services;

import com.example.distributed_storage_system.repo.ChunkDataRepo;
import com.example.distributed_storage_system.repo.ChunkMetaDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.example.distributed_storage_system.constant.Constants.DEFAULT_CHUNK_SIZE;
import static java.util.Objects.isNull;

@Service
public class DistributedStorageService {

    @Autowired
    private ChunkDataRepo chunkDataRepo;
    @Autowired
    private ChunkMetaDataRepo chunkMetaDataRepo;
    @Autowired
    private ConsistentHashingService consistentHashingService;

    public void chunkAndStoreFile(MultipartFile multipartFile) throws IOException {
        if (isNull(multipartFile)) {
            return;
        }
        String fileName = multipartFile.getOriginalFilename();
        File file = convertMultiPartToFile(multipartFile);
        if (isNull(file) || !file.exists()) {
            return;
        }
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[DEFAULT_CHUNK_SIZE];
            int bytesRead;
            int chunkSequence = 1;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                // Copy only the valid bytes in case the last chunk is smaller
                byte[] data = (bytesRead == DEFAULT_CHUNK_SIZE) ? buffer.clone() : copyPartialChunk(buffer, bytesRead);
                String chunkId = fileName + "_" + chunkSequence;
                System.out.println("chunkId " + chunkId);
                String serverId = consistentHashingService.getChunkMongoServerId(chunkId);
                /* Save both data and metadata */
                chunkDataRepo.saveChunkData(chunkSequence, chunkId, data, serverId);
                chunkMetaDataRepo.createAndSaveChunkMetaData(fileName, chunkId, chunkSequence, serverId);
                chunkSequence++;
            }
        }
    }


    private File convertMultiPartToFile(MultipartFile file) {
        File convertedFile = null;
        try {
            convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convertedFile);
            return convertedFile;
        } catch (Exception ex) {
            System.out.println("convertMultiPartToFile -> Exception in converting file : " + ex.getMessage());
        }
        return convertedFile;
    }

    private byte[] copyPartialChunk(byte[] buffer, int size) {
        byte[] chunk = new byte[size];
        System.arraycopy(buffer, 0, chunk, 0, size);
        return chunk;
    }
}
