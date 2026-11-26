# Billing

Status: design, not implemented. Date: 2026-11-24.

## Intent

A shipment must be paid for before it enters the network. Today nothing enforces that: the price is typed in by the
caller (`ShipmentCreateRequestApi.price`), nobody takes the money, and no invoice exists. Billing prices a shipment
from the operator's price list, takes the payment, blocks the shipment until it is paid and issues an invoice.

Manager 2.0 is a hobby application. Payments go through a real payment gateway in **test mode only**; no real money
moves and no invoice is sent to tax authorities.

## Who pays and how

| Channel | Who | How they pay | When |
| --- | --- | --- | --- |
| Department counter (`ShipmentSource.WEB`) | Private person sending a parcel in a department; an office worker creates the shipment in the GUI | Cash or card on the department's own card terminal; the worker records the payment in the GUI | Before the worker hands the parcel over |
| External API (`ShipmentSource.EXTERNAL`) | Online shop sending shipment requests | Online, through a Stripe Checkout payment link | Before the shipment can be collected |
| Public website (later) | Private person | Online, same mechanism as shops | Out of scope now; the design must not block it |

## Goals

- Price every new shipment from an operator-defined price list instead of a caller-supplied price, when the
  operator has payments turned on (they are off by default).
- Hold an unpaid shipment in `CREATED`: it cannot be collected, prepared or accepted until it is paid.
- Record counter payments (cash, card) made in a department.
- Let online shops pay per shipment through Stripe Checkout in test mode, confirmed by Stripe webhooks.
- Identify each shop so it can be invoiced; shops get a business-customer account and their own API key.
- Issue a numbered PDF invoice for every paid shipment and a correction invoice for every refund; e-mail it.
- Cancel shipments that stay unpaid past an operator-configured deadline.
- Refund a paid shipment that is cancelled before it is collected.

## Non-goals

- KSeF or any submission of invoices to tax authorities.
- Real (live-mode) payments.
- Cash-on-delivery collection and settlement (separate feature; `ShipmentPaymentConfiguration` stays as is).
- Billing operators for their platform subscription.
- General ledger, cost accounting, VAT/JPK reports. Department costs stay in `OrganizationAnalytics`.
- Postpaid monthly invoicing or prepaid balances for shops.
- Partial payments, discounts, promo codes, multi-currency conversion.
- The public website integration itself.

## Anti-goals

- A shipment reaching `COLLECTED`/`PREPARED`/`ACCEPTED` while unpaid.
- Charging twice for the same shipment (duplicate webhook, double click, retried request).
- Pricing or payment rules living in `ShipmentManagement`.
- Stripe secret keys, webhook secrets or shop API keys appearing in logs, responses or the repository.

## Approaches considered

1. **New `Billing` bounded context (`BillingApi` + `BillingImpl`)**, hexagonal, talking to `ShipmentManagement`
   through integration events and the Kafka outbox. **Chosen.** It matches every other module, keeps money rules out
   of the shipment aggregate, and the future public website is just another channel of the same context. Cost: more
   moving parts, and the unpaid → paid transition of a shipment is eventually consistent.
2. Payment fields and rules inside `ShipmentManagement`. Least code, but the price list and shop accounts have no
   natural home, and it repeats the coupling that was just removed by extracting deliveries into `logisticsdelivery`.
3. A separate service behind `Gateway`, like `ReturningTrackManager`. Full isolation, but its own database,
   deployment and Feign calls are not worth it for a hobby application.

## Module

- Maven: `Billing/BillingApi`, `Billing/BillingImpl`, added to the root reactor and to `Application`.
- Root package `com.warehouse.billing`, bounded contexts as sub-packages following `AGENTS.md`:
  `pricing`, `payment`, `invoice`, `customer`.
- All aggregates are operator-scoped (`BelongsToOperator`, repositories through `OperatorFilteredRepository`).
- New identifiers in `Common`: `PriceListId`, `PaymentId`, `InvoiceId`, `BillingCustomerId`.
- Topics in `com.warehouse.billing.api.event.BillingTopics`.
- Liquibase changesets under `Application/src/main/resources/changelog/db/postgresql`, author `s-soja`.

## Pricing

One active price list per operator (`price_lists`, `price_list_entries`). It replaces the legacy `price` table read by
`PriceRepositoryImpl` in `ShipmentManagement`; that table and its repository are removed once Billing prices
shipments.

A price list contains, all amounts net in the operator's currency:

- size classes defined in the price list by maximum dimensions (call: shipments carry only dimensions, and the legacy
  `ShipmentSize` enum with `CUSTOM`/`TEST` is not a pricing input); each class has a base price; a shipment larger than
  every class cannot be priced and is rejected;
- weight surcharge bands (from–to kg → surcharge);
- service-level multiplier for `ECONOMY`, `STANDARD`, `EXPRESS`;
- surcharge per delivery method (courier, pickup point, locker);
- insurance fee as a percentage of the shipment's `declaredValue`, charged only when the shipment has one (call:
  there is no "insurance chosen" flag and the `InsuranceAmount` value object is unused; `requireInsuranceAboveValue`
  keeps its current meaning and is not a pricing input);
- VAT rate (default 23%).

`PriceCalculator` (domain service) produces a `PriceQuote`: line items, net, VAT, gross, currency. Amounts use
`BigDecimal` with scale 2 and `RoundingMode.HALF_UP`, rounded per line item.

`BillingApi` exposes a synchronous `PriceQuoteApiService.quote(...)`. Billing persists every quote (`price_quotes`)
with its line items and returns a `PriceQuoteId`. Maven direction (call, following the existing
`ShipmentManagementImpl → LogisticsOrchestratorApi → ShipmentManagementApi` precedent, which avoids a cycle):
`BillingApi` depends on `ShipmentManagementApi` (shipment events) and `ShipmentManagementImpl` depends on `BillingApi`
(quotes, payment events).
`ShipmentManagement` calls it while creating a shipment and stores the `PriceQuoteId` and the gross amount as the
shipment's `price`; the `price` field is removed from `ShipmentCreateRequestApi` and
`ExternalShipmentCreateRequestApi`. The payment and the invoice are built from the stored quote, so a price-list
change during the payment deadline does not change what the customer pays (call).
`POST /billing/quotes` lets the GUI show the price before the shipment is saved.

Price lists live only in the database; no price values are hard-coded in Java. Liquibase seeds one template price
list (not tied to an operator) and copies it for each existing operator; `OperatorCreated` provisioning copies the
template for new operators.

## Payments switch (per operator)

Decided by the user: payments can be turned off per operator. `ShipmentPaymentConfiguration` gets
`paymentsEnabled` (boolean), **default `false`** for new operators and, through Liquibase, for every existing one.

- Off: `ShipmentPortImpl.ship()` does not call Billing at all — no quote, no price (`price` stays null), and the
  shipment is created `NOT_REQUIRED`. Billing therefore opens no payment, issues no invoice, and the payment guard
  never blocks it: every shipment operation works as before Billing existed. A missing price list does not matter.
- On: the behaviour described in the rest of this document.
- Switching does not touch existing shipments (call): shipments created while payments were on keep their
  `UNPAID`/`PAID` state and their open payments keep their deadline; shipments created while off stay
  `NOT_REQUIRED` after payments are turned on.
- The setting is part of the operator configuration API like `paymentDeadlineHours` and is read by
  ShipmentManagement through the shipment configuration it already loads in `ship()`.

## Shipment side (ShipmentManagement changes)

- New field on `Shipment`: `PaymentState` = `UNPAID | PAID | NOT_REQUIRED`.
  - Only shipments created by `ShipmentPortImpl.ship()` (GUI and external API) start `UNPAID`.
  - Every other shipment that publishes `ShipmentCreated` — the new `PARENT` from `changeShipmentType`, redirect and
    return shipments — starts `NOT_REQUIRED`; Billing ignores `ShipmentCreated` for `NOT_REQUIRED` shipments, so a
    split never opens a second payment.
  - Existing rows are migrated to `NOT_REQUIRED`.
  - Splitting (`changeShipmentType` to `CHILD`) and redirecting require the source shipment to be `PAID` or
    `NOT_REQUIRED`; an `UNPAID` source is rejected, so a derived `NOT_REQUIRED` shipment can never carry an unpaid
    parcel out of draft (call, from the task-4 review).
- Guard (call: one rule instead of per-method checks). The draft statuses `PLANNED`, `CREATED`, `PREPARED` are where
  an unpaid shipment waits; an operator whose `workflowSettings.defaultStatus()` is `PREPARED` is fine. Any transition
  that leaves the draft statuses — `notifyShipmentCollected`, `acceptInDepartment`, `notifyShipmentLoadedOnPallet`,
  `notifyShipmentAccepted`, and every `ShipmentStatusChangeStrategy` target outside the draft set — calls
  `ensurePaid()` and throws `ShipmentModificationException` when the state is `UNPAID`. Pickup-point drop-offs
  (`PLANNED`) follow the same rule: the shop pays online before the parcel is dropped off.
- `markAsPaid()` sets `PAID`; it is called from an inbound adapter listening to `PriceQuotePaidIntegrationEvent`, which finds the shipment by its `priceQuoteId`.
- `PaymentState` is added to `ShipmentEventData`, the read model and the shipment API responses.
- `CancellationReason.PAYMENT_EXPIRED` is added; an inbound listener on `PaymentExpiredIntegrationEvent` cancels the
  shipment through a system cancellation that skips `cancellationWindowMinutes` (call: the window protects customers
  from late self-service cancellation, not from expiry) but still requires a draft status.
- New field `PriceQuoteId` on the shipment and in `ShipmentEventData`, plus optional `BillingCustomerId`.

## Payment

Aggregate `Payment`: id, price quote id, amount as `Money` (gross with currency), channel (`COUNTER` | `ONLINE`),
method (`CASH` | `CARD` | `STRIPE`), status, gateway reference, deadline, timestamps, recorded-by user and
`DepartmentId` for counter payments. One payment per price quote (unique `price_quote_id`).

Decided: a payment has **no link to a shipment** — no shipment id, no tracking number. Billing pays a price quote;
the shipment holds the quote id and maps quote → shipment on its side. Lookups that start from a shipment
(counter payment, the shop's tracking number) resolve the quote id first: the GUI already has `priceQuoteId` in the
shipment response; `/external/payments/{trackingNumber}` asks `ShipmentApiService` for the quote id of that tracking
number and never stores it. Every amount that carries its own currency in Billing (payment amount, quote and quote-line
net/VAT/gross, invoice totals and lines) is `Money` (`com.warehouse.commonassets.model.Money`, embedded in entities as
amount + currency columns); price-list entries stay plain amounts in the list's single currency; `Money` gains immutable arithmetic (`add`, `subtract`, `multiply`,
`percentage`, `negate`, `isZero`/`isPositive`, `compareTo`) that rejects mixed currencies and rounds to scale 2
`HALF_UP` where a result is a monetary amount.

States: `PENDING → PAID → REFUND_PENDING → REFUNDED`, `PENDING → EXPIRED`, `PENDING → CANCELED`.

Flow:

1. `ShipmentCreatedIntegrationEvent` → Billing creates a `PENDING` payment for the event's `priceQuoteId` with the
   quote's gross amount and a deadline `quotedAt + OperatorConfiguration.paymentDeadline` (new setting, default 72
   hours). Shipments created with `NOT_REQUIRED` are ignored.
2. Counter: the worker clicks *Przyjmij płatność* on the shipment, picks cash or card;
   `POST /billing/quotes/{priceQuoteId}/counter-payment` marks it `PAID` with the user and department (`DepartmentId`).
   Payment creation is an idempotent upsert keyed by price quote id from the stored quote, so the call works even if
   the `ShipmentCreated` event has not been consumed yet (call). The shipment's `PAID` state still arrives through the
   event; until then the GUI shows "potwierdzanie płatności" and refreshes.
3. Online: the shop calls `GET /external/payments/{trackingNumber}`. On first call Billing creates a Stripe Checkout
   Session (operator currency; card, plus BLIK only when the currency is PLN (call); `success_url`/`cancel_url` from configuration, metadata = payment id, Stripe
   idempotency key = payment id) and returns `{status, amount, paymentUrl, expiresAt}`; later calls return the same
   session while it is valid. If the payment does not exist yet (event not processed) it answers `404` with a retry
   hint. The response of `POST /external/shipments` also includes this URL path.
4. Stripe calls `POST /billing/webhooks/stripe` (public, signature verified with the webhook secret). On
   `checkout.session.completed` the payment becomes `PAID`. Webhook event ids are stored to ignore duplicates. A
   completed session for a payment already `EXPIRED` or `CANCELED` is refunded automatically and never reopens the
   shipment (call).
5. On `PAID` Billing publishes `PriceQuotePaidIntegrationEvent(priceQuoteId, paymentId, paidAt)` and issues the
   invoice; ShipmentManagement finds the shipment by `priceQuoteId` and marks it paid. `PaymentExpired` and
   `PaymentRefunded` events likewise carry `priceQuoteId`, never a shipment id; `ShipmentCanceledIntegrationEvent`
   is matched to the payment through the `priceQuoteId` in `ShipmentEventData`.
6. `PaymentDeadlineJob` (`@Scheduled`, cron in properties) expires overdue `PENDING` payments, expires their Stripe
   session and publishes `PaymentExpiredIntegrationEvent`.
7. `ShipmentCanceledIntegrationEvent` (decided: automatic refund only before collection):
   - `PENDING` → `CANCELED`;
   - `PAID` and the shipment was still in a draft status when cancelled (`ShipmentCanceledIntegrationEvent` gets a new
     `previousStatus` property): online → `REFUND_PENDING`, Stripe refund requested, `REFUNDED` on the `charge.refunded` webhook;
     counter → `REFUND_PENDING` until a worker confirms paying the money back
     (`POST /billing/payments/{paymentId}/counter-refund`), then `REFUNDED`;
   - `PAID` and cancelled after leaving the draft statuses (e.g. `cancelAfterIncident`): no automatic refund; the
     payment stays `PAID`. An admin or manager can start a refund manually with
     `POST /billing/payments/{paymentId}/refund`, which then follows the same online/counter path.
   Every refund issues a correction invoice.

Gateway integration sits behind `PaymentGatewayServicePort` with a Stripe client adapter (Stripe Java SDK) and a
fake adapter selected by `billing.payment-gateway=stripe|fake` (fake is the default for tests and for running without
Stripe keys; its "payment URL" points at a dev endpoint that simulates the webhook). Configuration:
`STRIPE_SECRET_KEY`, `STRIPE_WEBHOOK_SECRET`, `BILLING_PAYMENT_SUCCESS_URL`, `BILLING_PAYMENT_CANCEL_URL`. Locally,
`stripe listen --forward-to localhost:8080/v1/api/billing/webhooks/stripe` forwards webhooks.

## Business customers (shops)

Aggregate `BillingCustomer`: id, operator, company name, tax id (`TaxId`), address, invoice e-mail, status
(`ACTIVE | BLOCKED`), linked `UserId`.

- An admin or manager creates the shop in the GUI. Billing creates the linked user with a new role
  `BUSINESS_CUSTOMER` (account level `USER`) through `Authorization`'s API and generates its API key with the
  existing per-user `ApiKey` mechanism (`UserPort.regenerateApiKey`, `findByApiKeyHash`).
- Decided: a `BUSINESS_CUSTOMER` user **cannot log in** (`/auth/login` rejects the role) and its key is accepted
  **only on `/external/**`**. This needs a dedicated security filter chain for `/external/**`, modelled on
  `McpSecurityConfiguration`: it authenticates the API key, requires the `BUSINESS_CUSTOMER` role and sets the
  operator context from the key's user. The main chain never accepts these keys, and the `/external` entry in the
  public matchers of `ApiExposureProperties` is removed.
- `POST /external/shipments` and `GET /external/payments/**` require that API key. The operator and the customer are
  taken from the key, not from `operatorId` in the body. The anonymous mode (operator id in body, IP rate limit only)
  is removed; the public website will get its own entry point later. The per-IP limiter becomes a per-customer limit.
- A `BLOCKED` customer cannot create shipments.
- Shipments created by a shop carry the `BillingCustomerId` (new optional field on the shipment).

## Invoice

Aggregate `Invoice`: id, number, type (`INVOICE | CORRECTION`), corrected invoice id, issue date, sale date, seller,
buyer, line items (from the `PriceQuote`), net/VAT/gross totals, payment method, payment id, PDF document id.

- Numbering per operator per month: `FV/{yyyy}/{MM}/{seq}` and `KOR/{yyyy}/{MM}/{seq}`, sequence held in a counter
  row locked pessimistically so numbers have no gaps or duplicates.
- Seller: operator `companyName` and `taxId` plus a new billing address and bank account in operator billing
  settings (the `Operator` aggregate has no address today).
- Buyer: the `BillingCustomer` for shops; for counter payments the shipment sender, with an optional tax id the
  worker can enter when recording the payment.
- The PDF is rendered with OpenPDF (already used by `PdfPalletDocumentServiceAdapter`) and stored through
  `DocumentStorageApi`.
- `InvoiceIssuedIntegrationEvent` → `MailService` sends it with a new `invoice.issued` template (`pl`, `en`, `de`).
- `InvoiceSubmissionServicePort` is not built; KSeF stays a non-goal. The invoice model keeps the fields FA(3) would
  need (seller/buyer tax ids, sale date, VAT rate per line) so it can be added later.

## Events

Published by Billing (in `BillingApi`, through `IntegrationEventPublisher`; all keyed by `priceQuoteId`, none carries a shipment id):
`PriceQuotePaidIntegrationEvent`, `PaymentExpiredIntegrationEvent`, `PaymentRefundedIntegrationEvent`,
`InvoiceIssuedIntegrationEvent`.

Consumed by Billing: `ShipmentCreatedIntegrationEvent`, `ShipmentCanceledIntegrationEvent`, `OperatorCreated`.

Billing also maps its domain events to `AuditLogEntryIntegrationEvent` (payment recorded, refund, invoice issued,
price list changed), like the other modules.

## REST API

| Endpoint | Audience | Purpose |
| --- | --- | --- |
| `GET/PUT /billing/price-list` | Admin | Read and replace the operator's price list |
| `POST /billing/quotes` | Office user | Price a shipment before saving |
| `GET /billing/payments`, `GET /billing/payments/{id}` | Office user | List/filter and read payments |
| `POST /billing/quotes/{priceQuoteId}/counter-payment` | Office user | Record a cash or card payment |
| `POST /billing/payments/{id}/counter-refund` | Office user | Confirm a counter refund |
| `POST /billing/payments/{id}/refund` | Admin, manager | Start a manual refund after collection |
| `GET /billing/invoices`, `GET /billing/invoices/{id}/pdf` | Office user | List and download invoices |
| `GET/POST/PUT /billing/customers`, `POST /billing/customers/{id}/api-key` | Admin, manager | Manage shops, rotate their API key |
| `GET /external/payments/{trackingNumber}` | Shop (API key) | Payment status and Stripe payment link |
| `POST /billing/webhooks/stripe` | Stripe | Webhook (public, signature verified) |

### Authorization of `/billing/**`

Decided: in scope. Today nothing checks roles (`anyRequest().authenticated()`, one `@PreAuthorize` in the codebase).
Billing endpoints get role checks with `@PreAuthorize` on the controllers, using the existing `AccountLevel`:
price list and business customers require `MANAGER` or higher, manual refunds `MANAGER` or higher, everything else
any authenticated office user (`USER` or higher). `BUSINESS_CUSTOMER` can never reach `/billing/**`. The "Audience"
column above is this rule. Method security is enabled for the main chain if it is not already.

## GUI (managerv2gui)

- Shipment creation: shows the quote; price is no longer an input.
- Shipment details: payment status badge; *Przyjmij płatność* (cash/card, optional buyer tax id); invoice download.
- New menu section *Rozliczenia*: payments, invoices, price list editor, business customers with API key rotation.
- Operator configuration: payment deadline and seller billing data.

## Terminal (managerv2terminal)

Collecting or accepting an `UNPAID` shipment fails on the server; the terminal shows a dedicated
"Przesyłka nieopłacona" message instead of a generic error. No payment is taken on the terminal.

## Testing

- Domain unit tests: `PriceCalculator`, `Payment` transitions, invoice numbering, `Shipment` payment guard.
- Application tests with the fake gateway: create → pay → collected allowed; create → expire → cancelled;
  paid → cancel → refund → correction invoice; duplicate webhook ignored.
- Webhook signature verification test with a Stripe-signed fixture.
- Architecture tests: Billing follows the package rules; `ShipmentManagementImpl` does not import Billing
  implementation packages.

## Left to the builder

- Exact class names inside the listed contexts, following `AGENTS.md` naming (`*ServicePort`, `*ServiceAdapter`,
  external-system adapters named `*Client`).
- Table and column names, indexes.
- GUI layout of the new screens, within the existing component style.
- Cron expression for `PaymentDeadlineJob` (suggested: every 15 minutes).
