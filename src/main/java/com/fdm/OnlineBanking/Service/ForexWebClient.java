package com.fdm.OnlineBanking.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ForexWebClient {

	private final WebClient webClient;
		
	private final String apiKey = "suTZbn0hrQngX3foArTuqMlIFAjFjd6n8DGz";
		
	public ForexWebClient() {
		super();
		this.webClient = WebClient.builder()
				.baseUrl("https://currencyapi.net/api/v1/")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	
	public String getCurrencies() {
		return webClient.get().uri(
				builder -> builder
					.path("/currencies")
					.queryParam("key", apiKey)
					.build())
				.retrieve().bodyToMono(String.class)
				.block();
	}
	

	public String getRates() {
		return webClient.get()
				.uri(builder -> builder.path("/rates")
						.queryParam("key", apiKey)
						.queryParam("output", "JSON")
						.build())
				.retrieve()
				.bodyToMono(String.class)
				.block();
	}
		
		// working code, but need to pay to use this.
//		public String convertCurrencies(double amount, String from, String to) {
//			return webClient.get().uri(builder -> builder
//				.path("/convert")
//				.queryParam("access_key",apiKey)
//				.queryParam("amount", amount)
//				.queryParam("from", from)
//				.queryParam("to", to)
//				.build())
//				.retrieve()
//				.bodyToMono(String.class)
//				.block();
//		}
}
	
