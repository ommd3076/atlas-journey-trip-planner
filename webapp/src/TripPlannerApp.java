import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.*;
import java.util.*;

/**
 * Trip Planner - Main Application Server
 * 
 * A lightweight embedded HTTP server that serves the frontend SPA
 * and provides RESTful API endpoints for all DAA algorithms.
 * 
 * Server runs on port 8080. Frontend accessible at http://localhost:8080
 */
public class TripPlannerApp {

    private static final int PORT = 8080;
    private static final String FRONTEND_DIR = "webapp/frontend";

    private static KnapsackService knapsackService = new KnapsackService();
    private static DijkstraService dijkstraService = new DijkstraService();
    private static HeapSortService heapSortService = new HeapSortService();

    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("  Trip Planner Web Application");
        System.out.println("  Initializing...");
        System.out.println("========================================");

        DatabaseManager.initializeSchema();
        SeedData.seedIfNeeded();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // API endpoints
        server.createContext("/api/login", TripPlannerApp::handleLogin);
        server.createContext("/api/dashboard", TripPlannerApp::handleDashboard);
        server.createContext("/api/knapsack", TripPlannerApp::handleKnapsack);
        server.createContext("/api/dijkstra", TripPlannerApp::handleDijkstra);
        server.createContext("/api/rankings", TripPlannerApp::handleRankings);
        server.createContext("/api/locations", TripPlannerApp::handleLocations);
        server.createContext("/api/edges", TripPlannerApp::handleEdges);
        server.createContext("/api/reset", TripPlannerApp::handleReset);
        server.createContext("/api/example-route", TripPlannerApp::handleExampleRoute);
        server.createContext("/api/example-budget", TripPlannerApp::handleExampleBudget);

        // Static file serving (SPA)
        server.createContext("/", TripPlannerApp::handleStatic);

        server.setExecutor(null);
        server.start();

        System.out.println("  Server running on http://localhost:" + PORT);
        System.out.println("  Open in your browser to start.");
        System.out.println("========================================");
    }

    // ── API Handlers ─────────────────────────────────────────────────────────

    private static void handleLogin(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        String body = readBody(exchange);
        String username = extractJsonString(body, "username");
        if (username.isEmpty()) username = "Traveler";
        sendJson(exchange, 200, "{\"success\":true,\"username\":\"" + esc(username) + "\"}");
    }

    private static void handleDashboard(HttpExchange exchange) throws IOException {
        List<Attraction> attractions = knapsackService.getAllAttractions();
        List<String> nodes = dijkstraService.getAllNodes();
        KnapsackResult opt = knapsackService.solveFromDB(500);
        String response = "{\"attractionCount\":" + attractions.size()
            + ",\"locationCount\":" + nodes.size()
            + ",\"algorithmCount\":3"
            + ",\"optimalCost\":" + opt.totalCost
            + ",\"optimalValue\":" + opt.totalValue
            + ",\"budget\":500}";
        sendJson(exchange, 200, response);
    }

    private static void handleKnapsack(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        String body = readBody(exchange);
        int budget = extractJsonInt(body, "budget");
        if (budget <= 0) budget = 500;
        KnapsackResult result = knapsackService.solveFromDB(budget);
        sendJson(exchange, 200, result.toJson());
    }

    private static void handleDijkstra(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        String body = readBody(exchange);
        String start = extractJsonString(body, "start");
        String end = extractJsonString(body, "end");
        if (start.isEmpty() || end.isEmpty()) {
            sendJson(exchange, 400, "{\"error\":\"Start and end locations are required\"}");
            return;
        }
        PathResult result = dijkstraService.findShortestPath(start, end);
        sendJson(exchange, 200, result.toJson());
    }

    private static void handleRankings(HttpExchange exchange) throws IOException {
        List<Attraction> sorted = heapSortService.getSortedFromDB();
        sendJson(exchange, 200, Attraction.toJsonArray(sorted));
    }

    private static void handleLocations(HttpExchange exchange) throws IOException {
        List<String> nodes = dijkstraService.getAllNodes();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nodes.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(nodes.get(i)).append("\"");
        }
        sb.append("]");
        sendJson(exchange, 200, sb.toString());
    }

    private static void handleEdges(HttpExchange exchange) throws IOException {
        List<Edge> edges = dijkstraService.getAllEdges();
        sendJson(exchange, 200, Edge.toJsonArray(edges));
    }

    private static void handleReset(HttpExchange exchange) throws IOException {
        SeedData.resetData();
        sendJson(exchange, 200, "{\"success\":true,\"message\":\"Demo data reset successfully\"}");
    }

    private static void handleExampleRoute(HttpExchange exchange) throws IOException {
        List<String> nodes = dijkstraService.getAllNodes();
        if (nodes.size() >= 2) {
            String start = nodes.get(0);
            String end = nodes.get(nodes.size() - 1);
            PathResult result = dijkstraService.findShortestPath(start, end);
            sendJson(exchange, 200, result.toJson());
        } else {
            sendJson(exchange, 200, "{\"found\":false}");
        }
    }

    private static void handleExampleBudget(HttpExchange exchange) throws IOException {
        KnapsackResult result = knapsackService.solveFromDB(500);
        sendJson(exchange, 200, result.toJson());
    }

    // ── Static File Server ───────────────────────────────────────────────────

    private static void handleStatic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if ("/".equals(path)) path = "/index.html";

        Path filePath = Paths.get(FRONTEND_DIR, path);
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            byte[] data = Files.readAllBytes(filePath);
            exchange.getResponseHeaders().set("Content-Type", getContentType(path));
            exchange.sendResponseHeaders(200, data.length);
            exchange.getResponseBody().write(data);
        } else {
            // SPA fallback: serve index.html for any unknown route
            Path indexPath = Paths.get(FRONTEND_DIR, "index.html");
            if (Files.exists(indexPath)) {
                byte[] data = Files.readAllBytes(indexPath);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, data.length);
                exchange.getResponseBody().write(data);
            } else {
                sendJson(exchange, 404, "{\"error\":\"Not found\"}");
            }
        }
        exchange.getResponseBody().close();
    }

    // ── Utilities ────────────────────────────────────────────────────────────

    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=utf-8";
        if (path.endsWith(".css")) return "text/css; charset=utf-8";
        if (path.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".svg")) return "image/svg+xml";
        if (path.endsWith(".ico")) return "image/x-icon";
        if (path.endsWith(".json")) return "application/json";
        return "application/octet-stream";
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
    }

    private static void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] response = json.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.getResponseBody().close();
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    private static String extractJsonString(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        int end = json.indexOf("\"", start);
        return end == -1 ? "" : json.substring(start, end);
    }

    private static int extractJsonInt(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return 0;
        start += search.length();
        while (start < json.length() && json.charAt(start) == ' ') start++;
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
        try { return Integer.parseInt(json.substring(start, end)); }
        catch (NumberFormatException e) { return 0; }
    }
}
