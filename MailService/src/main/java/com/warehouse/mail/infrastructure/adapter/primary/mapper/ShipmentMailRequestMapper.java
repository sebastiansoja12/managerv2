package com.warehouse.mail.infrastructure.adapter.primary.mapper;

import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.vo.EmailAddress;
import com.warehouse.reroute.api.event.ShipmentRerouteRequestedIntegrationEvent;
import com.warehouse.reroute.api.event.snapshot.ReroutePartyData;
import com.warehouse.shipment.api.event.snapshot.PartySnapshot;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ShipmentMailRequestMapper {

    private static final String EXTERNAL_SOURCE = "EXTERNAL";
    private static final MailParty EMPTY_PARTY = new MailParty(null, null, null, null, null, null, null, null);

    private final MailLocaleResolver mailLocaleResolver;
    private final String trackingUrl;
    private final String shipmentEditUrl;

    public ShipmentMailRequestMapper(final MailLocaleResolver mailLocaleResolver,
                                     final String trackingUrl,
                                     final String shipmentEditUrl) {
        this.mailLocaleResolver = mailLocaleResolver;
        this.trackingUrl = trackingUrl;
        this.shipmentEditUrl = shipmentEditUrl;
    }

    public List<MailRequest> mapCreated(final ShipmentEventData shipment) {
        if (shipment.shipmentRelatedId() != null) {
            return List.of();
        }
        return EXTERNAL_SOURCE.equals(shipment.source())
                ? map("shipment.created.external", shipment)
                : map("shipment.created", shipment);
    }

    public List<MailRequest> mapStatusChanged(final ShipmentEventData shipment) {
        return shipment.shipmentStatus() == null ? List.of()
                : map("shipment.status." + shipment.shipmentStatus().name().toLowerCase(Locale.ROOT), shipment);
    }

    public List<MailRequest> mapOutForDelivery(final ShipmentEventData shipment) {
        return map("shipment.out-for-delivery", shipment);
    }

    public List<MailRequest> mapDelivered(final ShipmentEventData shipment) {
        return map("shipment.delivered", shipment);
    }

    public List<MailRequest> mapRerouteRequested(final ShipmentRerouteRequestedIntegrationEvent event) {
        return forSenderAndRecipient("shipment.reroute.requested", event.trackingNumber(), "",
                party(event.sender()), party(event.recipient()));
    }

    private List<MailRequest> map(final String templateKeyPrefix, final ShipmentEventData shipment) {
        return forSenderAndRecipient(templateKeyPrefix,
                shipment.trackingNumber() == null ? null : shipment.trackingNumber().value(),
                shipment.shipmentStatus() == null ? "" : shipment.shipmentStatus().name(),
                party(shipment.sender()), party(shipment.recipient()));
    }

    private List<MailRequest> forSenderAndRecipient(final String templateKeyPrefix,
                                                    final String trackingNumber,
                                                    final String status,
                                                    final MailParty sender,
                                                    final MailParty recipient) {
        final Map<String, String> variables = new HashMap<>();
        final String normalizedTrackingNumber = text(trackingNumber);
        variables.put("trackingNumber", normalizedTrackingNumber);
        variables.put("status", status);
        variables.put("trackingUrl", link(this.trackingUrl, normalizedTrackingNumber));
        variables.put("shipmentEditUrl", link(this.shipmentEditUrl, normalizedTrackingNumber));
        putParty(variables, "sender", sender);
        putParty(variables, "recipient", recipient);
        return Stream.of(request(templateKeyPrefix + ".sender", sender, variables),
                        request(templateKeyPrefix + ".recipient", recipient, variables))
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<MailRequest> request(final String templateKey,
                                          final MailParty addressee,
                                          final Map<String, String> sharedVariables) {
        return EmailAddress.parse(addressee.email()).map(address -> {
            final Map<String, String> variables = new HashMap<>(sharedVariables);
            variables.put("firstName", text(addressee.firstName()));
            variables.put("lastName", text(addressee.lastName()));
            return new MailRequest(templateKey, this.mailLocaleResolver.forCountry(addressee.countryCode()), address,
                    variables);
        });
    }

    private static void putParty(final Map<String, String> variables, final String prefix, final MailParty party) {
        variables.put(prefix + "FirstName", text(party.firstName()));
        variables.put(prefix + "LastName", text(party.lastName()));
        variables.put(prefix + "Street", text(party.street()));
        variables.put(prefix + "PostalCode", text(party.postalCode()));
        variables.put(prefix + "City", text(party.city()));
        variables.put(prefix + "CountryCode", text(party.countryCode()));
        variables.put(prefix + "TelephoneNumber", text(party.telephoneNumber()));
    }

    private static MailParty party(final PartySnapshot party) {
        return party == null ? EMPTY_PARTY : new MailParty(party.firstName(), party.lastName(), party.email(),
                party.telephoneNumber(), party.street(), party.postalCode(), party.city(), party.countryCode());
    }

    private static MailParty party(final ReroutePartyData party) {
        return party == null ? EMPTY_PARTY : new MailParty(party.firstName(), party.lastName(), party.email(),
                party.telephoneNumber(), party.street(), party.postalCode(), party.city(), party.countryCode());
    }

    private static String link(final String baseUrl, final String trackingNumber) {
        if (baseUrl == null || baseUrl.isBlank() || trackingNumber.isEmpty()) {
            return "";
        }
        final String normalized = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return normalized + "/" + URLEncoder.encode(trackingNumber, StandardCharsets.UTF_8);
    }

    private static String text(final String value) {
        return value == null ? "" : value.trim();
    }

    private record MailParty(String firstName,
                             String lastName,
                             String email,
                             String telephoneNumber,
                             String street,
                             String postalCode,
                             String city,
                             String countryCode) {
    }
}
