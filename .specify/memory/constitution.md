<!--
  ============================================================
  SYNC IMPACT REPORT
  ============================================================
  Version change: 0.0.0 → 1.0.0 (MAJOR — initial ratification)

  Modified principles: N/A (first version)

  Added sections:
    - Core Principles (7 principles)
    - Technology Stack & Project Structure
    - Development Workflow
    - Governance

  Removed sections: None

  Templates requiring updates:
    - .specify/templates/plan-template.md        ✅ compatible (no changes needed)
    - .specify/templates/spec-template.md         ✅ compatible (no changes needed)
    - .specify/templates/tasks-template.md        ✅ compatible (no changes needed)
    - .specify/templates/checklist-template.md    ✅ compatible (no changes needed)

  Follow-up TODOs: None
  ============================================================
-->

# Status Metrô Constitution

## Core Principles

### I. User-Story-Driven Development

Every new use case implementation (except architectural changes) MUST
begin with a user story written using the template stored in
`./user_stories/`. The user story serves as the single source of truth
for requirements and MUST be referenced throughout implementation to
maintain alignment. If context is lost during development, the user
story is the authoritative recovery point.

### II. Test-Driven Development — Java (NON-NEGOTIABLE)

All Java code MUST follow strict TDD using jUnit:

- Write a jUnit test that exercises the expected behavior BEFORE
  writing the implementation.
- The test MUST fail (Red phase) — confirming it tests the right thing.
- Write the minimum implementation to make the test pass (Green phase).
- Refactor while keeping tests green (Refactor phase).

No Java implementation code may be committed without a corresponding
failing-then-passing test. Skipping the Red phase is a constitution
violation.

### III. Playwright UI Testing (NON-NEGOTIABLE)

Every implementation that impacts the user interface MUST have a
Playwright test case (written in TypeScript under `./playwright/`)
that validates the change. The Playwright test MUST be written and
executed successfully BEFORE the feature is declared complete. A UI
change without a passing Playwright test is not done.

### IV. Prototype-First UI Design

UI features SHOULD be prototyped using Google Stitch before
implementation. HTML prototypes are stored in `./prototypes/` and
serve as visual specifications for frontend development. When a
prototype exists, the implementation MUST match its layout and
behavior intent.

### V. Per-Directory Documentation

Each project directory (`./backend/`, `./frontend/`, `./playwright/`,
`./user_stories/`, `./prototypes/`) MUST have its own `CLAUDE.md`
file. These files MUST contain sufficient detail for any developer
(human or AI) to understand the directory's purpose, conventions,
build/run instructions, and key decisions. Err on the side of more
detail, not less.

### VI. API Contract Transparency

The backend REST API MUST be documented via Swagger (OpenAPI). Every
new or modified endpoint MUST have its Swagger definition updated
before the feature is considered complete. The Swagger specification
is the contract between backend and frontend.

### VII. Simplicity & YAGNI

Start with the simplest solution that satisfies the user story. Do
not build abstractions, configuration options, or extension points
that are not required by a current user story. Complexity MUST be
justified by a concrete, present-day need — not a hypothetical future
requirement.

## Technology Stack & Project Structure

### Stack

| Layer       | Technology                                    |
|-------------|-----------------------------------------------|
| Language    | Java 21                                       |
| Build       | Maven Wrapper (`./mvnw`)                      |
| Backend     | Spring Boot 4.0.3, Spring Data JPA            |
| Frontend    | Bun, Svelte, shadcn components                |
| API Docs    | Swagger (OpenAPI)                              |
| Unit Tests  | jUnit                                         |
| UI Tests    | Playwright (TypeScript)                       |
| Prototypes  | Google Stitch (HTML exports)                  |

### Directory Layout

```
metro/
├── backend/        # REST API (Spring Boot, Spring Data JPA)
├── frontend/       # Bun + Svelte application
├── user_stories/   # User story documents (template-driven)
├── prototypes/     # Google Stitch HTML prototypes
├── playwright/     # Playwright TypeScript test project
└── .specify/       # Spec Kit configuration and templates
```

Each directory listed above MUST contain a `CLAUDE.md` file
(see Principle V).

## Development Workflow

### New Feature Lifecycle

1. **Story** — Write a user story in `./user_stories/` using the
   established template.
2. **Prototype** (if UI-impacting) — Create or update a Google Stitch
   prototype in `./prototypes/`.
3. **Backend TDD** — Write jUnit tests first → Red → Green → Refactor.
4. **Swagger** — Update the OpenAPI specification for any new/changed
   endpoints.
5. **Frontend** — Implement the Svelte UI, referencing the prototype.
6. **Playwright** — Write and run Playwright tests for all UI changes.
7. **Review** — Verify all tests pass, Swagger is current, and the
   user story acceptance criteria are met.

### Architectural Changes

Changes that are purely architectural (e.g., dependency upgrades,
build pipeline, infrastructure) are exempt from the user-story
requirement but MUST still follow TDD for any Java code and MUST
document their rationale in the relevant `CLAUDE.md` or commit
message.

## Governance

This constitution is the highest-authority document for the Status
Metrô project. In case of conflict between this constitution and any
other project document, this constitution prevails.

### Amendment Procedure

1. Propose the change with a rationale.
2. Document the change in this file with an updated Sync Impact Report.
3. Increment the version according to semantic versioning:
   - **MAJOR**: Principle removal, redefinition, or backward-incompatible
     governance change.
   - **MINOR**: New principle or materially expanded guidance.
   - **PATCH**: Clarification, typo fix, or non-semantic refinement.
4. Update `LAST_AMENDED_DATE` to the date of the change.
5. Propagate any impacted references across templates and docs.

### Compliance

- All pull requests and code reviews MUST verify compliance with
  these principles.
- Violations MUST be flagged and resolved before merge.
- The Spec Kit plan template's "Constitution Check" gate MUST
  reference these principles when validating feature plans.

**Version**: 1.0.0 | **Ratified**: 2026-04-02 | **Last Amended**: 2026-04-02
