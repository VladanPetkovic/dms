package org.example.paperless_rest.app;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileUploadTest {
    private static final String FOLDER_PATH = "test_folder";
    private static final String TEST_FILE_NAME = "testfile.txt";

    @BeforeAll
    public static void setup() {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File testFile = new File(folder, TEST_FILE_NAME);
        try {
            if (!testFile.exists()) {
                testFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDown() {
        File testFile = new File(FOLDER_PATH, TEST_FILE_NAME);
        if (testFile.exists()) {
            testFile.delete();
        }
        File folder = new File(FOLDER_PATH);
        if (folder.exists()) {
            folder.delete();
        }
    }

    @Test
    public void testGetFileFromFolder_FileExists() {
        Resource file = FileUpload.getFileFromFolder(FOLDER_PATH + '/' + TEST_FILE_NAME);

        assertNotNull(file);
        assertTrue(file.exists(), "File should exist");
    }

    @Test
    public void testGetFileFromFolder_FileDoesNotExist() {
        String nonExistentFile = "nonexistentfile.txt";
        Resource file = FileUpload.getFileFromFolder(nonExistentFile);

        assertNull(file);
    }
}
