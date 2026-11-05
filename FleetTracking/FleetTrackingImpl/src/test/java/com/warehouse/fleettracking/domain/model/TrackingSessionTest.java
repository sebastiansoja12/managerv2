package com.warehouse.fleettracking.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.fleettracking.domain.enumeration.TrackingSessionStatus;
import com.warehouse.fleettracking.domain.exception.FleetTrackingException;
import com.warehouse.fleettracking.domain.vo.PositionFix;
import com.warehouse.fleettracking.domain.vo.TrackingDriver;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static com.warehouse.fleettracking.domain.exception.FleetTrackingException.Reason.ACCESS_DENIED;
import static com.warehouse.fleettracking.domain.exception.FleetTrackingException.Reason.CONFLICT;
import static com.warehouse.fleettracking.domain.exception.FleetTrackingException.Reason.INVALID_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TrackingSessionTest {

    private static final Instant NOW = Instant.parse("2026-11-05T08:00:00Z");

    private static final UserId DRIVER_ID = new UserId(1L);

    private static final UserId OTHER_ID = new UserId(2L);

    @Test
    void shouldAcceptPositionsInSequenceOrder() {
        final TrackingSession session = session();

        session.report(DRIVER_ID, List.of(fix(2, 52.2300, 21.0110, 30), fix(1, 52.2297, 21.0122, 0)));

        assertThat(session.acceptedPositions()).extracting(PositionFix::sequence).containsExactly(1L, 2L);
        assertThat(session.lastPosition().sequence()).isEqualTo(2L);
    }

    @Test
    void shouldIgnoreAlreadyReportedSequence() {
        final TrackingSession session = TrackingSession.rehydrate(session().trackingSessionId(), driver(),
                new DeviceId("MB:1"), null, null, NOW, TrackingSessionStatus.ACTIVE, fix(5, 52.2297, 21.0122, 0),
                null);

        session.report(DRIVER_ID, List.of(fix(5, 52.2297, 21.0122, 0), fix(4, 52.2297, 21.0122, -10)));

        assertThat(session.acceptedPositions()).isEmpty();
    }

    @Test
    void shouldRejectInaccuratePosition() {
        final TrackingSession session = session();

        session.report(DRIVER_ID, List.of(new PositionFix(1, 52.2297, 21.0122, 250.0, null, null, NOW)));

        assertThat(session.acceptedPositions()).isEmpty();
        assertThat(session.lastPosition()).isNull();
    }

    @Test
    void shouldRejectImplausibleJump() {
        final TrackingSession session = session();

        session.report(DRIVER_ID, List.of(fix(1, 52.2297, 21.0122, 0), fix(2, 50.0647, 19.9450, 60)));

        assertThat(session.acceptedPositions()).extracting(PositionFix::sequence).containsExactly(1L);
    }

    @Test
    void shouldRejectPositionsFromAnotherDriver() {
        assertThatThrownBy(() -> session().report(OTHER_ID, List.of(fix(1, 52.2297, 21.0122, 0))))
                .isInstanceOf(FleetTrackingException.class).hasFieldOrPropertyWithValue("reason", ACCESS_DENIED);
    }

    @Test
    void shouldRejectPositionsAfterFinish() {
        final TrackingSession session = session();
        session.finish(DRIVER_ID, NOW);

        assertThatThrownBy(() -> session.report(DRIVER_ID, List.of(fix(1, 52.2297, 21.0122, 0))))
                .isInstanceOf(FleetTrackingException.class).hasFieldOrPropertyWithValue("reason", CONFLICT);
    }

    @Test
    void shouldKeepFirstFinishTime() {
        final TrackingSession session = session();

        session.finish(DRIVER_ID, NOW);
        session.finish(DRIVER_ID, NOW.plusSeconds(60));

        assertThat(session.status()).isEqualTo(TrackingSessionStatus.FINISHED);
        assertThat(session.finishedAt()).isEqualTo(NOW);
    }

    @Test
    void shouldRejectCoordinatesOutOfRange() {
        assertThatThrownBy(() -> fix(1, 91.0, 21.0, 0))
                .isInstanceOf(FleetTrackingException.class).hasFieldOrPropertyWithValue("reason", INVALID_REQUEST);
    }

    private static TrackingSession session() {
        return TrackingSession.start(driver(), new DeviceId("MB:1"), new VehicleId(10L), new DepartmentCode("KT1"),
                NOW);
    }

    private static TrackingDriver driver() {
        return new TrackingDriver(DRIVER_ID, new Username("driver"));
    }

    private static PositionFix fix(final long sequence,
                                   final double latitude,
                                   final double longitude,
                                   final long secondsAfterNow) {
        return new PositionFix(sequence, latitude, longitude, 10.0, null, null, NOW.plusSeconds(secondsAfterNow));
    }
}
