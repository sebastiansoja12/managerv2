package com.warehouse.returning.infrastructure.adapter.primary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.application.port.primary.ReturnPort;
import com.warehouse.returning.application.port.primary.ReturnQueryPort;
import com.warehouse.returning.application.port.primary.command.CreateReturnPackageCommand;
import com.warehouse.returning.domain.vo.CreatedReturn;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.ReturnPackageRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReturnPackageControllerTest {
    private final ReturnPort port = mock(ReturnPort.class);
    private final ReturnQueryPort returnQueryPort = mock(ReturnQueryPort.class);
    private final CurrentUserApiService currentUser = mock(CurrentUserApiService.class);
    private MockMvc http;

    @BeforeEach
    void setUp() {
        final DefaultFormattingConversionService conversion = new DefaultFormattingConversionService();
        conversion.addConverter(String.class, ShipmentId.class, value -> new ShipmentId(Long.valueOf(value)));
        conversion.addConverter(String.class, ReturnPackageId.class, value -> new ReturnPackageId(Long.valueOf(value)));
        http = MockMvcBuilders.standaloneSetup(new ReturnPackageController(port, new ReturnPackageRequestMapper(), currentUser, returnQueryPort))
                .setConversionService(conversion).build();
    }

    @Test
    void shouldCreateUsingAuthenticatedUserAndDepartmentId() throws Exception {
        when(currentUser.getCurrentUserId()).thenReturn(new UserId(11L));
        when(port.create(any())).thenReturn(List.of(new CreatedReturn(new ShipmentId(456L), new ReturnPackageId(123L), ReturnStatus.CREATED)));

        http.perform(post("/returns/packages").contentType(MediaType.APPLICATION_JSON).content("""
                {"shipmentId":{"value":456},"departmentId":{"value":3},"reason":"Damaged parcel","reasonCode":"DAMAGED"}
                """)).andExpect(status().isCreated()).andExpect(jsonPath("$[0].returnId.value").value(123));

        final ArgumentCaptor<CreateReturnPackageCommand> command = ArgumentCaptor.forClass(CreateReturnPackageCommand.class);
        verify(port).create(command.capture());
        assertEquals(new DepartmentId(3L), command.getValue().getDepartmentId());
        assertEquals(new UserId(11L), command.getValue().getUserId());
    }

    @Test
    void shouldProcessCompleteAndCancelByReturnPackageId() throws Exception {
        http.perform(put("/returns/packages/123/process")).andExpect(status().isNoContent());
        http.perform(put("/returns/packages/123/complete")).andExpect(status().isNoContent());
        http.perform(delete("/returns/packages/123")).andExpect(status().isNoContent());

        verify(port).startProcessing(new ReturnPackageId(123L));
        verify(port).complete(new ReturnPackageId(123L));
        verify(port).cancel(new ReturnPackageId(123L));
        verifyNoMoreInteractions(port);
    }
}
