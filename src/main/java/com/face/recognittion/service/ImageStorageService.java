package com.face.recognittion.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageService {

    private static final String STORAGE_DIR = "face-images";

    public String storeImage(MultipartFile file) throws IOException {
        // Ensure the storage directory exists
        Path storagePath = Paths.get(STORAGE_DIR);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        // Save the file
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = storagePath.resolve(fileName);
        file.transferTo(filePath.toFile());

        return filePath.toString();
    }
}
