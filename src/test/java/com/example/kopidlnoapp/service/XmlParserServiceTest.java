package com.example.kopidlnoapp.service;

import com.example.kopidlnoapp.entity.Municipality;
import com.example.kopidlnoapp.entity.MunicipalityPart;
import com.example.kopidlnoapp.repository.MunicipalityPartRepository;
import com.example.kopidlnoapp.repository.MunicipalityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class XmlParserServiceTest {

    @Mock
    private MunicipalityRepository municipalityRepository;

    @Mock
    private MunicipalityPartRepository municipalityPartRepository;

    @InjectMocks
    private XmlParserService xmlParserService;

    private Document document;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        Path xmlFilePath = Paths.get("src/test/resources/test-to-zip.xml");
        File xmlFile = xmlFilePath.toFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        document = dBuilder.parse(xmlFile);
        document.getDocumentElement().normalize();
    }

    @Test
    void loadXMLDocument() {
        Document loadedDoc = xmlParserService.loadXMLDocument("src/test/resources/test-to-zip.xml");
        assertNotNull(loadedDoc);
        assertEquals("rootElement", loadedDoc.getDocumentElement().getNodeName());

        // Verify that the "Obce" element exists
        NodeList obceList = loadedDoc.getElementsByTagNameNS("*", "Obce");
        assertEquals(1, obceList.getLength(), "There should be one 'Obce' element");

        // Verify the "Obec" element inside "Obce"
        Element obceElement = (Element) obceList.item(0);
        NodeList obecList = obceElement.getElementsByTagNameNS("*", "Obec");
        assertEquals(1, obecList.getLength(), "There should be one 'Obec' element");

        Element obecElement = (Element) obecList.item(0);
        assertEquals("12345", obecElement.getElementsByTagNameNS("*", "Kod").item(0).getTextContent(), "Obec Kod should be '12345'");
        assertEquals("Test Municipality", obecElement.getElementsByTagNameNS("*", "Nazev").item(0).getTextContent(), "Obec Nazev should be 'Test Municipality'");

        // Verify that the "CastiObci" element exists
        NodeList castiObciList = loadedDoc.getElementsByTagNameNS("*", "CastiObci");
        assertEquals(1, castiObciList.getLength(), "There should be one 'CastiObci' element");

        // Verify the "CastObce" element inside "CastiObci"
        Element castiObciElement = (Element) castiObciList.item(0);
        NodeList castObceList = castiObciElement.getElementsByTagNameNS("*", "CastObce");
        assertEquals(1, castObceList.getLength(), "There should be one 'CastObce' element");

        Element castObceElement = (Element) castObceList.item(0);
        assertEquals("67890", castObceElement.getElementsByTagNameNS("*", "Kod").item(0).getTextContent(), "CastObce Kod should be '67890'");
        assertEquals("Test Municipality Part", castObceElement.getElementsByTagNameNS("*", "Nazev").item(0).getTextContent(), "CastObce Nazev should be 'Test Municipality Part'");
        assertEquals("12345", castObceElement.getElementsByTagNameNS("*", "Kod").item(1).getTextContent(), "Obec Kod in CastObce should be '12345'");
    }

    @Test
    void parseAndSaveData() {
        xmlParserService.parseAndSaveData(document);
        verify(municipalityRepository, times(1)).save(any(Municipality.class));
        verify(municipalityPartRepository, times(1)).save(any(MunicipalityPart.class));
    }
}