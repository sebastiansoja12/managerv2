package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.vo.RouteLogRecord;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteLogRecordDto;
import com.warehouse.tools.routelog.RouteTrackerLogProperties;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteLogServiceClient implements RouteLogServicePort {

    private final GenericFeignResourceService genericFeignResourceService;
    private final RouteTrackerLogProperties routeTrackerLogProperties;
    private final RouteLogRecordMapper routeLogRecordMapper;

    public RouteLogServiceClient(final GenericFeignResourceService genericFeignResourceService,
                                 final RouteTrackerLogProperties routeTrackerLogProperties,
                                 final RouteLogRecordMapper routeLogRecordMapper) {
        this.genericFeignResourceService = genericFeignResourceService;
        this.routeTrackerLogProperties = routeTrackerLogProperties;
        this.routeLogRecordMapper = routeLogRecordMapper;
    }

    @Override
    public RouteLogRecord findByShipmentId(final ShipmentId shipmentId) {
        try {
            final RouteLogRecordDto routeLogRecordDto = this.genericFeignResourceService.findById(
                    this.routeTrackerLogProperties.getUrl(),
                    shipmentId.getValue(),
                    RouteLogRecordDto.class
            );
            return this.routeLogRecordMapper.map(routeLogRecordDto);
        } catch (final FeignException.NotFound exception) {
            log.info("Route log not found for shipment {}", shipmentId.getValue());
            return null;
        } catch (final FeignException exception) {
            log.error("FeignException while fetching route log for shipment {}: {}",
                    shipmentId.getValue(), exception.getMessage());
            return null;
        }
    }
}
