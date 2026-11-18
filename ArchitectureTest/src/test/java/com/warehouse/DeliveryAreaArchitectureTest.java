package com.warehouse;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DeliveryAreaArchitectureTest {

    private static final String DELIVERY_AREA = "com.warehouse.organisationstructure.deliveryarea";

    private static final JavaClasses DELIVERY_AREA_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages(DELIVERY_AREA);

    @Test
    void shouldKeepDomainIndependentOfApplicationAdaptersAndFrameworks() {
        noClasses().that().resideInAPackage(DELIVERY_AREA + ".domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        DELIVERY_AREA + ".application..", DELIVERY_AREA + ".infrastructure..",
                        DELIVERY_AREA + ".configuration..", "com.warehouse.organisationstructure.department..",
                        "org.springframework..", "jakarta.persistence..", "jakarta.validation..", "tools.jackson..")
                .check(DELIVERY_AREA_CLASSES);
    }

    @Test
    void shouldKeepApplicationIndependentOfAdapters() {
        noClasses().that().resideInAPackage(DELIVERY_AREA + ".application..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        DELIVERY_AREA + ".infrastructure..", DELIVERY_AREA + ".configuration..",
                        "com.warehouse.organisationstructure.department..", "jakarta.persistence..")
                .check(DELIVERY_AREA_CLASSES);
    }

    @Test
    void shouldConnectPrimaryAdaptersOnlyToPrimaryPorts() {
        noClasses().that().resideInAPackage(DELIVERY_AREA + ".infrastructure.adapter.primary..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        DELIVERY_AREA + ".infrastructure.adapter.secondary..",
                        DELIVERY_AREA + ".application.port.secondary..")
                .check(DELIVERY_AREA_CLASSES);
    }

    @Test
    void shouldImplementSecondaryPortsInSecondaryAdapters() {
        classes().that().resideInAPackage(DELIVERY_AREA + ".infrastructure.adapter.secondary")
                .and().haveSimpleNameEndingWith("Adapter")
                .should().implement(DescribedPredicate.describe("an application secondary port",
                        javaClass -> javaClass.getPackageName().equals(DELIVERY_AREA + ".application.port.secondary")))
                .check(DELIVERY_AREA_CLASSES);
    }
}
