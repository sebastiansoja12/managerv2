package com.warehouse.deliverynetwork.infrastructure.adapter.primary.spreadsheet;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkByCodesCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DepartmentConnectionCodeResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DepartmentExportResult;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeliveryNetworkSpreadsheetServiceTest {

    private final DeliveryNetworkSpreadsheetService spreadsheetService = new DeliveryNetworkSpreadsheetService();

    @Test
    void shouldImportAnExportedWorkbookWithoutLosingDepartmentCodes() {
        final byte[] workbook = this.spreadsheetService.exportWorkbook(new DeliveryNetworkExportResult(
                List.of(
                        department("GD", DepartmentType.BRANCH),
                        department("KT1", DepartmentType.BRANCH),
                        department("NCS", DepartmentType.SORTING_FACILITY)),
                List.of(
                        connection("NCS", "KT1"),
                        connection("GD", "NCS"))));

        final ReplaceDeliveryNetworkByCodesCommand command = this.spreadsheetService.importWorkbook(
                new ByteArrayInputStream(workbook));

        assertEquals(List.of("GD", "KT1", "NCS"), command.departments().stream()
                .map(department -> department.departmentCode().getValue())
                .toList());
        assertEquals(2, command.connections().size());
        assertEquals("GD", command.connections().getFirst().firstDepartmentCode().getValue());
        assertEquals("NCS", command.connections().getFirst().secondDepartmentCode().getValue());
        assertEquals("KT1", command.connections().get(1).firstDepartmentCode().getValue());
        assertEquals("NCS", command.connections().get(1).secondDepartmentCode().getValue());
    }

    @Test
    void shouldExportDepartmentsWithoutRelations() throws IOException {
        final byte[] workbook = this.spreadsheetService.exportWorkbook(new DeliveryNetworkExportResult(
                List.of(department("SORT", DepartmentType.SORTING_FACILITY)),
                List.of()));

        try (XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(workbook))) {
            assertEquals(2, xssfWorkbook.getNumberOfSheets());
            assertEquals("Oddziały", xssfWorkbook.getSheetAt(0).getSheetName());
            assertEquals("SORT", xssfWorkbook.getSheet("Oddziały").getRow(1).getCell(0).getStringCellValue());
            assertEquals("Relacje", xssfWorkbook.getSheetAt(1).getSheetName());
            assertEquals(0, xssfWorkbook.getSheet("Relacje").getLastRowNum());
        }
    }

    @Test
    void shouldRejectWorkbookWithMissingDepartmentCode() throws IOException {
        final byte[] exportedWorkbook = this.spreadsheetService.exportWorkbook(new DeliveryNetworkExportResult(
                List.of(
                        department("KT1", DepartmentType.BRANCH),
                        department("NCS", DepartmentType.SORTING_FACILITY)),
                List.of(connection("KT1", "NCS"))));
        final byte[] invalidWorkbook;
        try (XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(exportedWorkbook));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            xssfWorkbook.getSheet("Relacje").getRow(1).getCell(1).setBlank();
            xssfWorkbook.write(outputStream);
            invalidWorkbook = outputStream.toByteArray();
        }

        assertThrows(
                InvalidDeliveryNetworkSpreadsheetException.class,
                () -> this.spreadsheetService.importWorkbook(new ByteArrayInputStream(invalidWorkbook)));
    }

    private static DepartmentConnectionCodeResult connection(final String firstCode, final String secondCode) {
        return new DepartmentConnectionCodeResult(new DepartmentCode(firstCode), new DepartmentCode(secondCode));
    }

    private static DepartmentExportResult department(final String code, final DepartmentType departmentType) {
        return new DepartmentExportResult(new DepartmentCode(code), departmentType, DepartmentStatus.ACTIVE);
    }
}
