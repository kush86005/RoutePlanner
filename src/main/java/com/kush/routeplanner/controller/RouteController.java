package com.kush.routeplanner.controller;

import com.kush.routeplanner.service.RouteService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RouteController {

    private final RouteService service;

    public RouteController(RouteService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public String health() { return "OK"; }

    @PostMapping("/add-city")
    public String addCity(@RequestParam String name) {
        service.addCity(name);
        return "City added";
    }

    @PostMapping("/add-road")
    public String addRoad(@RequestParam String from,
                          @RequestParam String to,
                          @RequestParam int distance) {
        service.addRoad(from, to, distance);
        return "Road added";
    }

    @GetMapping("/shortest")
    public Map<String, Object> shortest(@RequestParam String src,
                                        @RequestParam String dest) {
        return service.shortest(src, dest);
    }

    @GetMapping("/graph")
    public Map<String, Object> graph() {
        return service.graphSnapshot();
    }
}
