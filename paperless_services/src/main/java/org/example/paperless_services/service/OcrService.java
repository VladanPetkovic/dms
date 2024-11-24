package org.example.paperless_services.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import net.sourceforge.tess4j.Tesseract;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class OcrService {
    private final StorageService storageService;

    @Autowired
    public OcrService(StorageService storageService) {
        this.storageService = storageService;
    }

    public String getStringForFile(String fileName) {
        try {
            Resource resource = storageService.load(fileName);

            Path tempFile = Files.createTempFile("ocr-", ".tmp");
            Files.copy(resource.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("src/main/resources");
            tesseract.setLanguage("eng");
            tesseract.setPageSegMode(1);
            tesseract.setOcrEngineMode(1);

            String result = tesseract.doOCR(tempFile.toFile());

            Files.delete(tempFile);

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to perform OCR on file: " + fileName, e);
        }
    }
}
