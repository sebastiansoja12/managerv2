package com.warehouse.routetracker.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.warehouse.routetracker.domain.model.RouteInformation;
import com.warehouse.routetracker.domain.model.RouteLogRecord;
import com.warehouse.routetracker.domain.model.RouteLogRecordToChange;
import com.warehouse.routetracker.domain.port.secondary.RouteRepository;
import com.warehouse.routetracker.domain.vo.RouteDeleteRequest;
import com.warehouse.routetracker.domain.vo.RouteProcess;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.exception.RouteLogException;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteLogToEntityMapper;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteLogToModelMapper;
import com.warehouse.routetracker.infrastructure.adapter.secondary.mapper.RouteModelMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@AllArgsConstructor
@Slf4j
public class RouteRepositoryImpl implements RouteRepository {

    private final RouteReadRepository routeReadRepository;

    private final RouteLogRecordReadRepository routeLogRecordReadRepository;

    private final RouteModelMapper mapper;

    private final RouteLogToModelMapper logToModelMapper = getMapper(RouteLogToModelMapper.class);

    private final RouteLogToEntityMapper logToEntityMapper = getMapper(RouteLogToEntityMapper.class);


    @Override
    public List<RouteInformation> findByParcelId(Long parcelId) {

        final List<RouteEntity> routeEntity = routeReadRepository.findByParcelId(parcelId);

        return mapper.mapToRoutes(routeEntity);
    }

    @Override
    public RouteResponse save(RouteLogRecord routeLogRecord) {
        final RouteEntity routeEntity = mapper.map(routeLogRecord);

        routeReadRepository.save(routeEntity);

        return mapper.mapToRouteResponse(routeEntity);
    }

    @Override
    public RouteProcess save(RouteLogRecordToChange routeLogRecordToChange) {
        validateNotExists(routeLogRecordToChange.getParcelId());
        final RouteLogRecordEntity entity = logToEntityMapper.map(routeLogRecordToChange);
		routeLogRecordReadRepository.save(entity);
		log.info("Created route process {} for parcel {}", entity.getId(),
				routeLogRecordToChange.getParcelId());
        return mapper.map(entity);
    }

    private void validateNotExists(Long id) {
        final Optional<RouteLogRecordEntity> routeLogRecord = routeLogRecordReadRepository.findByParcelId(id);

        if (routeLogRecord.isPresent()) {
            throw new RouteLogException("Route log record already exists");
        }
    }

    @Override
    public List<RouteInformation> findByUsername(String username) {
        return mapper.mapToRoutes(routeReadRepository.findAllByUserUsername(username));
    }

    @Override
    public RouteLogRecordToChange find(Long parcelId) {
		return routeLogRecordReadRepository
                .findByParcelId(parcelId)
                .map(logToModelMapper::map)
				.orElseThrow(() -> new RouteLogException("Route log does not exist"));
    }

    @Override
    public void update(RouteLogRecordToChange routeLogRecord) {
        final RouteLogRecordEntity routeLogRecordEntity = logToEntityMapper.map(routeLogRecord);
        routeLogRecordReadRepository.save(routeLogRecordEntity);
    }

    @Override
    public List<RouteLogRecordToChange> findAll() {
		return routeLogRecordReadRepository
                .findAll()
                .stream()
                .map(mapper::mapToRecord)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRoute(RouteDeleteRequest request) {
        routeReadRepository.deleteById(request.getId());
    }
}
