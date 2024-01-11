package com.warehouse.zebra.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.zebra.domain.port.primary.ZebraInitializePort;
import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.Response;
import com.warehouse.zebra.infrastructure.adapter.primary.mapper.ZebraRequestMapper;
import com.warehouse.zebra.infrastructure.adapter.primary.mapper.ZebraResponseMapper;
import com.warehouse.zebra.infrastructure.api.requestmodel.ZebraDeviceInformation;
import com.warehouse.zebrainitialize.model.ZebraInitializeRequest;
import com.warehouse.zebrainitialize.model.ZebraInitializeResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/zebra/initialize")
@RequiredArgsConstructor
public class ZebraInitializeAdapter {
    

    private final ZebraInitializePort zebraInitializePort;
    private final ZebraRequestMapper requestMapper = getMapper(ZebraRequestMapper.class);

    private final ZebraResponseMapper responseMapper = getMapper(ZebraResponseMapper.class);

    private final Logger logger = LoggerFactory.getLogger(ZebraAdapter.class);


    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ZebraInitializeResponse processRequest(@RequestBody ZebraInitializeRequest zebraRequest) {

        final ZebraDeviceInformation zebraDeviceInformation = zebraRequest.getZebraDeviceInformation();

		logger.info("Initializing process from Zebra device: ID - {}, Version - {}, Responsible User - {}, Depot - {}",
				zebraDeviceInformation.getZebraId(), zebraDeviceInformation.getVersion(),
				zebraDeviceInformation.getUsername(), zebraDeviceInformation.getDepotCode());

        final Request request = requestMapper.map(zebraRequest);

        final Response response = zebraInitializePort.processRequest(request);

        return responseMapper.mapToInitializeResponse(response);
    }
    
}
