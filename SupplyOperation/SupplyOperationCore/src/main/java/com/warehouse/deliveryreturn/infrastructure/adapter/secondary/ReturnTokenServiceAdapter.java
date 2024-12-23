package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.model.ReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.port.secondary.ReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.vo.ReturnTokenResponse;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ReturnTokenRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ReturnTokenResponseDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.DeliveryReturnTokenRequestMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.DeliveryReturnTokenResponseMapper;
import com.warehouse.tools.returntoken.ReturnTokenProperties;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
public class ReturnTokenServiceAdapter implements ReturnTokenServicePort {


    private final ReturnTokenProperties returnTokenProperties;

    private final RestClient restClient;
    
    private final DeliveryReturnTokenRequestMapper requestMapper = getMapper(DeliveryReturnTokenRequestMapper.class);

    private final DeliveryReturnTokenResponseMapper responseMapper = getMapper(DeliveryReturnTokenResponseMapper.class);


	private ReturnTokenResponseDto signWithReturnToken(final PropertiesConfiguration configuration,
			final ReturnTokenRequestDto request) {

        final ResponseEntity<? extends ReturnTokenResponseDto> responseEntity = restClient
                .post()
				.uri("/v2/api/{endpoint}", configuration.getEndpoint())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ReturnTokenResponseDto.class);

        return responseEntity.getBody();
    }
    
    @Override
    public ReturnTokenResponse sign(final ReturnTokenRequest returnTokenRequest) {
        final PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(returnTokenProperties);
        final ReturnTokenRequestDto request = requestMapper.map(returnTokenRequest);
        final ReturnTokenResponseDto response = signWithReturnToken(propertiesConfiguration, request);

		return responseMapper.map(response);
    }
    
    
    @RequiredArgsConstructor
    private static class PropertiesConfiguration implements Properties {
        
        private final ReturnTokenProperties returnTokenProperties;

        @Override
        public String getUrl() {
            return returnTokenProperties.getUrl();
        }

        @Override
        public String getEndpoint() {
            return returnTokenProperties.getEndpoint();
        }
    }
}
