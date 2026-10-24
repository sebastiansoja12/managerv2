package com.warehouse.e2e;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.UserId;

public final class LogisticsE2EFixture {

    public static final String TERMINAL_DEVICE_SEED = "sql/terminal-device-seed.sql";

    public static final String DELIVERY_REJECT_SEED = "sql/delivery-reject-seed.sql";

    public static final DepartmentId DEPARTMENT_ID = new DepartmentId(1L);

    public static final DepartmentCode DEPARTMENT_CODE = new DepartmentCode("KT1");

    public static final UserId TERMINAL_USER_ID = new UserId(1001L);

    public static final String TERMINAL_USERNAME = "e2e.terminal@manager.local";

    public static final DeviceId TERMINAL_DEVICE_ID = new DeviceId("TL:e2e-terminal-0001");

    public static final String TERMINAL_PAIR_KEY = "e2e-terminal-0001-pair-key";

    public static final String TERMINAL_VERSION = "1.0.0";

    public static final SupplierCode SUPPLIER_CODE = new SupplierCode("e2eSupplier");

    public static final ShipmentId REJECTED_SHIPMENT_ID = new ShipmentId(900000001L);

    public static final TrackingNumber REJECTED_SHIPMENT_TRACKING_NUMBER = new TrackingNumber("E2E-REJECT-0001");

    public static final String REJECTED_DELIVERY_ID = "e2e-reject-delivery-0001";

    public static final String REJECT_REASON = "Recipient refused to accept the parcel";

    public static final String COLLECT_SEED = "sql/collect-seed.sql";

    public static final ShipmentId CREATED_SHIPMENT_ID = new ShipmentId(900000101L);

    public static final TrackingNumber CREATED_SHIPMENT_TRACKING_NUMBER = new TrackingNumber("E2E-COLLECT-0101");

    public static final ShipmentId PREPARED_SHIPMENT_ID = new ShipmentId(900000102L);

    public static final TrackingNumber PREPARED_SHIPMENT_TRACKING_NUMBER = new TrackingNumber("E2E-COLLECT-0102");

    public static final ShipmentId PARTIALLY_COLLECTED_SHIPMENT_ID = new ShipmentId(900000103L);

    public static final TrackingNumber PARTIALLY_COLLECTED_SHIPMENT_TRACKING_NUMBER = new TrackingNumber("E2E-COLLECT-0103");

    public static final ShipmentId SENT_SHIPMENT_ID = new ShipmentId(900000104L);

    public static final TrackingNumber SENT_SHIPMENT_TRACKING_NUMBER = new TrackingNumber("E2E-COLLECT-0104");

    public static final TrackingNumber UNKNOWN_TRACKING_NUMBER = new TrackingNumber("E2E-COLLECT-UNKNOWN");

    private LogisticsE2EFixture() {
    }

    public static String collectRequest(final List<TrackingNumber> trackingNumbers) {
        final String collectDetails = trackingNumbers.stream()
                .map(trackingNumber -> "<CollectDetail><TrackingNumber>" + trackingNumber.value()
                        + "</TrackingNumber></CollectDetail>")
                .collect(Collectors.joining());
        return fillTemplate("soap/collect-request.xml", Map.of(
                "version", TERMINAL_VERSION,
                "deviceId", TERMINAL_DEVICE_ID.value(),
                "username", TERMINAL_USERNAME,
                "departmentCode", DEPARTMENT_CODE.value(),
                "pairKey", TERMINAL_PAIR_KEY,
                "collectDetails", collectDetails));
    }

    public static String deliveryRejectRequest() {
        return fillTemplate("soap/delivery-reject-request.xml", Map.of(
                "version", TERMINAL_VERSION,
                "deviceId", TERMINAL_DEVICE_ID.value(),
                "username", TERMINAL_USERNAME,
                "departmentCode", DEPARTMENT_CODE.value(),
                "pairKey", TERMINAL_PAIR_KEY,
                "trackingNumber", REJECTED_SHIPMENT_TRACKING_NUMBER.value(),
                "supplierCode", SUPPLIER_CODE.value(),
                "rejectReason", REJECT_REASON));
    }

    private static String fillTemplate(final String templatePath, final Map<String, String> values) {
        String content = readResource(templatePath);
        for (final Map.Entry<String, String> value : values.entrySet()) {
            content = content.replace("${" + value.getKey() + "}", value.getValue());
        }
        return content;
    }

    private static String readResource(final String path) {
        try {
            return new ClassPathResource(path).getContentAsString(StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
