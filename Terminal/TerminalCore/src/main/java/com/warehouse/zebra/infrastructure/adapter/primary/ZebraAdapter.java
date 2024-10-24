package com.warehouse.zebra.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.zebra.domain.port.primary.ZebraPort;
import com.warehouse.zebra.infrastructure.adapter.primary.mapper.ZebraRequestMapper;
import com.warehouse.zebra.infrastructure.adapter.primary.mapper.ZebraResponseMapper;
import com.warehouse.zebra.infrastructure.api.requestmodel.TerminalDeviceInformation;
import com.warehouse.zebra.infrastructure.api.requestmodel.TerminalRequest;
import com.warehouse.zebra.infrastructure.api.responsemodel.TerminalResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/zebra")
@RequiredArgsConstructor
public class ZebraAdapter {

    private final ZebraPort zebraPort;

    private final ZebraRequestMapper requestMapper = getMapper(ZebraRequestMapper.class);

    private final ZebraResponseMapper responseMapper = getMapper(ZebraResponseMapper.class);

    private final Logger logger = LoggerFactory.getLogger(ZebraAdapter.class);

    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public TerminalResponse processRequest(@RequestBody final TerminalRequest terminalRequest) {

        final TerminalDeviceInformation terminalDeviceInformation = terminalRequest.getTerminalDeviceInformation();

		logger.info("Detected request from Zebra device: ID - {}, Version - {}, Responsible User - {}, Depot - {}",
				terminalDeviceInformation.getTerminalId(), terminalDeviceInformation.getVersion(),
				terminalDeviceInformation.getUsername(), terminalDeviceInformation.getDepotCode());

        final Request request = requestMapper.map(terminalRequest);

        final Response response = zebraPort.processRequest(request);

        return responseMapper.map(response);
    }
}
