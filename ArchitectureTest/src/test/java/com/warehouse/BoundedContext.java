package com.warehouse;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Objects;

import org.junit.jupiter.api.DynamicNode;
import org.springframework.data.repository.Repository;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

import lombok.Builder;
import lombok.Getter;

@Getter
class BoundedContext {
    private static final String API_PACKAGE = ".api";
    private static final String DOMAIN_PACKAGE = ".domain";
    private static final String INFRASTRUCTURE_PACKAGE = ".infrastructure";
    private static final String DOMAIN_MODEL_PACKAGE = DOMAIN_PACKAGE + ".model..";
    private static final String DOMAIN_ENUM_PACKAGE = DOMAIN_PACKAGE + ".enumeration";
    private static final String DOMAIN_VO_PACKAGE = DOMAIN_PACKAGE + ".vo";
    private static final String DOMAIN_VOS_PACKAGE = DOMAIN_VO_PACKAGE + "..";
    private static final String DOMAIN_SERVICE_PACKAGE = DOMAIN_PACKAGE + ".service..";
    private static final String DOMAIN_EXCEPTION_PACKAGE = DOMAIN_PACKAGE + ".exception..";
    private static final String DOMAIN_POLICY_PACKAGE = DOMAIN_PACKAGE + ".policy..";
    private static final String APPLICATION_SERVICE_PACKAGE = DOMAIN_PACKAGE + ".port.primary..";
    private static final String APPLICATION_SERVICE_INTERFACES_PACKAGE = API_PACKAGE + ".port.primary..";
    private static final String DOMAIN_PERSISTENCE_PORT_PACKAGE = DOMAIN_PACKAGE + ".port.secondary";
    private static final String DOMAIN_PERSISTENCE_PORTS_PACKAGE = DOMAIN_PERSISTENCE_PORT_PACKAGE + "..";
    private static final String INFRASTRUCTURE_PERSISTENCE_ADAPTER_PACKAGE =
            INFRASTRUCTURE_PACKAGE + ".adapter.secondary";
    private static final String INFRASTRUCTURE_PRIMARY_ADAPTER_PACKAGE =
            INFRASTRUCTURE_PACKAGE + ".adapter.primary";
    private static final String INFRASTRUCTURE_PERSISTENCE_ADAPTERS_PACKAGE =
            INFRASTRUCTURE_PERSISTENCE_ADAPTER_PACKAGE + "..";
    private static final String INFRASTRUCTURE_PRIMARY_ADAPTERS_PACKAGE =
            INFRASTRUCTURE_PRIMARY_ADAPTER_PACKAGE + "..";
    private static final String SUFFIX_REPOSITORY = "Repository";
    private static final String PREFIX_ABSTRACT = "Abstract";



    private String name;
    private String rootPackage;

    private JavaClasses domainClasses;
    private JavaClasses allClasses;
    private JavaClasses domainModelClasses;
    private JavaClasses valueObjectClasses;
    private JavaClasses portClasses;
    private JavaClasses adapterClasses;

    private ArchRule hexagonalArchitectureRule;
    private ArchRule domainRepositoryInterfacesRule;
    private ArchRule adapterRepositoryInterfacesRule;
    private ArchRule repositoryClassesAsInterfacesRule;
    private ArchRule enumsInProperPackageRule;
    private ArchRule valueObjectsInProperPackageRule;

    @Builder
    BoundedContext(String name, String rootPackage) {
        this.rootPackage = rootPackage;
        this.name = name;
        setUpRules();
        setUpClasses();
    }

    private void setUpRules() {
        hexagonalArchitectureRule =
                Architectures.onionArchitecture()
                        .domainModels(
                                pack(DOMAIN_MODEL_PACKAGE),
                                pack(DOMAIN_VO_PACKAGE),
                                pack(DOMAIN_ENUM_PACKAGE),
                                pack(DOMAIN_POLICY_PACKAGE)
                        ).domainServices(
                                pack(DOMAIN_SERVICE_PACKAGE),
                                pack(DOMAIN_EXCEPTION_PACKAGE),
                                pack(DOMAIN_PERSISTENCE_PORTS_PACKAGE))
                        .applicationServices(pack(APPLICATION_SERVICE_PACKAGE))
                        .adapter("primaryAdapters", pack(INFRASTRUCTURE_PRIMARY_ADAPTERS_PACKAGE))
                        .adapter("persistenceAdapters", pack(INFRASTRUCTURE_PERSISTENCE_ADAPTERS_PACKAGE))
                        .withOptionalLayers(true);


        domainRepositoryInterfacesRule =
                classes().that().haveSimpleNameEndingWith(SUFFIX_REPOSITORY)
                        .and().areNotAssignableTo(Repository.class)
                        .should().resideInAPackage(pack(DOMAIN_PERSISTENCE_PORT_PACKAGE))
                        .andShould().beInterfaces().orShould().haveSimpleNameStartingWith(PREFIX_ABSTRACT);

        adapterRepositoryInterfacesRule =
                classes().that().resideInAPackage(pack(INFRASTRUCTURE_PACKAGE) + "..")
                        .and().haveSimpleNameEndingWith(SUFFIX_REPOSITORY)
                        .should().beInterfaces().andShould().beAssignableTo(Repository.class)
                        .orShould().haveSimpleNameStartingWith(PREFIX_ABSTRACT);

        repositoryClassesAsInterfacesRule =
                classes()
                        .that().areNotNestedClasses()
                        .and().resideInAPackage(pack(DOMAIN_PACKAGE + ".."))
                        .should().resideInAPackage(pack(DOMAIN_ENUM_PACKAGE));

        enumsInProperPackageRule =
                classes().that().areEnums()
                        .and().resideInAPackage(pack(DOMAIN_PACKAGE + ".."))
                        .should().beAssignableTo(pack(DOMAIN_ENUM_PACKAGE) + ".EnumValue");

        valueObjectsInProperPackageRule =
                classes().that().areAnnotatedWith("lombok.Value")
                        .and().resideInAPackage(pack(DOMAIN_PACKAGE + ".."))
                        .should().beAssignableTo(pack(DOMAIN_VO_PACKAGE) + ".vo");
    }

    private void setUpClasses() {
        this.allClasses = GenericDynamicTestUtils.importClasses(pack(DOMAIN_PACKAGE), pack(INFRASTRUCTURE_PACKAGE));
        this.domainClasses = GenericDynamicTestUtils.importClasses(pack(DOMAIN_PACKAGE));
        this.domainModelClasses = GenericDynamicTestUtils.importClasses(pack(DOMAIN_MODEL_PACKAGE));
        this.valueObjectClasses = GenericDynamicTestUtils.importClasses(pack(DOMAIN_VO_PACKAGE));
        this.portClasses = GenericDynamicTestUtils.importClasses(pack(DOMAIN_PERSISTENCE_PORT_PACKAGE));
        this.adapterClasses = GenericDynamicTestUtils.importClasses(pack(INFRASTRUCTURE_PERSISTENCE_ADAPTER_PACKAGE));
    }

    @BoundedContextTest
    private DynamicNode domainObjectsShouldNotHaveGetters() {
        return dynamicContainer(name + " domain object classes should not have getters",
                domainModelClasses.stream().filter(JavaClass::isTopLevelClass).map(javaClass ->
                        GenericDynamicTestUtils.classShouldNotHaveMethodStartsWith(javaClass, "get")));
    }

    @BoundedContextTest
    private DynamicNode domainObjectsShouldNotHaveSetters() {
        return dynamicContainer(name + " domain object classes should not have setters",
                domainModelClasses.stream().filter(JavaClass::isTopLevelClass).map(javaClass ->
                GenericDynamicTestUtils.classShouldNotHaveMethodStartsWith(javaClass, "set")));
    }

    @BoundedContextTest
    private DynamicNode domainWithoutLog4J() {
        return dynamicContainer(name + " domain objects classes should not have dependencies to logging framework",
                domainClasses.stream().map(
                        javaClass -> GenericDynamicTestUtils.classShouldNotHaveDependenciesToPackage(javaClass, "org.slf4j")
                ));
    }

    @BoundedContextTest
    private DynamicNode domainWithoutSpring() {
        return dynamicContainer(name + " domain object classes should not have dependencies to spring framework",
				domainClasses.stream()
                        .map(javaClass -> GenericDynamicTestUtils
						.classShouldNotHaveDependenciesToPackage(javaClass, "org.springframework")));
    }

    @BoundedContextTest
    private DynamicNode domainWithoutConfiguration() {
        return dynamicContainer(name + " domain object classes should not have configuration annotations",
                domainClasses.stream()
                        .map(javaClass -> GenericDynamicTestUtils
                                .classShouldNotHaveDependenciesToPackage(javaClass, "configuration")));
    }

    @BoundedContextTest
    private DynamicNode valueObjectWithAnnotation() {
        return dynamicContainer(name + " value object classes should have configuration annotations",
                valueObjectClasses.stream()
                        .map(javaClass -> GenericDynamicTestUtils
                                .classShouldHaveDependenciesToPackage(javaClass, "lombok.Value")));
    }
    
	@BoundedContextTest
	private DynamicNode portsShouldHaveAdapters() {
		return dynamicContainer(name + " defined ports should have adapter implementations",
				portClasses.stream().filter(JavaClass::isTopLevelClass)
						.map(javaClass -> dynamicTest(javaClass.getFullName() + " should have adapter implementation",
								() -> assertTrue(adapterClasses.stream()
										.anyMatch(adapterClass -> adapterClass.getAllInterfaces().stream()
												.anyMatch(adapterInterface -> adapterInterface.getFullName()
														.equals(javaClass.getFullName())))))));
	}

    @BoundedContextTest
    private DynamicNode enumsShouldResideInProperPackage() {
        return dynamicTest(name + " enum classes should reside in proper package",
                () -> enumsInProperPackageRule.check(allClasses));
    }

    DynamicNode boundedContextTests() {
        return dynamicContainer(name + " bounded context tests", BoundedContextTestScanner.scanForTests(this));
    }

    private String pack(String packageName) {
        return rootPackage + packageName;
    }


    public static class BoundedContextBuilder {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final BoundedContextBuilder that = (BoundedContextBuilder) o;
            return Objects.equals(name, that.name) && Objects.equals(rootPackage, that.rootPackage);
        }
    }

}
