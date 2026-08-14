# InPost Global Tracking

The tracking integration follows the existing hexagonal module structure. `TrackingProviderServicePort` is the
provider contract, `TrackingProviderRegistry` selects an implementation and batches tracking numbers in groups of
ten, and `InPostTrackingServiceAdapter` maps InPost DTOs to the provider-independent tracking model returned by the
backend API. Provider credentials are tenant-scoped.

## Configuration

1. Set `CREDENTIALS_ENCRYPTION_KEY` to a stable, non-empty deployment secret. It encrypts credentials stored in the
   database.
2. Open **Global configuration → Integrations** in the warehouse GUI and select **Add integration**.
3. Select InPost, choose Stage or Production, enter Client ID and Client Secret, and use **Test connection**. The test
   uses the values currently entered in the dialog, so the configuration does not need to be saved first.
4. Enable the provider after the credentials have been verified.

The OAuth client uses Client Credentials with scope `api:tracking:read`. Stage uses
`https://stage-api.inpost-group.com`; Production uses `https://api.inpost-group.com`. The backend caches access tokens
until shortly before `expires_in`, serializes refreshes per application instance, and never returns credentials or
tokens to the GUI. Connect/read timeouts and bounded retry with exponential jitter can be changed with
`INPOST_CONNECT_TIMEOUT`, `INPOST_READ_TIMEOUT`, `INPOST_RETRY_MAX_ATTEMPTS`, `INPOST_RETRY_INITIAL_DELAY`, and
`INPOST_RETRY_MAX_DELAY`.

## Adding a provider

On the backend, add its ID, implement `TrackingProviderServicePort` (including `integrationDefinition()`), map the
provider DTO to `ExternalTrackingResult`, and register the adapter as a Spring bean. The backend definition validates
the submitted configuration. In the GUI, add one entry to `TrackingIntegrationRegistry.ts`; its fields use the generic
types `TEXT`, `SECRET`, `SELECT`, and `BOOLEAN`. The common dialog then renders fields such as `apiKey` without a new
provider-specific form component.

## Tests

Run backend tracking tests with Java 21:

```shell
mvn -pl ShipmentManagement/ShipmentManagementImpl -am \
  -Dtest='InPost*Test,TrackingProviderRegistryTest' \
  -Dsurefire.failIfNoSpecifiedTests=false test
```

Run the GUI tests and production build:

```shell
npm test -- --watchAll=false
npm run build
```
