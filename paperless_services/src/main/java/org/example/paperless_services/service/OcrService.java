package org.example.paperless_services.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class OcrService {
    public String processFile(Path filePath) throws Exception {
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);

        if (filePath.toString().toLowerCase().endsWith(".pdf")) {
            log.info("About to process pdf-file.");
            return processPdf(filePath, tesseract);
        } else {
            log.info("About to process image-file.");
            return tesseract.doOCR(filePath.toFile());
        }
    }

    private String processPdf(Path pdfPath, Tesseract tesseract) throws Exception {
        StringBuilder ocrResult = new StringBuilder();
        try (PDDocument document = Loader.loadPDF(pdfPath.toFile())) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                Path tempImage = Files.createTempFile("pdf_page_" + page, ".png");
                ImageIO.write(image, "png", tempImage.toFile());

                ocrResult.append(tesseract.doOCR(tempImage.toFile())).append("\n");
                Files.delete(tempImage);
            }
        }
        return ocrResult.toString();
    }
}
