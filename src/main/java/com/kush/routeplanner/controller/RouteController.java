package com.kush.routeplanner.controller;

import com.kush.routeplanner.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RouteController {

    private final RouteService service;
    public RouteController(RouteService service) { this.service = service; }

    @GetMapping("/graph")
    public Map<String, Object> graph() { return service.graphSnapshot(); }

    @PostMapping("/cities")
    public ResponseEntity<Map<String, Object>> addCity(@RequestBody CityRequest body) {
        if (body == null || body.name == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.addCity(body.name));
    }

    @PostMapping("/roads")
    public ResponseEntity<Map<String, Object>> addRoad(@RequestBody RoadRequest body) {
        if (body == null || body.src == null || body.dest == null || body.distance <= 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.addRoad(body.src, body.dest, body.distance));
    }

    @GetMapping("/shortest")
    public Map<String, Object> shortest(@RequestParam String src, @RequestParam String dest) {
        return service.shortest(src, dest);
    }

    public static final class CityRequest {
        public String name;
    }

    public static final class RoadRequest {
        public String src;
        public String dest;
        public int distance;
    }
}
