package com.rest.controller;

import com.rest.dto.ProductDto;
import com.rest.service.ProductService;
import com.rest.web.response.ProductResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for product management with pagination and filtering
 * <p>
 * Endpoints:
 * - GET /products: Retrieve paginated products with optional filtering
 * - GET /products/{id}: Get single product by ID
 * - POST /products: Create new product
 */
@RestController
@RequestMapping("/api")
public class ProductController {

    /**
     * Service layer dependency for product operations
     */
    @Autowired
    private ProductService productService;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    /**
     * GET /products
     * 
     * @param page Page number (default: 0)
     * @param pageSize Items per page (1-100, default: 10)
     * @param sort Sort field and direction (format: field,asc|desc)
     * @param filter JSON filter criteria (e.g. {"name":"widget"})
     * @return 200 OK with products and X-Total-Items header
     *         404 No products found
     * @see ProductService#getAllProducts For business logic
     */
    @RequestMapping(value = ProductResponse.GET_ALL_URL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
                                                           @RequestParam(value = "per_page", required = false, defaultValue = "10") final Integer pageSize,
                                                           @RequestParam(value = "sort", required = false, defaultValue = "name") final String sort,
                                                           @RequestParam(value = "filter", required = false, defaultValue = "{}") final String filter) {
        log.info("Initiating GET ALL PRODUCTS request");
        Pair<ProductResponse, Long> response = productService.getAllProducts(page, pageSize, sort, filter);

        if (!response.getFirst().getErrors().isEmpty()) {
            return new ResponseEntity<>(response.getFirst(), HttpStatus.NOT_FOUND);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.add("totalItems", String.valueOf(response.getSecond()));
        return new ResponseEntity<>(response.getFirst(), headers, HttpStatus.OK);
    }

    /**
     * GET /products/{id}
     * 
     * @param id Product ID (must be > 0)
     * @return 200 OK with product data
     *         404 Product not found
     */
    @RequestMapping(value = ProductResponse.GET_URL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> getProduct(@PathVariable final long id) {
        log.info("Processing GET PRODUCT request for ID: {}", id);
        ProductResponse productResponse = productService.getProduct(id);

        if (!productResponse.getErrors().isEmpty()) {
            return new ResponseEntity<>(productResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    /**
     * POST /products
     * 
     * @param productDto Product data (JSON)
     * @return 201 Created with new product
     *         400 Invalid input/duplicate code
     * @consumes application/json
     * @produces application/json
     */
    @RequestMapping(value = ProductResponse.POST_URL, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> addProduct(@RequestBody @Valid final ProductDto productDto) {
        log.info("Received POST request for new product: {}", productDto.getName());
        ProductResponse productResponse = productService.addProduct(productDto);

        if (!productResponse.getErrors().isEmpty()) {
            return new ResponseEntity<>(productResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }
}
