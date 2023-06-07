package com.chtw.midjourney.storage.mj.repository;

import com.chtw.midjourney.storage.mj.document.ImagineLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class ImagineRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ImagineLogs insertOne(ImagineLogs logs) {
        return mongoTemplate.insert(logs);
    }

    public ImagineLogs findOne(String id) {
        return mongoTemplate.findById(id, ImagineLogs.class);
    }
}
