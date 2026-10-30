package com.warehouse.commonassets.helper;

import com.warehouse.commonassets.identificator.DepartmentId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnapshotDifferenceTest {

    @Test
    void shouldReturnChangedFieldsIncludingNestedRecords() {
        final TestSnapshot previous = new TestSnapshot("Old name", new TestAddress("Katowice", "Mariacka 1"),
                new DepartmentId(1L), Set.of("B", "A"), Instant.parse("2026-10-30T10:00:00Z"));
        final TestSnapshot current = new TestSnapshot("New name", new TestAddress("Katowice", "Mariacka 2"),
                new DepartmentId(2L), Set.of("A", "B"), Instant.parse("2026-10-31T10:00:00Z"));

        final List<FieldDifference> differences = SnapshotDifference.between(previous, current, Set.of("updatedAt"));

        assertEquals(List.of(
                new FieldDifference("name", "Old name", "New name"),
                new FieldDifference("address.street", "Mariacka 1", "Mariacka 2"),
                new FieldDifference("departmentId", "1", "2")), differences);
    }

    @Test
    void shouldReturnNoDifferencesWhenSnapshotsAreEqual() {
        final TestSnapshot snapshot = new TestSnapshot("Name", new TestAddress("Katowice", "Mariacka 1"),
                new DepartmentId(1L), Set.of("A"), Instant.parse("2026-10-30T10:00:00Z"));

        assertTrue(SnapshotDifference.between(snapshot, snapshot, Set.of()).isEmpty());
    }

    @Test
    void shouldListCurrentValuesWhenPreviousSnapshotIsMissing() {
        final TestSnapshot snapshot = new TestSnapshot("Name", new TestAddress("Katowice", "Mariacka 1"),
                new DepartmentId(1L), Set.of("B", "A"), Instant.parse("2026-10-30T10:00:00Z"));

        final List<FieldDifference> differences = SnapshotDifference.of(snapshot, Set.of("updatedAt"));

        assertEquals(List.of(
                new FieldDifference("name", null, "Name"),
                new FieldDifference("address.city", null, "Katowice"),
                new FieldDifference("address.street", null, "Mariacka 1"),
                new FieldDifference("departmentId", null, "1"),
                new FieldDifference("tags", null, "[A, B]")), differences);
    }

    public record TestAddress(String city, String street) {
    }

    public record TestSnapshot(String name,
                               TestAddress address,
                               DepartmentId departmentId,
                               Set<String> tags,
                               Instant updatedAt) {
    }
}
