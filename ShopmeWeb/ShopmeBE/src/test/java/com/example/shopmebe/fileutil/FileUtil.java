package com.example.shopmebe.fileutil;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {

    public static String readFromJSONFileToString(String filePath) throws IOException {
        File resource = new ClassPathResource(filePath).getFile();
        byte[] byteArray = Files.readAllBytes(resource.toPath());
        return new String(byteArray);
    }
}
