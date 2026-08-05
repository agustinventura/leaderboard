# AGENTS.md

Context for AI agents working in this repository.

## Project Overview

Backend service for a gaming leaderboard: registers player scores and serves a global ranking. Built as a deliberate exercise in methodical, explicitly documented software design (DDD + Hexagonal Architecture, outside-in TDD/BDD). Every significant decision is captured in an ADR under `docs/ADRs/`.

Documentation (README, ADRs, NFRs) is written in **Spanish**; code and identifiers are in English.

## Tech Stack

- **Java 25 (LTS)** — target modern features: records, pattern matching, virtual threads, flexible constructor bodies, scoped values (ADR-004)
- **Spring Boot 3.5.x** in `infrastructure` only; `domain` and `application` are framework-free (ADR-010, ADR-006)
- **Gradle (Kotlin DSL)** multi-module build, version catalog in `gradle/libs.versions.toml` (ADR-005)
- **OpenAPI Generator** (spring generator, delegate pattern) for REST API interfaces and DTOs
- **PostgreSQL** + Spring Data JDBC, **Flyway** migrations (ADR-009, ADR-014)
- **Testing**: JUnit 5, AssertJ, Mockito, Instancio, ArchUnit, Cucumber + REST Assured (ADR-003, ADR-011)
- **Style**: Google Java Style Guide

## Project Layout

```
domain/               Core business logic. No framework dependencies.
application/          Use cases orchestrating the domain. Depends only on domain.
infrastructure/       Spring Boot app: REST adapters (generated from OpenAPI), persistence, configuration.
  src/main/resources/api/openapi.yaml   API contract (source of truth for REST layer)
architecture-tests/   ArchUnit rules enforcing hexagonal boundaries per module.
acceptance-tests/     Cucumber E2E tests (features in src/test/resources/features/).
docs/                 ADRs, NFRs, C4 diagrams.
gradle/libs.versions.toml   Dependency version catalog.
```

Package root: `dev.agustinventura.leaderboard`. Generated REST code lands in `...adapters.in.rest` (DTOs suffixed `RESTDTO`).

## SDLC Commands

- **Build everything**: `./gradlew build`
- **Run all tests**: `./gradlew test`
- **Run one module's tests**: `./gradlew :domain:test` (same for `:application`, `:infrastructure`, `:architecture-tests`, `:acceptance-tests`)
- **Run a single test class**: `./gradlew test --tests "fully.qualified.ClassName"`
- **Regenerate OpenAPI code**: `./gradlew :infrastructure:generate` (also runs automatically before `compileJava`)
- **Clean**: `./gradlew clean`

## Development Workflow

Outside-in TDD/BDD (ADR-003):

1. **Behavior (BDD)**: define behavior in Gherkin `.feature` files under `acceptance-tests`.
2. **Domain (TDD)**: implement domain logic Red-Green-Refactor.
3. **Adapters**: implement infrastructure adapters (REST controllers, repositories).

Architecture rules are enforced by ArchUnit — run `:architecture-tests:test` after structural changes.

## Documentation Index

Read on demand (progressive disclosure); don't preload everything. When making a decision that touches an ADR's topic, read that ADR first.

### General

- [README.md](README.md) — project intro, C4 diagrams, capabilities
- [CONTRIBUTING.md](CONTRIBUTING.md) — contribution guidelines
- [docs/NFRs.md](docs/NFRs.md) — non-functional requirements (performance, availability, security targets)

### Diagrams

- [docs/diagrams/c4/context_diagram.dsl](docs/diagrams/c4/context_diagram.dsl) — C4 context diagram (+ PNG)
- [docs/diagrams/c4/containers_diagram.dsl](docs/diagrams/c4/containers_diagram.dsl) — C4 containers diagram (+ PNG)
- [docs/diagrams/c4/components_diagram.dsl](docs/diagrams/c4/components_diagram.dsl) — C4 components diagram (+ PNG)

### Architectural Decision Records

- [ADR-001](docs/ADRs/ADR-001_Architecture.md) — Elección del Patrón de Arquitectura Principal (hexagonal)
- [ADR-002](docs/ADRs/ADR-002_Domain_modeling.md) — Elección del Enfoque de Modelado de Dominio (DDD)
- [ADR-003](docs/ADRs/ADR-003_Test_strategy.md) — Estrategia de Pruebas de Software (outside-in TDD/BDD)
- [ADR-004](docs/ADRs/ADR-004_Language.md) — Elección del Lenguaje de Programación (Java)
- [ADR-005](docs/ADRs/ADR-005_Build_tool.md) — Elección de la Herramienta de Construcción (Gradle)
- [ADR-006](docs/ADRs/ADR-006_Architecture_implementation.md) — Implementación de la Arquitectura Hexagonal (module layout)
- [ADR-007](docs/ADRs/ADR-007_Synchronous_API.md) — Elección del Mecanismo para la Interfaz Síncrona (REST)
- [ADR-008](docs/ADRs/ADR-008_Asynchronous_API.md) — Elección del Mecanismo para la Interfaz Asíncrona
- [ADR-009](docs/ADRs/ADR-009_Data_persistence.md) — Elección de la Solución de Persistencia de Datos (PostgreSQL)
- [ADR-010](docs/ADRs/ADR-010_Development_framework.md) — Elección del Framework de Desarrollo (Spring Boot)
- [ADR-011](docs/ADRs/ADR-011_Test_stack.md) — Elección del Stack de Pruebas
- [ADR-012](docs/ADRs/ADR-012_Observability.md) — Estrategia de Observabilidad
- [ADR-013](docs/ADRs/ADR-013_Configuration_management_and_secrets.md) — Gestión de la Configuración y Secretos
- [ADR-014](docs/ADRs/ADR-014_Database_migration_tool.md) — Estrategia de Migraciones de Base de Datos (Flyway)

### 🤖 Multi-Agent Architecture
This project uses a specialized agent system with distinct roles to prevent cognitive overload. Read their profiles on demand before delegating tasks:

* **Manager Agent (Orchestrator)**: `.github/agents/manager.agent.md` - Administrative runner and human-facing orchestrator. Controls the workflow, creates task folders, enforces Human-in-the-Loop (HITL) approvals by presenting draft files, and dynamically routes events via `messages.jsonl`. Does not read or write application code.
* **Leader Agent (Planner/Architect)**: `.github/agents/leader.agent.md` - Technical lead. Runs in the background to handle Task Triage and Technical Planning, outputting `draft_` files for the Manager. Extracts learnings to update `MEMORY.yaml`.
* **Scout Agent**: `.github/agents/scout.agent.md` - Codebase explorer and context filter.
* **Implementer Agent**: `.github/agents/implementer.agent.md` - Executes the implementation plan and writes the code.
* **Reviewer Agent**: `.github/agents/reviewer.agent.md` - Audits the implementation against the plan.
* **Documenter Agent**: `.github/agents/documenter.agent.md` - Updates documentation based on the completed cycle.

### 🔄 Communication Protocol and State Management

The golden rule is to avoid the "broken telephone" effect: agents **NEVER** pass code or extensive context directly through the chat. All coordination happens on disk inside the `agents_workspace/` directory, using structured data formats to prevent parsing errors and ambiguity.

#### Long-Term Memory (Persistent)
* `agents_workspace/MEMORY.yaml`: Acts as the project's episodic and semantic memory. Its sole function is to store persistent architectural decisions, project conventions, and lessons learned.
* **Read Rule**: The Leader Agent must read this file before drafting a strategy or action plan.
* **Update Rule**: After a successful task cycle, the Leader must synthesize an atomic summary with the new rules or discovered patterns and append it to this file.

#### Event Bus (Communication Log)
* `agents_workspace/messages.jsonl`: Serves as the central notification system. The Manager Agent appends JSON objects tracking the timestamp, origin, destination, action, and the specific `.yaml` file reference.

#### Short-Term Memory & The "Draft & Promote" Pattern
Task execution files are strictly isolated in subdirectories per task (e.g., `agents_workspace/tasks/t1_endpoint_setup/`).

To allow human supervision without breaking background processes, the system uses a **Draft & Promote** pattern:
1. The Leader writes `draft_task_strategy.yaml` or `draft_implementation_plan.yaml`.
2. The Manager pauses, reads the draft, and asks the human for approval in the chat.
3. Upon approval, the Manager renames the file (removing the `draft_` prefix) and continues execution.

**Standard File Templates (`agents_workspace/templates/`):**
* `task_strategy.yaml`: Defines the dynamic execution workflow (which agents to invoke and in what order).
* `scout_mission.yaml`: Details the precise instructions and target files the Scout must investigate.
* `scout_findings.yaml`: Written by the Scout depositing the pre-filtered context.
* `implementation_plan.yaml`: Contains the definitive action plan and strict acceptance criteria.
* `implementation_result.yaml`: Saved by the Implementer containing the list of modified files and execution notes.
* `reviewer_result.yaml`: Written by the Reviewer issuing an approved or rejected verdict.
* `documenters_result.yaml`: Logged by the Documenter registering updates to manuals and diagrams.
