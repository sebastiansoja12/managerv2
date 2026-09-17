package com.warehouse.commonassets.helper;

import com.warehouse.commonassets.identificator.ObjectValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SnapshotDifference {

    private static final String IDENTIFIER_PACKAGE = "com.warehouse.commonassets.identificator";

    private SnapshotDifference() {
    }

    public static List<FieldDifference> between(final Record previous,
                                                final Record current,
                                                final Set<String> ignoredFields) {
        if (previous.getClass() != current.getClass()) {
            throw new IllegalArgumentException("Snapshots must have the same type");
        }
        final List<FieldDifference> differences = new ArrayList<>();
        collect("", previous, current, ignoredFields, differences);
        return List.copyOf(differences);
    }

    public static List<FieldDifference> of(final Record current, final Set<String> ignoredFields) {
        final List<FieldDifference> differences = new ArrayList<>();
        collect("", null, current, ignoredFields, differences);
        return List.copyOf(differences);
    }

    private static void collect(final String prefix,
                                final Record previous,
                                final Record current,
                                final Set<String> ignoredFields,
                                final List<FieldDifference> differences) {
        for (final RecordComponent component : current.getClass().getRecordComponents()) {
            final String field = prefix + component.getName();
            if (ignoredFields.contains(field)) {
                continue;
            }
            final Object previousValue = previous == null ? null : read(component, previous);
            final Object currentValue = read(component, current);
            if (isNested(previousValue, currentValue)) {
                collect(field + ".", (Record) previousValue, (Record) currentValue, ignoredFields, differences);
                continue;
            }
            final String previousText = format(previousValue);
            final String currentText = format(currentValue);
            if (!Objects.equals(previousText, currentText)) {
                differences.add(new FieldDifference(field, previousText, currentText));
            }
        }
    }

    private static boolean isNested(final Object previousValue, final Object currentValue) {
        return currentValue instanceof final Record record
                && !(currentValue instanceof ObjectValue<?>)
                && record.getClass().getRecordComponents().length > 1
                && (previousValue == null || previousValue.getClass() == currentValue.getClass());
    }

    private static Object read(final RecordComponent component, final Record source) {
        return invoke(component.getAccessor(), source);
    }

    public static String format(final Object value) {
        return switch (value) {
            case null -> null;
            case final ObjectValue<?> objectValue -> format(objectValue.value());
            case final Record record when record.getClass().getRecordComponents().length == 1 ->
                    format(read(record.getClass().getRecordComponents()[0], record));
            case final Collection<?> collection -> collection.stream()
                    .map(SnapshotDifference::format)
                    .sorted()
                    .collect(Collectors.joining(", ", "[", "]"));
            default -> valueOf(value);
        };
    }

    private static String valueOf(final Object value) {
        if (!IDENTIFIER_PACKAGE.equals(value.getClass().getPackageName())) {
            return String.valueOf(value);
        }
        return Stream.of("value", "getValue")
                .flatMap(methodName -> Stream.of(value.getClass().getMethods())
                        .filter(method -> method.getName().equals(methodName) && method.getParameterCount() == 0))
                .findFirst()
                .map(method -> format(invoke(method, value)))
                .orElseGet(() -> String.valueOf(value));
    }

    private static Object invoke(final Method method, final Object source) {
        try {
            method.trySetAccessible();
            return method.invoke(source);
        } catch (final IllegalAccessException | InvocationTargetException exception) {
            throw new IllegalStateException("Cannot read value of " + source.getClass().getName(), exception);
        }
    }
}
