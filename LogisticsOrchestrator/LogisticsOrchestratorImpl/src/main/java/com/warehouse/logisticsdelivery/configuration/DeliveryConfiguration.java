package com.warehouse.logisticsdelivery.configuration;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.logisticsdelivery.api.DeliveryApiService;
import com.warehouse.logisticsdelivery.api.DeliveryReadModelRebuildApiService;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryHistoryPort;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryHistoryPortImpl;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPortImpl;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryReadModelPort;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryReadModelPortImpl;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryEventDataMapperPort;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryHistoryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryReadModelRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.DeliveryApiServiceAdapter;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.DeliveryReadModelRebuildApiServiceAdapter;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryApiMapper;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryReadModelResponseMapper;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryRequestMapper;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.DeliveryHistoryRepositoryImpl;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.DeliveryReadModelRepositoryImpl;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.DeliveryRepositoryImpl;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.ShipmentServiceAdapter;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryHistoryEntity;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryReadEntity;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper.DeliveryEventDataMapper;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper.DeliveryHistoryEntityMapper;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper.DeliveryReadModelMapper;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliveryConfiguration {

    @Bean
    public BaseRepository<DeliveryEntity> deliveryEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public DeliveryRepository deliveryRepository(
            @Qualifier("deliveryEntityRepository")
            final BaseRepository<DeliveryEntity> repository) {
        return new DeliveryRepositoryImpl(repository);
    }

    @Bean
    public BaseRepository<DeliveryReadEntity> deliveryReadEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public DeliveryReadModelRepository deliveryReadModelRepository(
            @Qualifier("deliveryReadEntityRepository")
            final BaseRepository<DeliveryReadEntity> repository) {
        return new DeliveryReadModelRepositoryImpl(repository, new DeliveryReadModelMapper());
    }

    @Bean
    public DeliveryReadModelPort deliveryReadModelPort(final DeliveryRepository deliveryRepository,
                                                       final DeliveryReadModelRepository deliveryReadModelRepository) {
        return new DeliveryReadModelPortImpl(deliveryRepository, deliveryReadModelRepository);
    }

    @Bean
    public DeliveryReadModelRebuildApiService deliveryReadModelRebuildApiService(
            final DeliveryReadModelPort deliveryReadModelPort,
            final OperatorContext operatorContext) {
        return new DeliveryReadModelRebuildApiServiceAdapter(deliveryReadModelPort, operatorContext);
    }

    @Bean
    public DeliveryReadModelResponseMapper deliveryReadModelResponseMapper() {
        return new DeliveryReadModelResponseMapper();
    }

    @Bean
    public DeliveryPort deliveryPort(final DeliveryRepository deliveryRepository,
                                     final ShipmentServicePort shipmentServicePort,
                                     final DomainEventPublisher domainEventPublisher,
                                     final OperatorContextProvider operatorContextProvider) {
        return new DeliveryPortImpl(deliveryRepository, shipmentServicePort, domainEventPublisher,
                operatorContextProvider);
    }

    @Bean("logisticsDelivery.shipmentServicePort")
    public ShipmentServicePort shipmentServicePort(final ShipmentApiService shipmentApiService) {
        return new ShipmentServiceAdapter(shipmentApiService);
    }

    @Bean
    public DeliveryEventDataMapperPort deliveryEventDataMapper() {
        return new DeliveryEventDataMapper();
    }

    @Bean
    public DeliveryResponseMapper deliveryResponseMapper(
            @Value("${logistics.delivery.attempt-limit:3}") final int attemptLimit) {
        return new DeliveryResponseMapper(attemptLimit);
    }

    @Bean
    public BaseRepository<DeliveryHistoryEntity> deliveryHistoryEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public DeliveryHistoryRepository deliveryHistoryRepository(
            @Qualifier("deliveryHistoryEntityRepository")
            final BaseRepository<DeliveryHistoryEntity> repository) {
        return new DeliveryHistoryRepositoryImpl(repository, new DeliveryHistoryEntityMapper());
    }

    @Bean
    public DeliveryHistoryPort deliveryHistoryPort(final DeliveryHistoryRepository deliveryHistoryRepository,
                                                   final OperatorContextProvider operatorContextProvider) {
        return new DeliveryHistoryPortImpl(deliveryHistoryRepository, operatorContextProvider);
    }

    @Bean
    public DeliveryRequestMapper deliveryRequestMapper() {
        return new DeliveryRequestMapper();
    }

    @Bean
    public DeliveryApiMapper deliveryApiMapper() {
        return new DeliveryApiMapper();
    }

    @Bean
    public DeliveryApiService deliveryApiService(final DeliveryPort deliveryPort,
                                                 final DeliveryApiMapper deliveryApiMapper) {
        return new DeliveryApiServiceAdapter(deliveryPort, deliveryApiMapper);
    }
}
