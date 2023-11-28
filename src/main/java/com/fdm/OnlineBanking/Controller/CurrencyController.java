package com.fdm.OnlineBanking.Controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fdm.OnlineBanking.Service.ForexService;


@RestController
@RequestMapping("/")
public class CurrencyController {
	
	@Autowired
	private ForexService forexService;
			
	@GetMapping("/currency")
	public String getAllCurrencies(){
		return forexService.getAllCurrencies();
	}
			
			
	@GetMapping("/convert")
	public double convertCurrencies(
			@RequestParam double amount, @RequestParam String from, @RequestParam String to
			)
	{
		return forexService.convertCurrency(amount, from, to);
	}
			
	@GetMapping("/rates")
	public String getRates() {
		return forexService.getRates();
	}
	
	
}

