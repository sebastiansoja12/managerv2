package com.warehouse.zebrareturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.Collections;
import java.util.Objects;

import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.response.Response;
import com.warehouse.zebrareturn.infrastructure.adapter.secondary.mapper.ReturnResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;

import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.zebrareturn.infrastructure.adapter.secondary.api.ReturnResponseDto;
import com.warehouse.zebrareturn.infrastructure.adapter.secondary.mapper.ReturnRequestMapper;
import com.warehouse.zebrareturn.infrastructure.adapter.secondary.properties.Properties;
import com.warehouse.zebrareturn.domain.port.secondary.ReturnServicePort;

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

        return new Response(
                request.getZebraDeviceInformation(),
                responseMapper.map(Objects.requireNonNull(responseEntity.getBody()).processReturn()),
                Collections.emptyList()
        );
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
