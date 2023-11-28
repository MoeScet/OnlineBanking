package com.fdm.OnlineBanking.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
//@ActiveProfiles("tests")
public class ForexServiceTest {

    @MockBean
    private ForexWebClient webClient;

    @Autowired
    private ForexService forexService;

    @Test
    @DisplayName("Test Get all currencies")
    public void testGetAllCurrencies() {
        
    	// Arrange
        String jsonString = "{\"currencies\":{\"USD\":\"United States Dollar\",\"EUR\":\"Euro\",\"GBP\":\"British Pound Sterling\",\"JPY\":\"Japanese Yen\",\"AUD\":\"Australian Dollar\",\"CAD\":\"Canadian Dollar\"}}";
        when(webClient.getCurrencies()).thenReturn(jsonString);

        // Act
        String result = forexService.getAllCurrencies();
        String expected = "USD, EUR, GBP, JPY, AUD, CAD";
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test conversion USD to EUR")
    public void testGetRates_USDtoEUR() {
        
    	// Arrange
    	String jsonString = "{\"rates\":{\"USD\":1.0,\"EUR\":0.843795,\"GBP\":0.725292,\"JPY\":109.513794,\"AUD\":1.353987,\"CAD\":1.262206}}";
        when(webClient.getRates()).thenReturn(jsonString);

        // Act
        double result = forexService.convertCurrency(100, "USD", "EUR");
        double expected = 84.379;;

        // Assert
        assertEquals(expected, result, 0.001);

    }
    
    @Test
    @DisplayName("Test conversion EUR to USD")
    public void testGetRates_EURtoUSD() {
        
    	// Arrange
    	String jsonString = "{\"rates\":{\"USD\":1.0,\"EUR\":0.843795,\"GBP\":0.725292,\"JPY\":109.513794,\"AUD\":1.353987,\"CAD\":1.262206}}";
        when(webClient.getRates()).thenReturn(jsonString);

        // Act
        double result = forexService.convertCurrency(100, "EUR", "USD");
        double expected = 118.512;

        // Assert
        assertEquals(expected, result, 0.001);

    }
    

    @Test
    @DisplayName("Test conversion AUD to GBP")
    public void testGetRates_AUDtoGBP() {
        
    	// Arrange
    	String jsonString = "{\"rates\":{\"USD\":1.0,\"EUR\":0.843795,\"GBP\":0.725292,\"JPY\":109.513794,\"AUD\":1.353987,\"CAD\":1.262206}}";
        when(webClient.getRates()).thenReturn(jsonString);

        // Act
        double result = forexService.convertCurrency(100, "AUD", "GBP");
        double expected = 53.567;

        // Assert
        assertEquals(expected, result, 0.001);

    }
    
    @Test
    @DisplayName("Test conversion GBP to AUD")
    public void testGetRates_GBPtoAUD() {
        
    	// Arrange
    	String jsonString = "{\"rates\":{\"USD\":1.0,\"EUR\":0.843795,\"GBP\":0.725292,\"JPY\":109.513794,\"AUD\":1.353987,\"CAD\":1.262206}}";
        when(webClient.getRates()).thenReturn(jsonString);

        // Act
        double result = forexService.convertCurrency(100, "GBP", "AUD");
        double expected = 186.6816;

        // Assert
        assertEquals(expected, result, 0.001);

    }












}


