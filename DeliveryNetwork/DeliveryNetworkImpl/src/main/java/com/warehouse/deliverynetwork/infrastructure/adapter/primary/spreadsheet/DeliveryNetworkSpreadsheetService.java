package com.warehouse.deliverynetwork.infrastructure.adapter.primary.spreadsheet;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentConnectionCodeCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentImportCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkByCodesCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DepartmentConnectionCodeResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DepartmentExportResult;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DeliveryNetworkSpreadsheetService {

    public static final String FILE_NAME = "delivery-network.xlsx";

    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static final long MAX_FILE_SIZE = 5L * 1024L * 1024L;

    private static final String DEPARTMENTS_SHEET_NAME = "Departments";

    private static final String RELATIONS_SHEET_NAME = "Relations";

    private static final String DEPARTMENT_CODE_HEADER = "Department code";

    private static final String DEPARTMENT_TYPE_HEADER = "Type";

    private static final String DEPARTMENT_STATUS_HEADER = "Status";

    private static final String FIRST_DEPARTMENT_HEADER = "Department A";

    private static final String SECOND_DEPARTMENT_HEADER = "Department B";

    private static final int MAX_DATA_ROWS = 10_000;

    public byte[] exportWorkbook(final DeliveryNetworkExportResult deliveryNetwork) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final Sheet departmentsSheet = workbook.createSheet(DEPARTMENTS_SHEET_NAME);
            createDepartmentsHeader(workbook, departmentsSheet);
            createDepartmentRows(departmentsSheet, deliveryNetwork.departments());
            formatSheet(departmentsSheet, deliveryNetwork.departments().size(), 2, 22, 28, 18);

            final Sheet relationsSheet = workbook.createSheet(RELATIONS_SHEET_NAME);
            createRelationsHeader(workbook, relationsSheet);
            createConnectionRows(relationsSheet, deliveryNetwork.connections());
            formatSheet(relationsSheet, deliveryNetwork.connections().size(), 1, 28, 28);
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (final IOException exception) {
            throw new InvalidDeliveryNetworkSpreadsheetException(
                    "Could not create the delivery network spreadsheet", exception);
        }
    }

    public ReplaceDeliveryNetworkByCodesCommand importWorkbook(final InputStream inputStream) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            final Sheet departmentsSheet = requiredSheet(workbook, DEPARTMENTS_SHEET_NAME);
            final Sheet relationsSheet = requiredSheet(workbook, RELATIONS_SHEET_NAME);
            validateDepartmentsHeader(departmentsSheet);
            validateRelationsHeader(relationsSheet);
            validateRowLimit(departmentsSheet);
            validateRowLimit(relationsSheet);
            final DataFormatter dataFormatter = new DataFormatter();
            return new ReplaceDeliveryNetworkByCodesCommand(
                    readDepartments(departmentsSheet, dataFormatter),
                    readConnections(relationsSheet, dataFormatter));
        } catch (final InvalidDeliveryNetworkSpreadsheetException exception) {
            throw exception;
        } catch (final IOException | RuntimeException exception) {
            throw new InvalidDeliveryNetworkSpreadsheetException(
                    "Could not read the Excel file. Use an .xlsx file exported from the application.",
                    exception);
        }
    }

    private Sheet requiredSheet(final XSSFWorkbook workbook, final String sheetName) {
        final Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw invalid("The workbook must contain a '" + sheetName + "' sheet");
        }
        return sheet;
    }

    private void createDepartmentsHeader(final XSSFWorkbook workbook, final Sheet sheet) {
        final Row header = sheet.createRow(0);
        header.setHeightInPoints(24);
        final CellStyle style = headerStyle(workbook);
        createHeaderCell(header, 0, DEPARTMENT_CODE_HEADER, style);
        createHeaderCell(header, 1, DEPARTMENT_TYPE_HEADER, style);
        createHeaderCell(header, 2, DEPARTMENT_STATUS_HEADER, style);
    }

    private void createRelationsHeader(final XSSFWorkbook workbook, final Sheet sheet) {
        final Row header = sheet.createRow(0);
        header.setHeightInPoints(24);
        final CellStyle style = headerStyle(workbook);
        createHeaderCell(header, 0, FIRST_DEPARTMENT_HEADER, style);
        createHeaderCell(header, 1, SECOND_DEPARTMENT_HEADER, style);
    }

    private CellStyle headerStyle(final XSSFWorkbook workbook) {
        final Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());

        final CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void createHeaderCell(
            final Row header,
            final int column,
            final String value,
            final CellStyle style) {
        final Cell cell = header.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createConnectionRows(
            final Sheet sheet,
            final List<DepartmentConnectionCodeResult> connections) {
        final List<DepartmentConnectionCodeResult> sortedConnections = connections.stream()
                .sorted(Comparator
                        .comparing((DepartmentConnectionCodeResult connection) ->
                                connection.firstDepartmentCode().getValue())
                        .thenComparing(connection -> connection.secondDepartmentCode().getValue()))
                .toList();

        for (int index = 0; index < sortedConnections.size(); index++) {
            final DepartmentConnectionCodeResult connection = sortedConnections.get(index);
            final Row row = sheet.createRow(index + 1);
            row.createCell(0).setCellValue(connection.firstDepartmentCode().getValue());
            row.createCell(1).setCellValue(connection.secondDepartmentCode().getValue());
        }
    }

    private void createDepartmentRows(
            final Sheet sheet,
            final List<DepartmentExportResult> departments) {
        final List<DepartmentExportResult> sortedDepartments = departments.stream()
                .sorted(Comparator.comparing(department -> department.departmentCode().getValue()))
                .toList();

        for (int index = 0; index < sortedDepartments.size(); index++) {
            final DepartmentExportResult department = sortedDepartments.get(index);
            final Row row = sheet.createRow(index + 1);
            row.createCell(0).setCellValue(department.departmentCode().getValue());
            row.createCell(1).setCellValue(department.departmentType().name());
            row.createCell(2).setCellValue(department.status().name());
        }
    }

    private void formatSheet(
            final Sheet sheet,
            final int dataRowCount,
            final int lastColumn,
            final int... columnWidths) {
        sheet.createFreezePane(0, 1);
        sheet.setAutoFilter(new CellRangeAddress(0, Math.max(dataRowCount, 0), 0, lastColumn));
        sheet.setDisplayGridlines(false);
        for (int column = 0; column < columnWidths.length; column++) {
            sheet.setColumnWidth(column, columnWidths[column] * 256);
        }
    }

    private void validateDepartmentsHeader(final Sheet sheet) {
        final Row header = sheet.getRow(0);
        final DataFormatter dataFormatter = new DataFormatter();
        if (header == null
                || !DEPARTMENT_CODE_HEADER.equals(cellValue(header, 0, dataFormatter))
                || !DEPARTMENT_TYPE_HEADER.equals(cellValue(header, 1, dataFormatter))
                || !DEPARTMENT_STATUS_HEADER.equals(cellValue(header, 2, dataFormatter))) {
            throw invalid("The first row of the '" + DEPARTMENTS_SHEET_NAME + "' sheet must contain the columns '"
                    + DEPARTMENT_CODE_HEADER + "', '" + DEPARTMENT_TYPE_HEADER + "' and '"
                    + DEPARTMENT_STATUS_HEADER + "'");
        }
    }

    private void validateRelationsHeader(final Sheet sheet) {
        final Row header = sheet.getRow(0);
        final DataFormatter dataFormatter = new DataFormatter();
        if (header == null
                || !FIRST_DEPARTMENT_HEADER.equals(cellValue(header, 0, dataFormatter))
                || !SECOND_DEPARTMENT_HEADER.equals(cellValue(header, 1, dataFormatter))) {
            throw invalid("The first row of the '" + RELATIONS_SHEET_NAME + "' sheet must contain the columns '"
                    + FIRST_DEPARTMENT_HEADER + "' and '" + SECOND_DEPARTMENT_HEADER + "'");
        }
    }

    private void validateRowLimit(final Sheet sheet) {
        if (sheet.getLastRowNum() > MAX_DATA_ROWS) {
            throw invalid("A sheet can contain at most " + MAX_DATA_ROWS + " data rows");
        }
    }

    private List<DepartmentImportCommand> readDepartments(
            final Sheet sheet,
            final DataFormatter dataFormatter) {
        final List<DepartmentImportCommand> departments = new ArrayList<>();
        final Set<String> normalizedDepartmentCodes = new HashSet<>();
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            final Row row = sheet.getRow(rowIndex);
            if (row == null || rowIsEmpty(row, 3, dataFormatter)) {
                continue;
            }
            final String departmentCode = cellValue(row, 0, dataFormatter);
            final String departmentType = cellValue(row, 1, dataFormatter);
            final String departmentStatus = cellValue(row, 2, dataFormatter);
            if (departmentCode.isBlank() || departmentType.isBlank() || departmentStatus.isBlank()) {
                throw invalid("Row " + (rowIndex + 1) + " of the '" + DEPARTMENTS_SHEET_NAME
                        + "' sheet must contain department code, type and status");
            }
            if (!normalizedDepartmentCodes.add(departmentCode.toUpperCase(Locale.ROOT))) {
                throw invalid("Duplicate department code in row " + (rowIndex + 1) + ": " + departmentCode);
            }
            departments.add(new DepartmentImportCommand(
                    new DepartmentCode(departmentCode),
                    departmentType(departmentType, rowIndex),
                    departmentStatus(departmentStatus, rowIndex)));
        }
        return departments;
    }

    private DepartmentType departmentType(final String value, final int rowIndex) {
        try {
            return DepartmentType.valueOf(value);
        } catch (final IllegalArgumentException exception) {
            throw invalid("Unknown department type in row " + (rowIndex + 1) + ": " + value);
        }
    }

    private DepartmentStatus departmentStatus(final String value, final int rowIndex) {
        try {
            return DepartmentStatus.valueOf(value);
        } catch (final IllegalArgumentException exception) {
            throw invalid("Unknown department status in row " + (rowIndex + 1) + ": " + value);
        }
    }

    private List<DepartmentConnectionCodeCommand> readConnections(
            final Sheet sheet,
            final DataFormatter dataFormatter) {
        final List<DepartmentConnectionCodeCommand> connections = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            final Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            final String firstDepartmentCode = cellValue(row, 0, dataFormatter);
            final String secondDepartmentCode = cellValue(row, 1, dataFormatter);
            if (firstDepartmentCode.isBlank() && secondDepartmentCode.isBlank()) {
                continue;
            }
            if (firstDepartmentCode.isBlank() || secondDepartmentCode.isBlank()) {
                throw invalid("Row " + (rowIndex + 1) + " must contain both department codes");
            }
            connections.add(new DepartmentConnectionCodeCommand(
                    new DepartmentCode(firstDepartmentCode),
                    new DepartmentCode(secondDepartmentCode)));
        }
        return connections;
    }

    private boolean rowIsEmpty(
            final Row row,
            final int columnCount,
            final DataFormatter dataFormatter) {
        for (int column = 0; column < columnCount; column++) {
            if (!cellValue(row, column, dataFormatter).isBlank()) {
                return false;
            }
        }
        return true;
    }

    private String cellValue(final Row row, final int column, final DataFormatter dataFormatter) {
        final Cell cell = row.getCell(column, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return "";
        }
        return dataFormatter.formatCellValue(cell).trim();
    }

    private InvalidDeliveryNetworkSpreadsheetException invalid(final String message) {
        return new InvalidDeliveryNetworkSpreadsheetException(message);
    }
}
