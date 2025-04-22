package com.rest.web.response;

import com.rest.dto.ProductDto;

import java.util.LinkedList;
import java.util.List;

/**
 * Unified REST API response wrapper for product
 * <p>
 * Handles both successful responses and error conditions through:
 * <ul>
 *   <li>{@link #errors} - Operational failure details</li>
 *   <li>{@link #products} - Successful response payload</li>
 * </ul>
 */
public class ProductResponse {

    /** Endpoint: GET /products
     * Retrieves all products in the catalog */
    public static final String GET_ALL_URL = "/products";

    /** Endpoint: GET /product/{id}
     * Fetches single product by numeric ID */
    public static final String GET_URL = "/product/{id}";

    /** Endpoint: POST /product
     * Adds a new product to the catalog */
    public static final String POST_URL = "/product";

    /**
     * Error messages collection (non-empty indicates failure)
     * <p>
     * Usage patterns:
     * <ol>
     *   <li>Single error - Add one message with {@link #addError}</li>
     *   <li>Multiple errors - Initialize via constructor</li>
     * </ol>
     */
    private List<String> errors = new LinkedList<>();

    /**
     * Successful operation payload
     * <p>
     * Contains validated product data when:
     * <ul>
     *   <li>No errors present</li>
     *   <li>Request processing completed successfully</li>
     * </ul>
     */
    private List<ProductDto> products = new LinkedList<>();

    /**
     * Default constructor for empty response initialization
     */
    public ProductResponse() {}

    /**
     * Error-only response constructor
     * @param errors Pre-populated error collection
     */
    public ProductResponse(List<String> errors) {
        this();
        this.errors = errors;
    }

    /**
     * Successful response constructor with payload
     * @param errors Empty list for successful responses
     * @param products Validated product data collection
     */
    public ProductResponse(List<String> errors, List<ProductDto> products) {
        this(errors);
        this.products = products;
    }

    /**
     * Retrieves the error messages collection
     * @return Non-empty list indicates operational failure
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Sets the error messages collection
     * @param errors Pre-populated error collection
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Appends an error message to the collection
     * @param error Descriptive error message
     */
    public void addError(String error) {
        this.errors.add(error);
    }

    /**
     * Retrieves the successful operation payload
     * @return Validated product data collection
     */
    public List<ProductDto> getProducts() {
        return products;
    }

    /**
     * Sets the successful operation payload
     * @param products Validated product data collection
     */
    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    /**
     * Adds a validated product to the payload
     * @param product Successfully processed product
     */
    public void addProduct(ProductDto product) {
        this.products.add(product);
    }
}
