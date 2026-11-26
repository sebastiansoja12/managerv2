# Billing Backend Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Price shipments from an operator price list, block unpaid shipments from entering the network, take counter and Stripe (test-mode) payments, refund and invoice them.

**Architecture:** New hexagonal bounded context `Billing` (`BillingApi` + `BillingImpl`, root package `com.warehouse.billing`, sub-contexts `pricing`, `payment`, `invoice`, `customer`). It talks to `ShipmentManagement` through a synchronous quote API and integration events over the Kafka outbox. Shops authenticate with per-user API keys on a dedicated `/external/**` filter chain.

**Tech Stack:** Java 21, Spring Boot 3.5.5, Spring Data JPA + `OperatorFilteredRepository`, Liquibase/PostgreSQL, Kafka via `EventOutbox`, OpenPDF, Stripe Java SDK (`com.stripe:stripe-java`), JUnit 5 + Mockito + AssertJ.

**Spec:** `docs/billing-design.md` — read it before every task; this plan only records decisions the spec leaves to the builder.

**Out of this plan:** GUI (`managerv2gui`) and terminal (`managerv2terminal`) changes get their own plans once this API exists.

## Global Constraints

- Follow `AGENTS.md`: no `var`, no `@Autowired`, `final` on all parameters and injected fields, no comments, constructor injection, beans wired in `*.configuration` classes, mappers only in adapters, ports in `*.application.port`, outgoing ports `*ServicePort`, adapters `*ServiceAdapter`, external-system adapters named `*Client`.
- No bare id primitives: use `PriceListId`, `PriceQuoteId`, `PaymentId`, `InvoiceId`, `BillingCustomerId`, `ShipmentId`, `UserId`, `OperatorId` in domain, commands, events and JPA entities (`@EmbeddedId` / `@Embedded` with `@AttributeOverride(name = "value", ...)`); REST DTOs use `*IdDto`.
- Aggregates are created through a domain `*Factory.create(...)`, never a static creator.
- Aggregate commands are `void` and guard themselves (early return for idempotent no-op, throw for illegal transition); no boolean results, no pre-checks in ports.
- Repository adapters use `OperatorFilteredRepository` + Criteria; no raw `EntityManager`/JPQL. Entities extend `BelongsToOperator`.
- Integration events: `@IntegrationEventType(value, version = 1, topic = BillingTopics.X)`, published via `IntegrationEventPublisher`, consumed with `@IntegrationEventListener(group = "manager-billing-...")` in `*.infrastructure.adapter.primary`.
- Liquibase: one file per task under `Application/src/main/resources/changelog/db/postgresql/changes/2026-11-24-billing-<topic>.xml`, included at the end of `.../postgresql/db.changelog-master.xml`, author `s-soja`; never edit an applied changeset.
- Money: `BigDecimal`, scale 2, `RoundingMode.HALF_UP`, rounded per line item; `com.warehouse.commonassets.enumeration.Currency`.
- Secrets (`STRIPE_SECRET_KEY`, `STRIPE_WEBHOOK_SECRET`, shop API keys) never logged, never returned except the one-time API key reveal.
- Test command (single module, from `managerv2/`):
  `JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home mvn -s ~/.m2/manager_settings.xml -o -q -pl <Module>/<ModuleImpl> -am test -Dtest='<Pattern>' -Dsurefire.failIfNoSpecifiedTests=false`; results in `target/surefire-reports/*.txt`. Task 6 adds a new dependency, so its first build runs without `-o`.
- Commits: only when the user has asked for them in this execution; one commit per module touched, one-sentence message, no `Co-Authored-By`.

## Review Focus

1. **Duplicate Stripe webhook / double click on "Przyjmij płatność"** — must charge once and issue one invoice. Pinned in Task 5 (`shouldIgnoreSecondCounterPaymentForPaidShipment`) and Task 6 (`shouldIgnoreAlreadyProcessedWebhookEvent`).
2. **Shipment created by split/redirect/return** — must not open a second payment. Pinned in Task 4 (`shouldCreateParentAsPaymentNotRequiredWhenShipmentIsSplit`) and Task 5 (`shouldIgnoreShipmentCreatedWithPaymentNotRequired`).
3. **Unpaid shipment moved by a route other than the three named in the spec** (department acceptance, pallet load, generic status PUT). Pinned in Task 4 (`shouldRejectEveryNonDraftTransitionWhenUnpaid`, parameterized).
4. **Shipment bigger than every size class / heavier than every weight band** — must be rejected with a clear error at creation, not priced at 0. Pinned in Task 2 (`shouldRejectShipmentLargerThanEverySizeClass`, `shouldRejectShipmentHeavierThanEveryWeightBand`).
5. **Stripe payment completing after the payment expired** — must refund, never revive the shipment. Pinned in Task 8 (`shouldRefundLateCompletionOfExpiredPayment`).

---

### Task 1: Billing module skeleton and identifiers

**Files:**
- Create: `Billing/pom.xml`, `Billing/BillingApi/pom.xml`, `Billing/BillingImpl/pom.xml` (copy the shape of `IncidentManagement/*/pom.xml`; `BillingApi` depends on `Common`, `ShipmentManagementApi`, `OrganisationStructureApi`, `AuthorizationApi`, `DocumentStorageApi`, `AuditLogApi`; `BillingImpl` on `BillingApi`, `Common` and the starters used by `IncidentManagementImpl`)
- Create: `Billing/BillingApi/src/main/java/com/warehouse/billing/api/event/BillingTopics.java`
- Create in `Common/src/main/java/com/warehouse/commonassets/identificator/`: `PriceListId`, `PriceQuoteId`, `PaymentId`, `InvoiceId`, `BillingCustomerId` (same shape as `IncidentId`: `@Embeddable record X(Long value) implements Serializable` with `nextId()`)
- Modify: `pom.xml` (add `<module>Billing</module>` after `OrganizationAnalytics`), `Application/pom.xml` (add `BillingImpl` next to `IncidentManagementImpl`), `ShipmentManagement/ShipmentManagementImpl/pom.xml` (add `BillingApi`)
- Test: `ArchitectureTest` — add `com.warehouse.billing` to the packages checked by the existing hexagonal-layer rules (follow how `com.warehouse.incident` is listed)

**Interfaces:**
- Produces: `BillingTopics.SHIPMENT_PAID = "billing.shipment-paid"`, `PAYMENT_EXPIRED = "billing.payment-expired"`, `PAYMENT_REFUNDED = "billing.payment-refunded"`, `INVOICE_ISSUED = "billing.invoice-issued"`; the five identifier records.

- [ ] **Step 1:** Create the poms, identifiers and `BillingTopics`; register the module.
- [ ] **Step 2:** Run `mvn ... -pl Application -am compile` (test command with `compile` instead of `test`). Expected: BUILD SUCCESS.
- [ ] **Step 3:** Run the architecture tests: `-pl ArchitectureTest -am test`. Expected: PASS.
- [ ] **Step 4:** Commit.

---

### Task 2: Pricing domain

**Files:**
- Create in `Billing/BillingImpl/src/main/java/com/warehouse/billing/pricing/domain/`:
  `model/PriceList.java`, `vo/SizeClass.java`, `vo/WeightBand.java`, `vo/PricingInput.java`, `vo/PriceQuoteLine.java`, `model/PriceQuote.java`, `enumeration/PriceQuoteLineType.java`, `service/PriceCalculator.java`, `service/PriceListFactory.java`, `exception/ShipmentNotPriceableException.java`
- Test: `Billing/BillingImpl/src/test/java/com/warehouse/billing/pricing/domain/service/PriceCalculatorTest.java`, `PriceListFixture.java`

**Interfaces:**
- Produces:
  - `record SizeClass(String code, BigDecimal maxLengthCm, BigDecimal maxWidthCm, BigDecimal maxHeightCm, BigDecimal netPrice)` — a shipment fits when its sorted dimensions are ≤ the class's sorted dimensions; classes are tried in ascending `netPrice` order.
  - `record WeightBand(BigDecimal fromKgInclusive, BigDecimal toKgExclusive, BigDecimal netSurcharge)`
  - `PriceList(PriceListId id, OperatorId operatorId, Currency currency, List<SizeClass> sizeClasses, List<WeightBand> weightBands, Map<ShipmentServiceLevel, BigDecimal> serviceLevelMultipliers, Map<DeliveryMethod, BigDecimal> deliveryMethodSurcharges, BigDecimal insurancePercent, BigDecimal vatPercent, Instant updatedAt)` with `void replace(final PriceList replacement)`
  - `record PricingInput(Dimensions dimensions, Weight weight, ShipmentServiceLevel serviceLevel, DeliveryMethod deliveryMethod, Money declaredValue)` (`Weight`, `Money` from `Common`; `declaredValue` may be null)
  - `PriceQuoteLineType { BASE, WEIGHT_SURCHARGE, SERVICE_LEVEL, DELIVERY_METHOD, INSURANCE }`
  - `record PriceQuoteLine(PriceQuoteLineType type, String description, BigDecimal net, BigDecimal vat, BigDecimal gross)`
  - `PriceQuote(PriceQuoteId id, PriceListId priceListId, Currency currency, List<PriceQuoteLine> lines, BigDecimal vatPercent, Instant quotedAt)` with `net()`, `vat()`, `gross()` summing lines
  - `PriceQuote PriceCalculator.quote(final PriceList priceList, final PricingInput input)`
  - `PriceList PriceListFactory.copyForOperator(final PriceList template, final OperatorId operatorId)` — new `PriceListId`, same entries. No price values live in Java code: defaults are a template row in the database (Task 3).
- Rules: WEIGHT_SURCHARGE line omitted when the band's surcharge is 0; SERVICE_LEVEL line = `(base + weight) × (multiplier − 1)`, omitted when multiplier is 1; INSURANCE line only when `declaredValue` is non-null and > 0; DELIVERY_METHOD line omitted when surcharge is 0. VAT per line = `net × vatPercent / 100`.
- Default price list values (seeded into the database template in Task 3; `PriceListFixture.defaultPln()` uses the same values for tests): size classes `S` 8×38×64 cm → 12.00, `M` 19×38×64 → 14.00, `L` 41×38×64 → 17.00, `XL` 80×60×120 → 39.00; weight bands 0–5 → 0, 5–10 → 3.00, 10–25 → 8.00, 25–50 → 20.00; multipliers ECONOMY 0.90, STANDARD 1.00, EXPRESS 1.50; surcharges COURIER 4.00, PICKUP_POINT 0, LOCKER 0; insurance 1.0 %; VAT 23 %.

- [ ] **Step 1: Write failing tests**

```java
@Test
void shouldQuoteStandardCourierShipmentWithoutInsurance() {
    PriceQuote quote = calculator.quote(PriceListFixture.defaultPln(),
            PriceListFixture.input(10, 30, 50, 4.0, ShipmentServiceLevel.STANDARD, DeliveryMethod.COURIER, null));

    assertThat(quote.lines()).extracting(PriceQuoteLine::type)
            .containsExactly(PriceQuoteLineType.BASE, PriceQuoteLineType.DELIVERY_METHOD);
    assertThat(quote.net()).isEqualByComparingTo("18.00");
    assertThat(quote.vat()).isEqualByComparingTo("4.14");
    assertThat(quote.gross()).isEqualByComparingTo("22.14");
}

@Test
void shouldApplyExpressMultiplierAndInsurance() {
    PriceQuote quote = calculator.quote(PriceListFixture.defaultPln(),
            PriceListFixture.input(8, 38, 64, 7.0, ShipmentServiceLevel.EXPRESS, DeliveryMethod.LOCKER,
                    Money.from(new BigDecimal("500.00"), Currency.PLN)));

    assertThat(quote.lines()).extracting(PriceQuoteLine::type).containsExactly(PriceQuoteLineType.BASE,
            PriceQuoteLineType.WEIGHT_SURCHARGE, PriceQuoteLineType.SERVICE_LEVEL, PriceQuoteLineType.INSURANCE);
    assertThat(quote.net()).isEqualByComparingTo("27.50");
}

@Test
void shouldRejectShipmentLargerThanEverySizeClass() {
    assertThatThrownBy(() -> calculator.quote(PriceListFixture.defaultPln(),
            PriceListFixture.input(100, 100, 130, 1.0, ShipmentServiceLevel.STANDARD, DeliveryMethod.COURIER, null)))
            .isInstanceOf(ShipmentNotPriceableException.class);
}

@Test
void shouldRejectShipmentHeavierThanEveryWeightBand() {
    assertThatThrownBy(() -> calculator.quote(PriceListFixture.defaultPln(),
            PriceListFixture.input(10, 30, 50, 50.0, ShipmentServiceLevel.STANDARD, DeliveryMethod.COURIER, null)))
            .isInstanceOf(ShipmentNotPriceableException.class);
}

@Test
void shouldFitRotatedShipmentIntoSizeClass() { /* 64×8×38 fits S: base line net 12.00 */ }
```

(`27.50` = base 12.00 + weight 3.00 + express 7.50 + insurance 5.00.)

- [ ] **Step 2:** Run `-pl Billing/BillingImpl -am test -Dtest='PriceCalculatorTest'`. Expected: FAIL (classes missing).
- [ ] **Step 3:** Implement the domain classes listed above.
- [ ] **Step 4:** Re-run. Expected: PASS.
- [ ] **Step 5:** Commit.

---

### Task 3: Price lists, quotes and the quote API

**Files:**
- Create in `com.warehouse.billing.pricing`: `application/port/primary/PricingPort.java`, `PricingPortImpl.java`, `command/QuoteShipmentCommand.java`, `command/ReplacePriceListCommand.java`, `application/port/secondary/PriceListRepository.java`, `PriceQuoteRepository.java`, `infrastructure/adapter/secondary/PriceListRepositoryImpl.java`, `PriceQuoteRepositoryImpl.java`, `entity/PriceListEntity.java` (size classes, bands and maps as `@ElementCollection` tables), `entity/PriceQuoteEntity.java` (+ `PriceQuoteLineEmbeddable`), `mapper/PricingPersistenceMapper.java`, `infrastructure/adapter/primary/PricingController.java`, `PriceQuoteApiServiceAdapter.java`, `PricingOperatorIntegrationListener.java`, `api/*ApiRequest/*ApiResponse`, `mapper/PricingWebMapper.java`, `configuration/PricingConfiguration.java`, `PricingExceptionHandler.java`
- Create in `BillingApi`: `com/warehouse/billing/api/PriceQuoteApiService.java`, `dto/PriceQuoteRequestDto.java`, `dto/PriceQuoteDto.java`, `dto/PriceQuoteIdDto.java`
- Create: `changes/2026-11-24-billing-pricing.xml` (tables `price_lists`, `price_list_size_classes`, `price_list_weight_bands`, `price_list_service_levels`, `price_list_delivery_methods`, `price_quotes`, `price_quote_lines`; `price_lists.operator_id` nullable with a boolean `template` column — exactly one row with `template = true` and `operator_id IS NULL` (partial unique index), and unique `operator_id` where not null; changesets inserting the template with the Task 2 values in PLN, then copying it for every row of `operators`)
- The template is not operator-scoped: read it through a dedicated non-filtered lookup in `PriceListRepositoryImpl` (`findTemplate()`), following how other platform-level rows are read in the codebase; `PriceListEntity` still extends `BelongsToOperator`.
- Test: `PricingPortImplTest.java`, `PricingControllerTest.java` (standalone MockMvc like `ShipmentContentControllerTest`)

**Interfaces:**
- Consumes: Task 2 domain.
- Produces:
  - `PriceQuoteApiService.quote(final PriceQuoteRequestDto request): PriceQuoteDto` — persists the quote; throws `ShipmentNotPriceableException` mapped to `422` by REST callers.
  - `record PriceQuoteDto(PriceQuoteIdDto quoteId, BigDecimal net, BigDecimal vat, BigDecimal gross, Currency currency)`
  - `PricingPort.findQuote(final PriceQuoteId quoteId): Optional<PriceQuote>` (used by Tasks 5, 9)
  - REST: `GET /billing/price-list`, `PUT /billing/price-list` (full replace), `POST /billing/quotes` (does **not** persist — call `PriceCalculator` only).
  - `@IntegrationEventListener(group = "manager-billing-pricing") handle(OperatorCreatedIntegrationEvent)` → `PricingPort.createDefaultPriceList(final OperatorId operatorId)` copying the database template; no-op when the operator already has one.
  - `PricingPort.findTemplate()` / `replaceTemplate(...)` are not exposed over REST in this plan (template changes go through Liquibase).

- [ ] **Step 1: Write failing tests**: `shouldPersistQuoteWhenQuotingForShipment`, `shouldNotPersistQuoteForPreview`, `shouldReplacePriceList`, `shouldCopyTemplateForNewOperator`, `shouldCreateDefaultPriceListOnlyOnce`, controller `shouldReturn422WhenShipmentNotPriceable`.
- [ ] **Step 2:** Run `-Dtest='Pricing*Test'`. Expected: FAIL.
- [ ] **Step 3:** Implement ports, adapters, entities, Liquibase, configuration.
- [ ] **Step 4:** Re-run. Expected: PASS.
- [ ] **Step 5:** Commit.

---

### Task 4: Shipment payment state, quoting on creation and the payment guard

**Files:**
- Create: `ShipmentManagement/ShipmentManagementImpl/src/main/java/com/warehouse/shipment/domain/enumeration/PaymentState.java` (`UNPAID, PAID, NOT_REQUIRED`), `application/port/secondary/ShipmentPricingServicePort.java`, `infrastructure/adapter/secondary/ShipmentPricingServiceAdapter.java`
- Modify: `domain/model/Shipment.java` (fields `paymentState`, `priceQuoteId`, `billingCustomerId`; `ensurePaid()`; `markAsPaid()`; `cancelForExpiredPayment(final LocalDateTime currentTime)`), `application/port/primary/ShipmentPortImpl.java` (`ship()` quotes via the port, sets `price` = gross, `priceQuoteId`, `UNPAID`; all other `new Shipment(...)`/`ShipmentCreated` paths — `changeShipmentType` parent, redirect, return at the lines the reviewer listed — set `NOT_REQUIRED`), `application/service/status/*StatusChangeStrategy.java` (call `shipment.ensurePaid()` before any target outside `PLANNED/CREATED/PREPARED`), `domain/model/CancellationReason` enum (add `PAYMENT_EXPIRED`), `api/ShipmentCreateRequestApi.java` and the validator (remove `price`), snapshot/entity/mappers/read model (`payment_state`, `price_quote_id`, `billing_customer_id`), `ShipmentManagementApi/.../snapshot/ShipmentEventData.java` (add `PaymentState paymentState`, `PriceQuoteId priceQuoteId`, `BillingCustomerId billingCustomerId` as last components, using a Common-level `PaymentStateDto` enum in `ShipmentManagementApi`), `ShipmentCanceledIntegrationEvent` (add `@JsonProperty("previousStatus") ShipmentStatus previousStatus`)
- Remove: `PriceEntity`, `PriceRepository`, `PriceRepositoryImpl`, `PriceReadRepository`, `PriceServiceImpl` and their bean in `ShipmentConfiguration`, once no caller remains
- Create: `changes/2026-11-24-billing-shipment-payment-state.xml` (add three columns to `shipment` and the read-model table, backfill `payment_state = 'NOT_REQUIRED'`, then `NOT NULL`; drop table `price`)
- Test: `ShipmentPaymentGuardTest.java` (domain), extend `ShipmentPortImplTest`

**Interfaces:**
- Consumes: `PriceQuoteApiService.quote(...)` (Task 3).
- Produces: `Shipment.markAsPaid()` (idempotent: returns early when `PAID`; throws `ShipmentModificationException` when `NOT_REQUIRED`), `Shipment.cancelForExpiredPayment(...)` (no-op when already `CANCELED`; throws unless `UNPAID` and in a draft status; skips `cancellationWindowMinutes`; reason `PAYMENT_EXPIRED`), `ShipmentEventData.paymentState()`, `ShipmentCanceledIntegrationEvent.previousStatus()`.
- `ShipmentPricingServicePort.quote(final ShipmentCreateCommand command): Result<PricedShipment, ErrorCode>` where `record PricedShipment(PriceQuoteId quoteId, Money gross)`; a not-priceable shipment returns a failure with a new `ErrorCode` `SHIPMENT_NOT_PRICEABLE` so `ship()` returns it like the department-resolution failures.

- [ ] **Step 1: Write failing tests**

```java
@ParameterizedTest
@MethodSource("nonDraftTransitions")
void shouldRejectEveryNonDraftTransitionWhenUnpaid(final Consumer<Shipment> transition) {
    Shipment shipment = ShipmentFixture.unpaidCreated();

    assertThatThrownBy(() -> transition.accept(shipment)).isInstanceOf(ShipmentModificationException.class);
}
// nonDraftTransitions: notifyShipmentCollected, acceptInDepartment, notifyShipmentLoadedOnPallet,
// handOver, and changeShipmentStatus to ACCEPTED via ShipmentAcceptedStatusChangeStrategy

@Test
void shouldAllowCollectionAfterPayment() { /* unpaidCreated → markAsPaid → notifyShipmentCollected → COLLECTED */ }

@Test
void shouldAllowUnpaidShipmentToStayPrepared() { /* PREPARED strategy on UNPAID does not throw */ }

@Test
void shouldCancelExpiredPaymentEvenWhenCancellationWindowIsClosed() { /* window 0, reason PAYMENT_EXPIRED */ }

@Test
void shouldCreateParentAsPaymentNotRequiredWhenShipmentIsSplit() { /* ShipmentPortImplTest: parent from changeShipmentType has NOT_REQUIRED */ }

@Test
void shouldReturnNotPriceableFailureFromShip() { /* pricing port failure → Result.failure(SHIPMENT_NOT_PRICEABLE), nothing saved */ }
```

- [ ] **Step 2:** Run `-pl ShipmentManagement/ShipmentManagementImpl -am test -Dtest='ShipmentPaymentGuardTest,ShipmentPortImplTest'`. Expected: FAIL.
- [ ] **Step 3:** Implement; fix every compile error caused by the removed request `price` and the new `ShipmentEventData` components (other modules constructing `ShipmentEventData` in tests pass `null` for the three new components).
- [ ] **Step 4:** Re-run, then the whole module: `-pl ShipmentManagement/ShipmentManagementImpl -am test`. Expected: PASS.
- [ ] **Step 5:** Commit.

---

### Task 5: Payments, counter payment and ShipmentPaid

**Files:**
- Create in `com.warehouse.billing.payment`: `domain/model/Payment.java`, `domain/service/PaymentFactory.java`, `domain/enumeration/PaymentStatus.java` (`PENDING, PAID, REFUND_PENDING, REFUNDED, EXPIRED, CANCELED`), `PaymentChannel` (`COUNTER, ONLINE`), `PaymentMethod` (`CASH, CARD, STRIPE`), `domain/event/PaymentPaid.java`, `domain/exception/PaymentException.java`, `application/port/primary/PaymentPort.java`, `PaymentPortImpl.java`, `command/OpenPaymentCommand.java`, `command/RecordCounterPaymentCommand.java`, `application/port/secondary/PaymentRepository.java`, `application/listener/PaymentIntegrationEventListener.java` (domain → `ShipmentPaidIntegrationEvent`), `infrastructure/adapter/primary/PaymentShipmentIntegrationListener.java`, `PaymentController.java`, `api/*`, `infrastructure/adapter/secondary/PaymentRepositoryImpl.java`, `entity/PaymentEntity.java`, `mapper/*`, `configuration/PaymentConfiguration.java`
- Create in `BillingApi`: `api/event/ShipmentPaidIntegrationEvent.java` (payload `ShipmentPaidData(ShipmentId shipmentId, PaymentId paymentId, Instant paidAt)`)
- Create in `ShipmentManagementImpl`: `infrastructure/adapter/primary/kafka/ShipmentBillingIntegrationListener.java` (group `manager-shipment-billing`) → new `ShipmentPort.markAsPaid(final ShipmentId shipmentId)`
- Create: `changes/2026-11-24-billing-payments.xml` (`payments`, unique `shipment_id`)
- Modify: `OrganisationStructure` operator configuration — add `paymentDeadlineHours` (int, default 72) to `ShipmentPaymentConfiguration` + DTO + embeddable + Liquibase column in the same changelog file
- Test: `PaymentTest.java`, `PaymentPortImplTest.java`, `PaymentFixture.java`

**Interfaces:**
- Consumes: `ShipmentCreatedIntegrationEvent` (`paymentState`, `priceQuoteId`, `shipmentId`, `trackingNumber`, `source`), `PricingPort.findQuote` (Task 3).
- Produces:
  - `Payment.markPaid(final PaymentMethod method, final UserId recordedBy, final DepartmentCode department, final Instant paidAt)` — early return when `PAID` with the same method; throws `PaymentException` from any other non-`PENDING` state.
  - `PaymentPort.open(final OpenPaymentCommand command)` — idempotent upsert keyed by `ShipmentId`; deadline = `createdAt + paymentDeadlineHours`.
  - `PaymentPort.recordCounterPayment(final RecordCounterPaymentCommand command)` — opens the payment if missing, then `markPaid`; command carries `ShipmentId`, `PaymentMethod` (`CASH|CARD` only), optional buyer `TaxId`.
  - `PaymentPort.findByShipment(final ShipmentId)`, `findById(final PaymentId)`, paginated `find(...)`.
  - REST: `POST /billing/shipments/{shipmentId}/counter-payment`, `GET /billing/payments`, `GET /billing/payments/{paymentId}`.
  - `ShipmentPaidIntegrationEvent` on topic `BillingTopics.SHIPMENT_PAID`.

- [ ] **Step 1: Write failing tests**: `shouldOpenPendingPaymentFromQuote`, `shouldIgnoreShipmentCreatedWithPaymentNotRequired`, `shouldOpenPaymentWhenCounterPaymentArrivesBeforeShipmentCreatedEvent`, `shouldIgnoreSecondCounterPaymentForPaidShipment` (one `PaymentPaid` published in total), `shouldRejectStripeMethodForCounterPayment`, `shouldSetDeadlineFromOperatorConfiguration` (72 h default).
- [ ] **Step 2:** Run `-pl Billing/BillingImpl -am test -Dtest='Payment*Test'`. Expected: FAIL.
- [ ] **Step 3:** Implement.
- [ ] **Step 4:** Re-run. Expected: PASS. Also run `ShipmentManagementImpl` with `-Dtest='ShipmentBillingIntegrationListener*'` after adding a listener test `shouldMarkShipmentPaidOnShipmentPaidEvent`.
- [ ] **Step 5:** Commit.

---

### Task 6: Payment gateway — fake and Stripe test mode

**Files:**
- Create in `com.warehouse.billing.payment`: `application/port/secondary/PaymentGatewayServicePort.java`, `infrastructure/adapter/secondary/gateway/FakePaymentGatewayClient.java`, `StripePaymentGatewayClient.java`, `infrastructure/adapter/primary/ExternalPaymentController.java` (`GET /external/payments/{trackingNumber}`), `StripeWebhookController.java` (`POST /billing/webhooks/stripe`), `FakeGatewayController.java` (`POST /billing/dev/fake-gateway/{paymentId}/{outcome}`, bean only when `billing.payment-gateway=fake`), `application/port/secondary/ProcessedWebhookRepository.java` + impl/entity
- Modify: `Billing/BillingImpl/pom.xml` (add `com.stripe:stripe-java`, latest release available — first build online), `Application/src/main/resources/application.properties` (`billing.payment-gateway=${BILLING_PAYMENT_GATEWAY:fake}`, `billing.stripe.secret-key=${STRIPE_SECRET_KEY:}`, `billing.stripe.webhook-secret=${STRIPE_WEBHOOK_SECRET:}`, `billing.payment.success-url=${BILLING_PAYMENT_SUCCESS_URL:http://localhost:3000/payment/success}`, `billing.payment.cancel-url=${BILLING_PAYMENT_CANCEL_URL:http://localhost:3000/payment/cancel}`), `ApiExposureProperties` (add `/billing/webhooks/**` to public endpoints)
- Create: `changes/2026-11-24-billing-gateway.xml` (`payment_gateway_sessions` columns on `payments`: `gateway_reference`, `checkout_url`, `checkout_expires_at`; table `processed_webhook_events(event_id PK, processed_at)`)
- Test: `PaymentGatewayFlowTest.java` (port + fake gateway), `StripeWebhookControllerTest.java` (fixture signed with `com.stripe.net.Webhook.Util.computeHmacSha256` and a test secret)

**Interfaces:**
- Produces:
  - `PaymentGatewayServicePort.openCheckout(final Payment payment): CheckoutSession` — `record CheckoutSession(String gatewayReference, String url, Instant expiresAt)`; Stripe: Checkout Session, mode `payment`, currency from the payment, payment methods `card` plus `blik` when `Currency.PLN`, `metadata.paymentId`, idempotency key `"checkout-" + paymentId`, `expires_at` = min(deadline, now + 24 h).
  - `PaymentGatewayServicePort.expireCheckout(final String gatewayReference)`, `refund(final String gatewayReference, final PaymentId paymentId)` (idempotency key `"refund-" + paymentId`).
  - `PaymentPort.openOnlineCheckout(final TrackingNumber trackingNumber): CheckoutView` — reuses a stored, unexpired session; `record CheckoutView(PaymentStatus status, Money amount, String paymentUrl, Instant expiresAt)`.
  - `PaymentPort.confirmOnlinePayment(final String gatewayReference, final String webhookEventId)` and `confirmRefund(...)`; both no-op when `webhookEventId` is already in `processed_webhook_events`.
  - External response: `404` with body `{"retryAfterSeconds": 2}` when no payment exists yet.

- [ ] **Step 1: Write failing tests**: `shouldReuseOpenCheckoutSession`, `shouldMarkPaidOnCompletedCheckout`, `shouldIgnoreAlreadyProcessedWebhookEvent`, `shouldRejectWebhookWithInvalidSignature` (401, nothing changed), `shouldOfferBlikOnlyForPln` (unit test on the Stripe params builder), `shouldReturn404WithRetryHintWhenPaymentNotOpenedYet`.
- [ ] **Step 2:** Run (online, without `-o`) `-pl Billing/BillingImpl -am test -Dtest='PaymentGatewayFlowTest,StripeWebhookControllerTest'`. Expected: FAIL.
- [ ] **Step 3:** Implement; select the adapter with `@ConditionalOnProperty(name = "billing.payment-gateway", havingValue = "stripe")` / `"fake"` (`matchIfMissing = true` on fake).
- [ ] **Step 4:** Re-run. Expected: PASS.
- [ ] **Step 5:** Commit.

---

### Task 7: Payment deadline and expiry cancellation

**Files:**
- Create: `com.warehouse.billing.payment.infrastructure.adapter.primary.PaymentDeadlineJob.java` (`@Scheduled(cron = "${billing.payment.deadline-cron:0 */15 * * * *}")`), `BillingApi/.../event/PaymentExpiredIntegrationEvent.java`, `domain/event/PaymentExpired.java`
- Modify: `PaymentPortImpl` (`expireOverdue(final Instant now)`), `Payment.expire(final Instant now)` (no-op unless `PENDING` and past deadline), `ShipmentBillingIntegrationListener` (handle `PaymentExpiredIntegrationEvent` → `ShipmentPort.cancelForExpiredPayment(final PriceQuoteId)` (shipment looked up by quote id))
- Test: `PaymentExpiryTest.java`, shipment listener test

**Interfaces:**
- Consumes: `Shipment.cancelForExpiredPayment` (Task 4), `PaymentGatewayServicePort.expireCheckout` (Task 6).
- Produces: `PaymentExpiredIntegrationEvent(PriceQuoteId priceQuoteId, PaymentId paymentId, Instant expiredAt)`. The job runs per operator (iterate active operators through `OperatorApiService` and set the operator context per iteration, following `InactiveDepartmentArchiveJob`).

- [ ] **Step 1: Write failing tests**: `shouldExpireOverduePendingPayment`, `shouldNotExpirePaidPayment`, `shouldExpireStripeCheckoutWhenPaymentExpires`, `shouldCancelShipmentOnPaymentExpired`.
- [ ] **Step 2:** Run the tests. Expected: FAIL.
- [ ] **Step 3:** Implement.
- [ ] **Step 4:** Re-run. Expected: PASS.
- [ ] **Step 5:** Commit.

---

### Task 7a: Per-operator payments switch (default off)

**Files:**
- Modify (OrganisationStructure): `ShipmentPaymentConfiguration` (domain) + `ShipmentPaymentConfigurationDto` + embeddable + `OperatorMapper` + `defaultConfiguration()` — new `boolean paymentsEnabled`, default `false`; null in DTO/cached JSON → `false`
- Modify (ShipmentManagement): wherever `ship()` reads the operator shipment configuration, expose `paymentsEnabled`; `ShipmentPortImpl.ship()` skips `ShipmentPricingServicePort` when off and creates the shipment with `price = null`, `priceQuoteId = null`, `PaymentState.NOT_REQUIRED`
- Create: `changes/2026-11-24-billing-payments-switch.xml` (column `shipment_payments_enabled BOOLEAN NOT NULL DEFAULT FALSE` on the operator configuration table and nullable on its `_aud` table, same pattern as `shipment_payment_deadline_hours`)
- Test: `OperatorConfigurationPortImplTest`/mapper test (default false, round trip), `ShipmentPortImplTest`

**Interfaces:**
- Consumes: `paymentDeadlineHours` pattern (Task 5), `ship()` pricing call (Task 4).
- Produces: `ShipmentPaymentConfiguration.isPaymentsEnabled()`; Billing needs no change (no quote → no payment).

- [ ] **Step 1: Write failing tests**: `shouldDisablePaymentsByDefault`, `shouldDefaultPaymentsDisabledWhenMissingInCachedConfiguration`, `shouldCreateShipmentWithoutPricingWhenPaymentsDisabled` (pricing port never called, NOT_REQUIRED, price null), `shouldPriceShipmentWhenPaymentsEnabled`, `shouldAllowCollectionOfShipmentCreatedWithPaymentsDisabled`.
- [ ] **Step 2:** Run. Expected: FAIL.
- [ ] **Step 3:** Implement.
- [ ] **Step 4:** Re-run, then OrganisationStructureImpl, ShipmentManagementImpl, Billing suites and Application test-compile. Expected: PASS.
- [ ] **Step 5:** Commit.

---

### Task 8: Cancellation, refunds and late completions

**Files:**
- Modify: `Payment` (`cancel()`, `requestRefund(final Instant now)`, `markRefunded(final Instant now)`), `PaymentPortImpl` (`handleShipmentCanceled(final PriceQuoteId, final ShipmentStatus previousStatus)`, `requestManualRefund(final PaymentId)`, `confirmCounterRefund(final PaymentId, final UserId)`), `PaymentShipmentIntegrationListener` (consume `ShipmentCanceledIntegrationEvent`), `PaymentController` (`POST /billing/payments/{id}/refund`, `POST /billing/payments/{id}/counter-refund`), `confirmOnlinePayment` (late completion branch)
- Create: `BillingApi/.../event/PaymentRefundedIntegrationEvent.java`, `domain/event/PaymentRefunded.java`
- Test: `PaymentRefundTest.java`

**Interfaces:**
- Produces: `PaymentRefundedIntegrationEvent(PriceQuoteId, PaymentId, Money amount, Instant refundedAt)` (consumed by Task 9 for the correction invoice).
- Rules (spec step 7): `PENDING` → `CANCELED`; `PAID` + `previousStatus` in `PLANNED/CREATED/PREPARED` → `REFUND_PENDING` (online: gateway refund; counter: wait for worker); `PAID` + any other previous status → unchanged; manual refund allowed only from `PAID`; online `REFUND_PENDING` → `REFUNDED` on `charge.refunded`; completed checkout for `EXPIRED`/`CANCELED` payment → gateway refund, payment goes `REFUND_PENDING` → `REFUNDED`, no `PriceQuotePaid`.

- [ ] **Step 1: Write failing tests**: `shouldCancelPendingPaymentWhenShipmentCanceled`, `shouldRefundOnlinePaymentCanceledBeforeCollection`, `shouldWaitForWorkerWhenCounterPaymentCanceledBeforeCollection`, `shouldNotRefundAutomaticallyAfterCollection`, `shouldAllowManualRefundAfterCollection`, `shouldRefundLateCompletionOfExpiredPayment` (no `PriceQuotePaidIntegrationEvent` published).
- [ ] **Step 2:** Run `-Dtest='PaymentRefundTest'`. Expected: FAIL.
- [ ] **Step 3:** Implement.
- [ ] **Step 4:** Re-run. Expected: PASS.
- [ ] **Step 5:** Commit.

---

### Task 9: Invoices

**Files:**
- Create in `com.warehouse.billing.invoice`: `domain/model/Invoice.java`, `domain/service/InvoiceFactory.java`, `domain/service/InvoiceNumberFormatter.java`, `domain/vo/InvoiceParty.java`, `InvoiceLine.java`, `domain/enumeration/InvoiceType.java`, `application/port/primary/InvoicePort.java`, `InvoicePortImpl.java`, `application/port/secondary/InvoiceRepository.java`, `InvoiceNumberSequenceRepository.java`, `InvoiceDocumentServicePort.java`, `InvoiceSellerServicePort.java`, `InvoiceBuyerServicePort.java`, `infrastructure/adapter/secondary/PdfInvoiceDocumentServiceAdapter.java` (OpenPDF, follow `PdfPalletDocumentServiceAdapter`; store through `DocumentStorageApiService.save`), `InvoiceNumberSequenceRepositoryImpl.java` (row per operator/type/month, `LockModeType.PESSIMISTIC_WRITE` via the Criteria builder's lock option, or a `@Lock` on a Spring Data query in the adapter if the builder has none), `infrastructure/adapter/primary/InvoicePaymentIntegrationListener.java`, `InvoiceController.java`, `application/listener/InvoiceIntegrationEventListener.java`
- Create in `BillingApi`: `event/InvoiceIssuedIntegrationEvent.java` (`InvoiceId`, number, buyer e-mail, buyer language, `UUID documentId`)
- Modify: `OrganisationStructure` — operator billing settings (`billingStreet`, `billingPostalCode`, `billingCity`, `billingCountryCode`, `bankAccount`) on operator configuration + API DTO + `OperatorApiService` read; `MailService` — listener for `InvoiceIssuedIntegrationEvent` mapping to template `invoice.issued` with the PDF attached; Liquibase rows in `mail_templates` for `pl`, `en`, `de`
- Create: `changes/2026-11-24-billing-invoices.xml` (`invoices`, `invoice_lines`, `invoice_number_sequences`, operator billing columns, mail templates)
- Test: `InvoiceNumberFormatterTest.java`, `InvoicePortImplTest.java`

**Interfaces:**
- Consumes: `PriceQuotePaidIntegrationEvent` (Task 5), `PaymentRefundedIntegrationEvent` (Task 8), `PricingPort.findQuote` (Task 3), `PaymentPort.findById` (Task 5).
- Produces: `InvoiceNumberFormatter.format(final InvoiceType type, final YearMonth month, final long sequence): String` → `FV/2026/11/0001`, `KOR/2026/11/0001` (sequence zero-padded to 4, wider when it overflows). Buyer: business customer (Task 10) when the shipment has `billingCustomerId`, otherwise the shipment sender with the optional tax id from the counter payment. Correction invoice lines are the original lines negated. REST: `GET /billing/invoices`, `GET /billing/invoices/{invoiceId}/pdf`.

- [ ] **Step 1: Write failing tests**: `shouldFormatInvoiceNumbers`, `shouldIssueInvoiceFromQuoteLinesWhenShipmentPaid`, `shouldIssueSingleInvoicePerPayment` (second event ignored), `shouldIssueCorrectionWithNegatedLinesOnRefund`, `shouldIncrementSequencePerOperatorAndMonth`.
- [ ] **Step 2:** Run `-Dtest='Invoice*Test'`. Expected: FAIL.
- [ ] **Step 3:** Implement.
- [ ] **Step 4:** Re-run, plus `-pl MailService -am test`. Expected: PASS.
- [ ] **Step 5:** Commit.

---

### Task 10: Business customers and the `/external/**` security chain

**Files:**
- Create in `com.warehouse.billing.customer`: `domain/model/BillingCustomer.java`, `domain/service/BillingCustomerFactory.java`, `domain/enumeration/BillingCustomerStatus.java`, `application/port/primary/BillingCustomerPort.java` + impl, `application/port/secondary/BillingCustomerRepository.java`, `BillingCustomerUserServicePort.java`, `infrastructure/adapter/secondary/BillingCustomerUserServiceAdapter.java` (calls `UserApiService`), `infrastructure/adapter/primary/BillingCustomerController.java`, `BillingCustomerApiServiceAdapter.java`
- Create in `BillingApi`: `BillingCustomerApiService.java` (`findByUser(final UserId userId): Optional<BillingCustomerDto>`)
- Modify `Authorization`: add role `BUSINESS_CUSTOMER(AccountLevel.USER)` to both `Role` enums (`OrganizationUser`, `UserEntity` with no permissions); `CreateOperatorUserApiRequest` pattern adds `BUSINESS_CUSTOMER`; `UserApiService` gains `String regenerateApiKey(final UserId userId)`; `AuthenticationServiceImpl.login` rejects `BUSINESS_CUSTOMER` with the same error as bad credentials; new `configuration/ExternalSecurityConfiguration.java` (`@Order(2)`, matcher `/external/**`) and `ExternalApiKeyAuthenticationFilter.java` modelled on `McpKeyAuthenticationFilter.authenticateLegacyApiKey` but requiring `BUSINESS_CUSTOMER` and an active operator; remove `/external` from the public matchers in `ApiExposureProperties`; the main chain's API-key path must reject `BUSINESS_CUSTOMER`
- Modify `ShipmentManagement`: `ShipmentExternalController` takes operator and `BillingCustomerId` from the authenticated user (via `BillingCustomerApiService`), drops `operatorId` from `ExternalShipmentCreateRequestApi`, rejects `BLOCKED` customers with `403`, keys the rate limiter by customer id; response includes `paymentPath` = `/external/payments/{trackingNumber}`
- Create: `changes/2026-11-24-billing-customers.xml` (`billing_customers`, unique `(operator_id, tax_id)`)
- Test: `BillingCustomerPortImplTest.java`, `ExternalApiKeyAuthenticationFilterTest.java`, `AuthenticationServiceImplTest` (new case), `ShipmentExternalControllerTest` (update)

**Interfaces:**
- Produces: REST `GET/POST/PUT /billing/customers`, `POST /billing/customers/{id}/api-key` (returns the key once), `POST /billing/customers/{id}/block`, `/unblock`. `BillingCustomerPort.create(...)` creates the linked user (username `shop-<taxId>`, random password never shown, department = the creating user's department, language `pl`).

- [ ] **Step 1: Write failing tests**: `shouldCreateCustomerWithLinkedBusinessUser`, `shouldRejectLoginForBusinessCustomer`, `shouldAuthenticateBusinessCustomerKeyOnExternalPath`, `shouldRejectBusinessCustomerKeyOnInternalPath` (401 on `/shipments`), `shouldRejectRegularUserKeyOnExternalPath`, `shouldRejectShipmentFromBlockedCustomer`, `shouldTakeOperatorFromApiKeyNotFromBody`.
- [ ] **Step 2:** Run in `Authorization/AuthorizationImpl`, `Billing/BillingImpl`, `ShipmentManagement/ShipmentManagementImpl`. Expected: FAIL.
- [ ] **Step 3:** Implement.
- [ ] **Step 4:** Re-run. Expected: PASS.
- [ ] **Step 5:** Commit.

---

### Task 11: Role checks on `/billing/**` and audit entries

**Files:**
- Modify: Authorization main security configuration (enable `@EnableMethodSecurity` if absent); every Billing controller method gets `@PreAuthorize` per spec: price list writes, customer management and manual refunds → `hasAnyRole('MANAGER','ADMIN','SUPERADMIN')`; other `/billing/**` → `hasAnyRole('USER','SUPPLIER','MANAGER','ADMIN','SUPERADMIN')` (i.e. not `BUSINESS_CUSTOMER`); check how authorities are named in the existing `@PreAuthorize` and reuse that form
- Modify: `AuditLogResourceType` and `AuditLogResourceTypeDto` (add `PAYMENT`, `INVOICE`, `PRICE_LIST`, `BILLING_CUSTOMER`); create `application/listener/BillingAuditLogListener.java` per sub-context mapping `PaymentPaid`, `PaymentRefunded`, `InvoiceIssued`, price list replaced, customer created/blocked to `AuditLogEntryIntegrationEvent` (follow `ShipmentAuditLogListener`)
- Test: `BillingControllerSecurityTest.java` (`@WithMockUser` per role), audit listener test

- [ ] **Step 1: Write failing tests**: `shouldForbidPriceListChangeForUser`, `shouldAllowCounterPaymentForUser`, `shouldForbidBillingEndpointsForBusinessCustomer`, `shouldPublishAuditEntryWhenPaymentRecorded`.
- [ ] **Step 2:** Run. Expected: FAIL.
- [ ] **Step 3:** Implement.
- [ ] **Step 4:** Re-run; then full backend `mvn ... test` (no `-pl`). Expected: PASS.
- [ ] **Step 5:** Commit.

---

### Task 12: Documentation

**Files:**
- Modify: `CHANGELOG.md` (`[2026.4]` → Added/Changed/Removed: Billing module, unpaid shipment guard, external API now requires a business-customer API key, legacy `price` table removed), `README.md`, `README.pl.md`, `README.de.md` (Billing row in the modules table; Stripe test-mode setup: env vars and `stripe listen --forward-to localhost:8080/v1/api/billing/webhooks/stripe`), `docs/billing-design.md` (status line → implemented)

- [ ] **Step 1:** Update the files.
- [ ] **Step 2:** Commit.
