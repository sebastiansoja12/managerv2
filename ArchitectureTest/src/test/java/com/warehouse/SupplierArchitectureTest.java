package com.warehouse;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class SupplierArchitectureTest {

    private static final String SUPPLIER = "com.warehouse.supplier";

    private static final String SUPPLIER_CONFIGURATION = "com.warehouse.supplierconfiguration";

    private static final JavaClasses SUPPLIER_CONFIGURATION_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages(SUPPLIER_CONFIGURATION, SUPPLIER);

    private static final JavaClasses SUPPLIER_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages(SUPPLIER);

    @Test
    void shouldKeepDomainIndependentOfApplicationAdaptersAndFrameworks() {
        noClasses().that().resideInAPackage(SUPPLIER + ".domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        SUPPLIER + ".application..", SUPPLIER + ".infrastructure..", SUPPLIER + ".configuration..",
                        SUPPLIER + ".api..", "org.springframework..", "org.slf4j..", "lombok..",
                        "com.google.common..", "jakarta.validation..", "com.warehouse.auth..",
                        "com.warehouse.organisationstructure..", "com.warehouse.mail..")
                .check(SUPPLIER_CLASSES);
    }

    @Test
    void shouldConnectSupplierAndSupplierConfigurationOnlyThroughApi() {
        noClasses().that().resideInAnyPackage(SUPPLIER + ".domain..", SUPPLIER + ".application..",
                        SUPPLIER + ".infrastructure..", SUPPLIER + ".configuration..")
                .should().dependOnClassesThat().resideInAPackage(SUPPLIER_CONFIGURATION + "..")
                .check(SUPPLIER_CLASSES);
        noClasses().that().resideInAPackage(SUPPLIER_CONFIGURATION + "..")
                .should().dependOnClassesThat().resideInAnyPackage(SUPPLIER + ".domain..",
                        SUPPLIER + ".application..", SUPPLIER + ".infrastructure..", SUPPLIER + ".configuration..")
                .check(SUPPLIER_CONFIGURATION_CLASSES);
    }

    @Test
    void shouldKeepApplicationIndependentOfAdapters() {
        noClasses().that().resideInAPackage(SUPPLIER + ".application..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        SUPPLIER + ".infrastructure..", SUPPLIER + ".configuration..", "jakarta.persistence..",
                        "com.warehouse.auth..", "com.warehouse.organisationstructure..", "com.warehouse.mail..")
                .check(SUPPLIER_CLASSES);
    }

    @Test
    void shouldConnectPrimaryAdaptersOnlyToPrimaryPorts() {
        noClasses().that().resideInAPackage(SUPPLIER + ".infrastructure.adapter.primary..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        SUPPLIER + ".infrastructure.adapter.secondary.entity..",
                        SUPPLIER + ".infrastructure.adapter.secondary.mapper..",
                        SUPPLIER + ".application.port.secondary..", SUPPLIER + ".domain.model..")
                .check(SUPPLIER_CLASSES);
    }

    @Test
    void shouldImplementSecondaryPortsInSecondaryAdapters() {
        classes().that().resideInAPackage(SUPPLIER + ".infrastructure.adapter.secondary")
                .and().haveSimpleNameEndingWith("Adapter")
                .should().implement(DescribedPredicate.describe("an application secondary port",
                        javaClass -> javaClass.getPackageName().equals(SUPPLIER + ".application.port.secondary")))
                .check(SUPPLIER_CLASSES);
    }
}
