/* ═══════════════════════════════════════════════════════════════════════════
   TRIP PLANNER - Application JavaScript
   ═══════════════════════════════════════════════════════════════════════════ */

const API_BASE = '';

// ── API Client ────────────────────────────────────────────────────────────
const API = {
  async login(username) {
    return fetchJson(`${API_BASE}/api/login`, {
      method: 'POST',
      body: JSON.stringify({ username, password: '' })
    });
  },
  async getDashboard() {
    return fetchJson(`${API_BASE}/api/dashboard`);
  },
  async solveKnapsack(budget) {
    return fetchJson(`${API_BASE}/api/knapsack`, {
      method: 'POST',
      body: JSON.stringify({ budget })
    });
  },
  async findRoute(start, end) {
    return fetchJson(`${API_BASE}/api/dijkstra`, {
      method: 'POST',
      body: JSON.stringify({ start, end })
    });
  },
  async getRankings() {
    return fetchJson(`${API_BASE}/api/rankings`);
  },
  async getLocations() {
    return fetchJson(`${API_BASE}/api/locations`);
  },
  async getEdges() {
    return fetchJson(`${API_BASE}/api/edges`);
  },
  async resetData() {
    return fetchJson(`${API_BASE}/api/reset`, { method: 'POST' });
  },
  async getExampleRoute() {
    return fetchJson(`${API_BASE}/api/example-route`, { method: 'POST' });
  },
  async getExampleBudget() {
    return fetchJson(`${API_BASE}/api/example-budget`, { method: 'POST' });
  }
};

async function fetchJson(url, opts = {}) {
  const resp = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...opts
  });
  if (!resp.ok) {
    const err = await resp.json().catch(() => ({ error: 'Request failed' }));
    throw new Error(err.error || `HTTP ${resp.status}`);
  }
  return resp.json();
}

// ═══════════════════════════════════════════════════════════════════════════
// ATLAS CITY DATA
// ═══════════════════════════════════════════════════════════════════════════

const ATLAS_CITY = {
  // Locations visible to users on the map
  visibleLocations: new Set([
    'City Center', 'Museum District', 'Innovation District', 'Market Square',
    'Central Park', 'Heritage Quarter', 'Old Town', 'Lake Gardens',
    'Sunset Point', 'Golden Coast'
  ]),

  // Internal graph nodes - kept for Dijkstra routing but hidden visually
  hiddenNodes: new Set([
    'North Gate', 'Park Entrance', 'Main Junction', 'South Bridge',
    'West End', 'Museum Circle', 'Heritage Cross', 'Old Gate',
    'Market Hub', 'East Crossing'
  ]),

  intersections: [
    { id: 'main-junction', name: 'Main Junction', x: 0.50, y: 0.50 },
    { id: 'north-gate', name: 'North Gate', x: 0.50, y: 0.15 },
    { id: 'east-crossing', name: 'East Crossing', x: 0.85, y: 0.50 },
    { id: 'south-bridge', name: 'South Bridge', x: 0.50, y: 0.85 },
    { id: 'west-end', name: 'West End', x: 0.15, y: 0.50 },
    { id: 'park-entrance', name: 'Park Entrance', x: 0.50, y: 0.30 },
    { id: 'museum-circle', name: 'Museum Circle', x: 0.30, y: 0.25 },
    { id: 'heritage-cross', name: 'Heritage Cross', x: 0.65, y: 0.50 },
    { id: 'market-hub', name: 'Market Hub', x: 0.40, y: 0.55 },
    { id: 'innovation-hub', name: 'Innovation Hub', x: 0.25, y: 0.45 },
    { id: 'old-gate', name: 'Old Gate', x: 0.60, y: 0.65 },
    { id: 'lake-view', name: 'Lake View', x: 0.75, y: 0.45 },
    { id: 'coast-road', name: 'Coast Road', x: 0.80, y: 0.70 },
    { id: 'sunset-junction', name: 'Sunset Junction', x: 0.20, y: 0.65 },
    { id: 'garden-path', name: 'Garden Path', x: 0.70, y: 0.35 },
    { id: 'plaza-central', name: 'Plaza Central', x: 0.45, y: 0.45 },
    { id: 'district-square', name: 'District Square', x: 0.55, y: 0.35 },
    { id: 'river-crossing', name: 'River Crossing', x: 0.55, y: 0.60 },
    { id: 'bay-bridge', name: 'Bay Bridge', x: 0.60, y: 0.80 },
    { id: 'valley-point', name: 'Valley Point', x: 0.35, y: 0.35 },
    { id: 'city-center', name: 'City Center', x: 0.50, y: 0.45 },
    { id: 'golden-point', name: 'Golden Point', x: 0.80, y: 0.80 }
  ],

  // from/to are indices into intersections array
  edges: [
    { from: 1, to: 5, distance: 1.5 },
    { from: 1, to: 6, distance: 2.2 },
    { from: 5, to: 16, distance: 1.0 },
    { from: 5, to: 15, distance: 1.8 },
    { from: 5, to: 19, distance: 1.6 },
    { from: 6, to: 19, distance: 1.1 },
    { from: 6, to: 4, distance: 2.8 },
    { from: 19, to: 15, distance: 1.3 },
    { from: 19, to: 9, distance: 1.3 },
    { from: 4, to: 9, distance: 1.2 },
    { from: 4, to: 13, distance: 1.7 },
    { from: 9, to: 15, distance: 2.0 },
    { from: 9, to: 13, distance: 2.1 },
    { from: 9, to: 8, distance: 1.7 },
    { from: 13, to: 8, distance: 2.3 },
    { from: 13, to: 3, distance: 3.4 },
    { from: 8, to: 15, distance: 1.1 },
    { from: 8, to: 17, distance: 1.6 },
    { from: 8, to: 10, distance: 2.2 },
    { from: 15, to: 0, distance: 0.8 },
    { from: 15, to: 20, distance: 0.6 },
    { from: 15, to: 16, distance: 1.3 },
    { from: 0, to: 20, distance: 0.6 },
    { from: 0, to: 17, distance: 1.1 },
    { from: 0, to: 7, distance: 1.6 },
    { from: 20, to: 16, distance: 1.1 },
    { from: 16, to: 14, distance: 1.5 },
    { from: 16, to: 7, distance: 1.7 },
    { from: 14, to: 11, distance: 1.3 },
    { from: 14, to: 7, distance: 1.7 },
    { from: 14, to: 2, distance: 2.0 },
    { from: 7, to: 17, distance: 1.4 },
    { from: 7, to: 10, distance: 1.8 },
    { from: 7, to: 11, distance: 1.1 },
    { from: 2, to: 11, distance: 1.1 },
    { from: 2, to: 12, distance: 2.3 },
    { from: 2, to: 14, distance: 2.0 },
    { from: 17, to: 10, distance: 0.9 },
    { from: 10, to: 18, distance: 1.6 },
    { from: 10, to: 3, distance: 2.3 },
    { from: 18, to: 3, distance: 1.2 },
    { from: 18, to: 12, distance: 2.4 },
    { from: 18, to: 21, distance: 2.4 },
    { from: 12, to: 21, distance: 1.0 },
    { from: 21, to: 11, distance: 3.6 },
    { from: 11, to: 14, distance: 1.3 }
  ],

  districts: [
    {
      id: 'city-center-dist',
      name: 'City Center',
      centerX: 0.50, centerY: 0.45,
      polygon: [[0.42,0.38],[0.58,0.38],[0.58,0.52],[0.42,0.52]],
      color: 'rgba(59,130,246,0.04)'
    },
    {
      id: 'museum-dist',
      name: 'Museum District',
      centerX: 0.30, centerY: 0.30,
      polygon: [[0.22,0.20],[0.38,0.20],[0.38,0.35],[0.22,0.35]],
      color: 'rgba(139,92,246,0.04)'
    },
    {
      id: 'heritage-dist',
      name: 'Heritage Quarter',
      centerX: 0.65, centerY: 0.55,
      polygon: [[0.58,0.48],[0.72,0.48],[0.72,0.62],[0.58,0.62]],
      color: 'rgba(245,158,11,0.04)'
    },
    {
      id: 'market-dist',
      name: 'Market Square',
      centerX: 0.40, centerY: 0.60,
      polygon: [[0.33,0.52],[0.47,0.52],[0.47,0.68],[0.33,0.68]],
      color: 'rgba(16,185,129,0.04)'
    },
    {
      id: 'central-park-dist',
      name: 'Central Park',
      centerX: 0.50, centerY: 0.25,
      polygon: [[0.42,0.18],[0.58,0.18],[0.58,0.32],[0.42,0.32]],
      color: 'rgba(16,185,129,0.06)'
    },
    {
      id: 'innovation-dist',
      name: 'Innovation District',
      centerX: 0.25, centerY: 0.50,
      polygon: [[0.18,0.38],[0.32,0.38],[0.32,0.52],[0.18,0.52]],
      color: 'rgba(236,72,153,0.04)'
    },
    {
      id: 'old-town-dist',
      name: 'Old Town',
      centerX: 0.60, centerY: 0.70,
      polygon: [[0.52,0.62],[0.68,0.62],[0.68,0.78],[0.52,0.78]],
      color: 'rgba(245,158,11,0.05)'
    },
    {
      id: 'lake-gardens-dist',
      name: 'Lake Gardens',
      centerX: 0.75, centerY: 0.42,
      polygon: [[0.68,0.32],[0.82,0.32],[0.82,0.48],[0.68,0.48]],
      color: 'rgba(59,130,246,0.06)'
    },
    {
      id: 'golden-coast-dist',
      name: 'Golden Coast',
      centerX: 0.80, centerY: 0.80,
      polygon: [[0.72,0.72],[0.88,0.72],[0.88,0.88],[0.72,0.88]],
      color: 'rgba(245,158,11,0.04)'
    },
    {
      id: 'sunset-dist',
      name: 'Sunset Point',
      centerX: 0.20, centerY: 0.70,
      polygon: [[0.13,0.62],[0.27,0.62],[0.27,0.78],[0.13,0.78]],
      color: 'rgba(249,115,22,0.04)'
    }
  ],

  parks: [
    { id: 'central-park', name: 'Central Park', cx: 0.50, cy: 0.25, rx: 0.07, ry: 0.055 },
    { id: 'garden-park', name: 'Botanical Gardens', cx: 0.72, cy: 0.33, rx: 0.04, ry: 0.03 }
  ],

  waterBodies: [
    { id: 'main-lake', type: 'lake', cx: 0.78, cy: 0.40, rx: 0.055, ry: 0.045 },
    {
      id: 'river', type: 'river',
      path: [
        { cmd: 'M', x: 0.82, y: 0.05 },
        { cmd: 'C', x: 0.78, y: 0.18, cx1: 0.85, cy1: 0.15, cx2: 0.80, cy2: 0.25 },
        { cmd: 'C', x: 0.75, y: 0.38, cx1: 0.82, cy1: 0.32, cx2: 0.78, cy2: 0.40 },
        { cmd: 'C', x: 0.65, y: 0.70, cx1: 0.72, cy1: 0.48, cx2: 0.70, cy2: 0.60 },
        { cmd: 'C', x: 0.55, y: 0.95, cx1: 0.62, cy1: 0.78, cx2: 0.58, cy2: 0.88 }
      ]
    },
    {
      id: 'pond', type: 'pond',
      cx: 0.42, cy: 0.22, rx: 0.025, ry: 0.02
    }
  ],

  // Map backend location names to Atlas City intersection ids
  backendMapping: {
    'City Center': 'city-center',
    'Museum District': 'museum-circle',
    'Heritage Quarter': 'heritage-cross',
    'Market Square': 'market-hub',
    'Central Park': 'park-entrance',
    'Innovation District': 'innovation-hub',
    'Old Town': 'old-gate',
    'Lake Gardens': 'lake-view',
    'Golden Coast': 'coast-road',
    'Sunset Point': 'sunset-junction'
  },

  // Display info for backend locations
  locationDetails: {
    'City Center': { displayName: 'City Center', description: 'Heart of Atlas City', icon: '\uD83C\uDFD9' },
    'Museum District': { displayName: 'Museum District', description: 'Art and culture district', icon: '\uD83C\uDFDB' },
    'Heritage Quarter': { displayName: 'Heritage Quarter', description: 'Historic architecture', icon: '\uD83C\uDFF0' },
    'Market Square': { displayName: 'Market Square', description: 'Market district', icon: '\uD83D\uDED2' },
    'Central Park': { displayName: 'Central Park', description: 'Urban green space', icon: '\uD83C\uDF33' },
    'Innovation District': { displayName: 'Innovation District', description: 'Tech and science hub', icon: '\uD83D\uDCF1' },
    'Old Town': { displayName: 'Old Town', description: 'Historic neighborhood', icon: '\uD83C\uDFF0' },
    'Lake Gardens': { displayName: 'Lake Gardens', description: 'Scenic lake district', icon: '\uD83D\uDCA7' },
    'Golden Coast': { displayName: 'Golden Coast', description: 'Waterfront district', icon: '\uD83C\uDF0A' },
    'Sunset Point': { displayName: 'Sunset Point', description: 'Panoramic viewpoint', icon: '\u26F0' },
    // Hidden nodes - used for "via" text in route display
    'North Gate': { displayName: 'North Gate', description: 'Northern entrance' },
    'Park Entrance': { displayName: 'Park District', description: 'Park area' },
    'Main Junction': { displayName: 'Central Junction', description: 'Main intersection' },
    'South Bridge': { displayName: 'South Bridge', description: 'Southern crossing' },
    'West End': { displayName: 'West District', description: 'Western area' },
    'Museum Circle': { displayName: 'Museum Area', description: 'Museum vicinity' },
    'Heritage Cross': { displayName: 'Heritage Area', description: 'Heritage vicinity' },
    'Old Gate': { displayName: 'Old Gate Area', description: 'Historic gate' },
    'Market Hub': { displayName: 'Market District', description: 'Market area' },
    'East Crossing': { displayName: 'East Crossing', description: 'Eastern crossing' }
  }
};

// Pre-compute lookup maps
const intersectionMap = {};
ATLAS_CITY.intersections.forEach((int, i) => { intersectionMap[int.id] = i; });
const intersectionById = {};
ATLAS_CITY.intersections.forEach((int, i) => { intersectionById[int.id] = int; });

// Build adjacency list for pathfinding
function buildGraph() {
  const graph = {};
  for (let i = 0; i < ATLAS_CITY.intersections.length; i++) {
    graph[i] = [];
  }
  for (const edge of ATLAS_CITY.edges) {
    graph[edge.from].push({ to: edge.to, weight: edge.distance });
    graph[edge.to].push({ to: edge.from, weight: edge.distance });
  }
  return graph;
}

const ROAD_GRAPH = buildGraph();

// ── Dijkstra on road graph ─────────────────────────────────────────────────
function findRoadPath(startIdx, endIdx) {
  if (startIdx === endIdx) return [startIdx];
  const n = ATLAS_CITY.intersections.length;
  const dist = new Array(n).fill(Infinity);
  const prev = new Array(n).fill(-1);
  const visited = new Array(n).fill(false);
  dist[startIdx] = 0;

  for (let i = 0; i < n; i++) {
    let minDist = Infinity;
    let u = -1;
    for (let j = 0; j < n; j++) {
      if (!visited[j] && dist[j] < minDist) {
        minDist = dist[j];
        u = j;
      }
    }
    if (u === -1 || u === endIdx) break;
    visited[u] = true;
    for (const nb of ROAD_GRAPH[u]) {
      if (!visited[nb.to] && dist[u] + nb.weight < dist[nb.to]) {
        dist[nb.to] = dist[u] + nb.weight;
        prev[nb.to] = u;
      }
    }
  }

  const path = [];
  let curr = endIdx;
  while (curr !== -1) {
    path.unshift(curr);
    curr = prev[curr];
  }
  return path;
}

// ═══════════════════════════════════════════════════════════════════════════
// ATLAS CITY MAP - SVG Map Generation & Management
// ═══════════════════════════════════════════════════════════════════════════

class AtlasCityMap {
  constructor(containerId, options = {}) {
    this.container = document.getElementById(containerId);
    if (!this.container) return;
    this.options = {
      interactive: true,
      mini: false,
      showRouteLabels: true,
      ...options
    };

    this.svg = null;
    this.mapGroup = null;
    this.zoomLevel = 1;
    this.panOffset = { x: 0, y: 0 };
    this.routePathEl = null;
    this.routePoints = [];
    this.startMarker = null;
    this.endMarker = null;
    this.pinElements = [];
    this.tooltip = null;
    this.animFrame = null;
    this.hoveredPin = null;

    this._init();
  }

  _init() {
    this._createTooltip();
    this._buildSVG();
    this._render();
  }

  _toSVG(x, y) {
    return { x: x * 1000, y: y * 800 };
  }

  _createTooltip() {
    this.tooltip = document.createElement('div');
    this.tooltip.className = 'map-tooltip hidden';
    document.body.appendChild(this.tooltip);
  }

  _showTooltip(text, e) {
    this.tooltip.textContent = text;
    this.tooltip.classList.remove('hidden');
    const rect = this.tooltip.getBoundingClientRect();
    let tx = e.clientX - rect.width / 2;
    let ty = e.clientY - rect.height - 16;
    if (tx < 4) tx = 4;
    if (tx + rect.width > window.innerWidth - 4) tx = window.innerWidth - rect.width - 4;
    if (ty < 4) ty = e.clientY + 16;
    this.tooltip.style.left = tx + 'px';
    this.tooltip.style.top = ty + 'px';
  }

  _hideTooltip() {
    this.tooltip.classList.add('hidden');
  }

  _buildSVG() {
    this.container.innerHTML = '';
    this.container.style.position = 'relative';
    this.container.style.overflow = 'hidden';

    this.svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    this.svg.setAttribute('viewBox', '0 0 1000 800');
    this.svg.setAttribute('preserveAspectRatio', 'xMidYMid meet');
    this.svg.style.width = '100%';
    this.svg.style.height = '100%';
    this.svg.style.display = 'block';
    this.svg.style.background = '#F8FAFC';
    this.svg.style.borderRadius = '12px';

    this.mapGroup = document.createElementNS('http://www.w3.org/2000/svg', 'g');
    this.svg.appendChild(this.mapGroup);

    // Defs for glow filter
    const defs = document.createElementNS('http://www.w3.org/2000/svg', 'defs');
    const filter = document.createElementNS('http://www.w3.org/2000/svg', 'filter');
    filter.setAttribute('id', 'route-glow');
    filter.innerHTML = '<feGaussianBlur stdDeviation="6" result="blur"/>'
      + '<feMerge><feMergeNode in="blur"/><feMergeNode in="SourceGraphic"/></feMerge>';
    defs.appendChild(filter);

    const dashFilter = document.createElementNS('http://www.w3.org/2000/svg', 'filter');
    dashFilter.setAttribute('id', 'pulse-filter');
    dashFilter.innerHTML = '<feGaussianBlur stdDeviation="3" result="blur"/>'
      + '<feMerge><feMergeNode in="blur"/><feMergeNode in="SourceGraphic"/></feMerge>';
    defs.appendChild(dashFilter);
    this.svg.appendChild(defs);

    this.container.appendChild(this.svg);

    if (this.options.interactive) {
      this._setupInteraction();
    }
  }

  _setupInteraction() {
    // Pin hover via delegation
    this.svg.addEventListener('mousemove', (e) => {
      // handled by individual pin handlers
    });

    // Zoom with scroll
    this.svg.addEventListener('wheel', (e) => {
      e.preventDefault();
      const delta = e.deltaY > 0 ? -0.1 : 0.1;
      this._adjustZoom(delta, e);
    }, { passive: false });
  }

  _adjustZoom(delta, e) {
    const oldZoom = this.zoomLevel;
    this.zoomLevel = Math.max(0.5, Math.min(2.5, this.zoomLevel + delta));
    if (this.zoomLevel !== oldZoom) {
      this._applyTransform();
    }
  }

  _applyTransform() {
    if (this.mapGroup) {
      const cx = 500, cy = 400;
      const tx = cx - (cx * this.zoomLevel) + this.panOffset.x;
      const ty = cy - (cy * this.zoomLevel) + this.panOffset.y;
      this.mapGroup.setAttribute('transform', `translate(${tx},${ty}) scale(${this.zoomLevel})`);
    }
  }

  setZoom(z) {
    this.zoomLevel = Math.max(0.5, Math.min(2.5, z));
    this._applyTransform();
  }

  zoomIn() {
    this.setZoom(this.zoomLevel + 0.2);
  }

  zoomOut() {
    this.setZoom(this.zoomLevel - 0.2);
  }

  fitToRoute() {
    if (!this.routePoints || this.routePoints.length < 2) return;
    let minX = Infinity, minY = Infinity, maxX = -Infinity, maxY = -Infinity;
    for (const idx of this.routePoints) {
      const int = ATLAS_CITY.intersections[idx];
      if (int.x < minX) minX = int.x;
      if (int.y < minY) minY = int.y;
      if (int.x > maxX) maxX = int.x;
      if (int.y > maxY) maxY = int.y;
    }
    const pad = 0.08;
    const rw = maxX - minX + pad * 2;
    const rh = maxY - minY + pad * 2;
    const cx = (minX + maxX) / 2;
    const cy = (minY + maxY) / 2;
    const zx = 1 / (rw || 0.5);
    const zy = 1 / (rh || 0.5);
    const zoom = Math.min(zx, zy) * 0.9;
    this.zoomLevel = Math.max(0.5, Math.min(2.5, zoom));
    this.panOffset = { x: 0, y: 0 };
    this._applyTransform();
  }

  resetView() {
    this.zoomLevel = 1;
    this.panOffset = { x: 0, y: 0 };
    this._applyTransform();
  }

  // ── SVG Element Helpers ──────────────────────────────────────────────────
  _addRect(parent, x, y, w, h, fill, attr) {
    const r = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
    r.setAttribute('x', x); r.setAttribute('y', y);
    r.setAttribute('width', w); r.setAttribute('height', h);
    r.setAttribute('fill', fill);
    if (attr) for (const k in attr) r.setAttribute(k, attr[k]);
    parent.appendChild(r);
    return r;
  }

  _addEllipse(parent, cx, cy, rx, ry, fill, attr) {
    const e = document.createElementNS('http://www.w3.org/2000/svg', 'ellipse');
    e.setAttribute('cx', cx); e.setAttribute('cy', cy);
    e.setAttribute('rx', rx); e.setAttribute('ry', ry);
    e.setAttribute('fill', fill);
    if (attr) for (const k in attr) e.setAttribute(k, attr[k]);
    parent.appendChild(e);
    return e;
  }

  _addPolygon(parent, points, fill, attr) {
    const p = document.createElementNS('http://www.w3.org/2000/svg', 'polygon');
    p.setAttribute('points', points.map(pt => pt.join(',')).join(' '));
    p.setAttribute('fill', fill);
    if (attr) for (const k in attr) p.setAttribute(k, attr[k]);
    parent.appendChild(p);
    return p;
  }

  _addLine(parent, x1, y1, x2, y2, stroke, width, attr) {
    const l = document.createElementNS('http://www.w3.org/2000/svg', 'line');
    l.setAttribute('x1', x1); l.setAttribute('y1', y1);
    l.setAttribute('x2', x2); l.setAttribute('y2', y2);
    l.setAttribute('stroke', stroke);
    l.setAttribute('stroke-width', width);
    if (attr) for (const k in attr) l.setAttribute(k, attr[k]);
    parent.appendChild(l);
    return l;
  }

  _addText(parent, x, y, text, fontSize, fill, attr) {
    const t = document.createElementNS('http://www.w3.org/2000/svg', 'text');
    t.setAttribute('x', x); t.setAttribute('y', y);
    t.setAttribute('font-size', fontSize);
    t.setAttribute('fill', fill);
    t.setAttribute('text-anchor', 'middle');
    t.setAttribute('dominant-baseline', 'central');
    if (attr) for (const k in attr) t.setAttribute(k, attr[k]);
    t.textContent = text;
    parent.appendChild(t);
    return t;
  }

  _addCircle(parent, cx, cy, r, fill, attr) {
    const c = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
    c.setAttribute('cx', cx); c.setAttribute('cy', cy);
    c.setAttribute('r', r); c.setAttribute('fill', fill);
    if (attr) for (const k in attr) c.setAttribute(k, attr[k]);
    parent.appendChild(c);
    return c;
  }

  _addPath(parent, d, stroke, width, fill, attr) {
    const p = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    p.setAttribute('d', d);
    if (stroke) p.setAttribute('stroke', stroke);
    if (width) p.setAttribute('stroke-width', width);
    p.setAttribute('fill', fill || 'none');
    if (attr) for (const k in attr) p.setAttribute(k, attr[k]);
    parent.appendChild(p);
    return p;
  }

  // ── Main Render ─────────────────────────────────────────────────────────
  _render() {
    const g = this.mapGroup;
    g.innerHTML = '';

    // Layer order (bottom → top):
    //  1 Base         background + grid
    //  2 Water        lake, river, pond
    //  3 Parks        green spaces + trees
    //  4 Districts    colored overlays
    //  5 Dist labels  district name text
    //  6 Roads        road network (gray lines, 2px)
    //  7 Intersect    intersection dots + labels
    //  8 ROUTE        highlighted path (blue glow + stroke + S/E markers)
    //  9 Pins         district attraction markers
    // 10 Mini adj     hide labels in mini mode

    // Layer 1: Base
    this._addRect(g, 0, 0, 1000, 800, '#F8FAFC');
    for (let x = 0; x <= 1000; x += 50) {
      this._addLine(g, x, 0, x, 800, '#E8EDF3', 0.5);
    }
    for (let y = 0; y <= 800; y += 50) {
      this._addLine(g, 0, y, 1000, y, '#E8EDF3', 0.5);
    }

    // Layer 2: Water
    this._renderWater(g);

    // Layer 3: Parks
    this._renderParks(g);

    // Layer 4: Districts
    this._renderDistricts(g);

    // Layer 5: District labels
    this._renderDistrictLabels(g);

    // Layer 6: Roads
    this._renderRoads(g);

    // Layer 7: Intersection labels
    this._renderIntersectionLabels(g);

    // Layer 8: Route (above roads, below markers)
    if (this.routePoints && this.routePoints.length > 1) {
      this._renderRoute(g);
    }

    // Layer 9: Pins (above route)
    this._renderPins(g);

    // Layer 10: Mini mode adjustments
    if (this.options.mini) {
      g.querySelectorAll('.district-label, .intersection-label, .grid-line').forEach(el => {
        el.style.display = 'none';
      });
    }

    this._applyTransform();
  }

  // ── Render Layers ────────────────────────────────────────────────────────
  _renderWater(g) {
    for (const w of ATLAS_CITY.waterBodies) {
      if (w.type === 'lake' || w.type === 'pond') {
        const c = this._toSVG(w.cx, w.cy);
        const rx = w.rx * 1000;
        const ry = w.ry * 800;
        this._addEllipse(g, c.x, c.y, rx, ry, '#DBEAFE', { stroke: '#BFDBFE', 'stroke-width': 1 });
      } else if (w.type === 'river') {
        let d = '';
        for (const pt of w.path) {
          const c = this._toSVG(pt.x, pt.y);
          if (pt.cmd === 'M') d += `M ${c.x} ${c.y} `;
          else if (pt.cmd === 'C') {
            const c1 = this._toSVG(pt.cx1, pt.cy1);
            const c2 = this._toSVG(pt.cx2, pt.cy2);
            d += `C ${c1.x} ${c1.y}, ${c2.x} ${c2.y}, ${c.x} ${c.y} `;
          }
        }
        this._addPath(g, d, '#BFDBFE', 12, 'rgba(219,234,254,0.6)');
        this._addPath(g, d, '#93C5FD', 4, 'none');
      }
    }
  }

  _renderParks(g) {
    for (const park of ATLAS_CITY.parks) {
      const c = this._toSVG(park.cx, park.cy);
      const rx = park.rx * 1000;
      const ry = park.ry * 800;
      this._addEllipse(g, c.x, c.y, rx, ry, '#D1FAE5', { stroke: '#A7F3D0', 'stroke-width': 1 });
      // Tree markers
      const treePositions = [
        [-0.04, -0.03], [0.03, -0.04], [-0.02, 0.03],
        [0.04, 0.02], [-0.05, 0.0], [0.02, -0.02],
        [0.0, 0.04], [-0.03, -0.01]
      ];
      if (!this.options.mini) {
        for (const tp of treePositions) {
          const tc = this._toSVG(park.cx + tp[0], park.cy + tp[1]);
          this._addCircle(g, tc.x, tc.y, 2.5, 'rgba(16,185,129,0.3)');
        }
      }
    }
  }

  _renderDistricts(g) {
    for (const dist of ATLAS_CITY.districts) {
      const pts = dist.polygon.map(p => this._toSVG(p[0], p[1]));
      this._addPolygon(g, pts.map(p => [p.x, p.y]), dist.color, {
        stroke: 'rgba(0,0,0,0.03)',
        'stroke-width': 0.5
      });
    }
  }

  _renderDistrictLabels(g) {
    for (const dist of ATLAS_CITY.districts) {
      const c = this._toSVG(dist.centerX, dist.centerY);
      const lbl = document.createElementNS('http://www.w3.org/2000/svg', 'text');
      lbl.setAttribute('x', c.x);
      lbl.setAttribute('y', c.y);
      lbl.setAttribute('font-size', '11');
      lbl.setAttribute('fill', '#94A3B8');
      lbl.setAttribute('text-anchor', 'middle');
      lbl.setAttribute('dominant-baseline', 'central');
      lbl.setAttribute('font-weight', '500');
      lbl.setAttribute('font-family', 'system-ui, sans-serif');
      lbl.textContent = dist.name;
      lbl.classList.add('district-label');
      g.appendChild(lbl);
    }
  }

  _renderRoads(g) {
    for (const edge of ATLAS_CITY.edges) {
      const from = ATLAS_CITY.intersections[edge.from];
      const to = ATLAS_CITY.intersections[edge.to];
      const f = this._toSVG(from.x, from.y);
      const t = this._toSVG(to.x, to.y);
      this._addLine(g, f.x, f.y, t.x, t.y, '#CBD5E1', 2, { 'stroke-linecap': 'round' });
    }
  }

  _renderIntersectionLabels(g) {
    if (this.options.mini) return;
    for (const int of ATLAS_CITY.intersections) {
      // Only render visible locations - hide internal graph nodes
      if (!ATLAS_CITY.visibleLocations.has(int.name)) continue;

      const c = this._toSVG(int.x, int.y);
      // Small dot
      this._addCircle(g, c.x, c.y, 4, '#CBD5E1', { stroke: '#94A3B8', 'stroke-width': 1.5 });
      // Label
      const lbl = document.createElementNS('http://www.w3.org/2000/svg', 'text');
      lbl.setAttribute('x', c.x);
      lbl.setAttribute('y', c.y + 16);
      lbl.setAttribute('font-size', '10');
      lbl.setAttribute('fill', '#64748B');
      lbl.setAttribute('text-anchor', 'middle');
      lbl.setAttribute('dominant-baseline', 'central');
      lbl.setAttribute('font-weight', '600');
      lbl.setAttribute('font-family', 'system-ui, sans-serif');
      lbl.textContent = int.name;
      lbl.classList.add('intersection-label');
      g.appendChild(lbl);
    }
  }

  _renderPins(g) {
    this.pinElements = [];
    // One pin per district (serves as attraction marker)
    for (const dist of ATLAS_CITY.districts) {
      const c = this._toSVG(dist.centerX, dist.centerY);
      const pinGroup = document.createElementNS('http://www.w3.org/2000/svg', 'g');
      pinGroup.classList.add('map-pin');
      pinGroup.dataset.pinId = dist.id;
      pinGroup.style.cursor = 'pointer';

      // Pin shadow
      this._addEllipse(pinGroup, c.x + 2, c.y + 4, 10, 4, 'rgba(0,0,0,0.1)');

      // Pin icon (diamond shape)
      const pinShape = document.createElementNS('http://www.w3.org/2000/svg', 'path');
      pinShape.setAttribute('d', `M${c.x},${c.y - 14} L${c.x + 10},${c.y} L${c.x},${c.y + 10} L${c.x - 10},${c.y} Z`);
      pinShape.setAttribute('fill', '#3B82F6');
      pinShape.setAttribute('stroke', '#2563EB');
      pinShape.setAttribute('stroke-width', '1.5');
      pinGroup.appendChild(pinShape);

      // Inner circle
      this._addCircle(pinGroup, c.x, c.y, 4, '#FFFFFF');

      // Pin label
      if (!this.options.mini) {
        const lbl = document.createElementNS('http://www.w3.org/2000/svg', 'text');
        lbl.setAttribute('x', c.x);
        lbl.setAttribute('y', c.y + 24);
        lbl.setAttribute('font-size', '9');
        lbl.setAttribute('fill', '#475569');
        lbl.setAttribute('font-weight', '600');
        lbl.setAttribute('text-anchor', 'middle');
        lbl.setAttribute('dominant-baseline', 'central');
        lbl.setAttribute('font-family', 'system-ui, sans-serif');
        lbl.textContent = dist.name;
        pinGroup.appendChild(lbl);
      }

      if (this.options.interactive) {
        pinGroup.addEventListener('mouseenter', (e) => {
          this.hoveredPin = dist;
          pinShape.setAttribute('fill', '#2563EB');
          pinShape.setAttribute('transform', 'scale(1.15) translate(' + (c.x * -0.15) + ',' + (c.y * -0.15) + ')');
          this._showTooltip(dist.name + ' District', e);
        });
        pinGroup.addEventListener('mousemove', (e) => {
          this._showTooltip(dist.name + ' District', e);
        });
        pinGroup.addEventListener('mouseleave', () => {
          this.hoveredPin = null;
          pinShape.setAttribute('fill', '#3B82F6');
          pinShape.setAttribute('transform', '');
          this._hideTooltip();
        });
        pinGroup.addEventListener('click', () => {
          // Find backend locations near this district
          const locs = Object.entries(ATLAS_CITY.backendMapping)
            .filter(([key, val]) => {
              const int = intersectionById[val];
              if (!int) return false;
              const dx = int.x - dist.centerX;
              const dy = int.y - dist.centerY;
              return Math.sqrt(dx*dx + dy*dy) < 0.12;
            })
            .map(([key]) => key);
          if (locs.length > 0) {
            this._showTooltip(dist.name + ': ' + locs.join(', '), { clientX: parseFloat(pinGroup.querySelector('text')?.getAttribute('x') || 500), clientY: parseFloat(pinGroup.querySelector('text')?.getAttribute('y') || 400) });
          }
        });
      }

      g.appendChild(pinGroup);
      this.pinElements.push(pinGroup);
    }
  }

  _renderRoute(g) {
    if (!this.routePoints || this.routePoints.length < 2) return;

    // Build segment path data
    let d = '';
    for (let i = 0; i < this.routePoints.length; i++) {
      const int = ATLAS_CITY.intersections[this.routePoints[i]];
      if (!int) continue;
      const c = this._toSVG(int.x, int.y);
      d += (i === 0 ? 'M' : 'L') + ` ${c.x} ${c.y} `;
    }

    // Remove old route if exists
    const oldRoute = g.querySelector('.route-layer');
    if (oldRoute) oldRoute.remove();

    const routeLayer = document.createElementNS('http://www.w3.org/2000/svg', 'g');
    routeLayer.classList.add('route-layer');

    // ── Route path layers ────────────────────────────────────────────────

    // Outer glow (wider, more visible)
    this._addPath(routeLayer, d, 'rgba(37,99,235,0.25)', 16, 'none', {
      'stroke-linecap': 'round', 'stroke-linejoin': 'round',
      'filter': 'url(#route-glow)'
    });

    // Mid glow
    this._addPath(routeLayer, d, 'rgba(59,130,246,0.4)', 10, 'none', {
      'stroke-linecap': 'round', 'stroke-linejoin': 'round'
    });

    // Main route line - bright blue, 7px, rounded
    this._addPath(routeLayer, d, '#2563EB', 7, 'none', {
      'stroke-linecap': 'round', 'stroke-linejoin': 'round'
    });

    // Animated center line (progressive drawing effect)
    const totalLength = d.length * 8;
    const dashPath = this._addPath(routeLayer, d, '#93C5FD', 3, 'none', {
      'stroke-linecap': 'round', 'stroke-linejoin': 'round',
      'stroke-dasharray': totalLength + 1000,
      'stroke-dashoffset': totalLength + 1000,
      'class': 'route-dash'
    });

    // ── Start marker (green) ────────────────────────────────────────────
    if (this.routePoints.length > 0) {
      const s = ATLAS_CITY.intersections[this.routePoints[0]];
      if (s) {
        const sc = this._toSVG(s.x, s.y);
        this._addCircle(routeLayer, sc.x, sc.y, 16, '#10B981', { stroke: '#047857', 'stroke-width': 2.5 });
        this._addCircle(routeLayer, sc.x, sc.y, 7, '#FFFFFF');
        this._addText(routeLayer, sc.x, sc.y, 'S', 13, '#10B981', { 'font-weight': '900' });
        if (this.options.showRouteLabels) {
          this._addText(routeLayer, sc.x, sc.y + 24, 'Start', 11, '#047857', { 'font-weight': '700' });
        }
      }
    }

    // ── End marker (red) ────────────────────────────────────────────────
    if (this.routePoints.length > 1) {
      const e = ATLAS_CITY.intersections[this.routePoints[this.routePoints.length - 1]];
      if (e) {
        const ec = this._toSVG(e.x, e.y);
        this._addCircle(routeLayer, ec.x, ec.y, 16, '#EF4444', { stroke: '#B91C1C', 'stroke-width': 2.5 });
        this._addCircle(routeLayer, ec.x, ec.y, 7, '#FFFFFF');
        this._addText(routeLayer, ec.x, ec.y, 'E', 13, '#EF4444', { 'font-weight': '900' });
        if (this.options.showRouteLabels) {
          this._addText(routeLayer, ec.x, ec.y + 24, 'End', 11, '#B91C1C', { 'font-weight': '700' });
        }
      }
    }

    // ── Intermediate waypoints (only visible locations) ───────────────────
    for (let i = 1; i < this.routePoints.length - 1; i++) {
      const int = ATLAS_CITY.intersections[this.routePoints[i]];
      if (!int) continue;
      // Skip hidden internal graph nodes - don't show markers for them
      if (!ATLAS_CITY.visibleLocations.has(int.name)) continue;

      const ic = this._toSVG(int.x, int.y);
      this._addCircle(routeLayer, ic.x, ic.y, 8, '#2563EB', { stroke: '#FFFFFF', 'stroke-width': 2.5 });
      // Show "via" label for visible intermediate stops
      this._addText(routeLayer, ic.x, ic.y + 18, 'via ' + int.name, 9, '#64748B', {
        'font-weight': '500', 'font-style': 'italic'
      });
    }

    g.appendChild(routeLayer);
    this.routePathEl = routeLayer;

    // Start progressive route drawing animation
    this._startProgressiveAnimation(dashPath, totalLength);
  }

  _startProgressiveAnimation(dashPath, totalLength) {
    if (!dashPath) return;
    const duration = 1000;
    const startTime = performance.now();
    if (this.animFrame) cancelAnimationFrame(this.animFrame);
    const animate = (now) => {
      const elapsed = now - startTime;
      const progress = Math.min(elapsed / duration, 1);
      const offset = totalLength + 1000 - (totalLength + 1000) * progress;
      dashPath.setAttribute('stroke-dashoffset', offset);
      if (progress < 1) {
        this.animFrame = requestAnimationFrame(animate);
      } else {
        // Once drawn, start the flowing dash animation
        this._startDashAnimation(dashPath);
      }
    };
    this.animFrame = requestAnimationFrame(animate);
  }

  _startDashAnimation(dashPath) {
    if (!dashPath) return;
    dashPath.setAttribute('stroke-dasharray', '12,16');
    let offset = 0;
    const animate = () => {
      offset = (offset - 0.8) % 28;
      dashPath.setAttribute('stroke-dashoffset', offset);
      this.animFrame = requestAnimationFrame(animate);
    };
    if (this.animFrame) cancelAnimationFrame(this.animFrame);
    animate();
  }

  _startDashAnimationFromBegin(dashPath) {
    if (!dashPath) return;
    let offset = 0;
    const animate = () => {
      offset = (offset - 0.5) % 16;
      dashPath.setAttribute('stroke-dashoffset', offset);
      this.animFrame = requestAnimationFrame(animate);
    };
    if (this.animFrame) cancelAnimationFrame(this.animFrame);
    animate();
  }

  stopAnimation() {
    if (this.animFrame) {
      cancelAnimationFrame(this.animFrame);
      this.animFrame = null;
    }
  }

  // ── Route Management ─────────────────────────────────────────────────────
  setRoute(backendPath) {
    console.log('=== ROUTE RENDER DEBUG ===');
    console.log('Backend path received:', backendPath);

    if (!backendPath || backendPath.length < 2) {
      console.log('Path too short, clearing route');
      this.clearRoute();
      return;
    }

    // Map backend locations to intersection indices
    const mapped = [];
    for (const loc of backendPath) {
      const intId = ATLAS_CITY.backendMapping[loc];
      if (intId && intersectionMap[intId] !== undefined) {
        mapped.push(intersectionMap[intId]);
      } else {
        console.log(`Skipping unmapped node: "${loc}"`);
      }
    }

    console.log('Mapped intersection indices:', mapped);
    console.log('Start node index:', mapped[0], '-', ATLAS_CITY.intersections[mapped[0]]?.name);
    const lastIdx = mapped[mapped.length - 1];
    console.log('End node index:', lastIdx, '-', ATLAS_CITY.intersections[lastIdx]?.name);

    if (mapped.length < 2) {
      console.log('Not enough mapped nodes (<2), clearing route');
      this.clearRoute();
      return;
    }

    // Find road-following path through the graph
    const fullPath = findRoadPath(mapped[0], lastIdx);
    console.log('Road path indices:', fullPath);
    console.log('Road path names:', fullPath.map(i => ATLAS_CITY.intersections[i]?.name));
    console.log('Route coordinates:', fullPath.map(i => {
      const int = ATLAS_CITY.intersections[i];
      if (!int) return `undefined index ${i}`;
      const svg = this._toSVG(int.x, int.y);
      return `${int.name}: (${int.x}, ${int.y}) → SVG (${svg.x}, ${svg.y})`;
    }));
    console.log('Path node count:', fullPath.length);

    this.routePoints = fullPath;
    this._render();
  }

  clearRoute() {
    this.routePoints = [];
    this.stopAnimation();
    const routeLayer = this.mapGroup.querySelector('.route-layer');
    if (routeLayer) routeLayer.remove();
    this.routePathEl = null;
  }

  destroy() {
    this.stopAnimation();
    this._hideTooltip();
    if (this.tooltip && this.tooltip.parentNode) {
      this.tooltip.parentNode.removeChild(this.tooltip);
    }
    this.container.innerHTML = '';
  }
}

// ═══════════════════════════════════════════════════════════════════════════
// SPA ROUTER
// ═══════════════════════════════════════════════════════════════════════════

const Router = {
  currentPage: 'dashboard',

  init() {
    window.addEventListener('hashchange', () => this.navigate(location.hash.slice(1) || 'dashboard'));
    this.navigate(location.hash.slice(1) || 'dashboard');
  },

  navigate(page) {
    this.currentPage = page || 'dashboard';
    document.querySelectorAll('.sidebar-item').forEach(el => {
      el.classList.toggle('active', el.dataset.page === this.currentPage);
    });
    document.querySelectorAll('.content-page').forEach(el => {
      el.classList.toggle('active-page', el.id === `page-${this.currentPage}`);
    });
    const activeItem = document.querySelector(`.sidebar-item[data-page="${this.currentPage}"]`);
    if (activeItem) {
      document.getElementById('page-title').textContent = activeItem.dataset.title;
    }
    document.getElementById('page-subtitle').textContent = this.getSubtitle(this.currentPage);
    this.onPageLoad(this.currentPage);
  },

  getSubtitle(page) {
    const subs = {
      dashboard: 'Your Atlas City trip at a glance',
      'trip-planner': 'Build your perfect day with smart budget optimization',
      'route-planner': 'Find the best path through Atlas City',
      'rankings': 'Discover the best attractions in Atlas City',
      'settings': 'Workspace settings and preferences'
    };
    return subs[page] || '';
  },

  async onPageLoad(page) {
    if (appState.dashboardMap) {
      appState.dashboardMap.stopAnimation();
    }
    if (appState.routeMap) {
      appState.routeMap.stopAnimation();
    }
    if (page === 'dashboard') await loadDashboard();
    else if (page === 'trip-planner') await loadBudgetPlanner();
    else if (page === 'route-planner') await loadRoutePlanner();
    else if (page === 'rankings') await loadDiscover();
  }
};

// ── Application State ─────────────────────────────────────────────────────
let appState = {
  username: 'Traveler',
  locations: [],
  edges: [],
  dashboardMap: null,
  routeMap: null,
  lastRouteResult: null,
  lastBudgetResult: null,
  attractions: [
    { name: 'Grand Museum', location: 'Museum District', description: 'World-class art and history museum', cost: 150, value: 95, rating: 5, duration: '3h', category: 'Culture' },
    { name: 'Central Gardens', location: 'Central Park', description: 'Botanical gardens with walking trails', cost: 50, value: 80, rating: 4, duration: '2h', category: 'Nature' },
    { name: 'Harbor Walk', location: 'Golden Coast', description: 'Scenic waterfront promenade', cost: 30, value: 65, rating: 4, duration: '1.5h', category: 'Leisure' },
    { name: 'Heritage Walk', location: 'Heritage Quarter', description: 'Historic architecture guided tour', cost: 80, value: 90, rating: 5, duration: '2.5h', category: 'Culture' },
    { name: 'Sunset Peak', location: 'Sunset Point', description: 'Panoramic city viewpoint', cost: 25, value: 70, rating: 4, duration: '1h', category: 'Nature' },
    { name: 'Innovation Hub', location: 'Innovation District', description: 'Interactive tech and science museum', cost: 100, value: 85, rating: 4, duration: '3h', category: 'Education' },
    { name: 'Market Bazaar', location: 'Market Square', description: 'Local artisan market and food stalls', cost: 40, value: 60, rating: 3, duration: '2h', category: 'Food' },
    { name: 'Lake Cruise', location: 'Lake Gardens', description: 'Scenic boat tour on Crescent Lake', cost: 90, value: 75, rating: 4, duration: '2h', category: 'Leisure' },
    { name: 'Old Town Alley', location: 'Old Town', description: 'Charming historic alleyway cafes', cost: 60, value: 70, rating: 4, duration: '1.5h', category: 'Culture' },
    { name: 'City View Deck', location: 'City Center', description: '360-degree observation deck', cost: 70, value: 78, rating: 4, duration: '2h', category: 'Education' },
    { name: 'Science Museum', location: 'Innovation District', description: 'Hands-on science exhibitions', cost: 180, value: 92, rating: 5, duration: '3h', category: 'Education' },
    { name: 'Sunset Beach', location: 'Golden Coast', description: 'Relaxing beach with sunset views', cost: 20, value: 50, rating: 3, duration: '2h', category: 'Leisure' }
  ]
};

// ═══════════════════════════════════════════════════════════════════════════
// LOGIN
// ═══════════════════════════════════════════════════════════════════════════

function initLogin() {
  const loginBtn = document.getElementById('login-btn');
  const userInput = document.getElementById('username-input');
  const passInput = document.getElementById('password-input');

  function doLogin() {
    const username = userInput.value.trim() || 'Traveler';
    appState.username = username;
    document.getElementById('user-name').textContent = username;
    document.getElementById('dash-hero-username').textContent = username;
    const avatar = document.querySelector('.user-avatar');
    if (avatar) avatar.textContent = username.charAt(0).toUpperCase();

    document.getElementById('login-page').classList.add('hidden');
    document.getElementById('app-shell').classList.remove('hidden');
    Router.init();
    loadAppData();
  }

  loginBtn.addEventListener('click', doLogin);
  passInput.addEventListener('keydown', e => { if (e.key === 'Enter') doLogin(); });
  userInput.addEventListener('keydown', e => { if (e.key === 'Enter') passInput.focus(); });
}

// ── Load App Data ─────────────────────────────────────────────────────────
async function loadAppData() {
  try {
    const [locs] = await Promise.all([API.getLocations()]);
    appState.locations = locs;
    populateLocationDropdowns();
  } catch (e) {
    console.error('Failed to load app data:', e);
  }
}

function populateLocationDropdowns() {
  const selects = ['start-location', 'end-location'];
  for (const id of selects) {
    const sel = document.getElementById(id);
    if (!sel) continue;
    sel.innerHTML = '';
    for (const loc of appState.locations) {
      const opt = document.createElement('option');
      const detail = ATLAS_CITY.locationDetails[loc];
      opt.value = loc;
      opt.textContent = detail ? `${detail.icon || ''} ${detail.displayName}` : loc;
      sel.appendChild(opt);
    }
  }
}

// ═══════════════════════════════════════════════════════════════════════════
// DASHBOARD
// ═══════════════════════════════════════════════════════════════════════════

async function loadDashboard() {
  try {
    const data = await API.getDashboard();
    document.getElementById('stat-attractions').textContent = data.attractionCount || '12';
    document.getElementById('stat-locations').textContent = data.locationCount || '10';
    document.getElementById('stat-algorithms').textContent = data.algorithmCount || '3';
    document.getElementById('stat-offline').textContent = '100%';
  } catch (e) {
    console.error('Dashboard load error:', e);
  }

  // Create dashboard mini map
  if (!appState.dashboardMap) {
    setTimeout(() => {
      const container = document.getElementById('dashboard-map-container');
      if (container) {
        appState.dashboardMap = new AtlasCityMap('dashboard-map-container', {
          interactive: true,
          mini: true,
          showRouteLabels: false
        });
      }
    }, 200);
  }

  // Populate dashboard cards
  _populateUpcomingJourney();
  _populateBudgetSnapshot();
  _populateRouteSnapshot();
  _populateDiscoverSnapshot();
}

function _populateUpcomingJourney() {
  const container = document.getElementById('upcoming-journey-content');
  if (!container) return;
  // Pick a random attraction as "next journey"
  const attrs = appState.attractions || [];
  if (attrs.length > 0) {
    const a = attrs[Math.floor(Math.random() * attrs.length)];
    container.innerHTML = `
      <div class="dash-card-icon" style="background:rgba(59,130,246,0.12)">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#3B82F6" stroke-width="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg>
      </div>
      <div class="dash-card-info">
        <span class="dash-card-value">${a.name}</span>
        <span class="dash-card-label">${a.location} · ${a.duration} · $${a.cost}</span>
      </div>
    `;
  }
}

function _populateBudgetSnapshot() {
  const container = document.getElementById('budget-snapshot-content');
  if (!container) return;
  const result = appState.lastBudgetResult;
  if (result && result.selected) {
    const pct = result.totalCost / (result.totalCost + result.remainingBudget) * 100;
    container.innerHTML = `
      <div style="display:flex;justify-content:space-between;margin-bottom:8px">
        <span class="dash-card-label">Budget Used</span>
        <span class="dash-card-value" style="font-size:16px">$${result.totalCost} / $${result.totalCost + result.remainingBudget}</span>
      </div>
      <div class="progress-bar">
        <div class="progress-fill" style="width:${Math.round(pct)}%"></div>
      </div>
      <div style="display:flex;justify-content:space-between;margin-top:6px">
        <span class="dash-card-label">${result.selected.length} experiences</span>
        <span class="dash-card-label" style="color:var(--accent)">$${result.remainingBudget} remaining</span>
      </div>
    `;
  } else {
    container.innerHTML = `
      <div class="dash-card-icon" style="background:rgba(245,158,11,0.12)">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#F59E0B" stroke-width="2"><circle cx="12" cy="12" r="3"/><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/></svg>
      </div>
      <div class="dash-card-info">
        <span class="dash-card-value">No Budget Set</span>
        <span class="dash-card-label">Plan a trip to see budget</span>
      </div>
    `;
  }
}

function _populateRouteSnapshot() {
  const container = document.getElementById('route-snapshot-content');
  if (!container) return;
  const result = appState.lastRouteResult;
  if (result && result.found) {
    container.innerHTML = `
      <div class="dash-card-icon" style="background:rgba(16,185,129,0.12)">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#10B981" stroke-width="2"><polyline points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/></svg>
      </div>
      <div class="dash-card-info">
        <span class="dash-card-value">${result.start} → ${result.end}</span>
        <span class="dash-card-label">${result.totalDistance} km · ${result.path.length} stops</span>
      </div>
    `;
  } else {
    container.innerHTML = `
      <div class="dash-card-icon" style="background:rgba(16,185,129,0.12)">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#10B981" stroke-width="2"><polyline points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/></svg>
      </div>
      <div class="dash-card-info">
        <span class="dash-card-value">No Route Planned</span>
        <span class="dash-card-label">Plan a route to see details</span>
      </div>
    `;
  }
}

function _populateDiscoverSnapshot() {
  const container = document.getElementById('discover-snapshot-content');
  if (!container) return;
  const score = a => a.rating * 10 + a.value;
  const sorted = [...appState.attractions].sort((a, b) => score(b) - score(a) || a.name.localeCompare(b.name));
  if (sorted.length > 0) {
    const top = sorted[0];
    container.innerHTML = `
      <div class="dash-card-icon" style="background:rgba(139,92,246,0.12)">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#8B5CF6" stroke-width="2"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
      </div>
      <div class="dash-card-info">
        <span class="dash-card-value">${top.name}</span>
        <span class="dash-card-label">★ ${top.rating} · ${top.location} · $${top.cost}</span>
      </div>
    `;
  }
}

// ═══════════════════════════════════════════════════════════════════════════
// BUDGET PLANNER (0-1 Knapsack)
// ═══════════════════════════════════════════════════════════════════════════

async function loadBudgetPlanner() {
  const btn = document.getElementById('solve-knapsack-btn');
  const exampleBtn = document.getElementById('example-budget-btn');
  const resetBtn = document.getElementById('reset-data-btn');
  const budgetInput = document.getElementById('budget-input');

  async function solve() {
    const budget = parseInt(budgetInput.value) || 500;
    btn.textContent = 'Optimizing...';
    btn.disabled = true;
    try {
      const result = await API.solveKnapsack(budget);
      appState.lastBudgetResult = result;
      displaySmartItinerary(result);
    } catch (e) {
      alert('Error: ' + e.message);
    } finally {
      btn.textContent = 'Optimize Budget';
      btn.disabled = false;
    }
  }

  btn.onclick = solve;
  budgetInput.addEventListener('keydown', e => { if (e.key === 'Enter') solve(); });
  exampleBtn.onclick = async () => {
    try {
      const result = await API.getExampleBudget();
      appState.lastBudgetResult = result;
      displaySmartItinerary(result);
      budgetInput.value = result.budget;
    } catch (e) { alert('Error: ' + e.message); }
  };
  resetBtn.onclick = async () => {
    try {
      await API.resetData();
      alert('Demo data reset successfully!');
    } catch (e) { alert('Error: ' + e.message); }
  };
}

function displaySmartItinerary(result) {
  // Summary chips
  const summary = document.getElementById('budget-summary');
  summary.classList.remove('hidden');
  document.getElementById('ks-cost').textContent = '$' + result.totalCost;
  document.getElementById('ks-value').textContent = result.totalValue;
  document.getElementById('ks-remaining').textContent = '$' + result.remainingBudget;
  document.getElementById('ks-count').textContent = result.selected.length;

  // Budget health bar
  const totalBudget = result.totalCost + result.remainingBudget;
  const pct = totalBudget > 0 ? (result.totalCost / totalBudget) * 100 : 0;
  const healthEl = document.getElementById('budget-health');
  if (healthEl) {
    const healthPct = Math.round(pct);
    const barColor = pct > 85 ? '#10B981' : pct > 60 ? '#F59E0B' : '#3B82F6';
    healthEl.innerHTML = `
      <div style="margin-bottom:12px">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:6px">
          <span style="font-weight:600;font-size:13px;color:var(--text-primary)">Budget Health</span>
          <span style="font-weight:700;font-size:14px;color:${barColor}">${healthPct}%</span>
        </div>
        <div class="progress-bar">
          <div class="progress-fill" style="width:${healthPct}%;background:${barColor}"></div>
        </div>
        <div style="display:flex;justify-content:space-between;margin-top:4px">
          <span style="font-size:11px;color:var(--text-secondary)">Utilized: $${result.totalCost}</span>
          <span style="font-size:11px;color:var(--text-secondary)">Remaining: $${result.remainingBudget}</span>
        </div>
      </div>
    `;
  }

  // Selected experiences
  const container = document.getElementById('knapsack-results');
  if (!result.selected || result.selected.length === 0) {
    container.innerHTML = `<div class="empty-state"><div class="empty-icon">&#128270;</div><p class="empty-title">No attractions fit this budget</p><p class="empty-desc">Try increasing your budget to see more options.</p></div>`;
  } else {
    container.innerHTML = result.selected.map((a, i) => {
      const detail = appState.attractions.find(at => at.name === a.name);
      const stars = detail ? '\u2605'.repeat(detail.rating) + '\u2606'.repeat(5 - detail.rating) : '';
      return `
        <div class="experience-card fade-in" style="animation-delay:${i * 0.05}s">
          <div class="exp-left">
            <div class="exp-number">${i + 1}</div>
            <div class="exp-info">
              <span class="exp-name">${a.name}</span>
              <span class="exp-location">${a.location}</span>
            </div>
          </div>
          <div class="exp-right">
            <span class="exp-cost">$${a.cost}</span>
            <span class="exp-value">${a.value} pts</span>
            ${detail ? `<span class="exp-stars">${stars}</span>` : ''}
          </div>
        </div>
      `;
    }).join('');
  }

  // Skipped
  const skipped = document.getElementById('knapsack-skipped');
  if (skipped) {
    skipped.innerHTML = result.skipped && result.skipped.length > 0
      ? result.skipped.map(a => `
        <div class="experience-card skipped">
          <div class="exp-left">
            <div class="exp-info">
              <span class="exp-name">${a.name}</span>
              <span class="exp-location">${a.location}</span>
            </div>
          </div>
          <div class="exp-right">
            <span class="exp-cost">$${a.cost}</span>
            <span class="tag tag-gray">Skipped</span>
          </div>
        </div>
      `).join('')
      : '<div class="empty-state-sm">All attractions fit within budget.</div>';
  }
}

// ═══════════════════════════════════════════════════════════════════════════
// ROUTE PLANNER (Dijkstra)
// ═══════════════════════════════════════════════════════════════════════════

async function loadRoutePlanner() {
  const findBtn = document.getElementById('find-route-btn');
  const exampleBtn = document.getElementById('example-route-btn');
  const zoomIn = document.getElementById('map-zoom-in');
  const zoomOut = document.getElementById('map-zoom-out');
  const fitRoute = document.getElementById('map-fit-route');
  const resetView = document.getElementById('map-reset-view');

  // Init SVG map immediately (no timeout to avoid race conditions)
  if (!appState.routeMap) {
    appState.routeMap = new AtlasCityMap('route-map-container', {
      interactive: true,
      mini: false,
      showRouteLabels: true
    });
    console.log('Route map initialized');
  } else {
    console.log('Route map already exists, reusing');
  }

  findBtn.onclick = () => findRoute();
  exampleBtn.onclick = async () => {
    try {
      const result = await API.getExampleRoute();
      console.log('Example route result:', result);
      if (result.found) {
        const s = document.getElementById('start-location');
        const e = document.getElementById('end-location');
        s.value = result.start;
        e.value = result.end;
        appState.lastRouteResult = result;
        displayRouteResult(result);
      }
    } catch (e) { alert('Error: ' + e.message); }
  };

  zoomIn.onclick = () => { if (appState.routeMap) appState.routeMap.zoomIn(); };
  zoomOut.onclick = () => { if (appState.routeMap) appState.routeMap.zoomOut(); };
  if (fitRoute) {
    fitRoute.onclick = () => { if (appState.routeMap) appState.routeMap.fitToRoute(); };
  }
  if (resetView) {
    resetView.onclick = () => { if (appState.routeMap) appState.routeMap.resetView(); };
  }
}

async function findRoute() {
  const start = document.getElementById('start-location').value;
  const end = document.getElementById('end-location').value;
  if (!start || !end) { alert('Please select both start and end locations.'); return; }

  console.log('=== FIND ROUTE ===');
  console.log('Start location:', start);
  console.log('End location:', end);
  console.log('Route map exists:', !!appState.routeMap);

  const btn = document.getElementById('find-route-btn');
  btn.textContent = 'Finding...';
  btn.disabled = true;
  try {
    const result = await API.findRoute(start, end);
    console.log('Backend Dijkstra result:', result);
    appState.lastRouteResult = result;
    displayRouteResult(result);
  } catch (e) {
    console.error('Route error:', e);
    alert('Error: ' + e.message);
  } finally {
    btn.textContent = 'Find Shortest Path';
    btn.disabled = false;
  }
}

function displayRouteResult(result) {
  const summary = document.getElementById('route-summary');
  const distEl = document.getElementById('route-distance');
  const timeEl = document.getElementById('route-time');
  const visitedEl = document.getElementById('route-visited');
  const pathEl = document.getElementById('route-path');

  summary.classList.remove('hidden');

  if (!result.found) {
    console.log('Route NOT found by backend Dijkstra');
    distEl.textContent = '-- km';
    if (timeEl) timeEl.textContent = '-- min';
    if (visitedEl) visitedEl.textContent = '--';
    pathEl.innerHTML = '<p class="path-placeholder">No route available between these locations.</p>';
    if (appState.routeMap) appState.routeMap.clearRoute();
    return;
  }

  console.log('Route FOUND by backend Dijkstra');
  const totalDist = result.totalDistance || 0;
  distEl.textContent = totalDist + ' km';
  if (timeEl) timeEl.textContent = Math.round(totalDist * 3) + ' min';
  if (visitedEl) visitedEl.textContent = result.path ? result.path.length : '--';

  // Build simplified path: only visible locations with "via" for hidden segments
  const simplifiedPath = [];
  const viaInfo = [];
  
  // Always include start and end (they're from the visible location dropdowns)
  // Filter out hidden nodes in between, collecting them as "via" info
  for (let i = 0; i < result.path.length; i++) {
    const node = result.path[i];
    const isFirst = i === 0;
    const isLast = i === result.path.length - 1;
    
    if (isFirst || isLast || ATLAS_CITY.visibleLocations.has(node)) {
      simplifiedPath.push(node);
      viaInfo.push(null);
    } else if (simplifiedPath.length > 0) {
      // Collect hidden node as "via" info for the preceding visible location
      const detail = ATLAS_CITY.locationDetails[node];
      const viaName = detail ? detail.displayName : node;
      const lastViaIdx = viaInfo.length - 1;
      viaInfo[lastViaIdx] = viaInfo[lastViaIdx]
        ? viaInfo[lastViaIdx] + ', ' + viaName
        : viaName;
    }
  }
  
  // Fallback: if simplification broke the path, use original
  if (simplifiedPath.length < 2) {
    simplifiedPath.length = 0;
    viaInfo.length = 0;
    simplifiedPath.push(...result.path);
    viaInfo.push(...new Array(result.path.length).fill(null));
  }

  pathEl.innerHTML = simplifiedPath.map((node, i) => {
    const detail = ATLAS_CITY.locationDetails[node];
    const displayName = detail ? detail.displayName : node;
    const isFirst = i === 0;
    const isLast = i === simplifiedPath.length - 1;
    const via = viaInfo[i];
    let viaText = '';
    if (via && !isLast) {
      viaText = `<div class="path-step-via">via ${via}</div>`;
    }
    return `
      <div class="path-step fade-in" style="animation-delay:${i * 0.08}s">
        <span class="path-step-icon">${isFirst ? '\u25CF' : isLast ? '\uD83D\uDCCD' : '\u2192'}</span>
        <span>${displayName}${isFirst ? ' (Start)' : isLast ? ' (End)' : ''}</span>
        ${viaText}
      </div>
    `;
  }).join('');

  if (appState.routeMap) {
    console.log('Calling routeMap.setRoute() with backend path:', result.path);
    appState.routeMap.setRoute(result.path);
  } else {
    console.error('routeMap is NULL - map not initialized yet!');
  }
}

// ═══════════════════════════════════════════════════════════════════════════
// DISCOVER PAGE (Heap Sort)
// ═══════════════════════════════════════════════════════════════════════════

async function loadDiscover() {
  const refreshBtn = document.getElementById('refresh-rankings-btn');
  await fetchAndDisplayDiscover();
  refreshBtn.onclick = fetchAndDisplayDiscover;
}

async function fetchAndDisplayDiscover() {
  try {
    const rankings = await API.getRankings();
    if (!rankings || rankings.length === 0) {
      _renderDiscoverFallback();
      return;
    }
    _renderDiscover(rankings);
  } catch (e) {
    console.error('Discover load error, using local data:', e);
    _renderDiscoverFallback();
  }
}

function _renderDiscoverFallback() {
  const score = a => a.rating * 10 + a.value;
  const sorted = [...appState.attractions].sort((a, b) => score(b) - score(a) || a.name.localeCompare(b.name));
  _renderDiscover(sorted);
}

function _renderDiscover(rankings) {
  // Featured attraction (top ranked)
  if (rankings.length === 0) return;
  const top = rankings[0];
  const featuredEl = document.getElementById('featured-attraction');
  if (featuredEl) {
    const stars = '\u2605'.repeat(top.rating || 5) + '\u2606'.repeat(5 - (top.rating || 5));
    featuredEl.innerHTML = `
      <div class="featured-hero">
        <div class="featured-image-area">
          <div class="featured-image-overlay"></div>
          <div class="featured-badge">#1 Attraction</div>
          <div class="featured-price">$${top.cost || 100}</div>
        </div>
        <div class="featured-info">
          <h3 class="featured-name">${top.name}</h3>
          <div class="featured-stars">${stars}</div>
          <p class="featured-desc">${top.description || 'A must-visit attraction in Atlas City.'}</p>
          <div class="featured-tags">
            <span class="tag tag-blue">${top.location || 'City Center'}</span>
            <span class="tag tag-green">${top.duration || '2h'}</span>
            <span class="tag tag-amber">${top.value || 90} pts</span>
          </div>
        </div>
      </div>
    `;
  }

  // Top 3 spotlight
  const top3El = document.getElementById('top3-spotlight');
  if (top3El) {
    const top3 = rankings.slice(0, 3);
    const medals = ['\uD83E\uDD47', '\uD83E\uDD48', '\uD83E\uDD49'];
    const colors = ['#F59E0B', '#94A3B8', '#B45309'];
    top3El.innerHTML = top3.map((a, i) => {
      const stars = '\u2605'.repeat(a.rating || 4) + '\u2606'.repeat(5 - (a.rating || 4));
      return `
        <div class="top3-card fade-in" style="animation-delay:${i * 0.1}s">
          <div class="top3-medal" style="color:${colors[i]}">${medals[i]}</div>
          <div class="top3-rank">#${i + 1}</div>
          <h4 class="top3-name">${a.name}</h4>
          <div class="top3-stars">${stars}</div>
          <div class="top3-info">${a.location || ''}</div>
          <div class="top3-tags">
            <span class="tag tag-blue">$${a.cost || 0}</span>
            <span class="tag tag-green">${a.value || 0} pts</span>
          </div>
        </div>
      `;
    }).join('');
  }

  // All attractions list
  const listEl = document.getElementById('discover-list');
  if (listEl) {
    listEl.innerHTML = rankings.map((a, i) => {
      const stars = '\u2605'.repeat(a.rating || 3) + '\u2606'.repeat(5 - (a.rating || 3));
      return `
        <div class="discover-card fade-in" style="animation-delay:${i * 0.03}s">
          <div class="disc-left">
            <div class="disc-rank">#${i + 1}</div>
            <div class="disc-info">
              <span class="disc-name">${a.name}</span>
              <span class="disc-location">${a.location || ''}</span>
            </div>
          </div>
          <div class="disc-center">
            <span class="disc-stars">${stars}</span>
          </div>
          <div class="disc-right">
            <span class="tag tag-blue">$${a.cost || 0}</span>
            <span class="tag tag-green">${a.duration || '2h'}</span>
            <span class="tag tag-amber">${a.value || 0} pts</span>
          </div>
        </div>
      `;
    }).join('');
  }

  // Update sort info
  const sortInfo = document.getElementById('sort-info');
  if (sortInfo) {
    sortInfo.textContent = `Ranked by composite score (\u2605\u00D710 + value) using Heap Sort \u2014 ${rankings.length} attractions`;
  }
}

// ═══════════════════════════════════════════════════════════════════════════
// INIT
// ═══════════════════════════════════════════════════════════════════════════

document.addEventListener('DOMContentLoaded', () => {
  // Settings
  document.getElementById('settings-reset-btn')?.addEventListener('click', async () => {
    try { await API.resetData(); alert('Demo data reset successfully!'); }
    catch (e) { alert('Error: ' + e.message); }
  });

  // Quick Actions (Dashboard)
  document.querySelectorAll('.action-card[data-nav]').forEach(el => {
    el.addEventListener('click', () => Router.navigate(el.dataset.nav));
  });

  // Dashboard nav cards
  document.querySelectorAll('.dash-card-link').forEach(el => {
    el.addEventListener('click', () => Router.navigate(el.dataset.nav));
  });

  // Start login
  initLogin();
});
