package com.warehouse.xmlconverter;

import java.io.StringWriter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;


public class XmlToStringServiceImpl implements XmlToStringService {

    public String convertToString(final Object obj) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());

            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            final StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);

            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException("Error converting object to XML: " + e.getMessage(), e);
        }
    }
}
