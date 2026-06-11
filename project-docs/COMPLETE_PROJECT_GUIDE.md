# Atlas Journey — Complete Technical Guide

> A comprehensive reference for DAA Viva, Architecture Questions, Code Walkthrough, and Faculty Review.

---

## Table of Contents

1. [Project Overview](#section-1--project-overview)
2. [Complete Folder Structure](#section-2--complete-folder-structure)
3. [Java Architecture](#section-3--java-architecture)
4. [Database Architecture](#section-4--database-architecture)
5. [Complete Request Flow](#section-5--complete-request-flow)
6. [Dijkstra Deep Dive](#section-6--dijkstra-deep-dive)
7. [0-1 Knapsack Deep Dive](#section-7--0-1-knapsack-deep-dive)
8. [Heap Sort Deep Dive](#section-8--heap-sort-deep-dive)
9. [Map System](#section-9--map-system)
10. [Frontend Deep Dive](#section-10--frontend-deep-dive)
11. [Most Important Files](#section-11--most-important-files)
12. [100 Likely Viva Questions](#section-12--100-likely-viva-questions)
13. [5-Minute Rapid Revision Sheet](#section-13--5-minute-rapid-revision-sheet)

---

# SECTION 1 — PROJECT OVERVIEW

## What is Atlas Journey?

Atlas Journey is a **trip planner application** built as a DAA (Design and Analysis of Algorithms) college project. It helps users plan visits to attractions in a fictional city called "Atlas City" by applying three classic algorithms:

| Algorithm | Purpose | Real-World Use |
|-----------|---------|----------------|
| **0-1 Knapsack** | Budget optimization | Maximize attraction value within a spending budget |
| **Dijkstra's** | Shortest path | Find the shortest route between two locations |
| **Heap Sort** | Ranking | Rank attractions by composite score (rating × 10 + value) |

## Problem Statement

Tourists visiting a city with limited time and budget need to:
1. **Prioritize** which attractions to visit (maximize value per dollar)
2. **Navigate** efficiently between locations (minimize travel distance)
3. **Compare** attractions objectively (data-driven ranking)

## Objectives

- Implement 3 core DAA algorithms in a real-world context
- Provide both a **desktop** (Java Swing) and **web** (HTTP server + SPA) interface
- Use a **SQLite database** for persistent storage
- Render an **interactive map** showing routes and attractions
- Demonstrate algorithm complexity trade-offs

## Technology Stack

| Layer | Technology | Details |
|-------|-----------|---------|
| Desktop UI | Java Swing | Custom painting, CardLayout, AnimatedPanel |
| Web Server | `com.sun.net.httpserver` | Built-in JDK HTTP server, port 8080 |
| Web Frontend | Vanilla HTML/CSS/JS | No frameworks, SPA architecture |
| Database | SQLite 3.42 | Via `sqlite-jdbc-3.42.0.0.jar` |
| Build | `javac` | Manual compilation, Eclipse project metadata |
| Map Rendering | Java2D (desktop) / SVG (web) | Custom coordinate system, normalized 0–1 |
| Fonts | DM Sans, DM Mono | Web: Google Fonts import; Desktop: system fallback |

## Features

1. **Login** — Demo authentication (admin/admin or any username)
2. **Dashboard** — Overview stats, mini map, quick actions
3. **Smart Itinerary (Budget Planner)** — 0-1 Knapsack optimization
4. **Route Planner** — Dijkstra shortest path with map visualization
5. **Rankings (Discover)** — Heap Sort attraction rankings
6. **Interactive Map** — SVG map with zoom, pan, route rendering
7. **Settings** — App info, appearance toggles (disabled)

---

# SECTION 2 — COMPLETE FOLDER STRUCTURE

```
Trip-Planner/
├── .classpath                          # Eclipse classpath (src/ → bin/)
├── .project                            # Eclipse project descriptor
├── README.md                           # Project documentation
├── design-system.json                  # Design tokens & component specs
├── trip.db                             # SQLite database (root copy)
│
├── src/                                # DESKTOP APP (Java Swing)
│   ├── app/
│   │   └── Main.java                   # Entry point: launches LoginPanel
│   ├── algorithms/
│   │   ├── DijkstraService.java        # Dijkstra shortest path
│   │   ├── HeapSortService.java        # Heap Sort rankings
│   │   └── KnapsackService.java        # 0-1 Knapsack DP
│   ├── db/
│   │   ├── DBConnection.java           # Singleton SQLite connection
│   │   ├── SchemaInit.java             # CREATE TABLE statements
│   │   └── SeedData.java              # 12 attractions, 68 edges, 2 users
│   ├── model/
│   │   ├── Attraction.java             # POJO: id, name, cost, value, rating, location, description
│   │   ├── Edge.java                   # POJO: id, fromNode, toNode, weight
│   │   ├── KnapsackResult.java         # Selected items + totals
│   │   └── PathResult.java             # Path list + distance
│   └── ui/
│       ├── Theme.java                  # Colors, fonts, drawing utilities
│       ├── LoginPanel.java             # Login screen (JFrame)
│       ├── DashboardFrame.java         # Main window with CardLayout
│       ├── DashboardHomePanel.java     # Home dashboard
│       ├── BudgetPlannerPanel.java     # Knapsack UI
│       ├── RoutePlannerPanel.java      # Dijkstra UI + MapPanel
│       ├── RankingsPanel.java          # Heap Sort rankings list
│       ├── TripSummaryPanel.java       # Algorithm summary cards
│       ├── SettingsPanel.java          # Settings/about page
│       └── components/
│           ├── AnimatedPanel.java      # Fade+slide animation base class
│           ├── ContentHeader.java      # Page title + subtitle
│           ├── GradientButton.java     # Styled JButton
│           ├── HeaderPanel.java        # Top bar with search + user
│           ├── MapPanel.java           # Java2D custom map renderer
│           ├── RoundedCardPanel.java   # Card with shadow + rounded corners
│           ├── SidebarButton.java      # Navigation button
│           ├── SidebarPanel.java       # Left sidebar with nav items
│           ├── StatCard.java           # Dashboard stat card
│           └── SummaryCard.java        # Summary display card
│
├── bin/                                # Compiled desktop .class files
│
├── lib/
│   └── sqlite-jdbc-3.42.0.0.jar       # SQLite JDBC driver
│
├── project-docs/                       # Documentation website
│   ├── index.html                      # Interactive documentation
│   ├── styles.css                      # Editorial-style CSS
│   └── script.js                       # Accordion, progress, navigation
│
└── webapp/                             # WEB APPLICATION
    ├── src/                            # Java HTTP server (default package)
    │   ├── TripPlannerApp.java         # Main server + API route handlers
    │   ├── DatabaseManager.java        # Singleton SQLite connection
    │   ├── SeedData.java              # 12 attractions, 62 edges, 3 users
    │   ├── Attraction.java             # POJO with toJson()
    │   ├── Edge.java                   # POJO with toJson()
    │   ├── PathResult.java             # Path + toJson()
    │   ├── KnapsackResult.java         # Selected + skipped + toJson()
    │   ├── DijkstraService.java        # Dijkstra (rebuilds graph per call)
    │   ├── HeapSortService.java        # Heap Sort rankings
    │   └── KnapsackService.java        # 0-1 Knapsack DP
    ├── bin/                            # Compiled web .class files
    ├── trip.db                         # Web-specific SQLite database
    └── frontend/                       # SPA frontend
        ├── index.html                  # Full HTML (538 lines)
        ├── styles.css                  # Complete CSS (1165 lines)
        └── app.js                      # All JS logic (1756 lines)
```

### Purpose of Each Directory

| Directory | Purpose |
|-----------|---------|
| `src/` | Desktop application source — Java Swing GUI |
| `src/app/` | Application entry point |
| `src/algorithms/` | Core DAA algorithm implementations |
| `src/db/` | Database connection, schema, seed data |
| `src/model/` | Data model POJOs (Attraction, Edge, results) |
| `src/ui/` | Swing UI panels and windows |
| `src/ui/components/` | Reusable UI components (buttons, cards, map) |
| `webapp/src/` | Web server source — HTTP handlers + algorithms |
| `webapp/frontend/` | Browser SPA — HTML + CSS + JavaScript |
| `lib/` | External JARs (SQLite JDBC driver) |
| `project-docs/` | Documentation website |
| `bin/` | Compiled bytecode output |

---

# SECTION 3 — JAVA ARCHITECTURE

## Desktop App Files

### `Main.java` — Entry Point
- **Package:** `app`
- **Purpose:** Application launcher
- **Methods:**
  - `main(String[] args)` — Sets system Look-and-Feel, initializes database, launches `LoginPanel` on Swing EDT
  - `initDB()` — Gets connection, creates schema, seeds data
- **Flow:** `main` → `initDB()` → `SchemaInit.initSchema()` → `SeedData.seedIfNeeded()` → `LoginPanel()`

### `DijkstraService.java` — Shortest Path
- **Package:** `algorithms`
- **Purpose:** Implements Dijkstra's algorithm on a weighted directed graph
- **Fields:**
  - `Map<String, List<Edge>> graph` — Adjacency list
  - `Set<String> nodes` — All node names
- **Methods:**
  - `DijkstraService()` — Constructor calls `loadGraph()`
  - `loadGraph()` — Queries `SELECT * FROM edges`, populates `graph` and `nodes`
  - `getAllNodes()` — Returns copy of `nodes` set
  - `findShortestPath(String start, String end)` — **Core Dijkstra:** Initializes distances (MAX_VALUE except start=0), uses PriorityQueue, relaxes edges, reconstructs path via prev map
- **Returns:** `PathResult` (path list, total distance, found flag)

### `HeapSortService.java` — Rankings
- **Package:** `algorithms`
- **Purpose:** Sorts attractions by composite score using heap sort
- **Methods:**
  - `getAllAttractions()` — Queries all attractions from DB
  - `sortByScore(List<Attraction> items)` — **Heap sort:** builds min-heap, extracts to end, produces descending order
  - `compare(Attraction a, Attraction b)` — Natural ascending comparator: `Integer.compare(a.getScore(), b.getScore())`
  - `heapify(Attraction[] arr, int n, int i)` — Min-heapify: swaps with smallest child when `compare() < 0`
  - `getSortedFromDB()` — Convenience: `sortByScore(getAllAttractions())`
- **Scoring:** `getScore() = rating * 10 + value`

### `KnapsackService.java` — Budget Optimization
- **Package:** `algorithms`
- **Purpose:** Solves 0-1 Knapsack problem via dynamic programming
- **Methods:**
  - `getAllAttractions()` — Queries all attractions
  - `solveKnapsack(List<Attraction> items, int budget)` — **DP:** Builds `dp[n+1][W+1]` table, backtracks to find selected items
  - `solveKnapsackFromDB(int budget)` — Convenience
- **Returns:** `KnapsackResult` (selected items, total value, total cost, remaining budget)

### `DBConnection.java` — Database Connection
- **Package:** `db`
- **Purpose:** Singleton SQLite connection manager
- **Fields:**
  - `URL = "jdbc:sqlite:trip.db"` — Connection string
  - `conn` — Singleton connection instance
- **Methods:**
  - `getConnection()` — Returns existing connection or creates new one

### `SchemaInit.java` — Schema Creation
- **Package:** `db`
- **Purpose:** Creates database tables
- **Methods:**
  - `initSchema()` — Creates 3 tables IF NOT EXISTS:
    - `attractions` (id, name, cost, value, rating, location, description)
    - `edges` (id, fromNode, toNode, weight)
    - `users` (id, username, password)

### `SeedData.java` — Sample Data
- **Package:** `db`
- **Purpose:** Populates empty tables with demo data
- **Methods:**
  - `seedIfNeeded()` — Checks if tables empty, seeds if so
  - `seedAttractions()` — Inserts 12 attractions
  - `seedEdges()` — Inserts ~68 directed weighted edges
  - `seedUsers()` — Inserts 2 users (admin/demo)

### `Attraction.java` — Data Model
- **Package:** `model`
- **Purpose:** Represents a tourist attraction
- **Fields:** `id`, `name`, `cost`, `value`, `rating`, `location`, `description`
- **Key method:** `getScore()` → `rating * 10 + value`

### `Edge.java` — Graph Edge
- **Package:** `model`
- **Purpose:** Represents a directed weighted edge between two locations
- **Fields:** `id`, `fromNode`, `toNode`, `weight`

### `PathResult.java` — Dijkstra Result
- **Package:** `model`
- **Purpose:** Encapsulates shortest path result
- **Fields:** `path` (List<String>), `dist` (int), `start`, `end`, `found` (boolean)
- **Methods:** `getFormattedPath()` → joins path with " → "

### `KnapsackResult.java` — Knapsack Result
- **Package:** `model`
- **Purpose:** Encapsulates budget optimization result
- **Fields:** `items` (List<Attraction>), `totalValue`, `totalCost`, `budget`
- **Methods:** `getRemainingBudget()` → `budget - totalCost`

### `DashboardFrame.java` — Main Window
- **Package:** `ui`
- **Purpose:** Main JFrame with sidebar + content area
- **Fields:** `sidebar`, `header`, `contentArea`, `cardLayout`
- **Constructor:** Creates 1280×820 window, adds 5 panels to CardLayout (home, trip, route, rankings, settings)
- **Methods:** `showPanel(String name, String title)` — switches visible panel

### `LoginPanel.java` — Login Screen
- **Package:** `ui`
- **Purpose:** Login JFrame with username/password fields
- **Inner class:** `RoundedBorder` — custom rounded border
- **Behavior:** On login, creates `DashboardFrame` and disposes login window

### `DashboardHomePanel.java` — Home Dashboard
- **Package:** `ui` (extends `AnimatedPanel`)
- **Purpose:** Overview dashboard with stats, map preview, quick actions
- **Key methods:**
  - `createHeroCard()` — Custom-painted gradient hero with welcome text
  - `createStatsRow()` — 4 stat cards (Attractions: 12, Locations: 11, Algorithms: 3, Offline: 100%)
  - `createBottomRow()` — Map preview + quick action buttons

### `BudgetPlannerPanel.java` — Knapsack UI
- **Package:** `ui` (extends `AnimatedPanel`)
- **Purpose:** Budget input + knapsack results display
- **Fields:** `KnapsackService service`, `JTextField budgetField`, stat labels, `resultList`
- **Key method:** `performCalculation()` — Parses budget, calls `service.solveKnapsackFromDB(budget)`, updates UI

### `RoutePlannerPanel.java` — Dijkstra UI
- **Package:** `ui` (extends `AnimatedPanel`)
- **Purpose:** Start/end selection + map route display
- **Fields:** `DijkstraService service`, two `JComboBox<String>`, `MapPanel mapPanel`, result labels
- **Key method:** `findPath()` — Gets selected locations, calls `service.findShortestPath()`, updates labels + map

### `RankingsPanel.java` — Rankings Display
- **Package:** `ui` (extends `AnimatedPanel`)
- **Purpose:** Displays heap-sorted attractions in a leaderboard
- **Fields:** `HeapSortService service`, `listContainer`
- **Key method:** `loadRankings()` — Calls `service.getSortedFromDB()`, creates rank rows

### `Theme.java` — Design System
- **Package:** `ui`
- **Purpose:** Centralized color, font, and layout constants
- **Key constants:** PRIMARY (#2D6A4F), fonts (DM Sans), radii, padding values
- **Key methods:**
  - `hq(Graphics g)` — Returns antialiased Graphics2D
  - `fillRoundRect()` — Rounded rectangle drawing
  - `drawSoftShadow()` — Layered shadow effect
  - `blend()` — Color interpolation

### `MapPanel.java` — Java2D Map
- **Package:** `ui.components`
- **Purpose:** Custom-painted interactive map
- **Fields:** `nodePositions` (20 nodes), `attractions` (10 visible), `currentRoute`, `zoom`
- **Key methods:**
  - `paintComponent()` — Master paint pipeline (10 draw methods)
  - `setRoute()` / `clearRoute()` — Route management
  - `zoomIn()` / `zoomOut()` — Zoom control
  - Drawing methods: `drawMapBackground`, `drawGridLines`, `drawWaterFeatures`, `drawDistrictOverlays`, `drawRoads`, `drawJunctions`, `drawRoute`, `drawMarkers`, `drawDistrictLabels`, `drawZoomIndicator`

### Reusable UI Components

| Component | Purpose |
|-----------|---------|
| `AnimatedPanel` | Base class with fade-in + slide animation (alpha 0→1, slideOffset decays) |
| `ContentHeader` | Page title + subtitle header |
| `GradientButton` | Styled JButton with gradient, hover/press states |
| `HeaderPanel` | Top bar with page title, search field, user chip |
| `RoundedCardPanel` | JPanel with rounded corners + soft shadow |
| `SidebarButton` | Navigation button with icon + label + hover animation |
| `SidebarPanel` | Left sidebar with brand, nav items, active indicator |
| `StatCard` | Dashboard stat card with icon, value, label |
| `SummaryCard` | Summary display card |

## Web App Files

### `TripPlannerApp.java` — HTTP Server
- **Purpose:** Embedded HTTP server with API endpoints
- **Port:** 8080
- **Endpoints:**
  - `POST /api/login` — Demo login
  - `GET /api/dashboard` — Stats (attraction count, location count, algorithm count)
  - `POST /api/knapsack` — Budget optimization
  - `POST /api/dijkstra` — Shortest path
  - `GET /api/rankings` — Heap-sorted attractions
  - `GET /api/locations` — All node names
  - `GET /api/edges` — All graph edges
  - `POST /api/reset` — Reset demo data
  - `POST /api/example-route` — Load example route
  - `POST /api/example-budget` — Load example budget
  - `GET /` — Static file server (SPA fallback to index.html)
- **Utilities:** `sendJson()`, `readBody()`, `esc()`, `extractJsonString()`, `extractJsonInt()`

### `DatabaseManager.java` — Web DB Connection
- **Purpose:** Thread-safe singleton SQLite connection
- **DB URL:** `jdbc:sqlite:webapp/trip.db`
- **Key difference from desktop:** Uses `synchronized` on `getConnection()`

### `SeedData.java` — Web Seed Data
- **Differences from desktop:** 3 users (adds traveler/traveler), 62 edges (vs 68), includes `resetData()` method

### Model Classes (Web)
- All in default package
- **Key difference:** Public fields (no getters/setters), manual `toJson()` / `toJsonArray()` methods
- `KnapsackResult` has extra `skippedItems` field

---

# SECTION 4 — DATABASE ARCHITECTURE

## Database Engine
- **SQLite 3.42** via JDBC driver (`sqlite-jdbc-3.42.0.0.jar`)
- **Desktop:** `trip.db` in project root
- **Web:** `webapp/trip.db`

## Schema (3 Tables)

### Table: `attractions`

```sql
CREATE TABLE IF NOT EXISTS attractions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    cost INTEGER NOT NULL,
    value INTEGER NOT NULL,
    rating INTEGER NOT NULL,
    location TEXT NOT NULL,
    description TEXT NOT NULL
);
```

| Column | Type | Description | Example |
|--------|------|-------------|---------|
| id | INTEGER | Primary key | 1 |
| name | TEXT | Attraction name | "Grand Museum" |
| cost | INTEGER | Visit cost (dollars) | 150 |
| value | INTEGER | Experience value (0–100) | 95 |
| rating | INTEGER | Star rating (1–5) | 5 |
| location | TEXT | District name | "Museum District" |
| description | TEXT | Short description | "World-class art collection" |

**Sample Records (12 attractions):**

| # | Name | Cost | Value | Rating | Location | Score (R×10+V) |
|---|------|------|-------|--------|----------|-----------------|
| 1 | Grand Museum | 150 | 95 | 5 | Museum District | 145 |
| 2 | Central Gardens | 50 | 80 | 4 | Central Park | 120 |
| 3 | Harbor Walk | 30 | 65 | 4 | Golden Coast | 105 |
| 4 | Heritage Walk | 80 | 90 | 5 | Heritage Quarter | 140 |
| 5 | Sunset Peak | 25 | 70 | 4 | Sunset Point | 110 |
| 6 | Innovation Hub | 100 | 85 | 4 | Innovation District | 125 |
| 7 | Market Bazaar | 40 | 60 | 3 | Market Square | 90 |
| 8 | Lake Cruise | 90 | 75 | 4 | Lake Gardens | 115 |
| 9 | Old Town Alley | 60 | 70 | 4 | Old Town | 110 |
| 10 | City View Deck | 70 | 78 | 4 | City Center | 118 |
| 11 | Science Museum | 180 | 92 | 5 | Innovation District | 142 |
| 12 | Sunset Beach | 20 | 50 | 3 | Golden Coast | 80 |

### Table: `edges`

```sql
CREATE TABLE IF NOT EXISTS edges (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fromNode TEXT NOT NULL,
    toNode TEXT NOT NULL,
    weight INTEGER NOT NULL
);
```

| Column | Type | Description | Example |
|--------|------|-------------|---------|
| id | INTEGER | Primary key | 1 |
| fromNode | TEXT | Source node name | "City Center" |
| toNode | TEXT | Destination node name | "Museum District" |
| weight | INTEGER | Edge weight (distance) | 5 |

**Graph Nodes (20 total):**

| Category | Nodes |
|----------|-------|
| **Visible Attractions (10)** | City Center, Museum District, Heritage Quarter, Market Square, Central Park, Innovation District, Old Town, Lake Gardens, Golden Coast, Sunset Point |
| **Hidden Junctions (10)** | Main Junction, North Gate, East Crossing, South Bridge, West End, Park Entrance, Museum Circle, Heritage Cross, Market Hub, Old Gate |

### Table: `users`

```sql
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    password TEXT NOT NULL
);
```

| Desktop Users | Web Users |
|---------------|-----------|
| admin / admin123 | admin / admin |
| demo / demo | demo / demo |
| — | traveler / traveler |

## Which Java Classes Use Each Table

| Table | Desktop Classes | Web Classes |
|-------|----------------|-------------|
| `attractions` | `HeapSortService`, `KnapsackService` | `HeapSortService`, `KnapsackService` |
| `edges` | `DijkstraService` | `DijkstraService` |
| `users` | `LoginPanel` (not validated) | `TripPlannerApp` (not validated) |

---

# SECTION 5 — COMPLETE REQUEST FLOW

## Flow 1: Route Planning (Dijkstra)

```
User selects Start + End → clicks "Find Route"
        ↓
[Desktop] RoutePlannerPanel.findPath()
         → DijkstraService.findShortestPath(start, end)
         → Returns PathResult
         → Updates labels (distance, path, status)
         → MapPanel.setRoute(path)

[Web]     findRoute() in app.js
         → API.findRoute(start, end)  → POST /api/dijkstra
         → TripPlannerApp.handleDijkstra()
         → DijkstraService.findShortestPath(start, end)
         → Returns PathResult.toJson()
         → displayRouteResult(result)
         → routeMap.setRoute(result.path)
```

**Database query:** `SELECT * FROM edges` (loads full graph)

**Algorithm:** Dijkstra's shortest path on weighted directed graph

**UI update:** Map renders route path with animated blue line, Start/End markers

## Flow 2: Budget Planning (Knapsack)

```
User enters Budget → clicks "Calculate"
        ↓
[Desktop] BudgetPlannerPanel.performCalculation()
         → KnapsackService.solveKnapsackFromDB(budget)
         → Returns KnapsackResult
         → Updates stat labels (cost, value, remaining, count)
         → Populates resultList with attraction cards

[Web]     solveKnapsack(budget) in app.js
         → API.solveKnapsack(budget)  → POST /api/knapsack
         → TripPlannerApp.handleKnapsack()
         → KnapsackService.solveFromDB(budget)
         → Returns KnapsackResult.toJson()
         → displaySmartItinerary(result)
         → Updates summary chips, health bar, experience cards
```

**Database query:** `SELECT * FROM attractions`

**Algorithm:** 0-1 Knapsack DP with backtracking

## Flow 3: Rankings (Heap Sort)

```
User opens Rankings page → clicks "Refresh"
        ↓
[Desktop] RankingsPanel.loadRankings()
         → HeapSortService.getSortedFromDB()
         → Returns sorted List<Attraction>
         → Populates listContainer with rank rows

[Web]     fetchAndDisplayDiscover() in app.js
         → API.getRankings()  → GET /api/rankings
         → TripPlannerApp.handleRankings()
         → HeapSortService.getSortedFromDB()
         → Returns Attraction.toJsonArray(sorted)
         → _renderDiscover(rankings)
         → Renders featured, top3, full list
```

**Database query:** `SELECT * FROM attractions`

**Algorithm:** Heap Sort (min-heap → descending output)

## Flow 4: Map Rendering

```
[Desktop] MapPanel.paintComponent()
         → drawMapBackground()     — gradient fill
         → drawGridLines()         — 50px grid
         → drawWaterFeatures()     — lake, river, pond
         → drawDistrictOverlays()  — 10 colored polygons
         → drawRoads()             — 33 road pairs as lines
         → drawJunctions()         — small dots for junctions
         → drawRoute()             — triple-pass blue line + animated dots
         → drawMarkers()           — pin markers for attractions
         → drawDistrictLabels()    — name labels for 10 districts
         → drawZoomIndicator()     — zoom percentage

[Web]     AtlasCityMap._render()
         → Layer 1: Base (background + grid)
         → Layer 2: Water (lake, river, pond)
         → Layer 3: Parks (green ellipses + tree dots)
         → Layer 4: Districts (colored polygon overlays)
         → Layer 5: District labels
         → Layer 6: Roads (gray edge lines)
         → Layer 7: Intersection labels (visible only)
         → Layer 8: Route (glow + stroke + markers)
         → Layer 9: Pins (district markers with hover)
```

## Flow 5: Dashboard

```
User logs in → Dashboard loads
        ↓
[Web] loadDashboard()
     → API.getDashboard()  → GET /api/dashboard
     → Returns {attractionCount, locationCount, algorithmCount, ...}
     → Creates mini AtlasCityMap for dashboard
     → Populates stat cards, upcoming journey, snapshots
```

---

# SECTION 6 — DIJKSTRA DEEP DIVE

## Implementation Location

- **Desktop:** `src/algorithms/DijkstraService.java` (28 lines)
- **Web:** `webapp/src/DijkstraService.java` (94 lines)

## Class Structure

```java
public class DijkstraService {
    private Map<String, List<Edge>> graph;  // Adjacency list
    private Set<String> nodes;              // All node names
}
```

## Data Structures

| Structure | Type | Purpose |
|-----------|------|---------|
| `graph` | `Map<String, List<Edge>>` | Adjacency list: node → list of edges |
| `nodes` | `Set<String>` | All unique node names |
| `dist` | `Map<String, Integer>` | Shortest distance from source to each node |
| `prev` | `Map<String, String>` | Previous node in shortest path |
| `visited` | `Set<String>` | Already processed nodes |
| `pq` | `PriorityQueue<String>` | Min-heap ordered by distance |

## Graph Representation

The graph is an **adjacency list** built from the `edges` table:

```
"City Center" → [Edge("Museum District", 5), Edge("Main Junction", 3), ...]
"Museum District" → [Edge("City Center", 5), Edge("Museum Circle", 2), ...]
```

Each `Edge` has: `fromNode`, `toNode`, `weight`

## Line-by-Line Execution (Desktop)

```java
// DijkstraService.java lines 26-58

public PathResult findShortestPath(String start, String end) {
    if (!nodes.contains(start) || !nodes.contains(end))
        return new PathResult(..., false);                    // Validate nodes exist
    if (start.equals(end))
        return new PathResult(List.of(start), 0, ...);       // Same start/end

    Map<String, Integer> dist = new HashMap<>();              // Distance map
    Map<String, String> prev = new HashMap<>();               // Predecessor map
    Set<String> visited = new HashSet<>();                    // Visited set
    for (String node : nodes) dist.put(node, Integer.MAX_VALUE); // Init all to ∞
    dist.put(start, 0);                                       // Source = 0

    PriorityQueue<String> pq = new PriorityQueue<>(           // Min-heap by distance
        Comparator.comparingInt(dist::get));
    pq.offer(start);                                          // Add source

    while (!pq.isEmpty()) {                                   // Main loop
        String u = pq.poll();                                 // Get min-distance node
        if (visited.contains(u)) continue;                    // Skip if visited
        visited.add(u);                                       // Mark visited

        if (u.equals(end)) break;                             // Early exit at target

        for (Edge edge : graph.getOrDefault(u, List.of())) {  // Relax neighbors
            String v = edge.toNode;
            int newDist = dist.get(u) + edge.weight;          // Calculate new distance
            if (newDist < dist.get(v)) {                      // If shorter path found
                dist.put(v, newDist);                         // Update distance
                prev.put(v, u);                               // Update predecessor
                pq.offer(v);                                  // Add to priority queue
            }
        }
    }

    // Path reconstruction
    List<String> path = new ArrayList<>();
    String curr = end;
    while (curr != null) {                                    // Backtrack from end
        path.add(0, curr);                                    // Prepend to path
        curr = prev.get(curr);                                // Move to predecessor
    }
    boolean found = path.get(0).equals(start);                // Verify path connects
    return new PathResult(path, dist.get(end), start, end, found);
}
```

## Pseudocode

```
DIJKSTRA(graph, source, target):
    dist[source] = 0
    dist[all others] = ∞
    pq = priority queue ordered by dist
    pq.insert(source)

    while pq is not empty:
        u = pq.extract_min()
        if u == target: break
        for each edge (u, v, weight):
            if dist[u] + weight < dist[v]:
                dist[v] = dist[u] + weight
                prev[v] = u
                pq.insert(v)

    path = backtrack from target using prev[]
    return path, dist[target]
```

## Complexity

| Metric | Value | Explanation |
|--------|-------|-------------|
| **Time** | O((V + E) log V) | Each vertex extracted once (log V each), each edge relaxed once (log V each) |
| **Space** | O(V) | Distance map, visited set, priority queue |

Where V = vertices (20 nodes), E = edges (~68 directed edges)

## Why Dijkstra?

- **Weighted graph:** Edges have different distances — BFS wouldn't work
- **Non-negative weights:** All edge weights are positive integers
- **Optimal substructure:** Shortest path to any node is composed of shortest paths to intermediate nodes
- **Greedy choice:** Always process the closest unvisited node first

---

# SECTION 7 — 0-1 KNAPSACK DEEP DIVE

## Implementation Location

- **Desktop:** `src/algorithms/KnapsackService.java` (40 lines)
- **Web:** `webapp/src/KnapsackService.java` (75 lines)

## Class Structure

```java
public class KnapsackService {
    // No fields — stateless service
}
```

## DP Table Structure

```
dp[i][w] = maximum value using items[0..i] with capacity w
```

- Rows: 0 to n (number of items)
- Columns: 0 to W (budget capacity)
- Size: (n+1) × (W+1) = 13 × 501 = 6,513 cells

## Line-by-Line Execution (Desktop)

```java
// KnapsackService.java lines 17-36

public KnapsackResult solveKnapsack(List<Attraction> items, int budget) {
    if (items.isEmpty() || budget <= 0)
        return new KnapsackResult(List.of(), 0, 0, budget);   // Edge case

    int n = items.size();
    int W = budget;
    int[][] dp = new int[n + 1][W + 1];                      // DP table

    // Fill DP table
    for (int i = 1; i <= n; i++) {                            // For each item
        Attraction a = items.get(i - 1);
        for (int w = 0; w <= W; w++) {                        // For each capacity
            dp[i][w] = dp[i - 1][w];                         // Exclude item
            if (a.cost <= w) {                                // If item fits
                dp[i][w] = Math.max(dp[i][w],                // Max of:
                    dp[i - 1][w],                            //   exclude
                    dp[i - 1][w - a.cost] + a.getScore());   //   include
            }
        }
    }

    // Backtrack to find selected items
    List<Attraction> selected = new ArrayList<>();
    int w = W;
    for (int i = n; i > 0; i--) {                            // From last item to first
        if (dp[i][w] != dp[i - 1][w]) {                     // If item was included
            selected.add(items.get(i - 1));                  // Add to selected
            w -= items.get(i - 1).cost;                      // Reduce capacity
        }
    }

    int totalCost = selected.stream().mapToInt(Attraction::getCost).sum();
    int totalValue = selected.stream().mapToInt(Attraction::getScore).sum();
    return new KnapsackResult(selected, totalValue, totalCost, budget);
}
```

## State Transition

```
dp[i][w] = max(
    dp[i-1][w],                           // Don't take item i
    dp[i-1][w - cost[i]] + score[i]       // Take item i (if cost[i] ≤ w)
)
```

## Selection Logic (Backtracking)

Starting from `dp[n][W]`:
1. If `dp[i][w] ≠ dp[i-1][w]` → item i was included
2. Subtract item i's cost from remaining capacity
3. Continue until i = 0

## Pseudocode

```
KNAPSACK(items, budget):
    n = number of items
    W = budget

    // Build DP table
    for i = 1 to n:
        for w = 0 to W:
            dp[i][w] = dp[i-1][w]
            if items[i].cost ≤ w:
                dp[i][w] = max(dp[i][w], dp[i-1][w-cost[i]] + score[i])

    // Backtrack
    selected = []
    w = W
    for i = n down to 1:
        if dp[i][w] ≠ dp[i-1][w]:
            selected.add(items[i])
            w -= items[i].cost

    return selected, dp[n][W]
```

## Complexity

| Metric | Value | Explanation |
|--------|-------|-------------|
| **Time** | O(n × W) | Fill DP table: n items × W capacity |
| **Space** | O(n × W) | DP table storage |

Where n = 12 items, W = budget (typically 500)

## Why 0-1 Knapsack?

- **Each attraction is visited once** (0-1 choice, not unbounded)
- **Budget is the constraint** (capacity W)
- **Value is the objective** (maximize composite score)
- **Optimal substructure:** Optimal solution contains optimal solutions to subproblems

---

# SECTION 8 — HEAP SORT DEEP DIVE

## Implementation Location

- **Desktop:** `src/algorithms/HeapSortService.java` (47 lines)
- **Web:** `webapp/src/HeapSortService.java` (72 lines)

## Class Structure

```java
public class HeapSortService {
    // No fields — stateless service
}
```

## Scoring Formula

```java
// Attraction.java line 16
public int getScore() { return rating * 10 + value; }
```

Example: Grand Museum → rating=5, value=95 → score = 5×10 + 95 = 145

## Heap Creation

```java
// HeapSortService.java line 20
for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
```

- Starts from the last non-leaf node (index `n/2 - 1`)
- Works backwards to the root (index 0)
- Each call to `heapify` ensures the subtree rooted at `i` satisfies the heap property
- Result: a valid min-heap where root = minimum score

## Heapify (Min-Heap)

```java
// HeapSortService.java lines 36-44
private void heapify(Attraction[] arr, int n, int i) {
    int largest = i;                          // Parent index
    int l = 2 * i + 1;                       // Left child
    int r = 2 * i + 2;                       // Right child

    if (l < n && compare(arr[l], arr[largest]) < 0) largest = l;   // If left < parent
    if (r < n && compare(arr[r], arr[largest]) < 0) largest = r;   // If right < smallest

    if (largest != i) {                       // If parent is not smallest
        swap(arr[i], arr[largest]);           // Swap parent with smallest child
        heapify(arr, n, largest);             // Recursively heapify affected subtree
    }
}
```

**Comparator (natural ascending):**
```java
private int compare(Attraction a, Attraction b) {
    int scoreCmp = Integer.compare(a.getScore(), b.getScore());
    if (scoreCmp != 0) return scoreCmp;
    return a.getName().compareTo(b.getName());  // Tie-break alphabetically
}
```

**Heap property:** `compare(child, parent) < 0` → child has lower score → swap child up
**Result:** Root = minimum score element

## Extraction

```java
// HeapSortService.java lines 21-24
for (int i = n - 1; i > 0; i--) {
    Attraction temp = arr[0]; arr[0] = arr[i]; arr[i] = temp;   // Swap root to end
    heapify(arr, i, 0);                                          // Restore heap on reduced heap
}
```

**Process:**
1. Swap root (minimum) to position `i` (end of unsorted portion)
2. Reduce heap size by 1
3. Heapify the root to restore min-heap property
4. Repeat until heap size = 1

**Result:** Array is sorted in **descending order** [highest, ..., lowest]

## Line-by-Line Execution Example

Input scores: [145, 120, 105, 140, 110, 125, 90, 115, 110, 118, 142, 80]

**Build min-heap:** Root becomes 80 (minimum)

**Extraction cycle:**
1. Swap 80 to end → [..., 80]. Heapify → root becomes 90
2. Swap 90 to end → [..., 90, 80]. Heapify → root becomes 105
3. ... continues until all extracted
4. Final: [145, 142, 140, 125, 120, 118, 115, 110, 110, 105, 90, 80]

## Pseudocode

```
HEAP_SORT(items):
    n = length(items)

    // Build min-heap
    for i = n/2 - 1 down to 0:
        MIN_HEAPIFY(items, n, i)

    // Extract elements
    for i = n - 1 down to 1:
        swap(items[0], items[i])
        MIN_HEAPIFY(items, i, 0)

    return items  // Descending order

MIN_HEAPIFY(arr, n, i):
    smallest = i
    left = 2*i + 1
    right = 2*i + 2

    if left < n AND arr[left] < arr[smallest]:
        smallest = left
    if right < n AND arr[right] < arr[smallest]:
        smallest = right

    if smallest ≠ i:
        swap(arr[i], arr[smallest])
        MIN_HEAPIFY(arr, n, smallest)
```

## Complexity

| Metric | Value | Explanation |
|--------|-------|-------------|
| **Time** | O(n log n) | Build heap: O(n), Extract n times × heapify: O(n log n) |
| **Space** | O(1) | In-place sort (no extra arrays) |

## Why Heap Sort?

- **In-place:** No additional array needed (unlike merge sort)
- **Guaranteed O(n log n):** No worst-case degradation (unlike quicksort)
- **Stable heap structure:** Natural fit for priority-based ranking
- **Demonstrates tree-based sorting:** Different paradigm from comparison-based sorts

---

# SECTION 9 — MAP SYSTEM

## Architecture Overview

The map system has **two implementations:**

| Aspect | Desktop (Java2D) | Web (SVG) |
|--------|-----------------|-----------|
| **File** | `MapPanel.java` (351 lines) | `app.js` → `AtlasCityMap` class (700+ lines) |
| **Rendering** | `Graphics2D` custom painting | SVG DOM manipulation |
| **Interaction** | Mouse wheel zoom | Wheel zoom + pan |
| **Route** | `List<String>` of node names | Array of intersection indices |

## Coordinate System

Both use **normalized coordinates (0–1):**

```java
// Desktop: MapPanel.java line 49
nodePositions.put("City Center", new float[]{0.50f, 0.45f});  // x=50%, y=45%

// Web: app.js line 102
{ id: 'city-center', name: 'City Center', x: 0.50, y: 0.45 }
```

**Conversion to screen/SVG coordinates:**
```java
// Desktop: MapPanel.java line 291
int x = (int)(pos[0] * w * zoom + offsetX);
int y = (int)(pos[1] * h * zoom + offsetY);

// Web: app.js line 387
_toSVG(x, y) { return { x: x * 1000, y: y * 800 } }
```

## Node Definitions (20 nodes)

**10 Visible Attractions (districts):**

| Name | Position (x, y) | District Color |
|------|-----------------|----------------|
| City Center | (0.50, 0.45) | Blue |
| Museum District | (0.30, 0.28) | Purple |
| Heritage Quarter | (0.65, 0.55) | Amber |
| Market Square | (0.40, 0.60) | Green |
| Central Park | (0.50, 0.25) | Green |
| Innovation District | (0.22, 0.48) | Pink |
| Old Town | (0.60, 0.70) | Amber |
| Lake Gardens | (0.75, 0.42) | Blue |
| Golden Coast | (0.82, 0.80) | Amber |
| Sunset Point | (0.18, 0.68) | Orange |

**10 Hidden Junctions (internal routing):**

| Name | Position (x, y) |
|------|-----------------|
| Main Junction | (0.50, 0.50) |
| North Gate | (0.50, 0.12) |
| East Crossing | (0.85, 0.50) |
| South Bridge | (0.50, 0.85) |
| West End | (0.15, 0.50) |
| Park Entrance | (0.50, 0.33) |
| Museum Circle | (0.30, 0.35) |
| Heritage Cross | (0.60, 0.60) |
| Market Hub | (0.40, 0.55) |
| Old Gate | (0.65, 0.80) |

## Label Filtering

**Desktop:** Uses an **allowlist** (`attractions` HashSet):
```java
// MapPanel.java line 72-76
attractions.addAll(Arrays.asList(
    "City Center", "Museum District", "Heritage Quarter", ...
));

// Line 188: Only labels attraction districts
for (String name : attractions) { ... }

// Line 289: Only draws markers for attractions
if (!attractions.contains(name)) continue;
```

**Web:** Uses an **allowlist** (`visibleLocations` Set):
```javascript
// app.js line 750
if (!ATLAS_CITY.visibleLocations.has(int.name)) continue;  // Only visible locations
```

## Route Rendering

**Desktop (3-pass rendering):**
1. Wide semi-transparent blue (14px) — glow effect
2. Solid primary blue (4px) — main line
3. White highlight (1.5px) — center highlight
4. Animated white dots moving along path

**Web (5-pass rendering):**
1. Outer glow: `rgba(37,99,235,0.25)`, 16px with SVG filter
2. Mid glow: `rgba(59,130,246,0.4)`, 10px
3. Main line: `#2563EB`, 7px, rounded
4. Animated dash: `#93C5FD`, 3px, progressive draw + flowing animation
5. Start marker (green "S") + End marker (red "E") + Intermediate waypoints

## Dijkstra ↔ Map Integration

```
DijkstraService.findShortestPath("City Center", "Golden Coast")
    ↓
Returns PathResult: ["City Center", "Main Junction", "Market Hub", "Old Gate", "Golden Coast"]
    ↓
MapPanel.setRoute(path) / routeMap.setRoute(path)
    ↓
Map renders route through ALL nodes (including hidden junctions)
    ↓
Hidden nodes appear as small dots but NO labels
```

---

# SECTION 10 — FRONTEND DEEP DIVE

## HTML Structure (`index.html`, 538 lines)

```html
<body>
  <div id="app">
    <div id="login-page">           <!-- Login screen -->
      <div class="login-hero">      <!-- Left hero panel -->
      <div class="login-form">      <!-- Right form card -->
    </div>
    <div id="app-shell" class="hidden">  <!-- Main app (hidden until login) -->
      <aside id="sidebar">          <!-- Left navigation -->
      <main id="main-content">
        <header id="header">        <!-- Top bar -->
        <div id="content-area">     <!-- Page content router -->
          <div id="page-dashboard" class="content-page active-page">
          <div id="page-trip-planner" class="content-page">
          <div id="page-route-planner" class="content-page">
          <div id="page-rankings" class="content-page">
          <div id="page-settings" class="content-page">
```

**Page navigation:** Hash-based (`#dashboard`, `#trip-planner`, etc.)

## CSS Structure (`styles.css`, 1165 lines)

**Design tokens (CSS custom properties):**
```css
--bg: #f5f4f0          /* Warm off-white */
--surface: #ffffff      /* Card surface */
--green: #2d6a4f        /* Primary green */
--text: #1a1a18         /* Dark text */
--muted: #888680        /* Secondary text */
--radius: 10px          /* Border radius */
--sidebar-width: 220px
--header-height: 60px
```

**Layout system:** Flexbox + CSS Grid
- App shell: `display: flex; height: 100vh`
- Sidebar: Fixed 220px width
- Content: `flex: 1; overflow-y: auto`
- Dashboard: `grid-template-columns: 1.6fr 1fr`
- Stats: `grid-template-columns: repeat(auto-fit, minmax(190px, 1fr))`

**Key animations:**
- `fadeIn`: translateY(8px) → 0
- `fadeInUp`: translateY(16px) → 0
- `slideIn`: translateX(-12px) → 0
- `scaleIn`: scale(.95) → 1
- `dashMove`: stroke-dashoffset animation for route

## JavaScript Structure (`app.js`, 1756 lines)

### Major Sections

| Lines | Section | Purpose |
|-------|---------|---------|
| 1–60 | API Client | `fetchJson()`, `API` object with all endpoints |
| 62–290 | ATLAS_CITY | City data model (nodes, edges, districts, mappings) |
| 292–348 | Graph utilities | `buildGraph()`, `findRoadPath()` (local Dijkstra) |
| 354–1061 | AtlasCityMap class | SVG map renderer (constructor, rendering, interaction) |
| 1067–1114 | Router | Hash-based SPA navigation |
| 1117–1139 | appState | Global application state |
| 1145–1170 | Login | Login functions |
| 1200–1330 | Dashboard | Dashboard data loading + rendering |
| 1333–1455 | Budget Planner | Knapsack UI + result display |
| 1459–1615 | Route Planner | Dijkstra UI + map route display |
| 1621–1731 | Discover/Rankings | Heap Sort results display |
| 1737–1756 | Init | DOMContentLoaded event listeners |

### State Management (`appState`)

```javascript
const appState = {
    username: 'Traveler',
    locations: [],          // From API: all node names
    edges: [],              // From API: all edges
    dashboardMap: null,     // AtlasCityMap instance (dashboard)
    routeMap: null,         // AtlasCityMap instance (route planner)
    lastRouteResult: null,  // Last Dijkstra result
    lastBudgetResult: null, // Last Knapsack result
    attractions: [...]      // 12 hardcoded attractions (for fallback)
};
```

### Router

```javascript
const Router = {
    currentPage: 'dashboard',
    init()         // Listen to hashchange
    navigate(page) // Update sidebar, show page, update header, call page loader
    onPageLoad(page)  // Stop map animations, call page-specific loader
}
```

### API Client

```javascript
const API = {
    login(username)          // POST /api/login
    getDashboard()           // GET  /api/dashboard
    solveKnapsack(budget)    // POST /api/knapsack
    findRoute(start, end)    // POST /api/dijkstra
    getRankings()            // GET  /api/rankings
    getLocations()           // GET  /api/locations
    getEdges()               // GET  /api/edges
    resetData()              // POST /api/reset
    getExampleRoute()        // POST /api/example-route
    getExampleBudget()       // POST /api/example-budget
}
```

---

# SECTION 11 — MOST IMPORTANT FILES

| Feature | File | Class/Function | Key Methods |
|---------|------|----------------|-------------|
| **Route Planning** | `src/algorithms/DijkstraService.java` | `DijkstraService` | `findShortestPath()`, `loadGraph()` |
| **Budget Planning** | `src/algorithms/KnapsackService.java` | `KnapsackService` | `solveKnapsack()`, `solveKnapsackFromDB()` |
| **Rankings** | `src/algorithms/HeapSortService.java` | `HeapSortService` | `sortByScore()`, `heapify()`, `compare()` |
| **Database** | `src/db/DBConnection.java` | `DBConnection` | `getConnection()` |
| **Schema** | `src/db/SchemaInit.java` | `SchemaInit` | `initSchema()` |
| **Seed Data** | `src/db/SeedData.java` | `SeedData` | `seedAttractions()`, `seedEdges()` |
| **Data Model** | `src/model/Attraction.java` | `Attraction` | `getScore()` → `rating*10 + value` |
| **Main Window** | `src/ui/DashboardFrame.java` | `DashboardFrame` | `showPanel()` |
| **Map (Desktop)** | `src/ui/components/MapPanel.java` | `MapPanel` | `paintComponent()`, `setRoute()` |
| **Map (Web)** | `webapp/frontend/app.js` | `AtlasCityMap` | `_render()`, `setRoute()`, `_renderRoute()` |
| **Web Server** | `webapp/src/TripPlannerApp.java` | `TripPlannerApp` | `handleRankings()`, `handleDijkstra()`, `handleKnapsack()` |
| **Frontend Logic** | `webapp/frontend/app.js` | — | `loadDashboard()`, `findRoute()`, `fetchAndDisplayDiscover()` |
| **Design System** | `src/ui/Theme.java` | `Theme` | `hq()`, `fillRoundRect()`, `drawSoftShadow()` |
| **Login** | `src/ui/LoginPanel.java` | `LoginPanel` | Login form + validation |

---

# SECTION 12 — 100 LIKELY VIVA QUESTIONS

## Category 1: Java Fundamentals (1–15)

**Q1: What is the difference between `extends` and `implements`?**
A: `extends` is for class inheritance (a class inherits from another class). `implements` is for interface adoption (a class promises to provide methods defined in an interface). Example: `MapPanel extends JPanel` (inherits JPanel behavior), but our service classes don't implement any interfaces.

**Q2: What is the purpose of `volatile` keyword?**
A: Ensures a variable's value is always read from main memory, not cached in thread-local memory. Used in multithreaded contexts. Not used in this project since the desktop app is single-threaded (Swing EDT) and the web server uses synchronized access.

**Q3: What is the difference between `==` and `.equals()`?**
A: `==` compares references (same object in memory). `.equals()` compares values (logical equality). In DijkstraService, we use `.equals()` to compare node name strings: `u.equals(end)`.

**Q4: What is the purpose of `try-with-resources`?**
A: Automatically closes resources (streams, connections) after the try block. Used in all DB access: `try (Connection conn = ...; Statement stmt = ...; ResultSet rs = ...)`. Ensures connections are closed even if exceptions occur.

**Q5: What is a `PriorityQueue`?**
A: A queue that always dequeues the element with highest priority (lowest value by default). Used in Dijkstra as a min-heap to always process the closest unvisited node: `PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get))`.

**Q6: What is the difference between `ArrayList` and `LinkedList`?**
A: `ArrayList` is backed by an array — O(1) random access, O(n) insertion/deletion. `LinkedList` is a doubly-linked list — O(1) insertion/deletion at ends, O(n) access. We use `ArrayList` for path results (indexed access needed).

**Q7: What is `HashMap` vs `TreeMap`?**
A: `HashMap` provides O(1) average lookup. `TreeMap` provides O(log n) lookup but keeps keys sorted. We use `HashMap` for distances in Dijkstra (fast lookup by node name).

**Q8: What is the purpose of `static` keyword?**
A: Belongs to the class, not instances. Used for utility methods (`SchemaInit.initSchema()`), singleton patterns (`DBConnection.getConnection()`), and constants.

**Q9: What is polymorphism?**
A: The ability of a reference to take many forms. Example: `AnimatedPanel panel = new RankingsPanel()` — the variable type is `AnimatedPanel` but the object is `RankingsPanel`, so `paintComponent()` calls the RankingsPanel version.

**Q10: What is encapsulation?**
A: Bundling data with methods that operate on that data, hiding internal state. Desktop model classes use private fields + public getters/setters. Web model classes use public fields directly (less encapsulated).

**Q11: What is the difference between `String`, `StringBuilder`, and `StringBuffer`?**
A: `String` is immutable. `StringBuilder` is mutable and fast (not thread-safe). `StringBuffer` is mutable and thread-safe. We use `StringBuilder` in web model `toJson()` methods for efficient string concatenation.

**Q12: What is an `enum`?**
A: A special class representing a fixed set of constants. Not used in this project, but could be used for route status, map modes, etc.

**Q13: What is the purpose of `Comparator`?**
A: Defines custom ordering for objects. In HeapSortService: `Comparator.comparingInt(dist::get)` creates a comparator that orders strings by their distance value. Used to order the PriorityQueue in Dijkstra.

**Q14: What is `Collections.unmodifiableList()`?**
A: Returns a read-only view of a list. Any modification attempt throws `UnsupportedOperationException`. Useful for returning safe copies of internal data.

**Q15: What is the difference between `final`, `finally`, and `finalize()`?**
A: `final` — constant (variable), no inheritance (class), no override (method). `finally` — always executes after try-catch. `finalize()` — called by GC before object回收. `finally` is used implicitly in try-with-resources.

## Category 2: Database (16–25)

**Q16: What is SQLite?**
A: A lightweight, serverless, self-contained SQL database engine. Stores the entire database in a single file (`trip.db`). No separate server process needed. Ideal for embedded applications.

**Q17: What is JDBC?**
A: Java Database Connectivity — a standard API for connecting Java applications to databases. We use `sqlite-jdbc-3.42.0.0.jar` as the JDBC driver. Key classes: `Connection`, `Statement`, `ResultSet`.

**Q18: What is a prepared statement?**
A: A pre-compiled SQL template that can be executed multiple times with different parameters. Prevents SQL injection. Example: `PreparedStatement ps = conn.prepareStatement("INSERT INTO attractions ...")`.

**Q19: What is the difference between `Statement` and `PreparedStatement`?**
A: `Statement` executes static SQL. `PreparedStatement` uses parameterized queries (safer, faster for repeated use). We use `Statement` for reads and `PreparedStatement` for writes in SeedData.

**Q20: What is a singleton pattern?**
A: Ensures only one instance of a class exists. `DBConnection.getConnection()` returns the same connection object every time. The web version uses `synchronized` for thread safety.

**Q21: What is `AUTOINCREMENT`?**
A: SQLite feature that auto-generates unique integer IDs for new rows. Used in all three tables: `id INTEGER PRIMARY KEY AUTOINCREMENT`.

**Q22: What is the difference between `DELETE` and `TRUNCATE`?**
A: `DELETE` removes rows one by one (can be rolled back, fires triggers). `TRUNCATE` removes all rows at once (faster, resets auto-increment). SQLite doesn't support `TRUNCATE`, so `SeedData.resetData()` uses `DELETE FROM`.

**Q23: What is `IF NOT EXISTS`?**
A: A SQL clause that prevents errors if the table already exists. Used in `SchemaInit.initSchema()` to make the app idempotent (safe to run multiple times).

**Q24: What is a foreign key?**
A: A column that references the primary key of another table, establishing a relationship. Our schema doesn't use foreign keys (edges reference node names as strings, not IDs).

**Q25: What is the difference between `INNER JOIN` and `LEFT JOIN`?**
A: `INNER JOIN` returns only matching rows from both tables. `LEFT JOIN` returns all rows from the left table and matching rows from the right (NULLs for non-matches). Not used in our queries (we use single-table SELECT).

## Category 3: Architecture (26–40)

**Q26: What is MVC architecture?**
A: Model-View-Controller. Model = data (Attraction, Edge), View = UI (RankingsPanel, MapPanel), Controller = logic (DijkstraService, HeapSortService). Our project loosely follows this pattern.

**Q27: What is the difference between desktop and web architecture?**
A: Desktop uses Swing (event-driven, EDT thread, direct DB access). Web uses HTTP server (request-response, stateless endpoints, JSON communication).

**Q28: What is a SPA (Single Page Application)?**
A: A web app that loads once and dynamically updates content without full page reloads. Our frontend uses hash-based routing to show/hide page divs.

**Q29: What is CORS?**
A: Cross-Origin Resource Sharing — browser security feature that restricts HTTP requests to different domains. Our server sends `Access-Control-Allow-Origin: *` to allow requests from any origin.

**Q30: What is a REST API?**
A: An architectural style for web APIs using HTTP methods (GET, POST, PUT, DELETE) on resources (URLs). Our API uses GET for reads and POST for operations.

**Q31: What is JSON?**
A: JavaScript Object Notation — a lightweight data format. Our Java backend manually constructs JSON strings (no library). The frontend parses JSON with `JSON.parse()`.

**Q32: What is the purpose of `com.sun.net.httpserver.HttpServer`?**
A: Built-in JDK HTTP server — no external dependencies needed. Provides `HttpServer`, `HttpExchange`, `HttpContext`. Lightweight alternative to Tomcat/Jetty for small projects.

**Q33: What is a singleton?**
A: Design pattern ensuring only one instance. Used for database connections: `DBConnection.getConnection()` returns the same Connection object.

**Q34: What is the difference between `get()` and `computeIfAbsent()`?**
A: `get()` returns null if key doesn't exist. `computeIfAbsent()` creates and inserts a value if the key is absent. Used in DijkstraService: `graph.computeIfAbsent(edge.fromNode, k -> new ArrayList<>()).add(edge)`.

**Q35: What is a `CardLayout`?**
A: A Swing layout manager that shows one card at a time. Used in `DashboardFrame` to switch between panels: `cardLayout.show(contentArea, "home")`.

**Q36: What is the Event Dispatch Thread (EDT)?**
A: The thread that handles all Swing UI events and painting. All UI updates must happen on the EDT. `SwingUtilities.invokeLater()` ensures code runs on EDT.

**Q37: What is the difference between `invokeLater()` and `invokeAndWait()`?**
A: `invokeLater()` queues code to run on EDT asynchronously. `invokeAndWait()` blocks until the queued code completes. `invokeLater()` is preferred to avoid deadlocks.

**Q38: What is a layout manager?**
A: Controls the position and size of components in a container. Used layouts: `BorderLayout` (main panels), `BoxLayout` (vertical lists), `GridLayout` (stat cards), `FlowLayout` (inline elements), `CardLayout` (page switching).

**Q39: What is custom painting in Swing?**
A: Overriding `paintComponent(Graphics g)` to draw custom graphics. Used in `MapPanel`, `Theme`, `SidebarPanel`, `StatCard`, `GradientButton`. Always call `super.paintComponent(g)` first.

**Q40: What is `AlphaComposite`?**
A: Java2D class for transparency effects. Used in `AnimatedPanel.paintComponent()`: `g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha))` to create fade-in animations.

## Category 4: Algorithms — Dijkstra (41–55)

**Q41: What is Dijkstra's algorithm?**
A: A greedy algorithm that finds the shortest path from a source to all other nodes in a weighted graph with non-negative edge weights. Always processes the closest unvisited node first.

**Q42: What data structure does Dijkstra use?**
A: A priority queue (min-heap) ordered by distance. Extract-min operation gets the closest unvisited node in O(log V).

**Q43: What is the time complexity of Dijkstra?**
A: O((V + E) log V) with a binary heap priority queue. V = vertices, E = edges. For our graph: V=20, E≈68 → ~2000 operations.

**Q44: Why can't Dijkstra handle negative weights?**
A: Dijkstra's greedy choice assumes processing a node gives the final shortest distance. Negative weights can create shorter paths through already-processed nodes, violating this assumption.

**Q45: What is the difference between Dijkstra and BFS?**
A: BFS works on unweighted graphs (all edges = 1). Dijkstra works on weighted graphs. BFS uses a regular queue; Dijkstra uses a priority queue.

**Q46: What is the difference between Dijkstra and Bellman-Ford?**
A: Dijkstra is faster O((V+E) log V) but can't handle negative weights. Bellman-Ford handles negative weights but is slower O(V×E). Dijkstra is greedy; Bellman-Ford uses dynamic programming.

**Q47: What is the relaxation step in Dijkstra?**
A: If a shorter path to a node is found through a neighbor, update its distance: `if (dist[u] + weight < dist[v]) { dist[v] = dist[u] + weight; prev[v] = u; }`.

**Q48: What is the `prev` map used for?**
A: Stores the predecessor of each node in the shortest path. Used for path reconstruction: backtrack from target to source using `prev`.

**Q49: How is the path reconstructed?**
A: Start from the target node. Follow `prev` pointers backwards until reaching the source. Reverse the list to get source→target order.

**Q50: What is the role of the `visited` set?**
A: Prevents processing a node multiple times. Once a node is extracted from the priority queue, its shortest distance is finalized.

**Q51: Why use a PriorityQueue instead of sorting?**
A: A priority queue provides efficient extract-min (O(log V)) without sorting all elements. Dijkstra only needs the minimum at each step, not a fully sorted list.

**Q52: What is the space complexity of Dijkstra?**
A: O(V) for the distance map, predecessor map, visited set, and priority queue.

**Q53: Can Dijkstra find paths between all pairs?**
A: Run Dijkstra from each node — O(V × (V+E) log V). Or use Floyd-Warshall for all-pairs — O(V³). We only need single-source-single-target.

**Q54: What happens if there's no path between start and end?**
A: The target is never reached. The `prev` map won't contain a chain from target to source. We detect this by checking if `path.get(0).equals(start)`.

**Q55: Why does the web version rebuild the graph per call?**
A: The web server is stateless — each request is independent. Caching the graph would require shared state between requests, complicating thread safety. The desktop version caches because it runs in a single long-lived process.

## Category 5: Algorithms — Knapsack (56–70)

**Q56: What is the 0-1 Knapsack problem?**
A: Given items with weights and values, and a capacity constraint, select items to maximize total value without exceeding capacity. Each item can be taken at most once (0 or 1).

**Q57: What is the DP approach?**
A: Build a table `dp[i][w]` where each cell represents the maximum value using items[0..i] with capacity w. Fill bottom-up using the recurrence relation.

**Q58: What is the state transition?**
A: `dp[i][w] = max(dp[i-1][w], dp[i-1][w-cost[i]] + value[i])` — max of excluding vs including item i.

**Q59: What is backtracking in Knapsack?**
A: After filling the DP table, trace back from `dp[n][W]` to determine which items were selected. If `dp[i][w] ≠ dp[i-1][w]`, item i was included.

**Q60: What is the time complexity?**
A: O(n × W) — n items × W capacity. For our case: 12 × 500 = 6,000 operations.

**Q61: What is the space complexity?**
A: O(n × W) for the full DP table. Can be optimized to O(W) using a 1D array (since we only need the previous row).

**Q62: Why 0-1 and not Unbounded Knapsack?**
A: 0-1 means each item can be taken once. Unbounded allows unlimited copies. Our attractions are unique — you visit a museum once, not multiple times.

**Q63: What is the greedy approach for Knapsack?**
A: Sort items by value/weight ratio, take highest ratio first. This works for Fractional Knapsack but NOT for 0-1 Knapsack (proven suboptimal).

**Q64: Why is the greedy approach wrong for 0-1?**
A: Counterexample: items (value=60, weight=10), (value=100, weight=20), (value=120, weight=30), capacity=50. Greedy takes 60+100=160. DP takes 100+120=220.

**Q65: What is the optimal substructure property?**
A: The optimal solution to the problem contains optimal solutions to subproblems. `dp[i][w]` depends on `dp[i-1][w]` and `dp[i-1][w-cost[i]]`.

**Q66: What is the overlapping subproblems property?**
A: The same subproblems are solved multiple times. DP stores results in a table to avoid recomputation. Without DP, the recursive solution would be O(2^n).

**Q67: How do you handle items with the same cost?**
A: The DP naturally handles this — it considers each item independently. If costs are equal, the algorithm picks the one with higher value.

**Q68: What if the budget is 0?**
A: `dp[i][0] = 0` for all i — no items can be selected. Our code handles this with the early return: `if (budget <= 0) return empty result`.

**Q69: How is the result displayed?**
A: Desktop shows a scrollable list of selected attractions with rank badges, cost chips, and value chips. Web shows summary chips, a health bar, experience cards, and a skipped items list.

**Q70: What is the difference between this and the Knapsack greedy?**
A: Greedy is O(n log n) but suboptimal for 0-1. DP is O(n × W) but guarantees optimal. For small n and W, DP is fast enough and always correct.

## Category 6: Algorithms — Heap Sort (71–82)

**Q71: What is a heap?**
A: A complete binary tree satisfying the heap property. Min-heap: parent ≤ children. Max-heap: parent ≥ children. Stored as an array where parent at i, children at 2i+1 and 2i+2.

**Q72: What is heapify?**
A: The process of restoring the heap property after a violation. Compare root with children, swap with the smallest (min-heap) or largest (max-heap), recurse.

**Q73: What is the time complexity of Heap Sort?**
A: O(n log n) in all cases — building heap is O(n), n extractions × O(log n) heapify each.

**Q74: Why is build-heap O(n) and not O(n log n)?**
A: Most nodes are near the bottom and require little heapification. The sum of heights converges to O(n). Only O(n) work total.

**Q75: Is Heap Sort stable?**
A: No. Equal elements may be reordered. Our comparator breaks ties by name, so stability isn't critical.

**Q76: Is Heap Sort in-place?**
A: Yes. Uses O(1) extra space (only temporary swap variable). No additional arrays needed.

**Q77: Why Heap Sort over Quick Sort?**
A: Heap Sort guarantees O(n log n) worst case. Quick Sort degrades to O(n²) on sorted input. Heap Sort is in-place like Quick Sort but with better worst-case guarantees.

**Q78: Why Heap Sort over Merge Sort?**
A: Merge Sort is O(n log n) but requires O(n) extra space. Heap Sort is in-place. Both have O(n log n) worst case.

**Q79: How does the min-heap produce descending output?**
A: Min-heap root = minimum. Extract min to end. After all extractions: [max, ..., min] = descending.

**Q80: What is the difference between min-heap and max-heap sort?**
A: Max-heap sort: root = max → extract max to end → ascending. Min-heap sort: root = min → extract min to end → descending.

**Q81: What is the composite score formula?**
A: `score = rating × 10 + value`. Rating is weighted more heavily (×10) so a 5-star attraction always beats a 4-star regardless of value.

**Q82: How are ties broken?**
A: Alphabetically by name: `a.getName().compareTo(b.getName())`. So "Central Gardens" comes before "Heritage Walk" if scores are equal.

## Category 7: Frontend (83–90)

**Q83: What is a SPA?**
A: Single Page Application — loads once, dynamically updates content via JavaScript. No full page reloads. Our frontend uses hash routing to show/hide page sections.

**Q84: What is hash-based routing?**
A: Navigation using URL hash (#dashboard, #trip-planner). `window.onhashchange` triggers page switch. No server-side routing needed.

**Q85: What is the DOM?**
A: Document Object Model — the tree structure of HTML elements. JavaScript manipulates the DOM to update the UI: `document.getElementById('route-path').innerHTML = ...`.

**Q86: What is event delegation?**
A: Attaching a single event listener to a parent element instead of multiple children. Used in quick action cards: `document.querySelectorAll('.action-card[data-nav]').forEach(...)`.

**Q87: What is `async/await`?**
A: Syntactic sugar for Promises. `async function` returns a Promise. `await` pauses execution until a Promise resolves. Used in all API calls: `const rankings = await API.getRankings()`.

**Q88: What is SVG?**
A: Scalable Vector Graphics — XML-based format for vector images. Our map uses SVG for resolution-independent rendering: circles, paths, polygons, text elements.

**Q89: What is `requestAnimationFrame`?**
A: Browser API for smooth animations. Synchronizes with display refresh rate (60fps). Used for route drawing animation and dash flowing effect.

**Q90: What is the difference between `innerHTML` and `textContent`?**
A: `innerHTML` parses HTML markup (can execute scripts — XSS risk). `textContent` treats as plain text (safe). We use `innerHTML` for dynamic card generation and `textContent` for safe text updates.

## Category 8: Design & Complexity (91–100)

**Q91: What design pattern is used for the map?**
A: Composite pattern — the map is composed of layers (water, parks, districts, roads, labels, route, pins). Each layer is rendered independently. Also uses Strategy pattern for different marker types.

**Q92: What is the time complexity of rendering the map?**
A: O(n) where n = number of nodes + edges + districts. All drawing operations are linear in the number of elements.

**Q93: What is the difference between eager and lazy loading?**
A: Eager loading loads all data upfront (desktop: graph cached in constructor). Lazy loading loads data on demand (web: graph rebuilt per request).

**Q94: What is the purpose of the seed data?**
A: Provides initial demo data so the app works out of the box. Prevents empty states. Can be reset via the settings page.

**Q95: What is the difference between the desktop and web SeedData?**
A: Desktop seeds 68 edges, 2 users. Web seeds 62 edges, 3 users. Web adds `resetData()` method. Edge counts differ slightly.

**Q96: How does the app handle errors?**
A: Desktop: try-catch with `e.printStackTrace()`. Web: HTTP status codes (400, 405, 500) with JSON error messages. Frontend: `try-catch` with fallback to local data.

**Q97: What is the difference between `getDeclaredMethod()` and `getMethod()`?**
A: `getDeclaredMethod()` finds methods declared in the class (including private). `getMethod()` finds only public methods (including inherited). Not used in this project.

**Q98: What is the purpose of `design-system.json`?**
A: Documents the design system — color palette, typography, component specs, animation tokens. Serves as a reference for consistent UI development.

**Q99: What is the difference between the desktop and web MapPanel rendering?**
A: Desktop uses Java2D (Graphics2D, custom paintComponent). Web uses SVG (DOM manipulation, XML elements). Both use the same coordinate system (0–1 normalized).

**Q100: What would you improve about this project?**
A: Potential improvements: Add unit tests, use a proper JSON library (Gson/Jackson), implement proper authentication (JWT), add input validation, use connection pooling, add error boundaries in frontend, implement caching for Dijkstra results.

---

# SECTION 13 — 5-MINUTE RAPID REVISION SHEET

## Architecture Diagram

```
┌─────────────────────────────────────────────────────┐
│                    ATLAS JOURNEY                     │
├──────────────────┬──────────────────────────────────┤
│   DESKTOP APP    │          WEB APP                  │
│   (Java Swing)   │  (HTTP Server + SPA)              │
├──────────────────┼──────────────────────────────────┤
│ LoginPanel       │ TripPlannerApp (port 8080)        │
│   ↓              │   ↓                               │
│ DashboardFrame   │  API Handlers (JSON)              │
│   ↓              │   ↓                               │
│ CardLayout:      │  DijkstraService                  │
│  - Home          │  HeapSortService                  │
│  - Budget        │  KnapsackService                  │
│  - Route         │   ↓                               │
│  - Rankings      │  DatabaseManager                  │
│  - Settings      │   ↓                               │
│   ↓              │  SQLite (trip.db)                  │
│ MapPanel (2D)    │   ↓                               │
│   ↓              │  app.js (SPA Frontend)             │
│ SQLite (trip.db) │  AtlasCityMap (SVG)               │
└──────────────────┴──────────────────────────────────┘
```

## Algorithm Complexities

| Algorithm | Time | Space | Use Case |
|-----------|------|-------|----------|
| **Dijkstra** | O((V+E) log V) | O(V) | Shortest path in weighted graph |
| **0-1 Knapsack** | O(n × W) | O(n × W) | Budget optimization |
| **Heap Sort** | O(n log n) | O(1) | Ranking attractions |

## Most Important Files

| File | Purpose |
|------|---------|
| `src/algorithms/DijkstraService.java` | Dijkstra shortest path |
| `src/algorithms/HeapSortService.java` | Heap Sort rankings |
| `src/algorithms/KnapsackService.java` | 0-1 Knapsack DP |
| `src/model/Attraction.java` | `getScore() = rating*10 + value` |
| `src/db/SeedData.java` | 12 attractions, 20 nodes, 68 edges |
| `webapp/src/TripPlannerApp.java` | HTTP server + API routes |
| `webapp/frontend/app.js` | SPA + SVG map renderer |
| `src/ui/components/MapPanel.java` | Java2D map |

## Key Formulas

```
Composite Score = rating × 10 + value
Dijkstra relaxation: if dist[u] + w < dist[v] then dist[v] = dist[u] + w
Knapsack: dp[i][w] = max(dp[i-1][w], dp[i-1][w-cost] + value)
Heap parent: i, children: 2i+1, 2i+2
```

## Most Likely Questions

1. **"Explain Dijkstra's algorithm"** — Greedy, PriorityQueue, relax edges, reconstruct path
2. **"How does the Knapsack DP work?"** — 2D table, state transition, backtracking
3. **"Why Heap Sort?"** — O(n log n) guaranteed, in-place, demonstrates tree-based sorting
4. **"How does the map work?"** — Normalized coordinates, SVG layers, route rendering
5. **"What's the difference between desktop and web?"** — Swing vs HTTP server, Java2D vs SVG, cached vs stateless
6. **"Walk through the request flow"** — Frontend → API → Backend → Algorithm → DB → Response → UI
7. **"Explain the database schema"** — 3 tables: attractions, edges, users
8. **"What design patterns are used?"** — Singleton (DB), MVC (loosely), Composite (map layers)
9. **"How do you handle hidden nodes?"** — Allowlist filtering, internal routing vs visible labels
10. **"What would you improve?"** — Tests, proper JSON library, auth, validation, caching

---

*This guide is based on the actual codebase of Atlas Journey (Trip-Planner). All file paths, line numbers, class names, and method signatures reference the real source code.*
