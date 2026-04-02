# Implementation Plan: Line Status Dashboard

**Branch**: `001-line-status-dashboard` | **Date**: 2026-04-02 | **Spec**: [spec.md](spec.md)
**Input**: Feature specification from `/specs/001-line-status-dashboard/spec.md`

## Summary

Build a web dashboard that displays the real-time operational status
of all São Paulo Metrô and CPTM rail lines. The backend (Spring Boot)
proxies the Grupo CCR public API and exposes a REST endpoint. The
frontend (Svelte) renders line cards grouped into two categories
(Metrô and Trens), color-coded by each line's official color, with
auto-refresh every 60 seconds and graceful error handling.

## Technical Context

**Language/Version**: Java 21 (backend), TypeScript (frontend, tests)
**Primary Dependencies**: Spring Boot 4.0.3, Spring Data JPA,
Bun + Svelte + shadcn (frontend), Playwright (UI tests)
**Build**: Maven Wrapper (backend), Bun (frontend)
**Storage**: N/A — no database; data is fetched live from external API
with optional in-memory caching
**Testing**: jUnit (backend TDD), Playwright (UI functional tests)
**Target Platform**: Web browser (modern desktop browsers)
**Project Type**: Web application (REST API + SPA frontend)
**Performance Goals**: Dashboard loads in <3s, auto-refresh every 60s
**Constraints**: External API latency is outside our control; backend
proxy MUST cache responses for a short TTL to avoid excessive calls
**Scale/Scope**: Single-page dashboard, 13 lines across 6 operators,
single API endpoint consumed

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Notes |
|-----------|--------|-------|
| I. User-Story-Driven Development | PASS | User story written at `user_stories/001-line-status-dashboard.md` |
| II. Test-Driven Development — Java | PASS | jUnit tests planned for service, DTO mapping, and controller |
| III. Playwright UI Testing | PASS | Playwright tests planned for all UI acceptance criteria |
| IV. Prototype-First UI Design | PASS | Prototype exists at `prototypes/sao-paulo-rail-status-left-border-accent.html` |
| V. Per-Directory Documentation | PASS | CLAUDE.md files planned for each project directory |
| VI. API Contract Transparency | PASS | Swagger/OpenAPI spec planned for backend proxy endpoint |
| VII. Simplicity & YAGNI | PASS | No database, no auth, no over-engineering — direct API proxy + SPA |

No violations. Proceeding to Phase 0.

## Project Structure

### Documentation (this feature)

```text
specs/001-line-status-dashboard/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   └── api.md           # Backend REST API contract
└── tasks.md             # Phase 2 output (/speckit.tasks)
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/statusmetro/
│   ├── StatusMetroApplication.java
│   ├── config/
│   │   └── RestClientConfig.java
│   ├── controller/
│   │   └── LineStatusController.java
│   ├── dto/
│   │   ├── LineStatusResponse.java
│   │   ├── ConcessionDTO.java
│   │   ├── LineDTO.java
│   │   └── LineStatusDTO.java
│   └── service/
│       └── LineStatusService.java
├── src/main/resources/
│   └── application.properties
├── src/test/java/com/statusmetro/
│   ├── controller/
│   │   └── LineStatusControllerTest.java
│   └── service/
│       └── LineStatusServiceTest.java
├── pom.xml
└── CLAUDE.md

frontend/
├── src/
│   ├── lib/
│   │   ├── components/
│   │   │   ├── LineCard.svelte
│   │   │   ├── LineGroup.svelte
│   │   │   ├── SummaryBanner.svelte
│   │   │   ├── Sidebar.svelte
│   │   │   ├── Header.svelte
│   │   │   └── ErrorBanner.svelte
│   │   └── services/
│   │       └── lineStatusApi.ts
│   ├── routes/
│   │   └── +page.svelte
│   └── app.html
├── package.json
├── svelte.config.js
└── CLAUDE.md

playwright/
├── tests/
│   └── line-status-dashboard.spec.ts
├── playwright.config.ts
├── package.json
└── CLAUDE.md

user_stories/
├── 001-line-status-dashboard.md
└── CLAUDE.md

prototypes/
├── sao-paulo-rail-status-left-border-accent.html
├── sao-paulo-rail-status-left-border-accent.png
└── CLAUDE.md
```

**Structure Decision**: Web application with separate `backend/` and
`frontend/` directories as defined in the project constitution. The
`playwright/` directory is a standalone TypeScript project for UI
testing. User stories and prototypes have their own top-level
directories.

## Complexity Tracking

No Constitution Check violations. No complexity justification needed.
