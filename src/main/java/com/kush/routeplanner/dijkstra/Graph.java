package com.kush.routeplanner.dijkstra;

import java.util.*;

public class Graph {
    public static final class Edge {
        public final String destination;
        public final int distance;
        public Edge(String destination, int distance) {
            this.destination = destination;
            this.distance = distance;
        }
    }

    public static final class PathResult {
        public final List<String> path;   // lowercase city names
        public final int distance;        // total km, Integer.MAX_VALUE if unreachable
        public PathResult(List<String> path, int distance) {
            this.path = path;
            this.distance = distance;
        }
    }

    private final Map<String, List<Edge>> adj = new HashMap<>();

    public boolean addCity(String name) {
        if (name == null) return false;
        String key = name.trim().toLowerCase();
        if (key.isEmpty()) return false;
        if (adj.containsKey(key)) return false;
        adj.put(key, new ArrayList<>());
        return true;
    }

    public boolean addRoad(String a, String b, int km) {
        if (a == null || b == null || km <= 0) return false;
        String u = a.trim().toLowerCase();
        String v = b.trim().toLowerCase();
        if (u.isEmpty() || v.isEmpty() || u.equals(v)) return false;

        adj.putIfAbsent(u, new ArrayList<>());
        adj.putIfAbsent(v, new ArrayList<>());

        if (hasEdge(u, v)) return false;

        adj.get(u).add(new Edge(v, km));
        adj.get(v).add(new Edge(u, km));
        return true;
    }

    public Set<String> cities() {
        return new TreeSet<>(adj.keySet());
    }

    public List<Road> roads() {
        List<Road> out = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (var e : adj.entrySet()) {
            String u = e.getKey();
            for (Edge ed : e.getValue()) {
                String v = ed.destination;
                String key = u.compareTo(v) < 0 ? u+"#"+v : v+"#"+u;
                if (seen.add(key)) out.add(new Road(u, v, ed.distance));
            }
        }
        out.sort(Comparator.comparing((Road r) -> r.src).thenComparing(r -> r.dest));
        return out;
    }

    public PathResult dijkstra(String src, String dest) {
        if (src == null || dest == null) return new PathResult(List.of(), Integer.MAX_VALUE);
        String s = src.trim().toLowerCase();
        String t = dest.trim().toLowerCase();
        if (!adj.containsKey(s) || !adj.containsKey(t)) return new PathResult(List.of(), Integer.MAX_VALUE);

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.d));
        for (String c : adj.keySet()) dist.put(c, Integer.MAX_VALUE);
        dist.put(s, 0);
        pq.add(new Node(s, 0));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (cur.d != dist.get(cur.city)) continue;
            if (cur.city.equals(t)) break;
            for (Edge e : adj.get(cur.city)) {
                int nd = cur.d + e.distance;
                if (nd < dist.get(e.destination)) {
                    dist.put(e.destination, nd);
                    parent.put(e.destination, cur.city);
                    pq.add(new Node(e.destination, nd));
                }
            }
        }

        if (dist.get(t) == Integer.MAX_VALUE) return new PathResult(List.of(), Integer.MAX_VALUE);

        List<String> path = new ArrayList<>();
        String cur = t;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);
        return new PathResult(path, dist.get(t));
    }

    private boolean hasEdge(String u, String v) {
        for (Edge e : adj.getOrDefault(u, List.of())) if (e.destination.equals(v)) return true;
        return false;
    }

    private static final class Node {
        final String city; final int d;
        Node(String c, int d) { this.city = c; this.d = d; }
    }

    // simple immutable representation for listing roads
    public static final class Road {
        public final String src;
        public final String dest;
        public final int distance;
        public Road(String src, String dest, int distance) {
            this.src = src; this.dest = dest; this.distance = distance;
        }
    }
}
