# Trip Planner - Web Application

A complete offline trip-planning web application built for a college DAA (Design and Analysis of Algorithms) project. Features a polished SaaS-style dashboard with three classic algorithm implementations.

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Trip Planner App                        │
├─────────────────────────────────────────────────────────────┤
│  Frontend (HTML/CSS/JS)          Backend (Java)             │
│  ┌──────────────────────┐       ┌────────────────────────┐ │
│  │  Login Page          │       │  TripPlannerApp.java   │ │
│  │  Dashboard           │       │  (HTTP Server :8080)   │ │
│  │  Trip Planner (KP)   │◄─────►│  ┌──────────────────┐  │ │
│  │  Route Planner (DJ)  │ JSON  │  │ Controllers      │  │ │
│  │  Rankings (HS)       │       │  ├──────────────────┤  │ │
│  │  Settings            │       │  │ Services         │  │ │
│  └──────────────────────┘       │  ├──────────────────┤  │ │
│                                  │  │ DatabaseManager  │  │ │
│  SQLite (trip.db) ◄─────────────►│  └──────────────────┘  │ │
│                                  └────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## Project Structure

```
Trip-Planner/
├── webapp/
│   ├── src/                          # Java Backend
│   │   ├── TripPlannerApp.java       # Main entry, HTTP server, API routes
│   │   ├── DatabaseManager.java      # SQLite connection & schema
│   │   ├── SeedData.java             # Sample data seeding
│   │   ├── KnapsackService.java      # 0-1 Knapsack algorithm
│   │   ├── DijkstraService.java      # Dijkstra's algorithm
│   │   ├── HeapSortService.java      # Heap Sort algorithm
│   │   ├── Attraction.java           # Attraction model
│   │   ├── Edge.java                 # Edge model
│   │   ├── PathResult.java           # Dijkstra result model
│   │   └── KnapsackResult.java       # Knapsack result model
│   ├── frontend/                     # Frontend SPA
│   │   ├── index.html                # Complete HTML structure
│   │   ├── styles.css                # Design system & styles
│   │   └── app.js                    # SPA logic, API client, map renderer
│   ├── bin/                          # Compiled classes
│   └── trip.db                       # SQLite database (auto-created)
├── lib/
│   └── sqlite-jdbc-3.42.0.0.jar     # SQLite JDBC driver
└── README.md
```

## Database Schema

### Attractions Table
| Column      | Type    | Description                     |
|-------------|---------|---------------------------------|
| id          | INTEGER | Primary key (auto-increment)    |
| name        | TEXT    | Attraction name                 |
| cost        | INTEGER | Cost to visit                   |
| value       | INTEGER | Popularity/experience value     |
| rating      | INTEGER | Rating (1-5)                    |
| location    | TEXT    | Map location                    |
| description | TEXT    | Brief description               |

### Edges Table
| Column   | Type    | Description                     |
|----------|---------|---------------------------------|
| id       | INTEGER | Primary key (auto-increment)    |
| fromNode | TEXT    | Source location                 |
| toNode   | TEXT    | Destination location            |
| weight   | INTEGER | Distance/cost of edge           |

## Algorithms

### 1. 0-1 Knapsack (Trip Planner Page)

**Purpose:** Optimize attraction selection within a given budget.

**Approach:** Dynamic Programming
- Build a 2D DP table where `dp[i][w]` = maximum value achievable using first `i` items with budget `w`
- For each item, decide to include or exclude based on which gives higher total value
- Backtrack through the table to identify which items were selected

**Complexity:**
- **Time:** O(n × W) where n = number of attractions, W = budget amount
- **Space:** O(n × W) for the DP table

**UI Integration:** Enter your budget → click "Optimize Budget" → view selected and skipped attractions with cost/value badges.

### 2. Dijkstra's Algorithm (Route Planner Page)

**Purpose:** Find the shortest path between two locations on the map.

**Approach:** Greedy + Priority Queue
- Maintain distance estimates from start to all nodes
- Use a min-priority queue to always expand the closest unvisited node
- For each neighbor, if a shorter path is found, update distance and predecessor
- Reconstruct the path by walking backwards from destination to start

**Complexity:**
- **Time:** O((V + E) log V) where V = vertices, E = edges
- **Space:** O(V) for distance and predecessor maps

**UI Integration:** Select start/destination → click "Find Shortest Path" → view path on map and distance in result card.

### 3. Heap Sort (Rankings Page)

**Purpose:** Sort attractions by rating in descending order.

**Approach:** Max-Heap Data Structure
- Build a max-heap from the attraction array (heapify all non-leaf nodes)
- Repeatedly extract the maximum element and place it at the end
- Restore heap property after each extraction
- Reverse to get descending order

**Complexity:**
- **Time:** O(n log n) for all cases (best, average, worst)
- **Space:** O(n) for the output array (in-place)

**UI Integration:** Click "Refresh Rankings" → view attractions sorted by rating with rank badges and star ratings.

## Setup Instructions

### Prerequisites
- Java JDK 11 or later
- Modern web browser (Chrome, Firefox, Edge)

### Running the Application

```bash
# Navigate to project root
cd Trip-Planner

# Clean compile
javac -cp "lib\sqlite-jdbc-3.42.0.0.jar" -d webapp\bin webapp\src\*.java

# Run the server
java -cp "webapp\bin;lib\sqlite-jdbc-3.42.0.0.jar" TripPlannerApp

# Open in browser
# http://localhost:8080
```

### Login Credentials
- Username: `admin` / Password: `admin`
- Or any non-empty username (demo mode)

## API Endpoints

| Method | Endpoint              | Description                    |
|--------|------------------------|--------------------------------|
| POST   | `/api/login`          | Demo login                     |
| GET    | `/api/dashboard`      | Dashboard statistics           |
| POST   | `/api/knapsack`       | Solve 0-1 knapsack             |
| POST   | `/api/dijkstra`       | Find shortest path             |
| GET    | `/api/rankings`       | Get heap-sorted rankings       |
| GET    | `/api/locations`      | Get all map locations          |
| GET    | `/api/edges`          | Get all graph edges            |
| POST   | `/api/reset`          | Reset demo data                |
| POST   | `/api/example-route`  | Load example route             |
| POST   | `/api/example-budget` | Load example budget plan       |

## Seed Data

- **12 Attractions** with varied costs ($20-$250), values (45-95), ratings (3-5)
- **10 Map Locations** connected by **18 directed edges**
- **3 Demo Users** (admin, demo, traveler)

## Key Features

- ✅ Pure Java backend with embedded HTTP server (no frameworks)
- ✅ Modern SPA frontend with card-based dashboard layout
- ✅ Offline map with canvas rendering, markers, and route overlay
- ✅ 3 fully implemented DAA algorithms with visible UI integration
- ✅ SQLite database with automatic schema creation and seeding
- ✅ Responsive design with CSS design system
- ✅ Subtle animations and microinteractions
- ✅ Demo controls for easy presentation
