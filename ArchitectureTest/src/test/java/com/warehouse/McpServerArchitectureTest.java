package com.warehouse;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class McpServerArchitectureTest {

    private static final JavaClasses MCP_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests()).importPackages("com.warehouse.mcp");

    @Test
    void shouldKeepDomainIndependentOfApplicationAdaptersAndFrameworks() {
        noClasses().that().resideInAPackage("com.warehouse.mcp.domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.mcp.application..", "com.warehouse.mcp.infrastructure..",
                        "com.warehouse.mcp.configuration..", "org.springframework..")
                .check(MCP_CLASSES);
    }

    @Test
    void shouldKeepApplicationIndependentOfAdaptersAndOtherContexts() {
        noClasses().that().resideInAPackage("com.warehouse.mcp.application..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.mcp.infrastructure..", "com.warehouse.mcp.configuration..",
                        "org.springframework..", "com.warehouse.auth..", "com.warehouse.shipment..",
                        "com.warehouse.process..", "com.warehouse.terminal..")
                .check(MCP_CLASSES);
    }

    @Test
    void shouldUseOnlyApiModulesOfOtherContexts() {
        noClasses().that().resideInAPackage("com.warehouse.mcp..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.shipment.domain..", "com.warehouse.shipment.application..",
                        "com.warehouse.shipment.infrastructure.adapter..",
                        "com.warehouse.process.domain..", "com.warehouse.process.infrastructure.adapter..",
                        "com.warehouse.terminal.domain..", "com.warehouse.terminal.infrastructure..",
                        "com.warehouse.auth.domain..", "com.warehouse.auth.configuration..",
                        "com.warehouse.auth.infrastructure.adapter..")
                .check(MCP_CLASSES);
    }

    @Test
    void shouldConnectToolsOnlyToPrimaryPort() {
        noClasses().that().resideInAPackage("com.warehouse.mcp.infrastructure.adapter.primary..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "com.warehouse.mcp.infrastructure.adapter.secondary..",
                        "com.warehouse.mcp.application.port.secondary..")
                .check(MCP_CLASSES);
    }

    @Test
    void shouldImplementOutboundPortsInSecondaryAdapters() {
        classes().that().resideInAPackage("com.warehouse.mcp.infrastructure.adapter.secondary")
                .and().haveSimpleNameEndingWith("ServiceAdapter")
                .should().implement(com.tngtech.archunit.base.DescribedPredicate.describe("an application secondary port",
                        javaClass -> javaClass.getPackageName().equals("com.warehouse.mcp.application.port.secondary")))
                .check(MCP_CLASSES);
    }
}
