package com.warehouse.incident.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.incident.domain.enumeration.IncidentCategory;
import com.warehouse.incident.domain.enumeration.IncidentEntryType;
import com.warehouse.incident.domain.enumeration.IncidentResolution;
import com.warehouse.incident.domain.enumeration.IncidentSeverity;
import com.warehouse.incident.domain.enumeration.IncidentStatus;
import com.warehouse.incident.domain.exception.IncidentException;
import com.warehouse.incident.domain.vo.IncidentActor;
import com.warehouse.incident.domain.vo.IncidentEntry;
import com.warehouse.incident.domain.vo.IncidentShipment;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.warehouse.incident.domain.exception.IncidentException.Reason.CONFLICT;
import static com.warehouse.incident.domain.exception.IncidentException.Reason.INVALID_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IncidentTest {

    private static final Instant NOW = Instant.parse("2026-11-04T10:00:00Z");

    private static final IncidentActor REPORTER = new IncidentActor(new UserId(1L), new Username("reporter"));

    private static final IncidentActor HANDLER = new IncidentActor(new UserId(2L), new Username("handler"));

    @Test
    void shouldOpenIncidentWithReportedEntry() {
        final Incident incident = incident(IncidentCategory.DAMAGED, "  Crushed box  ");

        assertThat(incident.status()).isEqualTo(IncidentStatus.OPEN);
        assertThat(incident.description()).isEqualTo("Crushed box");
        assertThat(incident.entries()).extracting(IncidentEntry::type).containsExactly(IncidentEntryType.REPORTED);
    }

    @Test
    void shouldRequireDescriptionForOtherCategory() {
        assertThatThrownBy(() -> incident(IncidentCategory.OTHER, " "))
                .isInstanceOf(IncidentException.class).hasFieldOrPropertyWithValue("reason", INVALID_REQUEST);
    }

    @Test
    void shouldMoveToInProgressWhenAssigned() {
        final Incident incident = incident(IncidentCategory.DAMAGED, null);

        incident.assign(HANDLER, NOW.plusSeconds(60));

        assertThat(incident.status()).isEqualTo(IncidentStatus.IN_PROGRESS);
        assertThat(incident.assignee()).isEqualTo(HANDLER);
        assertThat(incident.updatedAt()).isEqualTo(NOW.plusSeconds(60));
    }

    @Test
    void shouldNotRecordEntryWhenAssigningSameUserAgain() {
        final Incident incident = incident(IncidentCategory.DAMAGED, null);
        incident.assign(HANDLER, NOW);

        incident.assign(HANDLER, NOW.plusSeconds(60));

        assertThat(incident.entries()).extracting(IncidentEntry::type)
                .containsExactly(IncidentEntryType.REPORTED, IncidentEntryType.ASSIGNED);
    }

    @Test
    void shouldRejectBlankComment() {
        final Incident incident = incident(IncidentCategory.DAMAGED, null);

        assertThatThrownBy(() -> incident.comment(HANDLER, "  ", NOW))
                .isInstanceOf(IncidentException.class).hasFieldOrPropertyWithValue("reason", INVALID_REQUEST);
    }

    @Test
    void shouldResolveIncident() {
        final Incident incident = incident(IncidentCategory.DAMAGED, null);

        incident.resolve(HANDLER, IncidentResolution.RETURN_CREATED, "Sent back", NOW);

        assertThat(incident.status()).isEqualTo(IncidentStatus.RESOLVED);
        assertThat(incident.resolution()).isEqualTo(IncidentResolution.RETURN_CREATED);
        assertThat(incident.resolvedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldRejectResolvingResolvedIncident() {
        final Incident incident = incident(IncidentCategory.DAMAGED, null);
        incident.resolve(HANDLER, IncidentResolution.EXPLAINED, null, NOW);

        assertThatThrownBy(() -> incident.resolve(HANDLER, IncidentResolution.EXPLAINED, null, NOW))
                .isInstanceOf(IncidentException.class).hasFieldOrPropertyWithValue("reason", CONFLICT);
    }

    @Test
    void shouldReopenResolvedIncidentBackToAssignee() {
        final Incident incident = incident(IncidentCategory.DAMAGED, null);
        incident.assign(HANDLER, NOW);
        incident.resolve(HANDLER, IncidentResolution.EXPLAINED, null, NOW);

        incident.reopen(REPORTER, "Still damaged", NOW.plusSeconds(60));

        assertThat(incident.status()).isEqualTo(IncidentStatus.IN_PROGRESS);
        assertThat(incident.resolution()).isNull();
        assertThat(incident.resolvedAt()).isNull();
    }

    @Test
    void shouldRejectReopeningUnresolvedIncident() {
        final Incident incident = incident(IncidentCategory.DAMAGED, null);

        assertThatThrownBy(() -> incident.reopen(REPORTER, "Why", NOW))
                .isInstanceOf(IncidentException.class).hasFieldOrPropertyWithValue("reason", CONFLICT);
    }

    private Incident incident(final IncidentCategory category, final String description) {
        return Incident.report(new IncidentShipment(new ShipmentId(10L), new TrackingNumber("KT1")),
                new DepartmentCode("KT1"), category, IncidentSeverity.MEDIUM, description, REPORTER, NOW);
    }
}
