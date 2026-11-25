package com.warehouse.billing.payment.configuration;

import com.warehouse.billing.payment.application.port.primary.PaymentPort;
import com.warehouse.billing.payment.application.port.primary.PaymentPortImpl;
import com.warehouse.billing.payment.application.port.secondary.LateChargeRefundRepository;
import com.warehouse.billing.payment.application.port.secondary.PaymentActorServicePort;
import com.stripe.StripeClient;
import com.warehouse.billing.payment.application.port.secondary.PaymentDeadlineServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentOperatorContextServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentQuoteLookupServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.application.port.secondary.ProcessedWebhookRepository;
import com.warehouse.billing.payment.application.service.CheckoutCloser;
import com.warehouse.billing.payment.application.service.LateChargeRefundService;
import com.warehouse.billing.payment.application.service.OnlineCheckoutService;
import com.warehouse.billing.payment.application.service.OnlinePaymentCompletion;
import com.warehouse.billing.payment.application.service.OnlinePaymentConfirmationService;
import com.warehouse.billing.payment.application.service.OnlinePaymentFinder;
import com.warehouse.billing.payment.application.service.PaymentExpiration;
import com.warehouse.billing.payment.application.service.PaymentExpiryService;
import com.warehouse.billing.payment.application.service.PaymentOpeningService;
import com.warehouse.billing.payment.application.service.PaymentRefundService;
import com.warehouse.billing.payment.domain.service.CheckoutExpiryPolicy;
import com.warehouse.billing.payment.domain.service.LateChargeRefundFactory;
import com.warehouse.billing.payment.domain.service.PaymentFactory;
import com.warehouse.billing.payment.infrastructure.adapter.primary.PaymentDeadlineJob;
import com.warehouse.billing.payment.infrastructure.adapter.primary.StripeWebhookEventVerifier;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.PaymentIntegrationEventMapper;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.PaymentWebMapper;
import com.warehouse.billing.payment.infrastructure.adapter.primary.mapper.StripeWebhookMapper;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.LateChargeRefundRepositoryImpl;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.PaymentActorServiceAdapter;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.PaymentDeadlineServiceAdapter;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.PaymentOperatorContextServiceAdapter;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.PaymentQuoteLookupServiceAdapter;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.PaymentRepositoryImpl;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.ProcessedWebhookRepositoryImpl;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.entity.LateChargeRefundEntity;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.entity.PaymentEntity;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.entity.ProcessedWebhookEventEntity;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.gateway.FakePaymentGatewayClient;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.gateway.StripeCheckoutSessionParamsFactory;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.gateway.StripePaymentGatewayClient;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.mapper.PaymentPersistenceMapper;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.NoOperatorContextBaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;

@Slf4j
@Configuration
public class PaymentConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(String.class, PaymentId.class, source -> new PaymentId(Long.valueOf(source)));
        registry.addConverter(String.class, PriceQuoteId.class, source -> new PriceQuoteId(Long.valueOf(source)));
    }

    @Bean("paymentBaseRepository")
    public OperatorFilteredRepository<PaymentEntity> paymentBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public PaymentRepository paymentRepository(
            @Qualifier("paymentBaseRepository")
            final OperatorFilteredRepository<PaymentEntity> repository,
            final EntityManager entityManager) {
        return new PaymentRepositoryImpl(repository, new NoOperatorContextBaseRepository<>(entityManager),
                new PaymentPersistenceMapper());
    }

    @Bean("processedWebhookEventBaseRepository")
    public OperatorFilteredRepository<ProcessedWebhookEventEntity> processedWebhookEventBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public ProcessedWebhookRepository processedWebhookRepository(
            @Qualifier("processedWebhookEventBaseRepository")
            final OperatorFilteredRepository<ProcessedWebhookEventEntity> repository) {
        return new ProcessedWebhookRepositoryImpl(repository);
    }

    @Bean("lateChargeRefundBaseRepository")
    public OperatorFilteredRepository<LateChargeRefundEntity> lateChargeRefundBaseRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public LateChargeRefundRepository lateChargeRefundRepository(
            @Qualifier("lateChargeRefundBaseRepository")
            final OperatorFilteredRepository<LateChargeRefundEntity> repository,
            final EntityManager entityManager) {
        return new LateChargeRefundRepositoryImpl(repository, new NoOperatorContextBaseRepository<>(entityManager));
    }

    @Bean
    @ConditionalOnProperty(name = "billing.payment-gateway", havingValue = "fake", matchIfMissing = true)
    public PaymentGatewayServicePort fakePaymentGatewayServicePort(
            @Value("${billing.fake-gateway.url:http://localhost:8080/v1/api/billing/dev/fake-gateway}")
            final String checkoutBaseUrl) {
        log.warn("Fake payment gateway is active, online payments are simulated and never charged");
        return new FakePaymentGatewayClient(checkoutBaseUrl, new CheckoutExpiryPolicy(), Clock.systemUTC());
    }

    @Bean
    @ConditionalOnProperty(name = "billing.payment-gateway", havingValue = "stripe")
    public PaymentGatewayServicePort stripePaymentGatewayServicePort(
            @Value("${billing.stripe.secret-key}") final String secretKey,
            @Value("${billing.payment.success-url}") final String successUrl,
            @Value("${billing.payment.cancel-url}") final String cancelUrl) {
        return new StripePaymentGatewayClient(new StripeClient(secretKey),
                new StripeCheckoutSessionParamsFactory(successUrl, cancelUrl, new CheckoutExpiryPolicy()),
                Clock.systemUTC());
    }

    @Bean
    @ConditionalOnProperty(name = "billing.payment-gateway", havingValue = "stripe")
    public StripeWebhookEventVerifier stripeWebhookEventVerifier(
            @Value("${billing.stripe.webhook-secret}") final String webhookSecret) {
        return new StripeWebhookEventVerifier(webhookSecret);
    }

    @Bean
    @ConditionalOnProperty(name = "billing.payment-gateway", havingValue = "stripe")
    public StripeWebhookMapper stripeWebhookMapper() {
        return new StripeWebhookMapper();
    }

    @Bean
    public PaymentQuoteLookupServicePort paymentQuoteLookupServicePort(final ShipmentApiService shipmentApiService) {
        return new PaymentQuoteLookupServiceAdapter(shipmentApiService);
    }

    @Bean
    public PaymentOperatorContextServicePort paymentOperatorContextServicePort(final OperatorContext operatorContext) {
        return new PaymentOperatorContextServiceAdapter(operatorContext);
    }

    @Bean
    public PaymentDeadlineServicePort paymentDeadlineServicePort(
            final OperatorConfigurationApiService operatorConfigurationApiService) {
        return new PaymentDeadlineServiceAdapter(operatorConfigurationApiService);
    }

    @Bean
    public PaymentActorServicePort paymentActorServicePort(final OperatorContextProvider operatorContextProvider) {
        return new PaymentActorServiceAdapter(operatorContextProvider);
    }

    @Bean
    public PaymentPort paymentPort(final PaymentRepository paymentRepository,
                                   final PricingPort pricingPort,
                                   final PaymentDeadlineServicePort paymentDeadlineServicePort,
                                   final PaymentActorServicePort paymentActorServicePort,
                                   final DomainEventPublisher domainEventPublisher,
                                   final PaymentGatewayServicePort paymentGatewayServicePort,
                                   final ProcessedWebhookRepository processedWebhookRepository,
                                   final LateChargeRefundRepository lateChargeRefundRepository,
                                   final PaymentQuoteLookupServicePort paymentQuoteLookupServicePort,
                                   final PaymentOperatorContextServicePort paymentOperatorContextServicePort,
                                   final TransactionTemplate transactionTemplate) {
        final Clock clock = Clock.systemUTC();
        final PaymentOpeningService paymentOpeningService = new PaymentOpeningService(pricingPort,
                paymentDeadlineServicePort, new PaymentFactory(clock));
        final OnlinePaymentCompletion onlinePaymentCompletion = new OnlinePaymentCompletion(paymentRepository,
                domainEventPublisher, new LateChargeRefundService(lateChargeRefundRepository,
                paymentGatewayServicePort, new LateChargeRefundFactory(clock)), clock);
        final PaymentRefundService paymentRefundService = new PaymentRefundService(paymentRepository,
                paymentGatewayServicePort, domainEventPublisher, new CheckoutCloser(paymentGatewayServicePort), clock);
        final OnlinePaymentFinder onlinePaymentFinder = new OnlinePaymentFinder(paymentRepository,
                lateChargeRefundRepository);
        return new PaymentPortImpl(paymentRepository, paymentOpeningService, paymentActorServicePort,
                domainEventPublisher, clock,
                new OnlineCheckoutService(paymentRepository, paymentOpeningService, paymentGatewayServicePort,
                        onlinePaymentCompletion, clock),
                new OnlinePaymentConfirmationService(onlinePaymentFinder, processedWebhookRepository,
                        onlinePaymentCompletion, paymentRefundService, clock),
                paymentQuoteLookupServicePort, paymentOperatorContextServicePort, onlinePaymentFinder,
                new PaymentExpiryService(paymentRepository, paymentGatewayServicePort, onlinePaymentCompletion,
                        new PaymentExpiration(paymentRepository, domainEventPublisher), transactionTemplate),
                paymentRefundService);
    }

    @Bean
    public PaymentDeadlineJob paymentDeadlineJob(final PaymentPort paymentPort,
                                                 final OperatorApiService operatorApiService,
                                                 final OperatorContext operatorContext) {
        return new PaymentDeadlineJob(paymentPort, operatorApiService, operatorContext, Clock.systemUTC());
    }

    @Bean
    public PaymentWebMapper paymentWebMapper() {
        return new PaymentWebMapper();
    }

    @Bean
    public PaymentIntegrationEventMapper paymentIntegrationEventMapper() {
        return new PaymentIntegrationEventMapper();
    }
}
