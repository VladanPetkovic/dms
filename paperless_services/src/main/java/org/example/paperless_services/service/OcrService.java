package org.example.paperless_services.service;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import net.sourceforge.tess4j.Tesseract;

import java.nio.file.Path;

@Service
public class OcrService {
    public String processFile(Path filePath) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        return tesseract.doOCR(filePath.toFile());
    }
}
