package com.warehouse.returning.infrastructure.adapter.secondary;

import java.net.URI;
import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.returning.api.dto.ReturnPageDto;
import com.warehouse.commonassets.identificator.DepartmentId;
import java.util.Optional;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnDetailsMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ReturnPackageApi;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ReturnPageApi;
import org.springframework.web.util.UriComponentsBuilder;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.application.port.secondary.ReturningTrackManagerServicePort;
import com.warehouse.returning.domain.vo.CreatedReturn;
import com.warehouse.returning.domain.vo.CreateReturnRequest;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnState;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ChangeReasonCodeRequest;
import com.warehouse.returning.infrastructure.adapter.secondary.api.TokenValidationResponse;
import com.warehouse.returning.infrastructure.adapter.secondary.api.PickupRequest;
import com.warehouse.returning.infrastructure.adapter.secondary.api.LinkRequest;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ReturnRequestApi;
import com.warehouse.returning.infrastructure.adapter.secondary.api.RtmCreateResponse;
import com.warehouse.returning.infrastructure.adapter.secondary.api.RtmReturnResponse;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageOutboundMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageResponseMapper;
import com.warehouse.tools.returning.ReturnProperties;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReturningTrackManagerServiceClientAdapter implements ReturningTrackManagerServicePort {

    private final ReturnDetailsMapper returnDetailsMapper;
    private final RestClient restClient;
    private final ReturnProperties returnProperties;
    private final CurrentUserApiService currentUserApiService;
    private final ReturnPackageOutboundMapper returnPackageOutboundMapper;
    private final ReturnPackageResponseMapper returnPackageResponseMapper;

    public ReturningTrackManagerServiceClientAdapter(final RestClient.Builder restClientBuilder,
                                                     final ReturnProperties returnProperties,
                                                     final CurrentUserApiService currentUserApiService,
                                                     final ReturnPackageOutboundMapper returnPackageOutboundMapper,
                                                     final ReturnPackageResponseMapper returnPackageResponseMapper,
                                                     final ReturnDetailsMapper returnDetailsMapper) {
        this.returnDetailsMapper = returnDetailsMapper;
        this.restClient = restClientBuilder.clone()
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    throw new RestException(response.getStatusCode().value(), "RTM rejected the return operation");
                })
                .build();
        this.returnProperties = returnProperties;
        this.currentUserApiService = currentUserApiService;
        this.returnPackageOutboundMapper = returnPackageOutboundMapper;
        this.returnPackageResponseMapper = returnPackageResponseMapper;
    }

    @Override
    public List<CreatedReturn> create(final CreateReturnRequest createReturnRequest) {
        final ReturnRequestApi request = returnPackageOutboundMapper.map(createReturnRequest);
        final RtmCreateResponse response = restClient.post()
                .uri(baseUrl())
                .header("Authorization", authorization())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(RtmCreateResponse.class);
        if (response == null || response.processReturn() == null) {
            throw new IllegalStateException("RTM returned an invalid create response");
        }
        return returnPackageResponseMapper.map(response);
    }

    @Override
    public ReturnState get(final ReturnPackageId returnId) {
        return map(restClient.get().uri(baseUrl() + "/" + returnId.value())
                .header("Authorization", authorization()).retrieve().body(RtmReturnResponse.class));
    }

    @Override
    public ReturnState getByShipmentId(final ShipmentId shipmentId) {
        final RtmReturnResponse response = restClient.get().uri(baseUrl() + "/shipment/" + shipmentId.getValue())
                .header("Authorization", authorization()).retrieve().body(RtmReturnResponse.class);
        if (response == null) {
            throw new RestException(404, "Return package not found");
        }
        return map(response);
    }

    @Override
    public void startProcessing(final ReturnPackageId returnId) {
        changeStatus(returnId, "process");
    }

    @Override
    public void complete(final ReturnPackageId returnId) {
        changeStatus(returnId, "complete");
    }

    @Override
    public void cancel(final ReturnPackageId returnId) {
        restClient.delete().uri(baseUrl() + "/" + returnId.value())
                .header("Authorization", authorization()).retrieve().toBodilessEntity();
    }

    @Override
    public void changeReasonCode(final ReturnPackageId returnId, final ReasonCode reasonCode) {
        restClient.put().uri(baseUrl() + "/reason-code")
                .header("Authorization", authorization()).contentType(MediaType.APPLICATION_JSON)
                .body(new ChangeReasonCodeRequest(returnId, reasonCode))
                .retrieve().toBodilessEntity();
    }

    @Override
    public boolean validateToken(final ShipmentId shipmentId, final ReturnToken returnToken) {
        final TokenValidationResponse response = restClient.post().uri(baseUrl() + "/token/validate")
                .header("Authorization", authorization()).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("shipmentId", shipmentId.getValue(), "returnToken", returnToken.value()))
                .retrieve().body(TokenValidationResponse.class);
        if (response == null) {
            throw new IllegalStateException("RTM returned an empty token validation response");
        }
        return response.valid();
    }

    private void changeStatus(final ReturnPackageId returnId, final String action) {
        restClient.put().uri(baseUrl() + "/" + returnId.value() + "/" + action)
                .header("Authorization", authorization()).contentType(MediaType.APPLICATION_JSON)
                .retrieve().toBodilessEntity();
    }

    @Override
    public ReturnState pickup(final ReturnPackageId returnId, final UUID pickupId,
                              final DepartmentId scanDepartmentId) {
        return map(restClient.put().uri(baseUrl() + "/" + returnId.value() + "/pickup")
                .header("Authorization", authorization()).body(new PickupRequest(
                        pickupId, scanDepartmentId))
                .retrieve().body(RtmReturnResponse.class));
    }

    @Override
    public ReturnState linkReturnShipment(final ReturnPackageId returnId, final ShipmentId returnShipmentId) {
        return map(restClient.put().uri(baseUrl() + "/" + returnId.value() + "/return-shipment")
                .header("Authorization", authorization())
                .body(new LinkRequest(returnShipmentId))
                .retrieve().body(RtmReturnResponse.class));
    }

    private ReturnState map(final RtmReturnResponse response) {
        if (response == null || response.returnPackageId() == null || response.shipmentId() == null) {
            throw new IllegalStateException("RTM returned incomplete return state");
        }
        return returnPackageResponseMapper.map(response);
    }

    private String authorization() {
        return "Bearer " + currentUserApiService.getCurrentUserAuthentication().jwtToken();
    }

    private String baseUrl() {
        return returnProperties.getUrl() + returnProperties.getEndpoint();
    }

    @Override
    public ReturnDetailsDto getDetails(final ReturnPackageId returnId) {
        final ReturnPackageApi response = restClient.get().uri(baseUrl() + "/" + returnId.value())
                .header("Authorization", authorization()).retrieve().body(ReturnPackageApi.class);
        if (response == null) {
            throw new RestException(404, "Return package not found");
        }
        return returnDetailsMapper.map(response);
    }

    @Override
    public ReturnPageDto getReturns(final DepartmentId departmentId, final int page, final int size) {
        final URI uri = UriComponentsBuilder.fromUriString(baseUrl())
                .queryParam("departmentId", departmentId.value()).queryParam("page", page)
                .queryParam("size", size).build().encode().toUri();
        final ReturnPageApi response = restClient.get().uri(uri).header("Authorization", authorization())
                .retrieve().body(ReturnPageApi.class);
        if (response == null) {
            throw new IllegalStateException("RTM returned an empty return page");
        }
        return returnDetailsMapper.map(response);
    }

    @Override
    public Optional<ReturnDetailsDto> findByShipmentId(final ShipmentId shipmentId) {
        return Optional.ofNullable(restClient.get().uri(baseUrl() + "/shipment/" + shipmentId.getValue())
                .header("Authorization", authorization()).retrieve().body(ReturnPackageApi.class))
                .map(returnDetailsMapper::map);
    }
}
