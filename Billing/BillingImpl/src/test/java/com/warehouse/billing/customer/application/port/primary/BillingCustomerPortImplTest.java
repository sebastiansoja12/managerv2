package com.warehouse.billing.customer.application.port.primary;

import com.warehouse.billing.customer.application.port.primary.command.CreateBillingCustomerCommand;
import com.warehouse.billing.customer.application.port.primary.command.UpdateBillingCustomerCommand;
import com.warehouse.billing.customer.application.port.secondary.BillingCustomerRepository;
import com.warehouse.billing.customer.application.port.secondary.BillingCustomerUserServicePort;
import com.warehouse.billing.customer.domain.enumeration.BillingCustomerStatus;
import com.warehouse.billing.customer.domain.exception.BillingCustomerException;
import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.billing.customer.domain.service.BillingCustomerFactory;
import com.warehouse.billing.customer.domain.vo.BillingCustomerAccount;
import com.warehouse.billing.customer.domain.vo.BillingCustomerAddress;
import com.warehouse.billing.customer.domain.vo.BillingCustomerDetails;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class BillingCustomerPortImplTest {

    private static final Instant NOW = Instant.parse("2026-11-24T12:00:00Z");

    private static final TaxId TAX_ID = new TaxId("5260250995");

    private static final UserId USER_ID = new UserId(70L);

    private static final OperatorId OPERATOR_ID = OperatorId.of(7L);

    private static final BillingCustomerDetails DETAILS = new BillingCustomerDetails("Sklep ABC sp. z o.o.",
            new BillingCustomerAddress("Długa 1", "31-147", "Kraków", "PL"), "faktury@sklep.pl");

    private BillingCustomerRepository repository;

    private BillingCustomerUserServicePort userServicePort;

    private BillingCustomerPortImpl port;

    @BeforeEach
    void setUp() {
        this.repository = mock(BillingCustomerRepository.class);
        this.userServicePort = mock(BillingCustomerUserServicePort.class);
        this.port = new BillingCustomerPortImpl(this.repository, this.userServicePort, new BillingCustomerFactory(),
                Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void shouldCreateCustomerWithLinkedBusinessUser() {
        when(this.repository.findByTaxId(TAX_ID)).thenReturn(Optional.empty());
        when(this.userServicePort.currentOperatorId()).thenReturn(OPERATOR_ID);
        when(this.userServicePort.createUser("shop-7-5260250995", "Sklep ABC sp. z o.o.",
                "shop-7-5260250995@billing.invalid")).thenReturn(new BillingCustomerAccount(USER_ID, OPERATOR_ID));

        final BillingCustomer customer = this.port.create(new CreateBillingCustomerCommand(TAX_ID, DETAILS));

        assertThat(customer.id()).isNotNull();
        assertThat(customer.operatorId()).isEqualTo(OPERATOR_ID);
        assertThat(customer.userId()).isEqualTo(USER_ID);
        assertThat(customer.taxId()).isEqualTo(TAX_ID);
        assertThat(customer.details()).isEqualTo(DETAILS);
        assertThat(customer.status()).isEqualTo(BillingCustomerStatus.ACTIVE);
        assertThat(customer.createdAt()).isEqualTo(NOW);
        verify(this.repository).create(customer);
        verify(this.userServicePort, never()).regenerateApiKey(any());
    }

    @Test
    void shouldCreateCustomersWithSameTaxIdInDifferentOperators() {
        final OperatorId otherOperatorId = OperatorId.of(8L);
        when(this.repository.findByTaxId(TAX_ID)).thenReturn(Optional.empty());
        when(this.userServicePort.currentOperatorId()).thenReturn(OPERATOR_ID, otherOperatorId);
        when(this.userServicePort.createUser("shop-7-5260250995", "Sklep ABC sp. z o.o.",
                "shop-7-5260250995@billing.invalid")).thenReturn(new BillingCustomerAccount(USER_ID, OPERATOR_ID));
        when(this.userServicePort.createUser("shop-8-5260250995", "Sklep ABC sp. z o.o.",
                "shop-8-5260250995@billing.invalid"))
                .thenReturn(new BillingCustomerAccount(new UserId(71L), otherOperatorId));

        final BillingCustomer first = this.port.create(new CreateBillingCustomerCommand(TAX_ID, DETAILS));
        final BillingCustomer second = this.port.create(new CreateBillingCustomerCommand(TAX_ID, DETAILS));

        assertThat(first.operatorId()).isEqualTo(OPERATOR_ID);
        assertThat(second.operatorId()).isEqualTo(otherOperatorId);
        assertThat(first.userId()).isNotEqualTo(second.userId());
        assertThat(first.details().invoiceEmail()).isEqualTo("faktury@sklep.pl");
        assertThat(second.details().invoiceEmail()).isEqualTo("faktury@sklep.pl");
        verify(this.repository).create(first);
        verify(this.repository).create(second);
    }

    @Test
    void shouldRejectDuplicateTaxIdBeforeCreatingUser() {
        when(this.repository.findByTaxId(TAX_ID)).thenReturn(Optional.of(customer()));

        assertThatThrownBy(() -> this.port.create(new CreateBillingCustomerCommand(TAX_ID, DETAILS)))
                .isInstanceOfSatisfying(BillingCustomerException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(BillingCustomerException.Reason.DUPLICATE));
        verifyNoInteractions(this.userServicePort);
    }

    @Test
    void shouldUpdateCustomerDetails() {
        final BillingCustomer customer = customer();
        when(this.repository.findById(customer.id())).thenReturn(Optional.of(customer));
        final BillingCustomerDetails changed = new BillingCustomerDetails("Sklep XYZ",
                new BillingCustomerAddress("Krótka 2", "00-001", "Warszawa", "PL"), "nowe@sklep.pl");

        final BillingCustomer updated = this.port.update(new UpdateBillingCustomerCommand(customer.id(), changed));

        assertThat(updated.details()).isEqualTo(changed);
        assertThat(updated.taxId()).isEqualTo(TAX_ID);
        verify(this.repository).update(customer);
    }

    @Test
    void shouldRegenerateApiKeyOfLinkedUser() {
        final BillingCustomer customer = customer();
        when(this.repository.findById(customer.id())).thenReturn(Optional.of(customer));
        when(this.userServicePort.regenerateApiKey(USER_ID)).thenReturn("new-api-key");

        final String apiKey = this.port.regenerateApiKey(customer.id());

        assertThat(apiKey).isEqualTo("new-api-key");
    }

    @Test
    void shouldBlockAndUnblockOnlyCustomerStatus() {
        final BillingCustomer customer = customer();
        when(this.repository.findById(customer.id())).thenReturn(Optional.of(customer));

        assertThat(this.port.block(customer.id()).status()).isEqualTo(BillingCustomerStatus.BLOCKED);
        assertThat(this.port.block(customer.id()).status()).isEqualTo(BillingCustomerStatus.BLOCKED);
        assertThat(this.port.unblock(customer.id()).status()).isEqualTo(BillingCustomerStatus.ACTIVE);

        verifyNoInteractions(this.userServicePort);
    }

    @Test
    void shouldThrowNotFoundForUnknownCustomer() {
        final BillingCustomerId customerId = new BillingCustomerId(404L);
        when(this.repository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.port.block(customerId))
                .isInstanceOfSatisfying(BillingCustomerException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(BillingCustomerException.Reason.NOT_FOUND));
    }

    @Test
    void shouldReturnCurrentActiveCustomer() {
        final BillingCustomer customer = customer();
        when(this.userServicePort.currentUserId()).thenReturn(USER_ID);
        when(this.repository.findByUser(USER_ID)).thenReturn(Optional.of(customer));

        assertThat(this.port.currentActive()).isSameAs(customer);
    }

    @Test
    void shouldRejectBlockedCurrentCustomer() {
        final BillingCustomer customer = customer();
        customer.block(NOW);
        when(this.userServicePort.currentUserId()).thenReturn(USER_ID);
        when(this.repository.findByUser(USER_ID)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> this.port.currentActive())
                .isInstanceOfSatisfying(BillingCustomerException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(BillingCustomerException.Reason.NOT_ACTIVE));
    }

    @Test
    void shouldRejectCurrentUserWithoutCustomer() {
        when(this.userServicePort.currentUserId()).thenReturn(USER_ID);
        when(this.repository.findByUser(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.port.currentActive())
                .isInstanceOfSatisfying(BillingCustomerException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(BillingCustomerException.Reason.NOT_ACTIVE));
    }

    private BillingCustomer customer() {
        return new BillingCustomerFactory().create(TAX_ID, DETAILS, new BillingCustomerAccount(USER_ID, OPERATOR_ID),
                NOW);
    }
}
