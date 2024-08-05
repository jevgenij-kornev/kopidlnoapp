package com.example.kopidlnoapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FileDownloadService {

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadService.class);

    @Value("${file.temp-dir:temp}")
    private String tempDir;

    @Value("${file.url}")
    private String fileUrl;

    @Value("${file.output}")
    private String outputFileName;

    private final HttpClient httpClient;

    @Autowired
    public FileDownloadService(@Value("${file.temp-dir:temp}") String tempDir,
                               @Value("${file.url}") String fileUrl,
                               @Value("${file.output}") String outputFileName,
                               HttpClient httpClient) {
        this.tempDir = tempDir;
        this.fileUrl = fileUrl;
        this.outputFileName = outputFileName;
        this.httpClient = httpClient;
    }

    public void downloadAndUnzip() throws IOException, InterruptedException {
        Path tempDirPath = Paths.get(tempDir);
        if (!Files.exists(tempDirPath)) {
            Files.createDirectories(tempDirPath);
            logger.info("Temporary directory created at: {}", tempDirPath.toAbsolutePath());
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fileUrl))
                .build();

        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

        try (InputStream in = response.body();
             ZipInputStream zis = new ZipInputStream(in)) {

            ZipEntry zipEntry = zis.getNextEntry();
            if (zipEntry != null) {
                Path tempFilePath = tempDirPath.resolve(zipEntry.getName());
                try (FileOutputStream out = new FileOutputStream(tempFilePath.toString())) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
                zis.closeEntry();

                Path outputFilePath = tempDirPath.resolve(outputFileName);
                if (Files.exists(outputFilePath)) {
                    Files.delete(outputFilePath);
                }
                Files.move(tempFilePath, outputFilePath);
                logger.info("File extracted and renamed to: {}", outputFilePath.toAbsolutePath());
            } else {
                logger.warn("No entry found in the ZIP file.");
            }
        }
    }
}