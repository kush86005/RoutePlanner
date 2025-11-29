package com.kush.routeplanner.service;

import com.kush.routeplanner.dijkstra.Graph;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RouteService {

    private final Graph graph = new Graph();

    public RouteService() {
        graph.addRoad("pune", "mumbai", 150);
        graph.addRoad("mumbai", "surat", 280);
        graph.addRoad("surat", "ahmedabad", 260);
        graph.addRoad("ahmedabad", "jaipur", 650);
        graph.addRoad("jaipur", "delhi", 280);
        graph.addRoad("mumbai", "nashik", 180);
    }

    public void addCity(String name) {
        graph.addCity(name);
    }

    public void addRoad(String from, String to, int distance) {
        graph.addRoad(from, to, distance);
    }

    public Map<String, Object> shortest(String src, String dest) {
        Graph.PathResult r = graph.shortest(src, dest);
        Map<String, Object> res = new HashMap<>();
        res.put("path", r.path());
        res.put("distance", r.distance());
        return res;
    }

    public Map<String, Object> graphSnapshot() {
        Map<String, Object> res = new HashMap<>();
        res.putAll(graph.snapshot());
        return res;
    }
}
