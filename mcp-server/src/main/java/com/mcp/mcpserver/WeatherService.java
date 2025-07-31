package com.mcp.mcpserver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import reactor.core.publisher.Mono;

@Service
public class WeatherService {

	private static final String BASE_URL = "https://api.weather.gov";

	private final WebClient webClient;

	public WeatherService() {
		this.webClient = WebClient.builder()
			.baseUrl(BASE_URL)
			.defaultHeader("Accept", "application/geo+json")
			.defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
			.build();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Points(@JsonProperty("properties") Props properties) {
		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Props(@JsonProperty("forecast") String forecast) {
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Forecast(@JsonProperty("properties") Props properties) {
		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Props(@JsonProperty("periods") List<Period> periods) {
		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Period(@JsonProperty("number") Integer number, @JsonProperty("name") String name,
				@JsonProperty("startTime") String startTime, @JsonProperty("endTime") String endTime,
				@JsonProperty("isDaytime") Boolean isDayTime, @JsonProperty("temperature") Integer temperature,
				@JsonProperty("temperatureUnit") String temperatureUnit,
				@JsonProperty("temperatureTrend") String temperatureTrend,
				@JsonProperty("probabilityOfPrecipitation") Map probabilityOfPrecipitation,
				@JsonProperty("windSpeed") String windSpeed, @JsonProperty("windDirection") String windDirection,
				@JsonProperty("icon") String icon, @JsonProperty("shortForecast") String shortForecast,
				@JsonProperty("detailedForecast") String detailedForecast) {
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Alert(@JsonProperty("features") List<Feature> features) {

		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Feature(@JsonProperty("properties") Properties properties) {
		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Properties(@JsonProperty("event") String event, @JsonProperty("areaDesc") String areaDesc,
				@JsonProperty("severity") String severity, @JsonProperty("description") String description,
				@JsonProperty("instruction") String instruction) {
		}
	}

	/**
	 * Get forecast for a specific latitude/longitude
	 * @param latitude Latitude
	 * @param longitude Longitude
	 * @return The forecast for the given location
	 * @throws WebClientException if the request fails
	 */
	@Tool(description = "Get weather forecast for a specific latitude/longitude")
	public Mono<String> getWeatherForecastByLocation(double latitude, double longitude) {

		return webClient.get()
			.uri("/points/{latitude},{longitude}", latitude, longitude)
			.retrieve()
			.bodyToMono(Points.class)
			.flatMap(points -> webClient.get()
				.uri(points.properties().forecast())
				.retrieve()
				.bodyToMono(Forecast.class))
			.map(forecast -> {
				String forecastText = forecast.properties().periods().stream().map(p -> {
					return String.format("""
							%s:
							Temperature: %s %s
							Wind: %s %s
							Forecast: %s
							""", p.name(), p.temperature(), p.temperatureUnit(), p.windSpeed(), p.windDirection(),
							p.detailedForecast());
				}).collect(Collectors.joining());
				
				return forecastText;
			});
	}

	/**
	 * Get alerts for a specific area
	 * @param state Area code. Two-letter US state code (e.g. CA, NY)
	 * @return Human readable alert information
	 * @throws WebClientException if the request fails
	 */
	@Tool(description = "Get weather alerts for a US state. Input is Two-letter US state code (e.g. CA, NY)")
	public Mono<String> getAlerts(String state) {
		return webClient.get()
			.uri("/alerts/active/area/{state}", state)
			.retrieve()
			.bodyToMono(Alert.class)
			.map(alert -> alert.features()
				.stream()
				.map(f -> String.format("""
						Event: %s
						Area: %s
						Severity: %s
						Description: %s
						Instructions: %s
						""", f.properties().event(), f.properties().areaDesc(), f.properties().severity(),
						f.properties().description(), f.properties().instruction()))
				.collect(Collectors.joining("\n")));
	}

	public static void main(String[] args) {
		WeatherService client = new WeatherService();
		client.getWeatherForecastByLocation(47.6062, -122.3321)
			.subscribe(System.out::println);
		client.getAlerts("NY")
			.subscribe(System.out::println);
	}

}
