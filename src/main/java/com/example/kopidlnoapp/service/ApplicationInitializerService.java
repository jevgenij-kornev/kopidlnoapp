package com.example.kopidlnoapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ApplicationInitializerService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializerService.class);

    @Value("${file.temp-dir}")
    private String tempDir;

    @Value("${file.output}")
    private String outputFileName;

    private final FileDownloadService fileDownloadService;
    private final XmlParserService xmlParserService;

    public ApplicationInitializerService(FileDownloadService fileDownloadService, XmlParserService xmlParserService) {
        this.fileDownloadService = fileDownloadService;
        this.xmlParserService = xmlParserService;
    }

    public void initialize() throws Exception {
        fileDownloadService.downloadAndUnzip();
        Document doc = xmlParserService.loadXMLDocument(tempDir + "/" + outputFileName);

        if (doc != null) {
            logger.info("XML document loaded successfully.");
            xmlParserService.parseAndSaveData(doc);
        } else {
            logger.error("Failed to load XML document.");
        }
    }
}