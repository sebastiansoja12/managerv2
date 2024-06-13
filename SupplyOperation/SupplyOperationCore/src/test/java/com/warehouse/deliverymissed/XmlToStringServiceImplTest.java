package com.warehouse.deliverymissed;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.warehouse.deliverymissed.domain.service.XmlToStringService;
import com.warehouse.deliverymissed.domain.service.XmlToStringServiceImpl;
import com.warehouse.terminal.enumeration.ProcessType;
import com.warehouse.terminal.request.TerminalRequest;

public class XmlToStringServiceImplTest {

    private final XmlToStringService xmlToStringService = new XmlToStringServiceImpl();

    @Test
    void shouldParseXmlToString() {
        // given
        final TerminalRequest terminalRequest = new TerminalRequest(ProcessType.CREATED, null, 1L);
        String expectedXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <TerminalRequest>
                    <ProcessType>CREATED</ProcessType>
                    <ParcelID>1</ParcelID>
                </TerminalRequest>
                """;
        // when
        final String xmlString = xmlToStringService.convertToString(terminalRequest);
        // then
        assertEquals(expectedXml, xmlString);
    }
}
