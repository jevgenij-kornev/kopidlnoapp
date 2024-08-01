package com.example.kopidlnoapp.runner;

import com.example.kopidlnoapp.service.FileDownloadService;
import com.example.kopidlnoapp.service.XmlParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class FileDownloadRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadRunner.class);

    @Autowired
    private FileDownloadService fileDownloadService;

    @Autowired
    private XmlParserService xmlParserService;

    @Override
    public void run(String... args) throws Exception {
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