package com.jst.domain.file.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    public void createFiles(List<MultipartFile> files){
        String path="files";
        //File folder = new File(String.format("%s/%s", path, path));
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        UUID fileId=UUID.randomUUID();

        Path filePath = Paths.get(folder.getPath() + "/" + fileId.toString());
        files.stream().forEach(multipartFile -> {
            try {
                Files.copy(multipartFile.getInputStream(),filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
