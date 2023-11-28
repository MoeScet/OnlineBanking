package com.fdm.OnlineBanking.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdm.OnlineBanking.Service.ForexWebClient;

import lombok.extern.slf4j.Slf4j;


@Service
public class ForexService {

	@Autowired
	private ForexWebClient webClient;

	// this returns a string, but can be a list too if needed....i think
	public String getAllCurrencies() {
		
//		return webClient.getCurrencies();
		String jsonString = webClient.getCurrencies();
		ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
		try {
			jsonMap = (Map<String, Object>) mapper.readValue(jsonString, HashMap.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        HashMap<String, String> currenciesMap = (HashMap<String, String>) jsonMap.get("currencies");
        
        String currencySymbols = String.join(", ", currenciesMap.keySet());
		
        return currencySymbols;
		
	}
	
	public double convertCurrency(double amount, String from, String to) {
		
		// gets the exchange rates and parses it into a hashmap //////////////////////////
		String jsonString = webClient.getRates();
		ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
		try {
			jsonMap = (Map<String, Object>) mapper.readValue(jsonString, HashMap.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        HashMap<String, Double> ratesMap = (HashMap<String, Double>) jsonMap.get("rates");
        
        // convert currency ////////////////////////////      
		double fromRate = Double.parseDouble(String.valueOf(ratesMap.get(from)));
		double toRate = Double.parseDouble(String.valueOf(ratesMap.get(to)));
        double convertedAmount = 0;
        
        double conversionRate = toRate/fromRate;
		convertedAmount = amount * conversionRate;

		return convertedAmount;
        	
	}

	public String getRates() {
		return  webClient.getRates();
	}
		

}
	