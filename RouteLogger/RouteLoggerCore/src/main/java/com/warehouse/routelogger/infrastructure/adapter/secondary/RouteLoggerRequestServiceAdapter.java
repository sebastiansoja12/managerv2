package com.warehouse.routelogger.infrastructure.adapter.secondary;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.warehouse.routelogger.domain.model.Request;
import com.warehouse.routelogger.domain.port.secondary.RouteLoggerRequestServicePort;
import com.warehouse.routelogger.dto.ShipmentIdDto;
import com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto.TerminalRequestDto;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RouteLoggerRequestServiceAdapter implements RouteLoggerRequestServicePort {


    private final RouteTrackerLogProperties routeTrackerLogProperties;

    public RouteLoggerRequestServiceAdapter(final RouteTrackerLogProperties routeTrackerLogProperties) {
        this.routeTrackerLogProperties = routeTrackerLogProperties;
    }

    @Override
    public void logRequest(final Request request) {
        final RestClient restClient = RestClient.builder().baseUrl(routeTrackerLogProperties.getUrl()).build();
        request.getShipmentIds().forEach(shipmentId -> {
			final TerminalRequestDto terminalRequest = new TerminalRequestDto(request.getRequest(),
					new ShipmentIdDto(shipmentId.getValue()), request.getProcessType().name());
            restClient
                    .post()
                    .uri("/v2/api/routes/terminal-request")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(terminalRequest)
                    .retrieve()
                    .toEntity(Void.class);
		});
	}
}
