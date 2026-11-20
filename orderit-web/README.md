# OrderIt Web — Modern F&B Ordering & Kitchen Management

A complete, self-contained Node.js (v24) + TypeScript + SQLite web application and REST/SSE backend built from the concepts, database schemas, menu data, and design assets of [`LeeU1911/OrderIt`](https://github.com/leeu1911/orderit).

## Quick Start

```bash
cd orderit-web
npm install
npm run dev       # Development with auto-reload (http://localhost:3001)
# or production build:
npm run build && node dist/server.js
```

## Demo Accounts (1-Click Switch in UI)

| Role | Username | Password | Workspace |
| :--- | :--- | :--- | :--- |
| **Waiter / POS** | `staff1` | `password` | Floor tables, status toggles, fast menu ordering, billing & checkout |
| **Kitchen / Bar (KDS)** | `kitchen1` | `password` | Strict FIFO queue, live prep SLA countdowns, station filters, item-level completion |
| **Manager** | `manager1` | `password` | Live revenue/KPIs, top-ranking items, menu CRUD & 86'ing, table management |
| **Customer (QR)** | *(no login needed)* | — | Self-order per table with live preparation ETA tracking |

## Key Features
- **Strict First-Come, First-Served (FIFO) Kitchen Queue**: Orders appear in exact arrival order with elapsed/remaining prep timers and an explicit override guard (`409 fifoViolation`) if a chef attempts to skip an earlier queued ticket.
- **Real-Time Sync via SSE (`/api/events`)**: Replaces the legacy broken Firebase 3.6.9 dependency with zero-config Server-Sent Events across Waiter, Kitchen, Customer, and Manager views.
- **Zero External Database Setup**: Uses Node 24's built-in `node:sqlite` (`data/orderit.db`) pre-seeded with the authentic 60+ items from `sampleData.sql` and the classic items from the design mockups.
- **Live Billing & Checkout**: Tax %, discount %, item prep countdowns, and 2-tap "Finish & Pay" table reset.
- **Mobile-First & Bilingual (`🇻🇳 VI` / `🇬🇧 EN`)**: Thumb-friendly bottom navigation, master-detail table workflow, slide-up cart drawer, and instant 1-tap Vietnamese/English language switching.

## Verification

```bash
npm run typecheck
npm run build
npm test
```
