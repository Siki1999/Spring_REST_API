package com.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.converter.ProductDtoConverter;
import com.rest.dto.ProductDto;
import com.rest.entity.Product;
import com.rest.repository.ProductRepository;
import com.rest.web.response.ProductResponse;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Core service for product management operations with pagination and filtering
 * <p>
 * Key Features:
 * - CRUD operations with JPA repository integration
 * - Dynamic filtering using JSON query parameters
 * - Pagination and sorting support
 * - Currency conversion integration
 *
 * @see ProductRepository For database interactions
 * @see ProductDtoConverter For DTO/entity transformations
 */
@Service
public class ProductService {

    /**
     * JPA repository for product persistence operations
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Service for finding EUR->USD exchange rate
     */
    @Autowired
    private UsdConvertService usdConvertService;

    /**
     * Converter for DTO/entity transformations
     */
    @Autowired
    private ProductDtoConverter productDtoConverter;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ObjectMapper jsonMapper = new ObjectMapper();

    private long totalItems = 0;

    /**
     * Retrieves paginated/filtered products with USD pricing
     * @param page Zero-based page index (0+) 
     * @param pageSize Number of items per page (1-100)
     * @param sort Sorting criteria in format: property(,asc|desc)
     * @param filter JSON filter object (e.g. {"name":"widget", "available":true})
     * @return Pair containing:
     *         - ProductResponse with DTOs or errors
     *         - Total items count for pagination
     * @throws org.springframework.data.mapping.PropertyReferenceException For invalid sort/filter fields
     */
    public Pair<ProductResponse, Long> getAllProducts(final Integer page, final Integer pageSize, final String sort, final String filter) {
        log.info("Fetching all products from repository");
        final ProductResponse productResponse = new ProductResponse();
        List<Product> allProducts = new LinkedList<>();
        try {
            allProducts.addAll(findAllProducts(page, pageSize, sort, filter));
        } catch (Exception e) {
            log.error("Error fetching products: {}", e.getMessage());
            productResponse.addError("Error fetching products. Please check logs.");
            return Pair.of(productResponse, totalItems);
        }

        if (allProducts.isEmpty()) {
            log.info("No products found.");
            productResponse.addError("No products found.");
            return Pair.of(productResponse, totalItems);
        }

        final Double usdRate = usdConvertService.findUSDRate();
        List<ProductDto> productDtos = productDtoConverter.convertToDto(allProducts, usdRate);
        productResponse.setProducts(productDtos);
        log.info("Fetched all products.");
        return Pair.of(productResponse, totalItems);
    }

    /**
     * Finds product by ID with validation
     * @param id Must be > 0 and exist in database
     * @return ProductResponse with single DTO or error
     * @see ProductRepository#findById(Object) For underlying lookup
     */
    public ProductResponse getProduct(final Long id) {
        if (id == null) {
            log.error("Id is null.");
            return new ProductResponse(Collections.singletonList("Id is null."));
        }
        if (id <= 0) {
            log.error("Id must be a positive number");
            return new ProductResponse(Collections.singletonList("Id must be a positive number"));
        }

        log.info("Fetching product with ID: {}", id);
        final ProductResponse productResponse = new ProductResponse();
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            log.info("No product found.");
            productResponse.addError("No product found.");
            return productResponse;
        }

        final Double usdRate = usdConvertService.findUSDRate();
        ProductDto productDto = productDtoConverter.convert(product.get(), usdRate);
        productResponse.addProduct(productDto);
        log.info("Product found.");
        return productResponse;
    }

    /**
     * Creates new product with duplicate code prevention
     * @param productDto Input must have unique code and valid price
     * @return ProductResponse with created DTO or errors
     * @apiNote Price is rounded to 2 decimal places before persistence
     */
    public ProductResponse addProduct(final ProductDto productDto) {
        if (Objects.isNull(productDto)) {
            log.error("Product is null.");
            return new ProductResponse(Collections.singletonList("Product is null."));
        }
        if (productRepository.existsByCode(productDto.getCode())) {
            log.error("Product with code {} already exists.", productDto.getCode());
            return new ProductResponse(Collections.singletonList("Product with code " + productDto.getCode() + " already exists."));
        }

        log.info("Adding product to database.");
        final ProductResponse productResponse = new ProductResponse();
        DecimalFormat df = new DecimalFormat("#.##");
        productDto.setPriceEur(Double.valueOf(df.format(productDto.getPriceEur())));
        Product product = productDtoConverter.convert(productDto);
        productRepository.save(product);
        productResponse.addProduct(productDto);
        log.info("Product added.");
        return productResponse;
    }

    /**
     * Finds all products with pagination and optional filtering.
     *
     * @param page     Zero-based page index (0+)
     * @param pageSize Number of items per page (1-100)
     * @param sort     Sorting criteria in format: property(,asc|desc)
     * @param filter   JSON filter object (e.g. {"name":"widget", "available":true})
     * @return List of products matching filter criteria
     */
    private List<Product> findAllProducts(final Integer page, final Integer pageSize, final String sort, final String filter) {
        totalItems = 0;
        Specification<Product> filters = null;
        if (filter != null && !"{}".equals(filter)) {
            try {
                final Map<String, String> filterObject = jsonMapper.readValue(filter, Map.class);
                filters = (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    for (final Map.Entry<String, String> entry : filterObject.entrySet()) {
                        Predicate predicate = criteriaBuilder.like(root.get(entry.getKey()),"%" + entry.getValue() + "%");
                        predicates.add(predicate);
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                };
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sort));
        Page<Product> allProducts = productRepository.findAll(filters, pageable);
        totalItems = allProducts.getTotalElements();
        return allProducts.getContent();
    }
}
