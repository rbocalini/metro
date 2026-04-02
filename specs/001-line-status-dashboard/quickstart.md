# Quickstart: Line Status Dashboard

**Feature**: 001-line-status-dashboard
**Date**: 2026-04-02

## Prerequisites

- Java 21 (JDK)
- Bun (latest)
- Node.js 18+ (for Playwright)
- Git

## 1. Clone and checkout

```bash
git clone <repo-url> metro
cd metro
git checkout 001-line-status-dashboard
```

## 2. Start the backend

```bash
cd backend
./mvnw spring-boot:run
```

The backend starts on `http://localhost:8080`. Verify the API:

```bash
curl http://localhost:8080/api/line-status
```

You should see a JSON response with `lastUpdated` and `groups`
containing Metrô and Trens line data.

## 3. Start the frontend

```bash
cd frontend
bun install
bun run dev
```

The frontend starts on `http://localhost:5173` (default Vite port).
Open it in a browser to see the dashboard.

## 4. Run backend tests

```bash
cd backend
./mvnw test
```

## 5. Run Playwright tests

```bash
cd playwright
bun install
npx playwright install
npx playwright test
```

**Note**: Both backend and frontend MUST be running before executing
Playwright tests.

## 6. View API documentation

With the backend running, open:

```
http://localhost:8080/swagger-ui.html
```

## Verification Checklist

- [ ] Backend starts without errors
- [ ] `GET /api/line-status` returns line data with groups
- [ ] Frontend loads and displays all 13 lines
- [ ] Lines are grouped into Metrô and Trens sections
- [ ] Disrupted lines (if any) show description text
- [ ] "Last updated" timestamp is visible
- [ ] Auto-refresh updates data after 60 seconds
- [ ] Manual refresh button works
- [ ] All jUnit tests pass
- [ ] All Playwright tests pass
