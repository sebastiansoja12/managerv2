package com.warehouse.billing.invoice.configuration;

import com.warehouse.billing.api.BillingCustomerApiService;
import com.warehouse.billing.invoice.application.port.primary.InvoicePort;
import com.warehouse.billing.invoice.application.port.primary.InvoicePortImpl;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceBuyerServicePort;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceDocumentServicePort;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceNumberSequenceRepository;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceRepository;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceSellerServicePort;
import com.warehouse.billing.invoice.application.service.CorrectionInvoiceIssuer;
import com.warehouse.billing.invoice.application.service.InvoiceIssuer;
import com.warehouse.billing.invoice.application.service.InvoiceRegistration;
import com.warehouse.billing.invoice.domain.service.InvoiceFactory;
import com.warehouse.billing.invoice.domain.service.InvoiceNumberFormatter;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.mapper.InvoiceIntegrationEventMapper;
import com.warehouse.billing.invoice.infrastructure.adapter.primary.mapper.InvoiceWebMapper;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.InvoiceBuyerServiceAdapter;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.InvoiceNumberSequenceJpaRepository;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.InvoiceNumberSequenceRepositoryImpl;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.InvoiceRepositoryImpl;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.InvoiceSellerServiceAdapter;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.PdfInvoiceDocumentServiceAdapter;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity.InvoiceEntity;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity.InvoiceNumberSequenceEntity;
import com.warehouse.billing.invoice.infrastructure.adapter.secondary.mapper.InvoicePersistenceMapper;
import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.commonassets.identificator.InvoiceId;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.document.api.DocumentStorageApiService;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class InvoiceConfiguration implements WebMvcConfigurer {

    private static final ZoneId BUSINESS_ZONE = ZoneId.of("Europe/Warsaw");

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(String.class, InvoiceId.class, source -> new InvoiceId(Long.valueOf(source)));
    }

    @Bean("invoiceBaseRepository")
    public OperatorFilteredRepository<InvoiceEntity> invoiceBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean("invoiceNumberSequenceBaseRepository")
    public OperatorFilteredRepository<InvoiceNumberSequenceEntity> invoiceNumberSequenceBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public InvoiceRepository invoiceRepository(
            @Qualifier("invoiceBaseRepository") final OperatorFilteredRepository<InvoiceEntity> repository) {
        return new InvoiceRepositoryImpl(repository, new InvoicePersistenceMapper());
    }

    @Bean
    public InvoiceNumberSequenceRepository invoiceNumberSequenceRepository(
            final InvoiceNumberSequenceJpaRepository invoiceNumberSequenceJpaRepository,
            @Qualifier("invoiceNumberSequenceBaseRepository")
            final OperatorFilteredRepository<InvoiceNumberSequenceEntity> repository,
            final OperatorContextProvider operatorContextProvider) {
        return new InvoiceNumberSequenceRepositoryImpl(invoiceNumberSequenceJpaRepository, repository,
                operatorContextProvider);
    }

    @Bean
    public InvoiceDocumentServicePort invoiceDocumentServicePort(
            final DocumentStorageApiService documentStorageApiService) {
        return new PdfInvoiceDocumentServiceAdapter(documentStorageApiService, BUSINESS_ZONE, Clock.systemUTC());
    }

    @Bean
    public InvoiceSellerServicePort invoiceSellerServicePort(final OperatorApiService operatorApiService,
                                                             final OperatorContextProvider operatorContextProvider) {
        return new InvoiceSellerServiceAdapter(operatorApiService, operatorContextProvider);
    }

    @Bean
    public InvoiceBuyerServicePort invoiceBuyerServicePort(
            final ShipmentApiService shipmentApiService,
            final OperatorConfigurationApiService operatorConfigurationApiService,
            final BillingCustomerApiService billingCustomerApiService) {
        return new InvoiceBuyerServiceAdapter(shipmentApiService, operatorConfigurationApiService,
                billingCustomerApiService);
    }

    @Bean
    public InvoicePort invoicePort(final InvoiceRepository invoiceRepository,
                                   final InvoiceNumberSequenceRepository invoiceNumberSequenceRepository,
                                   final PaymentPort paymentPort,
                                   final PricingPort pricingPort,
                                   final InvoiceSellerServicePort invoiceSellerServicePort,
                                   final InvoiceBuyerServicePort invoiceBuyerServicePort,
                                   final InvoiceDocumentServicePort invoiceDocumentServicePort,
                                   final DomainEventPublisher domainEventPublisher) {
        final Clock clock = Clock.systemUTC();
        final InvoiceFactory invoiceFactory = new InvoiceFactory(new InvoiceNumberFormatter(), BUSINESS_ZONE, clock);
        final InvoiceRegistration invoiceRegistration = new InvoiceRegistration(invoiceRepository,
                invoiceDocumentServicePort, domainEventPublisher, clock);
        final InvoiceIssuer invoiceIssuer = new InvoiceIssuer(invoiceRepository, invoiceNumberSequenceRepository,
                paymentPort, pricingPort, invoiceSellerServicePort, invoiceBuyerServicePort, invoiceFactory,
                invoiceRegistration);
        return new InvoicePortImpl(invoiceIssuer,
                new CorrectionInvoiceIssuer(invoiceRepository, invoiceNumberSequenceRepository, paymentPort,
                        invoiceFactory, invoiceRegistration, invoiceIssuer),
                invoiceRepository, invoiceDocumentServicePort);
    }

    @Bean
    public InvoiceWebMapper invoiceWebMapper() {
        return new InvoiceWebMapper();
    }

    @Bean
    public InvoiceIntegrationEventMapper invoiceIntegrationEventMapper() {
        return new InvoiceIntegrationEventMapper();
    }
}
