# User Story: Line Status Dashboard

**Story ID**: US-001
**Feature Branch**: `001-line-status-dashboard`
**Created**: 2026-04-02
**Priority**: P1 — Core feature, MVP

---

## Story

**As a** São Paulo commuter,
**I want to** see the real-time operational status of all Metrô and
CPTM rail lines on a web dashboard,
**So that** I can make informed decisions about my commuting route
before leaving home or the office.

---

## Context

São Paulo's rail network is operated by multiple entities: Metrô SP
(Lines 1, 2, 3, 15), ViaQuatro (Line 4), ViaMobilidade (Lines 5, 8,
9), CPTM (Lines 10, 11, 12, 13), and TIC Trens (Line 7). The Grupo
CCR public API provides a unified endpoint that returns the current
operational status of all these lines:

```
GET https://webapi.grupoccr.com.br/v1/mobility/public/line-status/current/state/SP
```

The response groups lines by operator (concessão). Each line includes:
- `numero` — line number
- `nome` — color name (e.g., "Azul", "Coral")
- `corRgb` — hex color code for UI rendering
- `statusLinha.codigo` — machine-readable status code
  (e.g., "OperacaoNormal", "VelocidadeReduzida")
- `statusLinha.status` — human-readable status label
- `statusLinha.descricao` — optional free-text disruption description

A Google Stitch HTML prototype is available at:
`./prototypes/sao-paulo-rail-status-left-border-accent.html`

---

## Acceptance Criteria

### AC-1: Display all lines grouped into two categories
- **Given** the external API is reachable
- **When** the user opens the dashboard
- **Then** all lines appear grouped into two categories —
  **Metrô** (Lines 1-5, 15) and **Trens** (Lines 7-13) — with
  clear section headings
- **And** each line card shows: line number (color-coded badge),
  line name, operator label, and current status

### AC-2: Color-code lines using API data
- **Given** the API returns a `corRgb` hex value for each line
- **When** the line card is rendered
- **Then** the line's visual accent (left border, number badge)
  uses that exact color

### AC-3: Show disruption details
- **Given** a line has a status code other than "OperacaoNormal"
- **When** the dashboard renders that line
- **Then** the status is visually distinguished (e.g., amber for
  "Velocidade Reduzida", red for "Paralisada")
- **And** the `descricao` text is displayed below the status label
  (if not null/empty)

### AC-4: Display last-updated timestamp
- **Given** the API response includes `dataAtualizacao`
- **When** the dashboard renders
- **Then** a formatted "last updated" timestamp is visible in the
  header area

### AC-5: Summary banner
- **Given** all line statuses are loaded
- **When** the dashboard renders
- **Then** a banner at the top shows the overall operational
  percentage (lines with normal status / total lines) and quick
  status tags per operator group

### AC-6: Auto-refresh
- **Given** the dashboard is open
- **When** 60 seconds elapse since the last fetch
- **Then** data is re-fetched from the API and the UI updates
  without a full page reload
- **And** the "last updated" timestamp reflects the new data

### AC-7: Manual refresh
- **Given** the dashboard is open
- **When** the user clicks the refresh button
- **Then** data is immediately re-fetched and the UI updates

### AC-8: API failure — with cached data
- **Given** the API was previously reachable and data was loaded
- **When** a subsequent refresh fails (network error or API error)
- **Then** the last known data remains displayed
- **And** a warning banner appears indicating the data may be stale

### AC-9: API failure — first load
- **Given** the user opens the dashboard for the first time
- **When** the API is unreachable
- **Then** a user-friendly error message is displayed
- **And** a retry button is available

### AC-10: Dynamic rendering
- **Given** the API adds a new line or operator in the future
- **When** the dashboard fetches the updated response
- **Then** the new line/operator is rendered automatically without
  code changes

---

## Implementation Notes

### Backend (`./backend/`)
- Create a REST endpoint that proxies/caches the Grupo CCR API
  response. This avoids exposing the third-party URL to the frontend
  and allows server-side caching.
- Use Spring Boot with a scheduled task or cache TTL to limit
  external API calls.
- The proxy endpoint MUST return the data in the same structure or
  a simplified DTO that the frontend can consume directly.
- Write jUnit tests (TDD) for: the service that calls the external
  API, the DTO mapping, and the controller endpoint.

### Frontend (`./frontend/`)
- Build the dashboard using Svelte with shadcn components.
- Reference the prototype at
  `./prototypes/sao-paulo-rail-status-left-border-accent.html` for
  layout, typography, and visual styling.
- Implement auto-refresh using a Svelte reactive interval.
- Use the line's `corRgb` for dynamic inline styles (border color,
  badge background).

### Playwright (`./playwright/`)
- Write functional tests covering:
  - Dashboard loads and displays all lines
  - Lines are grouped by operator
  - Disrupted lines show description text
  - Refresh button triggers data reload
  - Error state shows warning banner

---

## Out of Scope

- User authentication / login
- Historical status data or trend analysis
- Push notifications
- Mobile-native application (responsive web is sufficient)
- Offline-first / service worker caching
- Map view (placeholder link only in prototype)
- Sidebar navigation pages (Sobre, Ajuda, Contato) — visual
  placeholders only, not functional in this feature

---

## Dependencies

- Grupo CCR public API availability at
  `https://webapi.grupoccr.com.br/v1/mobility/public/line-status/current/state/SP`
- Google Stitch prototype for UI reference
