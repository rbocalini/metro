# API Contract: Line Status Dashboard

**Feature**: 001-line-status-dashboard
**Date**: 2026-04-02

## Backend REST API

Base URL: `http://localhost:8080/api`

### GET /api/line-status

Returns the current operational status of all São Paulo rail lines,
grouped into Metrô and Trens categories.

**Authentication**: None (public endpoint)

**Request**: No parameters required.

**Response** (200 OK):

```json
{
  "lastUpdated": "2026-04-02T17:00:04",
  "groups": [
    {
      "name": "Metrô",
      "lines": [
        {
          "uid": "METRO-L1",
          "number": 1,
          "name": "Azul",
          "operator": "Metro SP",
          "colorHex": "#171796",
          "status": {
            "code": "OperacaoNormal",
            "label": "Operação Normal",
            "description": null
          },
          "category": "metro"
        },
        {
          "uid": "METRO-L2",
          "number": 2,
          "name": "Verde",
          "operator": "Metro SP",
          "colorHex": "#007A5E",
          "status": {
            "code": "OperacaoNormal",
            "label": "Operação Normal",
            "description": null
          },
          "category": "metro"
        }
      ]
    },
    {
      "name": "Trens",
      "lines": [
        {
          "uid": "TICTRENS-L7",
          "number": 7,
          "name": "Rubi",
          "operator": "TIC Trens",
          "colorHex": "#AC184A",
          "status": {
            "code": "OperacaoNormal",
            "label": "Operação Normal",
            "description": null
          },
          "category": "trens"
        },
        {
          "uid": "CPTM-L12",
          "number": 12,
          "name": "Safira",
          "operator": "CPTM",
          "colorHex": "#1B2477",
          "status": {
            "code": "VelocidadeReduzida",
            "label": "Velocidade Reduzida",
            "description": "Estamos circulando com velocidade reduzida..."
          },
          "category": "trens"
        }
      ]
    }
  ]
}
```

**Response** (502 Bad Gateway — external API unreachable):

```json
{
  "error": "EXTERNAL_API_UNAVAILABLE",
  "message": "Unable to fetch line status from data source. Please try again later."
}
```

**Response** (500 Internal Server Error):

```json
{
  "error": "INTERNAL_ERROR",
  "message": "An unexpected error occurred."
}
```

### Response Field Reference

| Field | Type | Description |
|-------|------|-------------|
| `lastUpdated` | string | ISO 8601 datetime from external API's `dataAtualizacao` |
| `groups` | array | Line groups: "Metrô" (Lines 1-5, 15) and "Trens" (Lines 7-13) |
| `groups[].name` | string | Group display name |
| `groups[].lines` | array | Lines in this group, sorted by number ascending |
| `lines[].uid` | string | Unique line identifier from external API |
| `lines[].number` | integer | Line number |
| `lines[].name` | string | Line color name (e.g., "Azul") |
| `lines[].operator` | string | Operating concessão name |
| `lines[].colorHex` | string | Hex color code (e.g., "#171796") |
| `lines[].status.code` | string | Machine-readable status code |
| `lines[].status.label` | string | Human-readable status in Portuguese |
| `lines[].status.description` | string|null | Disruption details; null when normal |
| `lines[].category` | string | "metro" or "trens" |

## External API Consumed

**Endpoint**: `GET https://webapi.grupoccr.com.br/v1/mobility/public/line-status/current/state/SP`
**Authentication**: None
**Rate Limiting**: Unknown — backend caches responses (30s TTL) to
minimize calls.
