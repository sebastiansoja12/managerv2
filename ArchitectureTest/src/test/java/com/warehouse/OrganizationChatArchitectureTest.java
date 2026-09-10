package com.warehouse;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.warehouse.commonassets.model.BelongsToOperator;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class OrganizationChatArchitectureTest {

    private static final JavaClasses CHAT_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests()).importPackages("com.warehouse.chat");

    @Test
    void shouldKeepDomainIndependentOfApplicationAdaptersAndFrameworks() {
        noClasses().that().resideInAPackage("com.warehouse.chat.domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.chat.application..", "com.warehouse.chat.infrastructure..",
                        "com.warehouse.chat.configuration..", "com.warehouse.auth..",
                        "org.springframework..", "jakarta.persistence..", "jakarta.validation..",
                        "com.fasterxml.jackson..", "com.warehouse.chat.api")
                .check(CHAT_CLASSES);
    }

    @Test
    void shouldKeepApplicationIndependentOfAdaptersAndExternalContracts() {
        noClasses().that().resideInAPackage("com.warehouse.chat.application..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.chat.infrastructure..", "com.warehouse.chat.configuration..",
                        "com.warehouse.auth..", "com.warehouse.chat.api", "org.springframework.data..",
                        "org.springframework.messaging..", "jakarta.persistence..")
                .check(CHAT_CLASSES);
    }

    @Test
    void shouldKeepHttpEntryPointConnectedToPrimaryPort() {
        classes().that().haveFullyQualifiedName("com.warehouse.chat.infrastructure.adapter.primary.OrganizationChatController")
                .should().dependOnClassesThat().haveFullyQualifiedName("com.warehouse.chat.application.port.primary.OrganizationChatPort")
                .check(CHAT_CLASSES);
        noClasses().that().resideInAPackage("com.warehouse.chat.infrastructure.adapter.primary..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.chat.infrastructure.adapter.secondary..", "com.warehouse.chat.application.port.secondary..")
                .check(CHAT_CLASSES);
    }

    @Test
    void shouldImplementOutboundPortsInSecondaryAdapters() {
        classes().that().resideInAPackage("com.warehouse.chat.infrastructure.adapter.secondary")
                .and().haveSimpleNameEndingWith("ServiceAdapter")
                .should().implement(com.tngtech.archunit.base.DescribedPredicate.describe("an application secondary port",
                        javaClass -> javaClass.getPackageName().equals("com.warehouse.chat.application.port.secondary")))
                .check(CHAT_CLASSES);
        classes().that().haveSimpleName("OrganizationChatRepositoryImpl")
                .should().implement("com.warehouse.chat.application.port.secondary.OrganizationChatRepository")
                .check(CHAT_CLASSES);
    }

    @Test
    void shouldUseOperatorFilteredPersistenceForChatEntities() {
        classes().that().resideInAPackage("com.warehouse.chat.infrastructure.adapter.secondary")
                .and().haveSimpleNameEndingWith("Entity")
                .should().beAssignableTo(BelongsToOperator.class)
                .check(CHAT_CLASSES);
        noClasses().that().resideInAPackage("com.warehouse.chat.infrastructure.adapter.secondary..")
                .should().dependOnClassesThat().resideInAnyPackage("org.springframework.data..")
                .check(CHAT_CLASSES);
    }

    @Test
    void shouldKeepInternalApiFreeOfImplementationDependencies() {
        noClasses().that().resideInAnyPackage("com.warehouse.chat.api..", "com.warehouse.chat")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.chat.domain..", "com.warehouse.chat.application..",
                        "com.warehouse.chat.infrastructure..", "com.warehouse.chat.configuration..")
                .check(CHAT_CLASSES);
    }
}
