package com.example.kopidlnoapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ApplicationInitializerService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializerService.class);

    @Autowired
    private FileDownloadService fileDownloadService;

    @Autowired
    private XmlParserService xmlParserService;

    public void initialize() throws Exception {
        fileDownloadService.downloadAndUnzip();
        Document doc = xmlParserService.loadXMLDocument("kopidlno.xml");

        if (doc != null) {
            logger.info("XML document loaded successfully.");
            xmlParserService.parseAndSaveData(doc);
        } else {
            logger.error("Failed to load XML document.");
        }
    }
}