package com.warehouse.routetracker.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.port.secondary.RouteLogRepository;
import com.warehouse.routetracker.domain.vo.RouteProcess;
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
    public RouteProcess save(RouteLogRecord routeLogRecord) {
        validateNotExists(routeLogRecord.getParcelId());
        final RouteLogRecordEntity entity = logToEntityMapper.map(routeLogRecord);
		routeLogRecordReadRepository.save(entity);
		log.info("Created route process {} for parcel {}", entity.getId(),
				routeLogRecord.getParcelId());
        return mapper.map(entity);
    }

    private void validateNotExists(Long id) {
        final Optional<RouteLogRecordEntity> routeLogRecord = routeLogRecordReadRepository.findByParcelId(id);

        if (routeLogRecord.isPresent()) {
            throw new RouteLogException("Route log record already exists");
        }
    }

    @Override
    public RouteLogRecord find(Long parcelId) {
		return routeLogRecordReadRepository
                .findByParcelId(parcelId)
                .map(logToModelMapper::map)
				.orElseThrow(() -> new RouteLogException("Route log does not exist"));
    }

    @Override
    public void update(RouteLogRecord routeLogRecord) {
        final RouteLogRecordEntity routeLogRecordEntity = logToEntityMapper.map(routeLogRecord);
        routeLogRecordReadRepository.save(routeLogRecordEntity);
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
