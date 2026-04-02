# User Stories — Status Metrô

## Purpose

Contains user story documents for all feature implementations.
Per Constitution Principle I, every new use case (except architectural
changes) MUST begin with a user story in this folder.

## Naming Convention

Files are named to match their feature branch:
`{number}-{feature-short-name}.md` (e.g., `001-line-status-dashboard.md`)

## Template

User stories follow a structured format with:
- Story statement (As a / I want to / So that)
- Context (background information, API details, prototypes)
- Acceptance Criteria (Given/When/Then)
- Implementation Notes (backend, frontend, playwright guidance)
- Out of Scope
- Dependencies

## Usage

1. Create a new `.md` file before starting implementation
2. Reference the story during development to maintain alignment
3. If context is lost, the user story is the authoritative recovery point
