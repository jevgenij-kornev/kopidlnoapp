package com.example.kopidlnoapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileCreator {

    private static final Logger logger = LoggerFactory.getLogger(ZipFileCreator.class);

    public byte[] createZipFile(Path xmlFilePath) throws IOException {
        logger.info("Creating ZIP file from {}", xmlFilePath.toAbsolutePath());
        byte[] xmlContent = Files.readAllBytes(xmlFilePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            zipOutputStream.putNextEntry(new ZipEntry("test-to-unzip.xml"));
            zipOutputStream.write(xmlContent);
            zipOutputStream.closeEntry();
            logger.info("ZIP entry for test-to-unzip.xml created");
        }
        logger.info("ZIP file creation completed");
        return byteArrayOutputStream.toByteArray();
    }
}