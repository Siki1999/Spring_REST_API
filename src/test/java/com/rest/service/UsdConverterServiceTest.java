package com.rest.service;

import com.rest.dto.ExchangeRates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UsdConverterServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UsdConvertService usdConverterService;

    ExchangeRates exchangeRates = new ExchangeRates("1", "2025-04-22", "SAD", "USA", "1.3255", "1.5435", "840", "2.0", "USD");;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findUSDRate_ReturnsValidRate_WhenApiResponds() {
        // Arrange
        exchangeRates.setSrednji_tecaj("1.1234");
        
        when(restTemplate.exchange(
            eq("https://api.hnb.hr/tecajn-eur/v3?valuta=USD"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            any(ParameterizedTypeReference.class))
        ).thenReturn(new ResponseEntity<>(Collections.singletonList(exchangeRates), HttpStatus.OK));

        // Act
        double rate = usdConverterService.findUSDRate();

        // Assert
        assertEquals(1.1234, rate);
    }

    @Test
    public void findUSDRate_ReturnsOne_WhenApiUnavailable() {
        when(restTemplate.exchange(
                eq("https://api.hnb.hr/tecajn-eur/v3?valuta=USD"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))
        ).thenThrow(new RuntimeException("API unavailable"));

        double rate = usdConverterService.findUSDRate();
        assertEquals(1.0, rate);
    }

    @Test
    public void findUSDRate_ReturnsOne_WhenMalformedResponse() {
        when(restTemplate.exchange(
            anyString(),
            any(),
            isNull(),
            any(ParameterizedTypeReference.class))
        ).thenReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK));

        double rate = usdConverterService.findUSDRate();
        assertEquals(1.0, rate);
    }

    @Test
    public void findUSDRate_ReturnsOne_WhenResponseNotOk() {
        when(restTemplate.exchange(
                anyString(),
                any(),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))
        ).thenReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST));

        double rate = usdConverterService.findUSDRate();
        assertEquals(1.0, rate);
    }
}
