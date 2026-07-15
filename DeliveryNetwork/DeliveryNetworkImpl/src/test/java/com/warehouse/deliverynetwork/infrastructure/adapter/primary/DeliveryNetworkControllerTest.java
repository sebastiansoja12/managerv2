package com.warehouse.deliverynetwork.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPort;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DepartmentConnectionCodeResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DepartmentExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DepartmentConnectionApi;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DepartmentIdApi;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DeliveryNetworkApiResponse;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.ReplaceDeliveryNetworkApiRequest;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper.DeliveryNetworkEditorApiMapper;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.spreadsheet.DeliveryNetworkSpreadsheetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryNetworkControllerTest {

    @Mock
    private DeliveryNetworkPort deliveryNetworkPort;

    private DeliveryNetworkController controller;

    @BeforeEach
    void setUp() {
        this.controller = new DeliveryNetworkController(
                this.deliveryNetworkPort,
                new DeliveryNetworkEditorApiMapper(),
                new DeliveryNetworkSpreadsheetService());
    }

    @Test
    void shouldReturnCurrentOperatorNetwork() {
        when(this.deliveryNetworkPort.getCurrentNetwork()).thenReturn(network(1L, 2L));

        final ResponseEntity<DeliveryNetworkApiResponse> response = this.controller.getCurrentNetwork();

        assertEquals(List.of(connectionApi(1L, 2L)), response.getBody().connections());
    }

    @Test
    void shouldReplaceCompleteCurrentOperatorNetwork() {
        final ReplaceDeliveryNetworkApiRequest request = new ReplaceDeliveryNetworkApiRequest(List.of(
                connectionApi(2L, 1L)));
        when(this.deliveryNetworkPort.replaceCurrentNetwork(org.mockito.ArgumentMatchers.any()))
                .thenReturn(network(1L, 2L));

        final ResponseEntity<DeliveryNetworkApiResponse> response = this.controller.replaceCurrentNetwork(request);

        final ArgumentCaptor<ReplaceDeliveryNetworkCommand> commandCaptor =
                ArgumentCaptor.forClass(ReplaceDeliveryNetworkCommand.class);
        verify(this.deliveryNetworkPort).replaceCurrentNetwork(commandCaptor.capture());
        assertEquals(new DepartmentId(2L), commandCaptor.getValue().connections().getFirst().firstDepartmentId());
        assertEquals(new DepartmentId(1L), commandCaptor.getValue().connections().getFirst().secondDepartmentId());
        assertEquals(List.of(connectionApi(1L, 2L)), response.getBody().connections());
    }

    @Test
    void shouldExportCurrentNetworkAsExcelWorkbook() {
        when(this.deliveryNetworkPort.getCurrentNetworkForExport()).thenReturn(exportNetwork());

        final ResponseEntity<byte[]> response = this.controller.exportCurrentNetwork();

        assertEquals(DeliveryNetworkSpreadsheetService.CONTENT_TYPE,
                response.getHeaders().getContentType().toString());
        assertEquals("attachment; filename=\"delivery-network.xlsx\"",
                response.getHeaders().getFirst("Content-Disposition"));
    }

    @Test
    void shouldImportAndReplaceCurrentNetworkFromExcelWorkbook() {
        final DeliveryNetworkSpreadsheetService spreadsheetService = new DeliveryNetworkSpreadsheetService();
        final byte[] workbook = spreadsheetService.exportWorkbook(exportNetwork());
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "delivery-network.xlsx",
                DeliveryNetworkSpreadsheetService.CONTENT_TYPE,
                workbook);
        when(this.deliveryNetworkPort.replaceCurrentNetworkByDepartmentCodes(org.mockito.ArgumentMatchers.any()))
                .thenReturn(network(1L, 2L));

        final ResponseEntity<DeliveryNetworkApiResponse> response = this.controller.importCurrentNetwork(file);

        assertEquals(List.of(connectionApi(1L, 2L)), response.getBody().connections());
        verify(this.deliveryNetworkPort).replaceCurrentNetworkByDepartmentCodes(
                org.mockito.ArgumentMatchers.argThat(command ->
                        command.departments().size() == 2
                                && "KT1".equals(command.connections().getFirst().firstDepartmentCode().getValue())
                                && "NCS".equals(command.connections().getFirst().secondDepartmentCode().getValue())));
    }

    private static DeliveryNetworkResult network(final Long firstDepartmentId, final Long secondDepartmentId) {
        return new DeliveryNetworkResult(Set.of(new DepartmentConnection(
                new DepartmentId(firstDepartmentId),
                new DepartmentId(secondDepartmentId))));
    }

    private static DeliveryNetworkExportResult exportNetwork() {
        return new DeliveryNetworkExportResult(
                List.of(
                        new DepartmentExportResult(
                                new DepartmentCode("KT1"), DepartmentType.BRANCH, DepartmentStatus.ACTIVE),
                        new DepartmentExportResult(
                                new DepartmentCode("NCS"),
                                DepartmentType.SORTING_FACILITY,
                                DepartmentStatus.ACTIVE)),
                List.of(new DepartmentConnectionCodeResult(
                        new DepartmentCode("KT1"),
                        new DepartmentCode("NCS"))));
    }

    private static DepartmentConnectionApi connectionApi(final Long firstDepartmentId, final Long secondDepartmentId) {
        return new DepartmentConnectionApi(
                new DepartmentIdApi(String.valueOf(firstDepartmentId)),
                new DepartmentIdApi(String.valueOf(secondDepartmentId)));
    }
}
