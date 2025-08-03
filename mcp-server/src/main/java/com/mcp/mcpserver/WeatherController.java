package com.mcp.mcpserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast")
    public String getWeatherForecast(@RequestParam double latitude, @RequestParam double longitude) {
        return weatherService.getWeatherForecastByLocation(latitude, longitude);
    }

    @GetMapping("/alerts/{state}")
    public String getAlerts(@PathVariable String state) {
        return weatherService.getAlerts(state);
    }
} 