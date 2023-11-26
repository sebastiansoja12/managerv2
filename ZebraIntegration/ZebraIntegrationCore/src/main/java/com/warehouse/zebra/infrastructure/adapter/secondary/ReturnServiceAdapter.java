package com.warehouse.zebra.infrastructure.adapter.secondary;

import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.Response;
import com.warehouse.zebra.domain.port.secondary.ReturnServicePort;
import com.warehouse.zebra.infrastructure.adapter.secondary.mapper.ReturnResponseMapper;
import com.warehouse.zebra.infrastructure.adapter.secondary.properties.Properties;
import com.warehouse.zebra.infrastructure.adapter.secondary.properties.ReturnProperty;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ReturnResponseDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.mapper.ReturnRequestMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;

import java.util.Objects;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class ReturnServiceAdapter extends RestGatewaySupport implements ReturnServicePort {


    private final ReturnProperty returnProperty;

    private final ReturnRequestMapper requestMapper = getMapper(ReturnRequestMapper.class);

    private final ReturnResponseMapper responseMapper = getMapper(ReturnResponseMapper.class);


    @Override
    public Response processReturn(Request zebraRequest) {
        final ReturnConfiguration configuration = new ReturnConfiguration(returnProperty);
        return processReturn(zebraRequest, configuration);
    }

    private Response processReturn(Request request, ReturnConfiguration configuration) {

        final ResponseEntity<ReturnResponseDto> responseEntity = getRestTemplate()
                .postForEntity(String.format(configuration.getUrl(), configuration.getEndpoint()),
                        requestMapper.map(request), ReturnResponseDto.class);

        return Response.builder()
                .processReturns(responseMapper.map(Objects.requireNonNull(responseEntity.getBody()).processReturn()))
                .zebraId(request.getZebraDeviceInformation().getZebraId())
                .username(request.getZebraDeviceInformation().getUsername())
                .version(request.getZebraDeviceInformation().getVersion())
                .build();
    }


    private static class ReturnConfiguration implements Properties {

        private final ReturnProperty returnProperty;

        private ReturnConfiguration(ReturnProperty returnProperty) {
            this.returnProperty = returnProperty;
        }

        @Override
        public String getUrl() {
            return returnProperty.getUrl();
        }

        @Override
        public String getEndpoint() {
            return returnProperty.getEndpoint();
        }
    }
}
