package com.example.distributed_storage_system.repo;

import com.example.distributed_storage_system.model.beans.FileMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.distributed_storage_system.utils.CommonUtil.isBlank;

@Repository
public class FileMetaDataRepo {

    private static final String FILE_NAME = "fileName";
    private static final String CHUNK_SEQUENCE = "chunkSequence";

    @Autowired
    private MongoTemplate metaDataDBMongoTemplate;

    public void createAndSaveFileMetaData(String fileName, List<FileMetaData.ChunkMetaData> chunkMetaDataList) {
        FileMetaData fileMetaData = FileMetaData.builder()
                .fileName(fileName)
                .chunkMetaDataList(chunkMetaDataList)
                .build();
        metaDataDBMongoTemplate.save(fileMetaData);
    }

    public FileMetaData retrieveFileMetaData(String fileName) {
        if(isBlank(fileName)) {
            return null;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where(FILE_NAME).is(fileName));

        return metaDataDBMongoTemplate.findOne(query, FileMetaData.class);
    }
}
