package com.kush.routeplanner.service;

import com.kush.routeplanner.dijkstra.Graph;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RouteService {

    private final Graph graph = new Graph();

    public RouteService() {
        // seed a few defaults so the UI works on first run
        graph.addCity("pune");
        graph.addCity("mumbai");
        graph.addCity("nashik");
        graph.addRoad("pune", "mumbai", 150);
        graph.addRoad("pune", "nashik", 200);
        graph.addRoad("mumbai", "nashik", 180);
    }

    public Map<String, Object> addCity(String name) {
        boolean ok = graph.addCity(name);
        Map<String, Object> res = new HashMap<>();
        res.put("success", ok);
        res.put("message", ok ? "City added" : "City already exists or invalid");
        return res;
    }

    public Map<String, Object> addRoad(String src, String dest, int km) {
        boolean ok = graph.addRoad(src, dest, km);
        Map<String, Object> res = new HashMap<>();
        res.put("success", ok);
        res.put("message", ok ? "Road added" : "Invalid road or already exists");
        return res;
    }

    public Map<String, Object> shortest(String src, String dest) {
        Graph.PathResult r = graph.dijkstra(src, dest);
        Map<String, Object> res = new HashMap<>();
        if (r.distance == Integer.MAX_VALUE) {
            res.put("path", new String[0]);
            res.put("distance", -1);
            res.put("message", "No route found or city missing");
            return res;
        }
        res.put("path", r.path.stream().map(RouteService::cap).toArray(String[]::new));
        res.put("distance", r.distance);
        res.put("message", "OK");
        return res;
    }

    public Map<String, Object> graphSnapshot() {
        Map<String, Object> res = new HashMap<>();
        res.put("cities", graph.cities());
        res.put("roads", graph.roads());
        return res;
    }

    public void reset() {
        // simplest reset: replace the instance
        synchronized (this) {
            // not strictly necessary for single-user dev, but fine
        }
    }

    private static String cap(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}
