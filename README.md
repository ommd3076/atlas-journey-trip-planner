# Atlas Journey

Atlas Journey is a full-stack trip planning platform built as a Design and Analysis of Algorithms (DAA) project.

The system demonstrates the practical application of:

- **Dijkstra's Algorithm**
- **0-1 Knapsack**
- **Heap Sort**

through an interactive travel planning experience.

## Features

### Route Optimization
Finds shortest travel routes using Dijkstra's Algorithm.

### Smart Itinerary Planning
Generates budget-aware itineraries using 0-1 Knapsack.

### Attraction Ranking
Ranks attractions using Heap Sort.

### Offline Map System
Custom Atlas City map with route visualization.

### Local Storage
SQLite-powered data persistence.

### Modern UI
Responsive travel-planner interface inspired by modern itinerary applications.

## Technology Stack

**Frontend:**
- HTML5
- CSS3
- JavaScript

**Backend:**
- Java 24

**Database:**
- SQLite

**Algorithms:**
- Dijkstra's Algorithm
- 0-1 Knapsack
- Heap Sort

## System Architecture

```
User
  ↓
Frontend (HTML/CSS/JS)
  ↓
Java Backend
  ↓
Algorithm Layer
  ↓
SQLite Database
  ↓
JSON Response
  ↓
Frontend Rendering
```

## Algorithms Used

### Dijkstra's Algorithm

**Purpose:** Computes the shortest route between two locations on the Atlas City map.

**Complexity:**
- **Time:** O((V + E) log V)
- **Space:** O(V)

### 0-1 Knapsack

**Purpose:** Optimizes attraction selection within a fixed budget.

**Complexity:**
- **Time:** O(nW)
- **Space:** O(nW)

### Heap Sort

**Purpose:** Ranks attractions by overall score.

**Score Formula:**
```
score = rating × 10 + value
```

**Complexity:**
- O(n log n)

## Installation

### Clone Repository

```bash
git clone https://github.com/ommd3076/atlas-journey-trip-planner.git
cd atlas-journey-trip-planner
```

### Requirements

- Java 24
- SQLite JDBC Driver

### Compile

```bash
javac -d webapp/bin -cp "lib/sqlite-jdbc-3.42.0.0.jar" @sources.txt
```

### Run

```bash
java -cp "webapp/bin;lib/sqlite-jdbc-3.42.0.0.jar" TripPlannerApp
```

### Open

```
http://localhost:8080
```

## Database Schema

### Attractions

| Column | Type |
|----------|----------|
| id | INTEGER |
| name | TEXT |
| rating | INTEGER |
| value | INTEGER |
| cost | INTEGER |
| x | INTEGER |
| y | INTEGER |

### Roads

| Column | Type |
|----------|----------|
| source | TEXT |
| destination | TEXT |
| distance | INTEGER |

## Project Workflow

### Route Planning

```
User selects source and destination
  ↓
Frontend sends request
  ↓
Backend builds graph
  ↓
Dijkstra computes path
  ↓
Path returned
  ↓
Map renders route
```

### Budget Planning

```
Budget entered
  ↓
Attractions loaded
  ↓
Knapsack optimization
  ↓
Best itinerary generated
```

### Rankings

```
Attractions loaded
  ↓
Scores computed
  ↓
Heap Sort executed
  ↓
Ranked list displayed
```

## Screenshots

### Dashboard

![Dashboard](docs/screenshots/dashboard.png)

### Route Planner

![Route Planner](docs/screenshots/routes.png)

### Budget Planner

![Budget Planner](docs/screenshots/budget.png)

### Discover

![Discover](docs/screenshots/discover.png)

## Future Scope

- Real-world map integration
- Multi-user collaboration
- Live traffic support
- Recommendation engine
- Cloud synchronization

## License

MIT License
