package com.example.shopmebe.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.error(String.format("Error while saving file: %s %s %s", fileName, ex.getMessage(), ex));
            throw ex;
        }
    }

    public static void cleanDirectory(String dir) {
        Path dirPath = Paths.get(dir);

        try(Stream<Path> pathList = Files.list(dirPath)) {
            pathList.forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException ex) {
                        log.error(String.format("Could not delete file: %s", file));
                    }
                }
            });
        } catch (IOException ex) {
            log.error(String.format("Could not list directory: %s", dirPath));
        }
    }
}
