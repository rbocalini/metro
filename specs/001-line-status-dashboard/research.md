# Research: Line Status Dashboard

**Feature**: 001-line-status-dashboard
**Date**: 2026-04-02

## 1. External API Integration

**Decision**: Proxy the Grupo CCR API through the Spring Boot backend
rather than calling it directly from the Svelte frontend.

**Rationale**: A backend proxy avoids CORS issues (the CCR API does
not set Access-Control-Allow-Origin headers for arbitrary origins),
enables server-side caching to reduce external API load, and hides
the third-party URL from the browser.

**Alternatives considered**:
- Direct frontend fetch: Rejected due to CORS restrictions and no
  ability to cache server-side.
- Server-side rendering (SSR): Overkill for a single API call;
  SvelteKit SSR could work but adds unnecessary complexity.

## 2. Backend Caching Strategy

**Decision**: Use Spring's `@Cacheable` with a Caffeine in-memory
cache and a TTL of 30 seconds.

**Rationale**: The external API updates roughly every few minutes.
A 30-second cache TTL balances freshness (well within the 60-second
frontend refresh interval) with protection against excessive external
calls. No external cache infrastructure (Redis) needed for a
single-instance application.

**Alternatives considered**:
- No caching: Rejected — would hit the external API on every frontend
  poll, risking rate limiting.
- Redis cache: Overkill for a single-instance app with one cached
  endpoint.
- Manual TTL with ConcurrentHashMap: Unnecessary when Spring Cache +
  Caffeine handles this declaratively.

## 3. Frontend HTTP Client

**Decision**: Use the native `fetch` API in Svelte to call the
backend proxy endpoint.

**Rationale**: No additional HTTP library needed. Svelte's reactivity
model handles re-rendering on data updates naturally. A simple
`setInterval` drives the auto-refresh cycle.

**Alternatives considered**:
- Axios: Adds a dependency for no meaningful benefit over `fetch`.
- SvelteKit `load` function: Would work for initial load but
  complicates client-side polling.

## 4. Line Grouping Logic

**Decision**: The backend groups lines into two categories — Metrô
(Lines 1-5, 15) and Trens (Lines 7-13) — based on line number
ranges. The frontend receives pre-grouped data.

**Rationale**: Grouping in the backend keeps the frontend simple and
ensures consistent categorization. The line number ranges are stable
(defined by the São Paulo transit network structure).

**Alternatives considered**:
- Frontend grouping: Rejected — would duplicate business logic and
  require the frontend to know line number classification rules.
- Operator-based grouping (6 groups): Rejected by clarification —
  commuters think in terms of subway vs. trains.

## 5. Status Visual Mapping

**Decision**: Map status codes to visual indicators:

| Status Code | Label | Color | Icon |
|-------------|-------|-------|------|
| OperacaoNormal | Operação Normal | Green (#22c55e) | Filled circle |
| VelocidadeReduzida | Velocidade Reduzida | Amber (#f59e0b) | Filled circle |
| OperacaoEncerrada | Operação Encerrada | Gray (#6b7280) | Filled circle |
| OperacaoParcial | Operação Parcial | Orange (#f97316) | Filled circle |
| Paralisada | Paralisada | Red (#ef4444) | Filled circle |

**Rationale**: Follows standard severity color conventions (green →
amber → orange → red). Gray for closed operations (non-emergency).
Matches prototype's use of green for normal and amber for reduced.

**Alternatives considered**:
- Single "disrupted" color: Rejected — different severities require
  different urgency signals.

## 6. Swagger / OpenAPI

**Decision**: Auto-generate OpenAPI spec using springdoc-openapi
(integrated with Spring Boot 4.x). The Swagger UI will be accessible
at `/swagger-ui.html`.

**Rationale**: Springdoc auto-generates accurate API documentation
from controller annotations. Zero manual YAML maintenance.

**Alternatives considered**:
- Manual OpenAPI YAML: Higher maintenance burden with no benefit.
- SpringFox: Deprecated; springdoc-openapi is the maintained
  successor.
