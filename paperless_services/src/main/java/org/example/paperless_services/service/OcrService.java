package org.example.paperless_services.service;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import net.sourceforge.tess4j.Tesseract;

import java.nio.file.Path;

@Service
public class OcrService {
    private final String tessDataPath = "src/main/resources/tessdata";

    public String processFile(Path filePath) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);
        System.out.println(tessDataPath);
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        return tesseract.doOCR(filePath.toFile());
    }
}
