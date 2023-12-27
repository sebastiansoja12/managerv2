package com.warehouse.zebra.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;

import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.zebra.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.Response;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ReturnResponseDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.mapper.ReturnRequestMapper;
import com.warehouse.zebra.infrastructure.adapter.secondary.mapper.ReturnResponseMapper;
import com.warehouse.zebra.infrastructure.adapter.secondary.properties.Properties;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnServiceAdapter extends RestGatewaySupport implements ReturnServicePort {


    private final ReturnProperties returnProperties;

    private final ReturnRequestMapper requestMapper = getMapper(ReturnRequestMapper.class);

    private final ReturnResponseMapper responseMapper = getMapper(ReturnResponseMapper.class);


    @Override
    public Response processReturn(Request zebraRequest) {
        final ReturnConfiguration configuration = new ReturnConfiguration(returnProperties);
        return processReturn(zebraRequest, configuration);
    }

    private Response processReturn(Request request, ReturnConfiguration configuration) {

        final ResponseEntity<ReturnResponseDto> responseEntity = getRestTemplate()
                .postForEntity(configuration.getUrl() + configuration.getEndpoint(),
                        requestMapper.map(request), ReturnResponseDto.class);

        return Response.builder()
                .processReturns(responseMapper.map(Objects.requireNonNull(responseEntity.getBody()).processReturn()))
                .zebraId(request.getZebraDeviceInformation().getZebraId())
                .username(request.getZebraDeviceInformation().getUsername())
                .version(request.getZebraDeviceInformation().getVersion())
                .build();
    }


    private record ReturnConfiguration(ReturnProperties returnProperties) implements Properties {

		@Override
		public String getUrl() {
			return returnProperties.getUrl();
		}

		@Override
		public String getEndpoint() {
			return returnProperties.getEndpoint();
		}
	}
}
