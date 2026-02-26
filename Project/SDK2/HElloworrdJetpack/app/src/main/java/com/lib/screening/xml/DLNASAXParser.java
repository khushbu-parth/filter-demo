package com.lib.screening.xml;

import org.seamless.xml.SAXParser;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParserFactory;

public class DLNASAXParser extends SAXParser {
    @Override
    protected XMLReader create() {
        try {
            SAXParserFactory newInstance = SAXParserFactory.newInstance();
            newInstance.setFeature("http://xml.org/sax/features/external-general-entities", false);
            newInstance.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            if (getSchemaSources() != null) {
                newInstance.setSchema(createSchema(getSchemaSources()));
            }
            XMLReader xMLReader = newInstance.newSAXParser().getXMLReader();
            xMLReader.setErrorHandler(getErrorHandler());
            return xMLReader;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
