package com.warehouse.xmlconverter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.warehouse.terminal.request.TerminalRequest;

public class XmlToStringServiceImplTest {

    private final XmlToStringService xmlToStringService = new XmlToStringServiceImpl();

    @Test
    void shouldParseXmlToString() {
        // given
        final TerminalRequest terminalRequest = new TerminalRequest();
        final String expectedXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <TerminalRequest/>
                """;
        // when
        final String xmlString = xmlToStringService.convertToString(terminalRequest);
        // then
        assertEquals(expectedXml, xmlString);
    }
}
