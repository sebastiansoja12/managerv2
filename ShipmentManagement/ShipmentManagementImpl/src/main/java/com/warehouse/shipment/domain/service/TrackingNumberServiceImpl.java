package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberDateFormat;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberRule;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class TrackingNumberServiceImpl implements TrackingNumberService {

    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public TrackingNumber nextTrackingNumber(final TrackingNumberRule rule) {
        return nextTrackingNumber(rule, null, null);
    }

    @Override
    public TrackingNumber nextTrackingNumber(final TrackingNumberRule rule, final ShipmentId shipmentId) {
        return nextTrackingNumber(rule, shipmentId, null);
    }

    @Override
    public TrackingNumber nextTrackingNumber(final TrackingNumberRule rule,
                                             final ShipmentId shipmentId,
                                             final Long sequenceValue) {
        final TrackingNumberRule trackingNumberRule = Objects.requireNonNullElse(rule, TrackingNumberRule.defaults());
        final String separator = trackingNumberRule.separator();
        final String datePart = trackingNumberRule.includeDate()
                ? separator + formatCurrentDate(trackingNumberRule.dateFormat())
                : "";
        final String trackingNumber = trackingNumberRule.key()
                + datePart
                + separator
                + nextValueFor(trackingNumberRule, shipmentId, sequenceValue);

        return new TrackingNumber(trackingNumberRule.uppercase()
                ? trackingNumber.toUpperCase(Locale.ROOT)
                : trackingNumber);
    }

    private String nextValueFor(final TrackingNumberRule rule,
                                final ShipmentId shipmentId,
                                final Long sequenceValue) {
        return switch (rule.source()) {
            case SEQUENCE -> sequenceValue(rule, sequenceValue);
            case SHIPMENT_ID -> shipmentIdValue(shipmentId);
            case RANDOM -> randomValue(rule.randomLength());
        };
    }

    private String sequenceValue(final TrackingNumberRule rule, final Long sequenceValue) {
        if (sequenceValue == null) {
            throw new IllegalArgumentException("Sequence value is required for sequence based tracking number");
        }
        return leftPad(sequenceValue, rule.randomLength());
    }

    private String shipmentIdValue(final ShipmentId shipmentId) {
        if (shipmentId == null || shipmentId.getValue() == null) {
            throw new IllegalArgumentException("Shipment id is required for shipment-id based tracking number");
        }

        return String.valueOf(shipmentId.getValue());
    }

    private String randomValue(final int length) {
        final int safeLength = Math.max(length, 1);
        final StringBuilder value = new StringBuilder(safeLength);
        for (int i = 0; i < safeLength; i++) {
            value.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }

        return value.toString();
    }

    private String leftPad(final long value, final int length) {
        final int safeLength = Math.max(length, 1);
        return String.format("%0" + safeLength + "d", value);
    }

    private String formatCurrentDate(final TrackingNumberDateFormat dateFormat) {
        final DateTimeFormatter formatter = switch (dateFormat) {
            case YYYYMMDD -> DateTimeFormatter.BASIC_ISO_DATE;
            case YYMMDD -> DateTimeFormatter.ofPattern("yyMMdd");
            case YYYYMM -> DateTimeFormatter.ofPattern("yyyyMM");
        };

        return LocalDate.now().format(formatter);
    }
}
