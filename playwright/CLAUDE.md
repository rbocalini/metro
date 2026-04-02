# Playwright — Status Metrô

## Purpose

End-to-end functional tests for the Status Metrô web dashboard using
Playwright with TypeScript.

## Tech Stack

- Playwright Test
- TypeScript

## Prerequisites

Both backend and frontend MUST be running before executing tests:
1. Backend: `cd ../backend && ./mvnw spring-boot:run` (port 8080)
2. Frontend: `cd ../frontend && npm run dev` (port 5173)

## Run Tests

```bash
# Install dependencies:
npm install

# Install browsers (first time only):
npx playwright install

# Run all tests:
npx playwright test

# Run with UI:
npx playwright test --ui

# Run specific test file:
npx playwright test tests/line-status-dashboard.spec.ts
```

## Directory Structure

```
playwright/
├── tests/
│   └── line-status-dashboard.spec.ts  # All dashboard functional tests
├── playwright.config.ts               # Config (baseURL: localhost:5173)
├── package.json
└── CLAUDE.md
```

## Test Coverage

Tests validate:
- Dashboard loads and displays all 13 lines
- Lines grouped under Metrô and Trens headings
- Disrupted lines show description text
- Line cards have color-coded left border
- Manual refresh button triggers data reload
- Auto-refresh updates timestamp
- Error banner appears on API failure
- Retry button works on error state

## Conventions

- One test file per feature
- Use descriptive test names matching acceptance criteria
- Tests target `http://localhost:5173` (frontend dev server)
- Constitution Principle III: All UI changes must have passing Playwright tests
