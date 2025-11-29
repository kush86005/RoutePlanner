package com.kush.routeplanner.dijkstra;

import java.util.*;

public class Graph {

    private final Map<String, List<Edge>> adj = new HashMap<>();

    public static class Edge {
        public String to;
        public int distance;

        public Edge(String to, int distance) {
            this.to = to;
            this.distance = distance;
        }
    }

    public void addCity(String city) {
        if (city == null || city.isBlank()) return;
        city = city.toLowerCase().trim();
        adj.putIfAbsent(city, new ArrayList<>());
    }

    public void addRoad(String a, String b, int d) {
        if (a == null || b == null || d <= 0) return;
        a = a.toLowerCase().trim();
        b = b.toLowerCase().trim();
        if (a.equals(b)) return;
        addCity(a);
        addCity(b);
        if (!hasEdge(a, b)) adj.get(a).add(new Edge(b, d));
        if (!hasEdge(b, a)) adj.get(b).add(new Edge(a, d));
    }

    private boolean hasEdge(String from, String to) {
        List<Edge> list = adj.getOrDefault(from, Collections.emptyList());
        for (Edge e : list) if (e.to.equals(to)) return true;
        return false;
    }

    public Map<String, List<Edge>> snapshot() {
        Map<String, List<Edge>> copy = new TreeMap<>();
        for (var e : adj.entrySet()) {
            copy.put(e.getKey(), new ArrayList<>(e.getValue()));
        }
        return copy;
    }

    public PathResult shortest(String src, String dest) {
        src = src.toLowerCase().trim();
        dest = dest.toLowerCase().trim();
        if (!adj.containsKey(src) || !adj.containsKey(dest)) {
            return new PathResult(null, -1);
        }

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.d));

        for (String c : adj.keySet()) dist.put(c, Integer.MAX_VALUE);
        dist.put(src, 0);
        pq.add(new Node(src, 0));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (cur.d != dist.get(cur.city)) continue; // stale
            if (cur.city.equals(dest)) break;

            for (Edge e : adj.get(cur.city)) {
                int nd = cur.d + e.distance;
                if (nd < dist.get(e.to)) {
                    dist.put(e.to, nd);
                    parent.put(e.to, cur.city);
                    pq.add(new Node(e.to, nd));
                }
            }
        }

        if (dist.get(dest) == Integer.MAX_VALUE) return new PathResult(null, -1);

        List<String> path = new ArrayList<>();
        for (String at = dest; at != null; at = parent.get(at)) path.add(at);
        Collections.reverse(path);
        return new PathResult(path, dist.get(dest));
    }

    private static class Node {
        String city; int d;
        Node(String c, int d) { this.city = c; this.d = d; }
    }

    public record PathResult(List<String> path, int distance) {}
}
