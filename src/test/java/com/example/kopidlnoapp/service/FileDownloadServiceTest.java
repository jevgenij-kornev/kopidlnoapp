package com.example.kopidlnoapp.service;

import com.example.kopidlnoapp.util.ZipFileCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FileDownloadServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadServiceTest.class);

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<InputStream> httpResponse;

    @InjectMocks
    private FileDownloadService fileDownloadService;

    private final Path xmlFilePath = Paths.get("src/test/resources/test-to-zip.xml");
    private final Path outputPath = Paths.get("src/test/resources/temp/unzipped.xml");

    // TODO поменять адрес сохраняемого файла.
    // TODO добавить логику на удаление файла kopidlno.xml
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileDownloadService = new FileDownloadService(
                "src/test/resources/temp",
                "http://example.com/file.zip",
                "unzipped.xml",
                httpClient
        );
    }

    @Test
    void downloadAndUnzip() throws Exception {
        ZipFileCreator zipFileCreator = new ZipFileCreator();
        byte[] zipContent = zipFileCreator.createZipFile(xmlFilePath);
        InputStream inputStream = new ByteArrayInputStream(zipContent);

        logger.info("Test ZIP file created");

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenAnswer(invocation -> {
            HttpResponse<InputStream> response = httpResponse;
            when(response.body()).thenReturn(inputStream);
            when(response.statusCode()).thenReturn(200);
            return response;
        });

        logger.info("Mocks set up");

        fileDownloadService.downloadAndUnzip();

        logger.info("Method downloadAndUnzip called");

        assertTrue(Files.exists(outputPath));

        // TODO добавить сравнения самих файлов.

        logger.info("Output file path: {}", outputPath.toAbsolutePath());
    }
}