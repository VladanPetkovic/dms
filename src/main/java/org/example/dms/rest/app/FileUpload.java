package org.example.dms.rest.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class FileUpload {
    public static final String FOLDER_PATH = "/uploads";

    public static void saveFileLocally(MultipartFile file, String uniqueFileName) throws IOException {
        File directory = new File(FOLDER_PATH);
        File targetFile = new File(directory, uniqueFileName);

        file.transferTo(targetFile);
        log.info("File saved to: " + targetFile.getAbsolutePath());
    }

    static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    public static String getUniqueFileName(MultipartFile file) {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        return UUID.randomUUID() + fileExtension;
    }

    public static File getFileFromFolder(String fileName) {
        log.info("Filename: " + fileName);
        File file = new File(fileName);

        if (!file.exists()) {
            log.error("File does not exist: " + fileName);
            return null;
        }

        return file;
    }
}
