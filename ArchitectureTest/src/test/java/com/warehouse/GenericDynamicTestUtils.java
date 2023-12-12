package com.warehouse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DynamicTest;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

class GenericDynamicTestUtils {

    private GenericDynamicTestUtils() {

    }

	public static DynamicTest classShouldHaveMethodEndsWith(JavaClass javaClass, String methodNamePrefix) {
		return DynamicTest.dynamicTest(javaClass.getFullName() + " should have methods ends with " + methodNamePrefix,
				() -> assertTrue(javaClass.getMethods().stream()
						.anyMatch(method -> StringUtils.endsWithIgnoreCase(method.getName(), methodNamePrefix))));
    }

    public static DynamicTest classShouldNotHaveMethodStartsWith(JavaClass javaClass, String methodNamePrefix) {
        return DynamicTest.dynamicTest(javaClass.getFullName() + " should have methods ends with " + methodNamePrefix,
                () -> assertTrue(javaClass.getMethods().stream()
                        .noneMatch(method -> method.getName().startsWith(methodNamePrefix))));
    }

    public static DynamicTest classShouldNotHaveMethodEndsWith(JavaClass javaClass, String methodNamePrefix) {
        return DynamicTest.dynamicTest(javaClass.getFullName() + " should not have methods ends with " + methodNamePrefix,
                () -> assertTrue(javaClass.getMethods().stream()
                        .noneMatch(method -> StringUtils.endsWithIgnoreCase(method.getName(), methodNamePrefix))));
    }
    
    public static DynamicTest classShouldNotHaveDependenciesToPackage(JavaClass javaClass, String notAllowedPackage) {
        return DynamicTest.dynamicTest(
                javaClass.getFullName() + " should not have dependencies to package " + notAllowedPackage,
                () -> {
                    final ConditionEvents events = new ConditionEvents();
                    javaClass.getDirectDependenciesFromSelf()
                            .stream()
                            .filter(d -> d.getTargetClass().getPackageName().contains(notAllowedPackage))
                            .forEach(d -> events.add(SimpleConditionEvent.violated(d, d.getDescription())));

                    javaClass.getAllAccessesFromSelf()
                            .stream()
                            .filter(d -> d.getTargetOwner().getPackageName().contains(notAllowedPackage))
                            .forEach(d -> events.add(SimpleConditionEvent.violated(d, d.getDescription())));

                    javaClass.getFieldAccessesFromSelf()
                            .stream()
                            .filter(d -> d.getTargetOwner().getPackageName().contains(notAllowedPackage))
                            .forEach(d -> events.add(SimpleConditionEvent.violated(d, d.getDescription())));

                    javaClass.getMethodCallsFromSelf()
                            .stream()
                            .filter(d -> d.getTargetOwner().getPackageName().contains(notAllowedPackage))
                            .forEach(d -> events.add(SimpleConditionEvent.violated(d, d.getDescription())));

                    javaClass.getConstructorCallsFromSelf()
                            .stream()
                            .filter(d -> d.getTargetOwner().getPackageName().contains(notAllowedPackage))
                            .forEach(d -> events.add(SimpleConditionEvent.violated(d, d.getDescription())));

                    assertFalse(events.containViolation(),
                            events.getFailureMessages()
                                    .stream()
                                    .reduce(String::join)
                                    .orElse(""));
                });
    }

    public static JavaClasses importClasses(String... packages) {
        return new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages(packages);
    }
}
