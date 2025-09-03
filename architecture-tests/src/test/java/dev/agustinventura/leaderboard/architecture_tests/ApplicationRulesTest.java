package dev.agustinventura.leaderboard.architecture_tests;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "dev.agustinventura.leaderboard")
class ApplicationRulesTest {

    /**
     * Rule 1: The application layer should not depend on the infrastructure layer.
     * The application should only know about abstractions (ports), not concrete implementations.
     */
    @ArchTest
    static final ArchRule applicationShouldNotDependOnInfrastructure = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat()
            .resideInAPackage("..infrastructure..");

    /**
     * Rule 2: The application layer can only depend on the domain and itself.
     * This ensures that the application logic is not coupled to anything external, except for the business core.
     * Dependencies on standard Java libraries are allowed.
     */
    @ArchTest
    static final ArchRule applicationShouldOnlyDependOnDomain = classes()
            .that().resideInAPackage("..application..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                    "dev.agustinventura.leaderboard.application..",
                    "dev.agustinventura.leaderboard.domain..",
                    "java.."
            );

    /**
     * Rule 3: Naming convention for application services.
     * Ensures that classes implementing use cases follow a consistent pattern
     * and are in the correct package.
     */
    @ArchTest
    static final ArchRule applicationServicesShouldBeNamedCorrectly = classes()
            .that().haveSimpleNameEndingWith("Service")
            .and().areNotInterfaces()
            .should().resideInAPackage("..application.service..")
            .as("Application services should end with 'Service' and be in the 'service' package");

    /**
     * Rule 4: Input ports (Use Cases) must be interfaces ending in 'UseCase'.
     * These define the use cases that the application offers.
     */
    @ArchTest
    static final ArchRule inputPortsShouldBeUseCaseInterfaces = classes()
            .that().resideInAPackage("..application.port.in..")
            .should().haveSimpleNameEndingWith("UseCase")
            .andShould().beInterfaces()
            .as("Input ports must be interfaces and end with 'UseCase'");

    /**
     * Rule 5: Output ports (Ports) must be interfaces ending in 'Port'.
     * These define the dependencies that the application needs from the outside (e.g., a repository).
     */
    @ArchTest
    static final ArchRule outputPortsShouldBePortInterfaces = classes()
            .that().resideInAPackage("..application.port.out..")
            .should().haveSimpleNameEndingWith("Port")
            .andShould().beInterfaces()
            .as("Output ports must be interfaces and end with 'Port'");
}
