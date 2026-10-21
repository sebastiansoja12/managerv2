# Operator Subscription

The subscription bounded context lives in `OrganisationStructureImpl` under
`com.warehouse.organisationstructure.subscription` and owns the subscription plan assigned to an Operator. It is
separate from the `operator` context of the same module: it does not import its packages and checks operator existence
only through `OperatorApiService`.

## Model

- Plans: `MINI`, `BASIC`, `ENTERPRISE`.
- One subscription per operator (`operator_subscriptions`, primary key `subscription_id` as
  `OperatorSubscriptionId`, unique `operator_id` mapped through `BelongsToOperator`,
  foreign key to `operators` with `ON DELETE CASCADE`). Changing the plan or its period
  overwrites the current assignment. `PlatformAuditInterceptor` records only the request (method, path, status), so
  the previous and new plan with their periods are written to the application log on every change. There is no
  history table.
- `start_date` and `end_date` are calendar dates (`DATE`) and both are inclusive.
- Status for a given day: before `start_date` → `NOT_STARTED`, from `start_date` to `end_date` → `ACTIVE`,
  after `end_date` → `EXPIRED`.

## Time zone

"Today" is resolved in a single business time zone, configured by `manager.subscription.time-zone`
(default `Europe/Warsaw`). A plan ending on 2026-10-31 is active until 23:59:59 Warsaw time on that day, regardless of
the server or browser time zone. The API exchanges dates as ISO `yyyy-MM-dd` strings without time or offset, and the
frontend formats them without converting through UTC.

## API

| Endpoint | Audience | Purpose |
|---|---|---|
| `GET /operators/{operatorId}/subscription` | platform owner | Current assignment, `204` when none |
| `PUT /operators/{operatorId}/subscription` | platform owner (not impersonating) | Assign or change plan and period |
| `GET /subscription` | organization user | Plan state of the signed-in operator |

## Enforcement

`SubscriptionAccessInterceptor` runs on every MVC request after authentication. When the current operator has no plan
(`NOT_ASSIGNED`) or its plan is `NOT_STARTED` or `EXPIRED` it answers `402 Payment Required` with
`{"subscriptionInactive": true, "status", "plan", "startDate", "endDate", "message"}`. This applies to user sessions and device API keys alike.

Always available: `/auth/login`, `/auth/logout`, `/auth/refresh`, `/auth/csrf`, `/auth/signup`,
`/auth/refresh-tokens`, `/auth/me`, `/auth/me/impersonation`, `/auth/me/language`, `/auth/me/password`,
`/subscription/**`, `/maintenance/**`, `OPTIONS` requests, unauthenticated requests and requests of platform owners
(including impersonation, so support can investigate an expired operator). Generating device API keys and changing
the profile name are blocked.

The organization chat WebSocket handshake (`/chat/ws`) is rejected with `402` through `OperatorSubscriptionApiService`.
A chat session opened before the plan became inactive stays connected until it reconnects.

The subscription is cached per operator for 5 seconds (at most 10 000 entries per instance), so a renewal takes effect
within that time on every instance. Platform owner requests are recognised from the authentication without a
database lookup.

## Decisions on ambiguous requirements

- **Operators without an assigned plan are blocked.** There is no data migration: after deployment every existing
  operator stays blocked until a platform owner assigns a plan in the operator workspace.
- **New operators get a starter plan.** `OperatorSubscriptionKafkaEventListener` consumes
  `OperatorCreatedIntegrationEvent` (consumer group `manager.kafka.consumer-groups.operator-subscription-create`) and
  assigns `manager.subscription.starter.plan` (default `MINI`) for `manager.subscription.starter.duration-days`
  (default 30, inclusive) starting on the operator creation day in the business time zone, recorded as `system`.
  An operator that already has a plan keeps it. Until the event is processed the new operator is blocked as
  `NOT_ASSIGNED`.
- **A plan that has not started yet blocks access** the same way as an expired plan, but the operator is told the
  start date instead of being asked to renew.
- Kafka listeners, scheduled jobs and other background processing are not blocked; the restriction targets access by the
  operator, not background processing of data already in the system.
