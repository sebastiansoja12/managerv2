package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.time.LocalDateTime;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.deliveryreturn.domain.model.ReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.port.secondary.ReturnTokenServicePort;
import com.warehouse.deliveryreturn.domain.vo.ReturnTokenResponse;
import com.warehouse.deliveryreturn.domain.vo.ReturnTokenValidationRequest;
import com.warehouse.deliveryreturn.domain.vo.ReturnTokenValidationResult;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ReturnTokenRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ReturnTokenResponseDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ReturnTokenValidationRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ReturnTokenValidationResponseDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.DeliveryReturnTokenRequestMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.DeliveryReturnTokenResponseMapper;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessCommunicationEvent;
import com.warehouse.tools.returntoken.ReturnTokenProperties;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class ReturnTokenServiceAdapter implements ReturnTokenServicePort {


    private final ReturnTokenProperties returnTokenProperties;

    private final RestClient restClient;

    private final ProcessHubEventPublisher processHubEventPublisher;

    private final CurrentUserApiService currentUserApiService;
    
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

    @Override
    public ReturnTokenValidationResult validate(final ReturnTokenValidationRequest request) {
        final ReturnTokenValidationRequestDto requestDto = new ReturnTokenValidationRequestDto(
                request.shipmentId().getValue(),
                request.returnToken() != null ? request.returnToken().value() : null
        );
        final String requestLog = formatValidationRequest(requestDto);
        try {
            final ReturnTokenValidationResponseDto response = restClient
                    .post()
                    .uri("/v2/api/returns/token/validate")
                    .body(requestDto)
                    .header("Authorization", "Bearer " + currentUserApiService.getCurrentUserAuthentication().jwtToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(ReturnTokenValidationResponseDto.class);
            final ReturnTokenValidationResult result = map(response, request);
            publishCommunication(request, requestLog, formatValidationResponse(response), null);
            return result;
        } catch (final RuntimeException ex) {
            log.error("Error while validating return token for shipment {}", request.shipmentId().getValue(), ex);
            publishCommunication(request, requestLog, ex.getMessage(), ex.getMessage());
            return ReturnTokenValidationResult.invalid(request.shipmentId());
        }
    }

    private ReturnTokenValidationResult map(final ReturnTokenValidationResponseDto response,
                                            final ReturnTokenValidationRequest request) {
        if (response == null) {
            return ReturnTokenValidationResult.invalid(request.shipmentId());
        }
        return new ReturnTokenValidationResult(request.shipmentId(), response.valid(), response.message());
    }

    private void publishCommunication(final ReturnTokenValidationRequest request,
                                      final String requestLog,
                                      final String responseLog,
                                      final String faultDescription) {
        processHubEventPublisher.publish(new ProcessCommunicationEvent(
                request.processId(),
                ServiceType.DELIVERY_OPERATION,
                ServiceType.RETURNING_TRACK_MANAGER,
                ProcessType.RETURN,
                LocalDateTime.now(),
                requestLog,
                responseLog,
                faultDescription
        ));
    }

    private String formatValidationRequest(final ReturnTokenValidationRequestDto request) {
        return "ReturnTokenValidationRequest{shipmentId=%s, returnToken=%s}"
                .formatted(request.shipmentId(), request.returnToken());
    }

    private String formatValidationResponse(final ReturnTokenValidationResponseDto response) {
        if (response == null) {
            return null;
        }
        return "ReturnTokenValidationResponse{shipmentId=%s, valid=%s, message=%s}"
                .formatted(response.shipmentId(), response.valid(), response.message());
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
