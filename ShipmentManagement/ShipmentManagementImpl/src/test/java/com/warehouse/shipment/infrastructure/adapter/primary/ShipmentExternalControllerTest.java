package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.shipment.application.port.primary.ShipmentCustomerPort;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.secondary.ShipmentConfigurationPort;
import com.warehouse.shipment.domain.exception.BillingCustomerNotActiveException;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ExternalShipmentCreateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ShipmentExternalControllerTest {

    private static final BillingCustomerId CUSTOMER_ID = new BillingCustomerId(55L);

    private static final String BODY = """
            {"operatorId":999,"shipment":{}}""";

    private ShipmentPort shipmentPort;

    private ShipmentCustomerPort shipmentCustomerPort;

    private ShipmentRequestMapper requestMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.shipmentPort = mock(ShipmentPort.class);
        this.shipmentCustomerPort = mock(ShipmentCustomerPort.class);
        this.requestMapper = mock(ShipmentRequestMapper.class);
        final ShipmentConfigurationPort shipmentConfigurationPort = mock(ShipmentConfigurationPort.class);
        when(shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration())
                .thenReturn(OperatorShipmentConfiguration.defaults());
        when(this.shipmentCustomerPort.currentActiveCustomer()).thenReturn(CUSTOMER_ID);
        when(this.requestMapper.mapExternalCreateRequest(any())).thenAnswer(invocation -> new ShipmentCreateCommand());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ShipmentExternalController(this.shipmentPort,
                        this.shipmentCustomerPort, mock(ShipmentRequestValidator.class), this.requestMapper,
                        shipmentConfigurationPort, 2, Duration.ofMinutes(10)))
                .build();
    }

    @Test
    void shouldTakeOperatorFromApiKeyNotFromBody() throws Exception {
        givenShipped(true);

        this.mockMvc.perform(post("/external/shipments").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());

        final ArgumentCaptor<ShipmentCreateCommand> command = ArgumentCaptor.forClass(ShipmentCreateCommand.class);
        verify(this.shipmentPort).ship(command.capture());
        assertThat(command.getValue().getBillingCustomerId()).isEqualTo(CUSTOMER_ID);
        verify(this.shipmentPort, never()).isOperatorActive(any());
        assertThat(Arrays.stream(ExternalShipmentCreateRequestApi.class.getRecordComponents())
                .map(component -> component.getName())).containsExactly("shipment");
    }

    @Test
    void shouldReturnPaymentPathWhenPaymentIsRequired() throws Exception {
        givenShipped(true);

        this.mockMvc.perform(post("/external/shipments").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingNumber").value("TRK-1"))
                .andExpect(jsonPath("$.paymentPath").value("/external/payments/TRK-1"));
    }

    @Test
    void shouldOmitPaymentPathWhenPaymentIsNotRequired() throws Exception {
        givenShipped(false);

        this.mockMvc.perform(post("/external/shipments").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingNumber").value("TRK-1"))
                .andExpect(jsonPath("$.paymentPath").doesNotExist());
    }

    @Test
    void shouldRejectShipmentFromBlockedCustomer() throws Exception {
        when(this.shipmentCustomerPort.currentActiveCustomer()).thenThrow(new BillingCustomerNotActiveException());

        this.mockMvc.perform(post("/external/shipments").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isForbidden());

        verify(this.shipmentPort, never()).ship(any());
    }

    @Test
    void shouldLimitRequestsPerCustomer() throws Exception {
        givenShipped(false);

        this.mockMvc.perform(post("/external/shipments").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/external/shipments").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/external/shipments").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isTooManyRequests());

        when(this.shipmentCustomerPort.currentActiveCustomer()).thenReturn(new BillingCustomerId(56L));
        this.mockMvc.perform(post("/external/shipments").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
    }

    private void givenShipped(final boolean paymentRequired) {
        when(this.shipmentPort.ship(any())).thenReturn(Result.success(
                new ShipmentCreateResponse(new ExternalId<>(UUID.randomUUID()), "TRK-1", paymentRequired)));
    }
}
