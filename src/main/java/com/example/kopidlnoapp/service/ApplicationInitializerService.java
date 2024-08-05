package com.example.kopidlnoapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class ApplicationInitializerService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializerService.class);

    private final String tempDir;
    private final String outputFileName;

    private final FileDownloadService fileDownloadService;
    private final XmlParserService xmlParserService;

    public ApplicationInitializerService(
            @Value("${file.temp-dir}") String tempDir,
            @Value("${file.output}") String outputFileName,
            FileDownloadService fileDownloadService,
            XmlParserService xmlParserService) {
        this.tempDir = tempDir;
        this.outputFileName = outputFileName;
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