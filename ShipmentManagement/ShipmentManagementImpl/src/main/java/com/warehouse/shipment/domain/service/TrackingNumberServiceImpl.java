package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.model.TrackingSequence;
import com.warehouse.shipment.domain.port.secondary.ShipmentConfigurationServicePort;
import com.warehouse.shipment.domain.port.secondary.TrackingSequenceRepository;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberDateFormat;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberRule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

@Service
public class TrackingNumberServiceImpl implements TrackingNumberService {

    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DEFAULT_SEQUENCE_ID = "TRACKING_NUMBER";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ShipmentConfigurationServicePort shipmentConfigurationServicePort;

    private final TrackingSequenceRepository sequenceRepository;

    public TrackingNumberServiceImpl(final ShipmentConfigurationServicePort shipmentConfigurationServicePort,
                                     final TrackingSequenceRepository sequenceRepository) {
        this.shipmentConfigurationServicePort = shipmentConfigurationServicePort;
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    @Transactional
    public TrackingNumber nextTrackingNumber(final ShipmentId shipmentId) {
        final OperatorShipmentConfiguration shipmentConfiguration = this
                .shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration();
        return nextTrackingNumber(shipmentConfiguration.trackingNumberRule(), shipmentId);
    }

    @Override
    @Transactional
    public TrackingNumber nextTrackingNumber(final TrackingNumberRule rule) {
        return nextTrackingNumber(rule, null);
    }

    @Override
    @Transactional
    public TrackingNumber nextTrackingNumber(final TrackingNumberRule rule, final ShipmentId shipmentId) {
        final TrackingNumberRule trackingNumberRule = Objects.requireNonNullElse(rule, TrackingNumberRule.defaults());
        final String separator = trackingNumberRule.separator();
        final String datePart = trackingNumberRule.includeDate()
                ? separator + formatCurrentDate(trackingNumberRule.dateFormat())
                : "";
        final String trackingNumber = trackingNumberRule.key()
                + datePart
                + separator
                + nextValueFor(trackingNumberRule, shipmentId);

        return new TrackingNumber(trackingNumberRule.uppercase()
                ? trackingNumber.toUpperCase(Locale.ROOT)
                : trackingNumber);
    }

    private String nextValueFor(final TrackingNumberRule rule, final ShipmentId shipmentId) {
        return switch (rule.source()) {
            case SEQUENCE -> nextSequenceValue(rule);
            case SHIPMENT_ID -> shipmentIdValue(shipmentId);
            case RANDOM -> randomValue(rule.randomLength());
        };
    }

    private String nextSequenceValue(final TrackingNumberRule rule) {
        final String sequenceId = sequenceId(rule);
        final TrackingSequence sequence = sequenceRepository.findById(sequenceId)
                .orElseGet(() -> new TrackingSequence(sequenceId, 1L));
        final long nextValue = sequence.next();
        sequenceRepository.save(sequence);

        return leftPad(nextValue, rule.randomLength());
    }

    private String sequenceId(final TrackingNumberRule rule) {
        return DEFAULT_SEQUENCE_ID + "_" + rule.key().toUpperCase(Locale.ROOT);
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
