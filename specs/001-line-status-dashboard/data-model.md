# Data Model: Line Status Dashboard

**Feature**: 001-line-status-dashboard
**Date**: 2026-04-02

## Overview

This feature has **no persistent storage**. All data is fetched in
real-time from the Grupo CCR external API and passed through to the
frontend. The data model below describes the DTO (Data Transfer
Object) structure used between backend and frontend.

## External API Response Structure

Source: `GET https://webapi.grupoccr.com.br/v1/mobility/public/line-status/current/state/SP`

```
ApiResponse
├── status: boolean
├── message: string
├── errorCode: string
└── data
    ├── dataAtualizacao: string (ISO datetime)
    └── concessoes: Concessao[]
        ├── uid: string
        ├── nome: string
        ├── estados: string
        ├── logo._path: string
        └── linhas: Linha[]
            ├── uid: string
            ├── numero: int|string
            ├── nome: string
            ├── icone._path: string
            ├── corRgb: string (hex color)
            └── statusLinha
                ├── codigo: string
                ├── status: string
                └── descricao: string|null
```

## Backend DTO Structure (proxy response to frontend)

The backend transforms the external API response into a simplified,
pre-grouped structure for the frontend.

```
LineStatusResponse
├── lastUpdated: string (ISO datetime)
├── groups: LineGroup[]
    ├── name: string ("Metrô" | "Trens")
    └── lines: Line[]
        ├── uid: string
        ├── number: int
        ├── name: string (color name, e.g., "Azul")
        ├── operator: string (e.g., "Metro SP", "CPTM")
        ├── colorHex: string (e.g., "#171796")
        ├── status: LineStatus
        │   ├── code: string (e.g., "OperacaoNormal")
        │   ├── label: string (e.g., "Operação Normal")
        │   └── description: string|null
        └── category: string ("metro" | "trens")
```

## Entity Descriptions

### LineStatusResponse
Top-level response returned by the backend proxy endpoint.
- `lastUpdated`: Timestamp from the external API's `dataAtualizacao`
- `groups`: Array of line groups (Metrô, Trens)

### LineGroup
A logical grouping of rail lines.
- `name`: Display name for the section heading
- `lines`: Ordered list of lines within this group (sorted by number)

### Line
A single rail line.
- `uid`: Unique identifier from the external API
- `number`: Line number (1-15)
- `name`: Color-based name (e.g., "Azul", "Coral")
- `operator`: Name of the operating concessão
- `colorHex`: Official line color for UI rendering
- `status`: Current operational status
- `category`: Classification key ("metro" or "trens")

### LineStatus
The operational state of a line at a point in time.
- `code`: Machine-readable status (e.g., "OperacaoNormal",
  "VelocidadeReduzida", "Paralisada")
- `label`: Human-readable Portuguese label
- `description`: Free-text explanation of disruption; null/empty
  when status is normal

## Grouping Rules

| Category | Line Numbers | Operators Included |
|----------|-------------|-------------------|
| Metrô | 1, 2, 3, 4, 5, 15 | Metro SP, ViaQuatro, ViaMobilidade 5 |
| Trens | 7, 8, 9, 10, 11, 12, 13 | CPTM, ViaMobilidade 8 e 9, TIC Trens |

Lines within each group are sorted by number ascending.

## State Transitions

Lines have no application-managed state transitions. The status
is read-only and reflects whatever the external API reports at
fetch time. The known status codes are:

- `OperacaoNormal` — all services running normally
- `VelocidadeReduzida` — trains running at reduced speed
- `OperacaoParcial` — partial operation (some stations skipped)
- `Paralisada` — line is stopped
- `OperacaoEncerrada` — operations closed (end of service hours)

New status codes may appear; the frontend MUST handle unknown codes
gracefully by displaying the label text as-is with a neutral visual
indicator.
