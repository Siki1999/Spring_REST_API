package com.rest.service;

import com.rest.dto.ExchangeRates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Service handling EUR->USD conversion using daily exchange rates from HNB API.
 * <p>
 * Features:
 * - Real-time rate fetching from HNB API (https://api.hnb.hr)
 * - Fallback to 1.0 rate when API unavailable
 * - Audit logging of conversion operations
 */
@Service
public class UsdConvertService {

    /**
     * Logger instance for tracking API calls and conversion operations
     */
    private static final Logger log = LoggerFactory.getLogger(UsdConvertService.class);

    /**
     * REST client configured for HNB API communication
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Fetches latest EUR->USD exchange rate from HNB API
     * @return Current exchange rate or 1.0 if unavailable
     * @throws org.springframework.dao.InvalidDataAccessApiUsageException If API returns malformed data
     * @throws org.springframework.web.client.ResourceAccessException If API endpoint is unreachable
     * @apiNote Rates are not cached - calls external API directly. Consider rate limiting
     *          when using in high-frequency scenarios.
     */
    public Double findUSDRate() {
        log.info("Finding USD rate.");
        String url = "https://api.hnb.hr/tecajn-eur/v3?valuta=USD";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            final ResponseEntity<List<ExchangeRates>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
            });

            if (response.getBody() == null || response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("USD Rate not found.");
            }

            final List<ExchangeRates> rates = response.getBody();
            log.info("Found USD rate. Rate: " + rates.get(0).getSrednji_tecaj());
            final String srednji_tecaj = rates.get(0).getSrednji_tecaj().replace(",", ".");
            return Double.parseDouble(srednji_tecaj);
        } catch (Exception e) {
            log.error("Error finding USD rate.", e);
        }

        log.info("USD rate was not found. Defaulting to 1.");
        return 1.0;
    }
}
