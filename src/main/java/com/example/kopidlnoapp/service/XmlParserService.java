package com.example.kopidlnoapp.service;

import com.example.kopidlnoapp.entity.Municipality;
import com.example.kopidlnoapp.entity.MunicipalityPart;
import com.example.kopidlnoapp.repository.MunicipalityPartRepository;
import com.example.kopidlnoapp.repository.MunicipalityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@Service
public class XmlParserService {

    private static final Logger logger = LoggerFactory.getLogger(XmlParserService.class);
    private static final String NAMESPACE_OBEC = "urn:cz:isvs:ruian:schemas:ObecIntTypy:v1";
    private static final String NAMESPACE_CASTOBCE = "urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1";

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private MunicipalityPartRepository municipalityPartRepository;

    public Document loadXMLDocument(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            logger.info("Root element: {}", doc.getDocumentElement().getNodeName());
            return doc;
        } catch (Exception e) {
            logger.error("Failed to load XML document", e);
            return null;
        }
    }

    public void parseAndSaveData(Document doc) {
        NodeList obceList = doc.getElementsByTagNameNS("*", "Obce");
        if (obceList.getLength() > 0) {
            Element obceElement = (Element) obceList.item(0);
            NodeList municipalities = obceElement.getElementsByTagNameNS("*", "Obec");
            logger.info("Found {} municipalities", municipalities.getLength());
            for (int i = 0; i < municipalities.getLength(); i++) {
                Element municipalityElement = (Element) municipalities.item(i);
                logger.info("Processing element: {} with namespace: {}", municipalityElement.getTagName(), municipalityElement.getNamespaceURI());
                parseAndSaveMunicipality(municipalityElement);
            }
        } else {
            logger.warn("No municipalities found.");
        }

        NodeList castiObciList = doc.getElementsByTagNameNS("*", "CastiObci");
        if (castiObciList.getLength() > 0) {
            Element castiObciElement = (Element) castiObciList.item(0);
            NodeList municipalityParts = castiObciElement.getElementsByTagNameNS("*", "CastObce");
            logger.info("Found {} municipality parts", municipalityParts.getLength());
            for (int i = 0; i < municipalityParts.getLength(); i++) {
                Element municipalityPartElement = (Element) municipalityParts.item(i);
                logger.info("Processing element: {} with namespace: {}", municipalityPartElement.getTagName(), municipalityPartElement.getNamespaceURI());
                parseAndSaveMunicipalityPart(municipalityPartElement);
            }
        } else {
            logger.warn("No municipality parts found.");
        }
    }

    private void parseAndSaveMunicipality(Element municipalityElement) {
        try {
            NodeList codeList = municipalityElement.getElementsByTagNameNS(NAMESPACE_OBEC, "Kod");
            NodeList nameList = municipalityElement.getElementsByTagNameNS(NAMESPACE_OBEC, "Nazev");

            if (codeList.getLength() == 0 || nameList.getLength() == 0) {
                logger.warn("Municipality element missing required children.");
                return;
            }

            String code = codeList.item(0).getTextContent();
            String name = nameList.item(0).getTextContent();

            Municipality municipality = new Municipality();
            municipality.setCode(code);
            municipality.setName(name);

            municipalityRepository.save(municipality);

            logger.info("Saved Municipality: {}", municipality);
        } catch (Exception e) {
            logger.error("Failed to parse and save municipality", e);
        }
    }

    private void parseAndSaveMunicipalityPart(Element municipalityPartElement) {
        try {
            NodeList codeList = municipalityPartElement.getElementsByTagNameNS(NAMESPACE_CASTOBCE, "Kod");
            NodeList nameList = municipalityPartElement.getElementsByTagNameNS(NAMESPACE_CASTOBCE, "Nazev");
            NodeList municipalityCodeList = municipalityPartElement.getElementsByTagNameNS(NAMESPACE_OBEC, "Kod");

            if (codeList.getLength() == 0 || nameList.getLength() == 0 || municipalityCodeList.getLength() == 0) {
                logger.warn("Municipality part element missing required children.");
                return;
            }

            String code = codeList.item(0).getTextContent();
            String name = nameList.item(0).getTextContent();
            String municipalityCode = municipalityCodeList.item(0).getTextContent();

            MunicipalityPart municipalityPart = new MunicipalityPart();
            municipalityPart.setCode(code);
            municipalityPart.setName(name);
            municipalityPart.setMunicipalityCode(municipalityCode);

            municipalityPartRepository.save(municipalityPart);

            logger.info("Saved MunicipalityPart: {}", municipalityPart);
        } catch (Exception e) {
            logger.error("Failed to parse and save municipality part", e);
        }
    }
}