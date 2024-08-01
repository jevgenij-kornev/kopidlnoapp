package com.example.kopidlnoapp.service;

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

    @Value("${file.temp-dir:temp}")
    private String tempDir;

    @Value("${file.url}")
    private String fileUrl;

    @Value("${file.output}")
    private String outputFileName;

    public void downloadAndUnzip() throws IOException, InterruptedException {
        Path tempDirPath = Paths.get(tempDir);
        if (!Files.exists(tempDirPath)) {
            Files.createDirectory(tempDirPath);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fileUrl))
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        try (InputStream in = response.body();
             ZipInputStream zis = new ZipInputStream(in)) {

            ZipEntry zipEntry = zis.getNextEntry();
            if (zipEntry != null) {
                try (FileOutputStream out = new FileOutputStream(tempDir + "/" + outputFileName)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
                zis.closeEntry();
            }
        }
    }
}