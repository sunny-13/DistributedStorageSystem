package com.example.distributed_storage_system.services;

import com.example.distributed_storage_system.beans.ChunkData;
import com.example.distributed_storage_system.beans.FileMetaData;
import com.example.distributed_storage_system.repo.ChunkDataRepo;
import com.example.distributed_storage_system.repo.FileMetaDataRepo;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

import static com.example.distributed_storage_system.constant.Constants.BULK_SEQUENCE_SIZE;
import static com.example.distributed_storage_system.constant.Constants.DEFAULT_CHUNK_SIZE;
import static com.example.distributed_storage_system.utils.CommonUtil.isEmpty;
import static java.util.Objects.isNull;

@Service
public class DistributedStorageService {

    @Autowired
    private ChunkDataRepo chunkDataRepo;
    @Autowired
    private FileMetaDataRepo fileMetaDataRepo;
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
        String chunkId = null;
        String serverId = null;
        byte[] data = null;
        List<FileMetaData.ChunkMetaData> chunkMetaDataList = new ArrayList<>();
        Map<String, List<ChunkData>> serverIdChunkDataListMap = new HashMap<>();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[DEFAULT_CHUNK_SIZE];
            int bytesRead;
            int chunkSequence = 0;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                chunkSequence++;
                /*Copy only the valid bytes in case the last chunk is smaller */
                chunkId = fileName + "_" + chunkSequence;
                serverId = consistentHashingService.getChunkMongoServerId(chunkId);
                data = (bytesRead == DEFAULT_CHUNK_SIZE) ? buffer.clone() : copyPartialChunk(buffer, bytesRead);
                /* Add chunk data */
                ChunkData chunkData = new ChunkData(chunkId, chunkSequence, new Binary(data));
                serverIdChunkDataListMap.computeIfAbsent(serverId, k -> new ArrayList<>()).add(chunkData);
                /* Add chunkMetaData */
                chunkMetaDataList.add(new FileMetaData.ChunkMetaData(chunkId, chunkSequence, serverId));
                if(chunkSequence % BULK_SEQUENCE_SIZE == 0) {
                    chunkDataRepo.bulkSaveData(serverIdChunkDataListMap);
                    serverIdChunkDataListMap.clear();
                }
            }
            chunkDataRepo.bulkSaveData(serverIdChunkDataListMap);
            /* Save fileMeta data */
            fileMetaDataRepo.createAndSaveFileMetaData(fileName, chunkMetaDataList);
        }
    }

    public ByteArrayResource fetchFile(String fileName) throws FileNotFoundException {
        /* Fetch File Metadata */
        FileMetaData fileMetaData = fileMetaDataRepo.retrieveFileMetaData(fileName);
        if (isNull(fileMetaData) || isEmpty(fileMetaData.getChunkMetaDataList())) {
            throw new FileNotFoundException("File not found " + fileName);
        }

        /* Step 2: Fetch Chunk Data */
        List<FileMetaData.ChunkMetaData> chunkMetaDataList = fileMetaData.getChunkMetaDataList();
        List<ChunkData> chunkDataList = chunkDataRepo.getChunkDataForFile(chunkMetaDataList);
        /* Step 3: Sort Chunks by Sequence */
        chunkDataList.sort(Comparator.comparingInt(ChunkData::getChunkSequence));
        /* Step 4: Merge Chunks */
        byte[] fileBytes = mergeChunks(chunkDataList);
        /* Step 5: Return as Downloadable File */
        return new ByteArrayResource(fileBytes);
    }

    private byte[] mergeChunks(List<ChunkData> chunkDataList) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            for (ChunkData chunkData : chunkDataList) {
                outputStream.write(chunkData.getData().getData());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while merging file chunks: " + e.getMessage());
        }
        return outputStream.toByteArray();
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
