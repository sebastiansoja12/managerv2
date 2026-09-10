package com.warehouse;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class AsyncJobArchitectureTest {

    private static final String ASYNC_JOB = "com.warehouse.asyncjob";

    private static final JavaClasses ASYNC_JOB_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages(ASYNC_JOB);

    @Test
    void shouldKeepDomainIndependentOfApplicationAdaptersAndFrameworks() {
        noClasses().that().resideInAPackage(ASYNC_JOB + ".domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        ASYNC_JOB + ".application..", ASYNC_JOB + ".infrastructure..", ASYNC_JOB + ".configuration..",
                        ASYNC_JOB + ".api..", "org.springframework..", "jakarta.persistence..", "lombok..",
                        "org.slf4j..")
                .check(ASYNC_JOB_CLASSES);
    }

    @Test
    void shouldKeepApplicationIndependentOfAdaptersAndOtherContexts() {
        noClasses().that().resideInAPackage(ASYNC_JOB + ".application..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        ASYNC_JOB + ".infrastructure..", ASYNC_JOB + ".configuration..", ASYNC_JOB + ".api..",
                        "com.warehouse.shipment..", "com.warehouse.organisationstructure..",
                        "com.warehouse.logisticsdelivery..", "jakarta.persistence..",
                        "org.springframework.scheduling..")
                .check(ASYNC_JOB_CLASSES);
    }

    @Test
    void shouldConnectPrimaryAdaptersOnlyToPrimaryPorts() {
        noClasses().that().resideInAPackage(ASYNC_JOB + ".infrastructure.adapter.primary..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        ASYNC_JOB + ".infrastructure.adapter.secondary..", ASYNC_JOB + ".application.port.secondary..",
                        ASYNC_JOB + ".application.service..")
                .check(ASYNC_JOB_CLASSES);
    }

    @Test
    void shouldImplementSecondaryPortsInSecondaryAdapters() {
        classes().that().resideInAPackage(ASYNC_JOB + ".infrastructure.adapter.secondary")
                .and().haveSimpleNameEndingWith("ServiceAdapter")
                .should().implement(DescribedPredicate.describe("an application secondary port",
                        javaClass -> javaClass.getPackageName().equals(ASYNC_JOB + ".application.port.secondary")))
                .check(ASYNC_JOB_CLASSES);
        classes().that().haveSimpleName("AsyncJobRepositoryImpl")
                .should().implement(ASYNC_JOB + ".application.port.secondary.AsyncJobRepository")
                .check(ASYNC_JOB_CLASSES);
    }
}
