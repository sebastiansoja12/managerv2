package com.warehouse;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class OperatorSubscriptionArchitectureTest {

    private static final JavaClasses SUBSCRIPTION_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("com.warehouse.organisationstructure.subscription");

    @Test
    void shouldKeepDomainIndependentOfApplicationAdaptersAndFrameworks() {
        noClasses().that().resideInAPackage("com.warehouse.organisationstructure.subscription.domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.organisationstructure.subscription.application..",
                        "com.warehouse.organisationstructure.subscription.infrastructure..",
                        "com.warehouse.organisationstructure.subscription.configuration..", "com.warehouse.auth..",
                        "com.warehouse.organisationstructure.api..", "com.warehouse.organisationstructure.operator..",
                        "org.springframework..", "jakarta.persistence..", "jakarta.validation..", "com.fasterxml.jackson..")
                .check(SUBSCRIPTION_CLASSES);
    }

    @Test
    void shouldKeepApplicationIndependentOfAdaptersAndExternalContracts() {
        noClasses().that().resideInAPackage("com.warehouse.organisationstructure.subscription.application..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.organisationstructure.subscription.infrastructure..",
                        "com.warehouse.organisationstructure.subscription.configuration..", "com.warehouse.auth..",
                        "com.warehouse.organisationstructure.api..", "com.warehouse.organisationstructure.operator..",
                        "org.springframework..", "jakarta.persistence..")
                .check(SUBSCRIPTION_CLASSES);
    }

    @Test
    void shouldConnectPrimaryAdaptersOnlyToPrimaryPorts() {
        noClasses().that().resideInAPackage("com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary..",
                        "com.warehouse.organisationstructure.subscription.application.port.secondary..")
                .check(SUBSCRIPTION_CLASSES);
    }

    @Test
    void shouldNotUseOperatorContextInternals() {
        noClasses().should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.organisationstructure.operator..",
                        "com.warehouse.organisationstructure.operatorconfiguration..")
                .check(SUBSCRIPTION_CLASSES);
    }

    @Test
    void shouldImplementOutboundPortsInSecondaryAdapters() {
        classes().that().resideInAPackage("com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary")
                .and().haveSimpleNameEndingWith("ServiceAdapter")
                .should().implement(DescribedPredicate.describe("an application secondary port",
                        javaClass -> javaClass.getPackageName()
                                .equals("com.warehouse.organisationstructure.subscription.application.port.secondary")))
                .check(SUBSCRIPTION_CLASSES);
        classes().that().haveSimpleName("OperatorSubscriptionRepositoryImpl")
                .should().implement("com.warehouse.organisationstructure.subscription.application.port.secondary.OperatorSubscriptionRepository")
                .check(SUBSCRIPTION_CLASSES);
    }
}
