package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.model.ReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.port.secondary.DeliveryReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnSignature;
import com.warehouse.deliveryreturn.domain.vo.ReturnTokenResponse;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentReturnTokenRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.TokenDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.DeliveryReturnTokenRequestMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.DeliveryReturnTokenResponseMapper;
import com.warehouse.tools.returntoken.ReturnTokenProperties;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
public class DeliveryReturnServiceAdapter implements DeliveryReturnTokenServicePort {


    private final ReturnTokenProperties returnTokenProperties;

    private final RestClient restClient;
    
    private final DeliveryReturnTokenRequestMapper requestMapper = getMapper(DeliveryReturnTokenRequestMapper.class);

    private final DeliveryReturnTokenResponseMapper responseMapper = getMapper(DeliveryReturnTokenResponseMapper.class);


	private DeliveryReturnSignature signWithReturnToken(PropertiesConfiguration configuration,
			ShipmentReturnTokenRequestDto request) {

        final ResponseEntity<? extends TokenDto> responseEntity = restClient
                .post()
				.uri("/v2/api/{endpoint}", configuration.getEndpoint())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(TokenDto.class);

        return responseMapper.map(responseEntity.getBody());
    }
    
	private DeliveryReturnSignature sign(PropertiesConfiguration configuration,
			ShipmentReturnTokenRequestDto request) {
        return signWithReturnToken(configuration, request);
	}
    
    @Override
    public ReturnTokenResponse sign(ReturnTokenRequest returnTokenRequest) {
        final PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(returnTokenProperties);
        final List<ShipmentReturnTokenRequestDto> returnTokenRequests = requestMapper.map(returnTokenRequest);
        
        final List<DeliveryReturnSignature> signatures = returnTokenRequests.stream()
                .map(shipmentTokenRequest -> sign(propertiesConfiguration, shipmentTokenRequest))
                .toList();

		return new ReturnTokenResponse(signatures, returnTokenRequest.getSupplier().getSupplierCode());
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
