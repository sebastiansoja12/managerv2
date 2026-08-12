package com.warehouse.routetracker.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.exception.RouteLogException;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteLogToEntityMapper;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteLogToModelMapper;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteModelMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@AllArgsConstructor
@Slf4j
public class RouteLogRepositoryImpl implements RouteLogRepository {

    private final RouteLogRecordReadRepository routeLogRecordReadRepository;

    private final RouteModelMapper mapper = getMapper(RouteModelMapper.class);

    private final RouteLogToModelMapper logToModelMapper = getMapper(RouteLogToModelMapper.class);

    private final RouteLogToEntityMapper logToEntityMapper = getMapper(RouteLogToEntityMapper.class);


    @Override
    public RouteProcess save(final RouteLogRecord routeLogRecord) {
        validateNotExists(routeLogRecord.getShipmentId());
        final RouteLogRecordEntity entity = logToEntityMapper.map(routeLogRecord);
		this.routeLogRecordReadRepository.save(entity);
		log.info("Created route process {} for shipment {}", entity.getId(),
				routeLogRecord.getShipmentId().value());
        return mapper.map(entity);
    }

    private void validateNotExists(final ShipmentId shipmentId) {
        final Optional<RouteLogRecordEntity> routeLogRecord = this.routeLogRecordReadRepository
                .findByShipmentId(shipmentId);

        if (routeLogRecord.isPresent()) {
            throw new RouteLogException("Route log record already exists");
        }
    }

    @Override
    public RouteLogRecord find(final ShipmentId shipmentId) {
		return this.findById(shipmentId)
				.orElseThrow(() -> new RouteLogException("Route log does not exist"));
    }

    @Override
    public Optional<RouteLogRecord> findById(final ShipmentId shipmentId) {
        return this.routeLogRecordReadRepository
                .findByShipmentId(shipmentId)
                .map(this.logToModelMapper::map);
    }

    @Override
    @Transactional
    public void update(RouteLogRecord routeLogRecord) {
        final RouteLogRecordEntity routeLogRecordEntity = logToEntityMapper.map(routeLogRecord);
        this.routeLogRecordReadRepository.save(routeLogRecordEntity);
    }

    @Override
    public List<RouteLogRecord> findAll() {
		return routeLogRecordReadRepository
                .findAll()
                .stream()
                .map(mapper::mapToRecord)
                .collect(Collectors.toList());
    }
}
