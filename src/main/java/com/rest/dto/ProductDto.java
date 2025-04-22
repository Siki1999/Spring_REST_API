package com.rest.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * Data Transfer Object representing product information
 */
public class ProductDto implements Serializable {

    /**
     * Unique product identifier
     */
    private Long id;

    /**
     * Unique product identifier code 
     * Must be exactly 10 alphanumeric characters
     */
    @NotBlank(message = "Product code is required")
    @Size(min = 10, max = 10, message = "Product code must be 10 characters long")
    private String code;
    
    /**
     * Product display name
     * Required field, maximum 100 characters
     */
    @NotBlank(message = "Product name is required")
    private String name;
    
    /**
     * Price in Euros
     * Required positive numeric value
     */
    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be a positive number")
    private Double priceEur;
    
    /**
     * Price in US Dollars (automatically calculated)
     * Optional field, derived from EUR rate
     */
    private Double priceUsd;
    
    /**
     * Product availability status
     * True = available for purchase
     * False = out of stock
     */
    private Boolean isAvailable;

    /**
     * Default constructor for serialization
     */
    public ProductDto() {}

    /**
     * Parameterized constructor
     * @param id Product ID
     * @param code Product code
     * @param name Product name
     * @param priceEur Price in Euros
     * @param priceUsd Price in US Dollars
     * @param isAvailable Product availability status
     */
    public ProductDto(long id, String code, String name, Double priceEur, Double priceUsd, Boolean isAvailable) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.priceEur = priceEur;
        this.priceUsd = priceUsd;
        this.isAvailable = isAvailable;
    }

    /**
     * Retrieves the product's unique identifier
     * @return Product ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the product's unique identifier
     * @param id Product ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the product's unique identifier code
     * @return 10-character product code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the product code with validation
     * @param code 10-character alphanumeric string
     * @throws IllegalArgumentException if code format is invalid
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Retrieves localized price value
     * @return price in Euros as positive double
     */
    public Double getPriceEur() {
        return priceEur;
    }

    /**
     * Sets Euro price with validation
     * @param priceEur Positive numeric value
     * @throws IllegalArgumentException if value is not positive
     */
    public void setPriceEur(Double priceEur) {
        this.priceEur = priceEur;
    }

    /**
     * Returns the product display name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product display name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns price in US Dollars
     */
    public Double getPriceUsd() {
        return priceUsd;
    }

    /**
     * Sets price in US Dollars
     */
    public void setPriceUsd(Double priceUsd) {
        this.priceUsd = priceUsd;
    }

    /**
     * Returns availability status
     */
    public Boolean getAvailable() {
        return isAvailable;
    }

    /**
     * Sets availability status
     */
    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
