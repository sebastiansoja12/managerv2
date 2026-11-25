package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.application.port.secondary.BillingCustomerServicePort;
import com.warehouse.shipment.application.port.secondary.ShipmentActorServicePort;
import com.warehouse.shipment.domain.exception.BillingCustomerNotActiveException;
import com.warehouse.shipment.domain.vo.ShipmentBillingCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShipmentCustomerPortImplTest {

    private static final UserId USER_ID = new UserId(70L);

    private static final BillingCustomerId CUSTOMER_ID = new BillingCustomerId(55L);

    private BillingCustomerServicePort billingCustomerServicePort;

    private ShipmentCustomerPortImpl port;

    @BeforeEach
    void setUp() {
        final ShipmentActorServicePort shipmentActorServicePort = mock(ShipmentActorServicePort.class);
        this.billingCustomerServicePort = mock(BillingCustomerServicePort.class);
        when(shipmentActorServicePort.currentUserId()).thenReturn(USER_ID);
        this.port = new ShipmentCustomerPortImpl(shipmentActorServicePort, this.billingCustomerServicePort);
    }

    @Test
    void shouldReturnActiveCustomerOfAuthenticatedUser() {
        when(this.billingCustomerServicePort.findByUser(USER_ID))
                .thenReturn(Optional.of(new ShipmentBillingCustomer(CUSTOMER_ID, true)));

        assertThat(this.port.currentActiveCustomer()).isEqualTo(CUSTOMER_ID);
    }

    @Test
    void shouldRejectBlockedCustomer() {
        when(this.billingCustomerServicePort.findByUser(USER_ID))
                .thenReturn(Optional.of(new ShipmentBillingCustomer(CUSTOMER_ID, false)));

        assertThatThrownBy(() -> this.port.currentActiveCustomer())
                .isInstanceOf(BillingCustomerNotActiveException.class);
    }

    @Test
    void shouldRejectUserWithoutCustomer() {
        when(this.billingCustomerServicePort.findByUser(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.port.currentActiveCustomer())
                .isInstanceOf(BillingCustomerNotActiveException.class);
    }
}
