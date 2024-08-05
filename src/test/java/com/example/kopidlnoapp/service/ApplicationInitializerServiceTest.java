package com.example.kopidlnoapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApplicationInitializerServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializerServiceTest.class);

    @Mock
    private FileDownloadService fileDownloadService;

    @Mock
    private XmlParserService xmlParserService;

    @Mock
    private Document document;

    @InjectMocks
    private ApplicationInitializerService applicationInitializerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationInitializerService = new ApplicationInitializerService("src/test/resources/temp", "unzipped.xml", fileDownloadService, xmlParserService);
    }

    @Test
    void initialize() throws Exception {
        // Mock the behavior of fileDownloadService and xmlParserService
        doNothing().when(fileDownloadService).downloadAndUnzip();
        when(xmlParserService.loadXMLDocument("src/test/resources/temp/unzipped.xml")).thenReturn(document);
        doNothing().when(xmlParserService).parseAndSaveData(document);

        // Call the method under test
        applicationInitializerService.initialize();

        // Verify interactions and assert conditions
        verify(fileDownloadService, times(1)).downloadAndUnzip();
        verify(xmlParserService, times(1)).loadXMLDocument("src/test/resources/temp/unzipped.xml");
        verify(xmlParserService, times(1)).parseAndSaveData(document);
        assertNotNull(document);

        logger.info("Test for initialize() completed successfully.");
    }

    @Test
    void initialize_withLoadXmlDocumentFailure() throws Exception {
        // Mock the behavior of fileDownloadService and xmlParserService
        doNothing().when(fileDownloadService).downloadAndUnzip();
        when(xmlParserService.loadXMLDocument("src/test/resources/temp/unzipped.xml")).thenReturn(null);

        // Call the method under test
        applicationInitializerService.initialize();

        // Verify interactions and assert conditions
        verify(fileDownloadService, times(1)).downloadAndUnzip();
        verify(xmlParserService, times(1)).loadXMLDocument("src/test/resources/temp/unzipped.xml");
        verify(xmlParserService, times(0)).parseAndSaveData(document);

        logger.info("Test for initialize() with XML document load failure completed successfully.");
    }
}