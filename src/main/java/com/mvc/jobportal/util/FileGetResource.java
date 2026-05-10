package com.mvc.jobportal.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileGetResource {
    private static Path filePath;

    public static Resource getFileAsResource(String directory,String fileName) throws IOException {
        Path path= Paths.get(directory);

        Files.list(path)
                .forEach(file->{
                    if(file.getFileName().startsWith(fileName)){
                        filePath=file;
                    }
                });

        if(filePath!=null){
            return new UrlResource(filePath.toUri());
        }
        return null;
    }


}
