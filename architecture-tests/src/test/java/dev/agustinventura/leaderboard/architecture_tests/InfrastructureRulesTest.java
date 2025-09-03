package dev.agustinventura.leaderboard.architecture_tests;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

@AnalyzeClasses(packages = "dev.agustinventura.leaderboard")
class InfrastructureRulesTest {

    /**
     * Rule 1: Input adapters should not directly access output adapters.
     * They must go through the application layer.
     */
    @ArchTest
    static final ArchRule inputAdaptersShouldNotAccessOutputAdapters = noClasses()
            .that().resideInAPackage("..infrastructure.adapter.in..")
            .should().dependOnClassesThat()
            .resideInAPackage("..infrastructure.adapter.out..");

    /**
     * Rule 2: Convention for REST controllers.
     * They must be in the 'in.web' package, be annotated with @RestController, and their name must end with 'Controller'.
     */
    @ArchTest
    static final ArchRule controllersShouldFollowConvention = classes()
            .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .should().resideInAPackage("..infrastructure.adapter.in.web..")
            .andShould().haveSimpleNameEndingWith("Controller")
            .as("REST controllers must be in the 'in.web' package and end with 'Controller'");

    /**
     * Rule 3: Convention for persistence adapters.
     * They must be in the 'out.persistence' package, be annotated with @Repository, and their name must end with 'Repository'.
     */
    @ArchTest
    static final ArchRule persistenceAdaptersShouldFollowConvention = classes()
            .that().areAnnotatedWith("org.springframework.data.repository.Repository")
            .should().resideInAPackage("..infrastructure.adapter.out.persistence.jdbc.")
            .andShould().haveSimpleNameEndingWith("Repository")
            .andShould().beInterfaces()
            .as("Persistence adapters must be in the 'out.persistence' package and end with 'Adapter'");

    /**
     * Rule 4: Spring configuration classes must be in the 'config' package.
     */
    @ArchTest
    static final ArchRule configurationClassesShouldBeInConfigPackage = classes()
            .that().areAnnotatedWith("org.springframework.context.annotation.Configuration")
            .and().doNotHaveSimpleName("LeaderboardApplication")
            .should().resideInAPackage("..infrastructure.config..")
            .as("@Configuration classes must be in the 'config' package");

    /**
     * Rule 5: Prevent multiple entry points. There will be only one @SpringBootApplication placed in infrastructure
     * root.
     */
    @Test
    void aSingleSpringBootApplicationShouldExistInTheInfrastructureRoot() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages("dev.agustinventura.leaderboard");

        List<JavaClass> applicationClasses = importedClasses.stream()
                .filter(javaClass -> javaClass.isAnnotatedWith("org.springframework.boot.autoconfigure.SpringBootApplication"))
                .toList();

        assertThat(applicationClasses).hasSize(1);

        JavaClass mainClass = applicationClasses.getFirst();
        assertThat(mainClass.getSimpleName()).isEqualTo("LeaderboardApplication");
        assertThat(mainClass.getPackageName()).isEqualTo("dev.agustinventura.leaderboard.infrastructure");
    }
}
