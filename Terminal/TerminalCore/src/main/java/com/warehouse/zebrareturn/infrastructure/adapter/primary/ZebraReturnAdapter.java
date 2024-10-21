package com.warehouse.zebrareturn.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.response.Response;
import com.warehouse.zebrareturn.domain.port.primary.ZebraReturnPort;
import com.warehouse.zebrareturn.infrastructure.adapter.primary.mapper.ZebraResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.zebra.infrastructure.api.requestmodel.ZebraDeviceInformation;
import com.warehouse.zebra.infrastructure.api.requestmodel.ZebraRequest;
import com.warehouse.zebra.infrastructure.api.responsemodel.ZebraResponse;
import com.warehouse.zebrareturn.infrastructure.adapter.primary.mapper.ZebraRequestMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/zebra/initialize")
@RequiredArgsConstructor
public class ZebraReturnAdapter {

    private final ZebraReturnPort zebraReturnPort;

    private final ZebraRequestMapper requestMapper = getMapper(ZebraRequestMapper.class);

    private final ZebraResponseMapper responseMapper = getMapper(ZebraResponseMapper.class);

    private final Logger logger = LoggerFactory.getLogger(ZebraReturnAdapter.class);


    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ZebraResponse processRequest(@RequestBody ZebraRequest zebraRequest) {

        final ZebraDeviceInformation zebraDeviceInformation = zebraRequest.getZebraDeviceInformation();

		logger.info("Initializing process from Zebra device: ID - {}, Version - {}, Responsible User - {}, Depot - {}",
				zebraDeviceInformation.getZebraId(), zebraDeviceInformation.getVersion(),
				zebraDeviceInformation.getUsername(), zebraDeviceInformation.getDepotCode());

        final Request request = requestMapper.map(zebraRequest);

        final Response response = zebraReturnPort.processRequest(request);

        return responseMapper.map(response);
    }
    
}
