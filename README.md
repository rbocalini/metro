# Status Metrô SP

Aplicação web que exibe em tempo real a situação operacional de todas as linhas de metrô e trem de São Paulo.

Os dados são obtidos da API pública do Grupo CCR e apresentados em um dashboard interativo com atualização automática a cada 60 segundos.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-green)
![Svelte](https://img.shields.io/badge/SvelteKit-latest-red)
![License](https://img.shields.io/badge/license-MIT-blue)

## Funcionalidades

- Visualização em tempo real do status de **13 linhas** de metrô e trem
- Linhas agrupadas em **Metrô** (Linhas 1-5, 15) e **Trens** (Linhas 7-13)
- Cards coloridos com a cor oficial de cada linha
- Banner de resumo com percentual operacional da malha
- Atualização automática a cada 60 segundos
- Botão de atualização manual
- Exibição de detalhes quando há interrupções ou velocidade reduzida
- Tratamento de erros com aviso de dados desatualizados

## Operadoras cobertas

| Operadora | Linhas |
|-----------|--------|
| Metrô SP | 1-Azul, 2-Verde, 3-Vermelha, 15-Prata |
| ViaQuatro | 4-Amarela |
| ViaMobilidade | 5-Lilás, 8-Diamante, 9-Esmeralda |
| CPTM | 10-Turquesa, 11-Coral, 12-Safira, 13-Jade |
| TIC Trens | 7-Rubi |

## Stack

| Camada | Tecnologia |
|--------|-----------|
| Backend | Java 21, Spring Boot 4.0.3, Maven Wrapper |
| Cache | Caffeine (in-memory, TTL 30s) |
| Frontend | SvelteKit, TypeScript, Tailwind CSS |
| Documentação API | Swagger / OpenAPI (springdoc) |
| Testes unitários | jUnit (10 testes) |
| Testes e2e | Playwright + TypeScript (11 testes) |

## Estrutura do projeto

```
metro/
├── backend/          # API REST (Spring Boot) — proxy da API do Grupo CCR
├── frontend/         # Dashboard web (SvelteKit + Tailwind CSS)
├── playwright/       # Testes e2e (Playwright + TypeScript)
├── user_stories/     # Histórias de usuário
├── prototypes/       # Protótipos HTML (Google Stitch)
└── specs/            # Especificações e plano de implementação
```

## Pré-requisitos

- **Java 21** (JDK)
- **Node.js 18+**
- **npm** (ou Bun)

## Instalação e execução

### 1. Clone o repositório

```bash
git clone https://github.com/rbocalini/metro.git
cd metro
git checkout 001-line-status-dashboard
```

### 2. Inicie o backend

```bash
cd backend
./mvnw spring-boot:run
```

> No Windows, use `mvnw.cmd spring-boot:run`

O backend inicia em **http://localhost:8081**. Para verificar:

```bash
curl http://localhost:8081/api/line-status
```

### 3. Inicie o frontend

Em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

O frontend inicia em **http://localhost:5173**. Abra no navegador para ver o dashboard.

### 4. Acesse a documentação da API

Com o backend rodando, acesse:

```
http://localhost:8081/swagger-ui.html
```

## Rodando os testes

### Testes unitários (backend)

```bash
cd backend
./mvnw test
```

### Testes e2e (Playwright)

> O backend e o frontend **devem estar rodando** antes de executar os testes.

```bash
cd playwright
npm install
npx playwright install
npx playwright test
```

Para rodar com interface visual:

```bash
npx playwright test --ui
```

## API

### `GET /api/line-status`

Retorna o status de todas as linhas agrupadas em Metrô e Trens.

**Exemplo de resposta:**

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
        }
      ]
    },
    {
      "name": "Trens",
      "lines": [...]
    }
  ]
}
```

### Códigos de status

| Código | Significado | Cor no dashboard |
|--------|------------|-----------------|
| `OperacaoNormal` | Operação Normal | Verde |
| `VelocidadeReduzida` | Velocidade Reduzida | Amarelo |
| `OperacaoParcial` | Operação Parcial | Laranja |
| `Paralisada` | Paralisada | Vermelho |
| `OperacaoEncerrada` | Operação Encerrada | Cinza |

## Fonte de dados

Os dados são obtidos da API pública do Grupo CCR:

```
GET https://webapi.grupoccr.com.br/v1/mobility/public/line-status/current/state/SP
```

Esta API não requer autenticação e retorna o status atualizado de todas as concessionárias de transporte sobre trilhos do estado de São Paulo.

## Licença

Este projeto é de uso educacional e pessoal.
