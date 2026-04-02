# Feature Specification: Line Status Dashboard

**Feature Branch**: `001-line-status-dashboard`
**Created**: 2026-04-02
**Status**: Draft
**Input**: User description: "Build an application that can show the line status for Sao Paulo Metrô and CPTM (train service). You should fetch the data from https://webapi.grupoccr.com.br/v1/mobility/public/line-status/current/state/SP."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - View Current Line Status (Priority: P1)

As a São Paulo commuter, I want to open the dashboard and immediately
see the current operational status of all Metrô and CPTM lines, so
that I can decide my route before leaving home or the office.

The dashboard displays every line grouped by operator (Metrô, CPTM,
ViaQuatro, ViaMobilidade, TIC Trens). Each line shows its number,
color-coded name, operating company, and current status (e.g.,
"Operação Normal", "Velocidade Reduzida", "Operação Parcial",
"Paralisada"). Lines with disruptions MUST display the description
text explaining the issue.

**Why this priority**: This is the core value proposition — without
real-time line status visibility, the application has no purpose.

**Independent Test**: Can be fully tested by opening the dashboard
URL in a browser and verifying that all lines appear with their
current status fetched from the external API.

**Acceptance Scenarios**:

1. **Given** the external API is reachable, **When** the user opens
   the dashboard, **Then** all lines from every operator are displayed
   with their current status and the "last updated" timestamp is
   visible.
2. **Given** a line has status other than "Operação Normal", **When**
   the user views the dashboard, **Then** the disrupted line is
   visually distinguished (color/icon) and the disruption description
   is displayed.
3. **Given** the external API is reachable, **When** the user opens
   the dashboard, **Then** lines are grouped into two categories —
   Metrô (Lines 1-5, 15) and Trens (Lines 7-13) — with clear
   section headings.

---

### User Story 2 - Automatic Data Refresh (Priority: P2)

As a commuter monitoring the situation during a disruption, I want
the dashboard to periodically refresh the line status data without
requiring me to manually reload the page, so I stay informed of
changes in real time.

**Why this priority**: Stale data during disruptions leads to poor
commuting decisions. Auto-refresh transforms the dashboard from a
snapshot tool into a live monitoring tool.

**Independent Test**: Can be tested by observing the dashboard over
a period of time and verifying the "last updated" timestamp changes
automatically, or by manually triggering a refresh and verifying data
updates.

**Acceptance Scenarios**:

1. **Given** the dashboard is open, **When** the configured refresh
   interval elapses, **Then** the status data is re-fetched and the
   UI is updated without a full page reload.
2. **Given** the dashboard is open, **When** the user clicks a manual
   refresh button, **Then** the status data is immediately re-fetched
   and updated.

---

### User Story 3 - Graceful Error Handling (Priority: P3)

As a user, when the external data source is temporarily unavailable,
I want to see the last known status with a clear indication that the
data may be stale, so I am not misled by outdated information.

**Why this priority**: The external API may experience downtime or
network issues. Users must understand when displayed data is not
current to avoid making decisions based on stale information.

**Independent Test**: Can be tested by simulating an API failure and
verifying the UI shows an appropriate warning while retaining the
last known data.

**Acceptance Scenarios**:

1. **Given** the external API is unreachable, **When** the dashboard
   attempts a refresh, **Then** the previously loaded data remains
   displayed with a visible warning banner indicating the data may
   be outdated.
2. **Given** the external API returns an error, **When** the user
   first opens the dashboard (no cached data), **Then** a
   user-friendly error message is displayed with a retry option.

---

### Edge Cases

- What happens when the external API returns an empty `concessoes`
  array? The dashboard MUST display a message indicating no line data
  is currently available.
- What happens when a line has a `null` or empty `descricao` field
  during a non-normal status? The status label MUST still be shown;
  the description area is simply omitted.
- What happens when a new line or operator is added to the API
  response? The dashboard MUST render it dynamically without
  requiring code changes — lines are rendered from the data, not
  hardcoded.
- What happens when the user has no internet connection? The browser
  displays a standard offline indication; the dashboard does not
  attempt to hide this.

## Clarifications

### Session 2026-04-02

- Q: How should the 6 API operators be grouped in the UI? → A: Group into 2 high-level categories: **Metrô** (Lines 1-5, 15) and **Trens** (Lines 7-13), matching how commuters think about the network (subway vs. trains), as shown in the prototype.
- Q: Are sidebar navigation pages (Sobre, Ajuda, Contato) in scope? → A: No. Dashboard only — sidebar renders as visual chrome but other links are non-functional placeholders for this feature.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST fetch line status data from the Grupo CCR
  public API endpoint for the state of São Paulo.
- **FR-002**: System MUST display all lines returned by the API,
  grouped into two high-level categories — **Metrô** (Lines 1-5, 15)
  and **Trens** (Lines 7-13) — showing line number, color name,
  operator label, and current operational status.
- **FR-003**: System MUST use each line's `corRgb` value from the API
  to color-code the line's visual representation in the UI.
- **FR-004**: System MUST display the disruption description text
  for any line whose status is not "Operação Normal".
- **FR-005**: System MUST show a "last updated" timestamp derived
  from the API's `dataAtualizacao` field.
- **FR-006**: System MUST auto-refresh the line status data at a
  regular interval (default: 60 seconds).
- **FR-007**: System MUST provide a manual refresh button for
  on-demand data updates.
- **FR-008**: System MUST display a summary banner showing the
  overall operational percentage and a quick status per operator
  group.
- **FR-009**: System MUST handle API failures gracefully, showing
  a warning when data may be stale while retaining the last
  successfully fetched data.
- **FR-010**: System MUST render lines dynamically from the API
  response — no hardcoded line definitions.

### Key Entities

- **Operator (Concessão)**: A transit operator (e.g., "Metro SP",
  "CPTM", "ViaQuatro"). Has a unique ID, name, state, and logo path.
  Contains one or more lines.
- **Line (Linha)**: A single rail line operated by a concessão. Has a
  unique ID, number, color name, hex color code (`corRgb`), icon
  path, and current status.
- **Line Status (Status da Linha)**: The operational state of a line.
  Has a code (e.g., "OperacaoNormal", "VelocidadeReduzida"), a
  human-readable status label, and an optional description with
  details about disruptions.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can see the current status of all São Paulo rail
  lines within 3 seconds of opening the dashboard.
- **SC-002**: Line status data refreshes automatically at least once
  per minute without user intervention.
- **SC-003**: 100% of lines returned by the API are rendered on the
  dashboard — no lines are omitted.
- **SC-004**: When a disruption occurs, users can read the disruption
  description within the line's card without additional clicks or
  navigation.
- **SC-005**: When the data source is unavailable, users see a clear
  staleness warning within 5 seconds of the failed refresh attempt.

## Assumptions

- Users have a stable internet connection and access a modern web
  browser (Chrome, Firefox, Safari, Edge — latest two versions).
- The Grupo CCR public API does not require authentication and
  remains publicly accessible at the documented endpoint.
- The API response structure (concessões → linhas → statusLinha)
  remains stable; breaking API changes are outside the scope of
  this feature.
- The UI layout follows the Google Stitch prototype stored in
  `./prototypes/sao-paulo-rail-status-left-border-accent.html`.
- Mobile-responsive layout is desirable but not a P1 requirement
  for the initial delivery.
- No user authentication is required — the dashboard is publicly
  accessible.
- Sidebar navigation links (Sobre, Ajuda, Contato) are visual
  placeholders only — those pages are not implemented in this feature.
