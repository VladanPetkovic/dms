package org.example.paperless_services.service;

import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class OcrServiceTest {
    private final OcrService ocrService = new OcrService();

    private Path testImagePath;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        Resource resource = new ClassPathResource("hello.png");
        testImagePath = resource.getFile().toPath();
    }

//    @Test
//    public void testProcessFile() throws TesseractException {
//        String expectedText = "Hello, this is me.\n";
//
//        String result = ocrService.processFile(testImagePath);
//
//        assertEquals(expectedText, result);
//    }
}
