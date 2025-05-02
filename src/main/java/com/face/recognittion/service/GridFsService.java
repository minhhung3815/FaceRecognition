package com.face.recognittion.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class GridFsService {

    @Autowired
    private GridFSBucket gridFSBucket;

    public String storeFile(byte[] data, String contentType, String filename) {
        ObjectId fileId = gridFSBucket.uploadFromStream(filename, new ByteArrayInputStream(data));
        return fileId.toHexString();
    }

    public GridFSFile getFile(String fileId) {
        return gridFSBucket.find(new org.bson.Document("_id", new ObjectId(fileId))).first();
    }

    public byte[] downloadFile(String fileId) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridFSBucket.downloadToStream(new ObjectId(fileId), outputStream);
        return outputStream.toByteArray();
    }
}
