package com.warehouse;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class LogisticsArchitectureTest {

    private static final String LOGISTICS = "com.warehouse.logistics";

    private static final JavaClasses LOGISTICS_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages(LOGISTICS);

    @Test
    void shouldKeepDomainIndependentOfApplicationAdaptersAndFrameworks() {
        noClasses().that().resideInAPackage(LOGISTICS + ".domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        LOGISTICS + ".application..", LOGISTICS + ".infrastructure..", LOGISTICS + ".configuration..",
                        "com.warehouse.terminal..", "com.warehouse.logisticsdelivery..", "org.springframework..",
                        "jakarta.persistence..", "lombok..", "com.fasterxml.jackson..", "org.slf4j..")
                .check(LOGISTICS_CLASSES);
    }

    @Test
    void shouldKeepApplicationIndependentOfAdaptersAndTerminalTransport() {
        noClasses().that().resideInAPackage(LOGISTICS + ".application..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        LOGISTICS + ".infrastructure..", LOGISTICS + ".configuration..", "com.warehouse.terminal.jaxb..",
                        "com.warehouse.logisticsdelivery..", "jakarta.persistence..")
                .check(LOGISTICS_CLASSES);
    }

    @Test
    void shouldConnectPrimaryAdaptersOnlyToPrimaryPorts() {
        noClasses().that().resideInAPackage(LOGISTICS + ".infrastructure.adapter.primary..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        LOGISTICS + ".infrastructure.adapter.secondary..", LOGISTICS + ".application.port.secondary..")
                .check(LOGISTICS_CLASSES);
    }

    @Test
    void shouldImplementOutboundPortsInSecondaryAdapters() {
        classes().that().resideInAPackage(LOGISTICS + ".infrastructure.adapter.secondary")
                .and().haveSimpleNameEndingWith("ServiceAdapter")
                .should().implement(DescribedPredicate.describe("an application secondary port",
                        javaClass -> javaClass.getPackageName().equals(LOGISTICS + ".application.port.secondary")))
                .check(LOGISTICS_CLASSES);
        classes().that().haveSimpleName("SupplierRepositoryImpl")
                .should().implement(LOGISTICS + ".application.port.secondary.SupplierRepository")
                .check(LOGISTICS_CLASSES);
    }
}
