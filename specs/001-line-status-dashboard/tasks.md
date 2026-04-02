# Tasks: Line Status Dashboard

**Input**: Design documents from `/specs/001-line-status-dashboard/`
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/api.md, quickstart.md

**Tests**: Included — Constitution mandates TDD (jUnit) for Java and Playwright for UI changes.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Backend**: `backend/src/main/java/com/statusmetro/`
- **Backend tests**: `backend/src/test/java/com/statusmetro/`
- **Frontend**: `frontend/src/`
- **Playwright**: `playwright/tests/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization, build tooling, and directory structure

- [x] T001 Initialize Spring Boot 4.0.3 backend project with Maven Wrapper in `backend/` (pom.xml with spring-boot-starter-web, spring-boot-starter-cache, caffeine, springdoc-openapi dependencies)
- [x] T002 Initialize Bun + Svelte frontend project in `frontend/` (package.json, svelte.config.js, app.html with Tailwind CSS and Inter font)
- [x] T003 [P] Initialize Playwright project in `playwright/` (package.json, playwright.config.ts targeting http://localhost:5173)
- [x] T004 [P] Create CLAUDE.md in `backend/CLAUDE.md` with build/run instructions, package structure, and conventions
- [x] T005 [P] Create CLAUDE.md in `frontend/CLAUDE.md` with build/run instructions, component conventions, and styling approach
- [x] T006 [P] Create CLAUDE.md in `playwright/CLAUDE.md` with test run instructions and conventions
- [x] T007 [P] Create CLAUDE.md in `user_stories/CLAUDE.md` with user story template reference and usage guidelines
- [x] T008 [P] Create CLAUDE.md in `prototypes/CLAUDE.md` with prototype usage and Google Stitch workflow notes

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core backend infrastructure that MUST be complete before ANY user story can be implemented

**CRITICAL**: No user story work can begin until this phase is complete

- [x] T009 Create DTO classes: `LineStatusResponse.java`, `LineGroupDTO.java`, `LineDTO.java`, `LineStatusDTO.java` in `backend/src/main/java/com/statusmetro/dto/`
- [x] T010 Configure Caffeine cache in `backend/src/main/java/com/statusmetro/config/CacheConfig.java` (30-second TTL for line-status cache)
- [x] T011 [P] Configure RestClient bean in `backend/src/main/java/com/statusmetro/config/RestClientConfig.java` for external API calls
- [x] T012 [P] Configure CORS in `backend/src/main/java/com/statusmetro/config/WebConfig.java` to allow frontend origin (http://localhost:5173)
- [x] T013 Set application properties in `backend/src/main/resources/application.properties` (server port, external API URL, cache settings)
- [x] T014 [P] Create frontend API service in `frontend/src/lib/services/lineStatusApi.ts` (fetch wrapper for `GET /api/line-status` with error handling)

**Checkpoint**: Foundation ready — user story implementation can now begin

---

## Phase 3: User Story 1 — View Current Line Status (Priority: P1)

**Goal**: Display all Metrô and CPTM lines with real-time status, grouped into two categories, color-coded by line color.

**Independent Test**: Open the dashboard URL and verify all 13 lines appear grouped into Metrô and Trens sections with correct status from the live API.

### Tests for User Story 1

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation (Constitution Principle II)**

- [x] T015 [P] [US1] Write jUnit test for LineStatusService: test external API call, DTO mapping, and grouping logic (Metrô lines 1-5,15 / Trens lines 7-13) in `backend/src/test/java/com/statusmetro/service/LineStatusServiceTest.java`
- [x] T016 [P] [US1] Write jUnit test for LineStatusController: test GET /api/line-status returns grouped response with correct structure in `backend/src/test/java/com/statusmetro/controller/LineStatusControllerTest.java`

### Implementation for User Story 1

- [x] T017 [US1] Implement LineStatusService in `backend/src/main/java/com/statusmetro/service/LineStatusService.java` — fetch from external CCR API, map to DTOs, group lines into Metrô (1-5, 15) and Trens (7-13), sort by number, apply @Cacheable
- [x] T018 [US1] Implement LineStatusController in `backend/src/main/java/com/statusmetro/controller/LineStatusController.java` — GET /api/line-status endpoint, Swagger annotations
- [x] T019 [US1] Run jUnit tests — verify T015 and T016 pass (Red→Green)
- [x] T020 [P] [US1] Create Sidebar component in `frontend/src/lib/components/Sidebar.svelte` — navigation chrome with Dashboard active, other links as non-functional placeholders, system status indicator
- [x] T021 [P] [US1] Create Header component in `frontend/src/lib/components/Header.svelte` — page title, last-updated timestamp display, refresh and notification button placeholders
- [x] T022 [P] [US1] Create SummaryBanner component in `frontend/src/lib/components/SummaryBanner.svelte` — operational percentage calculation, per-category quick status tags, gradient background matching prototype
- [x] T023 [P] [US1] Create LineCard component in `frontend/src/lib/components/LineCard.svelte` — line number badge with dynamic colorHex background, line name, operator label, status indicator (green/amber/orange/red/gray based on status code), disruption description text
- [x] T024 [P] [US1] Create LineGroup component in `frontend/src/lib/components/LineGroup.svelte` — section heading, list of LineCard components for a group
- [x] T025 [US1] Build main dashboard page in `frontend/src/routes/+page.svelte` — compose Sidebar, Header, SummaryBanner, and two LineGroup components (Metrô, Trens), wire up API service call on mount
- [x] T026 [US1] Style dashboard to match prototype in `prototypes/sao-paulo-rail-status-left-border-accent.html` — left-border accent on line cards, typography (Inter font), color scheme, spacing, rounded cards

### Playwright Tests for User Story 1

> **NOTE: Write and run before declaring US1 complete (Constitution Principle III)**

- [x] T027 [US1] Write Playwright test in `playwright/tests/line-status-dashboard.spec.ts`: verify dashboard loads, all 13 lines visible, lines grouped under "Metrô" and "Trens" headings, disrupted lines show description text, line cards have color-coded left border

**Checkpoint**: User Story 1 complete — dashboard displays all lines with live status

---

## Phase 4: User Story 2 — Automatic Data Refresh (Priority: P2)

**Goal**: Dashboard auto-refreshes line status every 60 seconds and provides a manual refresh button.

**Independent Test**: Open the dashboard, wait 60+ seconds, and verify the "last updated" timestamp changes. Click the refresh button and verify immediate update.

### Tests for User Story 2

- [x] T028 [P] [US2] Write jUnit test verifying the backend cache TTL (30s) works correctly — second call within TTL returns cached data, call after TTL fetches fresh data in `backend/src/test/java/com/statusmetro/service/LineStatusServiceTest.java` (add test method)

### Implementation for User Story 2

- [x] T029 [US2] Add auto-refresh logic to `frontend/src/routes/+page.svelte` — setInterval polling every 60 seconds, update reactive state, refresh "last updated" timestamp without full page reload
- [x] T030 [US2] Wire manual refresh button in Header component `frontend/src/lib/components/Header.svelte` — click triggers immediate API fetch, update timestamp, visual feedback during fetch (spinning icon)
- [x] T031 [US2] Run jUnit test T028 — verify cache behavior passes

### Playwright Tests for User Story 2

- [x] T032 [US2] Write Playwright test in `playwright/tests/line-status-dashboard.spec.ts`: verify manual refresh button triggers data reload (timestamp updates), verify auto-refresh updates timestamp after interval

**Checkpoint**: User Stories 1 AND 2 both work independently

---

## Phase 5: User Story 3 — Graceful Error Handling (Priority: P3)

**Goal**: When the external API is unavailable, show last known data with a staleness warning. On first-load failure, show error with retry.

**Independent Test**: Stop the backend or simulate API failure, verify the UI shows a warning banner while retaining last data. On fresh load with API down, verify error message with retry button.

### Tests for User Story 3

- [x] T033 [P] [US3] Write jUnit test for LineStatusController error handling — verify 502 response when external API unreachable, verify error JSON structure in `backend/src/test/java/com/statusmetro/controller/LineStatusControllerTest.java` (add test method)

### Implementation for User Story 3

- [x] T034 [US3] Add exception handling to LineStatusService in `backend/src/main/java/com/statusmetro/service/LineStatusService.java` — catch external API failures, throw custom ExternalApiException
- [x] T035 [US3] Add global exception handler in `backend/src/main/java/com/statusmetro/config/GlobalExceptionHandler.java` — map ExternalApiException to 502 with error JSON body
- [x] T036 [US3] Create ErrorBanner component in `frontend/src/lib/components/ErrorBanner.svelte` — warning banner for stale data (amber), error state for first-load failure (red) with retry button
- [x] T037 [US3] Add error handling to `frontend/src/routes/+page.svelte` — on fetch failure: if data exists show ErrorBanner (stale warning) and retain data; if no data show full-page error with retry
- [x] T038 [US3] Run jUnit test T033 — verify error handling passes

### Playwright Tests for User Story 3

- [x] T039 [US3] Write Playwright test in `playwright/tests/line-status-dashboard.spec.ts`: verify error banner appears when backend returns 502, verify retry button triggers new fetch, verify stale data warning shows when refresh fails after initial load

**Checkpoint**: All user stories independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [x] T040 [P] Add Swagger/OpenAPI annotations to LineStatusController — operation summaries, response schema documentation, ensure Swagger UI accessible at /swagger-ui.html
- [x] T041 [P] Add edge case handling in frontend — empty concessoes array shows "no data available" message, null/empty descricao gracefully omitted
- [x] T042 Run full jUnit test suite in `backend/` — verify all tests pass
- [x] T043 Run full Playwright test suite in `playwright/` — verify all tests pass (11/11 passed)
- [x] T044 Run quickstart.md verification checklist end-to-end

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — can start immediately
- **Foundational (Phase 2)**: Depends on T001 and T002 completion — BLOCKS all user stories
- **User Story 1 (Phase 3)**: Depends on Foundational phase completion
- **User Story 2 (Phase 4)**: Depends on Foundational phase; can run in parallel with US1 but shares frontend page
- **User Story 3 (Phase 5)**: Depends on Foundational phase; can run after US1 (needs existing data display to test stale behavior)
- **Polish (Phase 6)**: Depends on all user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) — No dependencies on other stories
- **User Story 2 (P2)**: Can start after US1 (needs the page to add refresh to) — Shares `+page.svelte`
- **User Story 3 (P3)**: Can start after US1 (needs existing data flow to add error handling to) — Shares `+page.svelte` and service layer

### Within Each User Story

- jUnit tests MUST be written and FAIL before Java implementation (Constitution Principle II)
- Java implementation makes tests pass (Red → Green)
- Frontend components can be built in parallel (marked [P])
- Page composition after components
- Playwright tests MUST pass before story is declared complete (Constitution Principle III)

### Parallel Opportunities

**Phase 1**: T003-T008 all run in parallel (after T001/T002 start)
**Phase 2**: T010+T011+T012 in parallel; T014 in parallel
**Phase 3**: T015+T016 in parallel (tests); T020-T024 in parallel (frontend components)
**Phase 4**: T028 independent; T029+T030 sequential
**Phase 5**: T033 independent; T034+T035 sequential; T036 parallel with backend work

---

## Parallel Example: User Story 1

```bash
# Launch jUnit tests in parallel:
Task T015: "Write jUnit test for LineStatusService"
Task T016: "Write jUnit test for LineStatusController"

# After tests written, implement backend (sequential):
Task T017: "Implement LineStatusService"
Task T018: "Implement LineStatusController"
Task T019: "Run jUnit tests — verify pass"

# Launch frontend components in parallel:
Task T020: "Create Sidebar component"
Task T021: "Create Header component"
Task T022: "Create SummaryBanner component"
Task T023: "Create LineCard component"
Task T024: "Create LineGroup component"

# Compose page (depends on components):
Task T025: "Build main dashboard page"
Task T026: "Style dashboard to match prototype"

# Playwright test (depends on full page):
Task T027: "Write and run Playwright test"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL — blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: All 13 lines display with live status
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Auto-refresh working → Deploy/Demo
4. Add User Story 3 → Error handling robust → Deploy/Demo
5. Polish → Swagger docs, edge cases → Final delivery

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- jUnit tests MUST fail before implementing (TDD — Constitution Principle II)
- Playwright tests MUST pass before story complete (Constitution Principle III)
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Prototype reference: `prototypes/sao-paulo-rail-status-left-border-accent.html`
