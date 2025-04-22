package com.rest.converter;

import com.rest.dto.ProductDto;
import com.rest.entity.Product;
import com.rest.service.UsdConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Bidirectional converter between {@link Product} entities and {@link ProductDto} objects with currency formatting.
 * <p>
 * Responsibilities:
 * - Entity->DTO: Adds formatted USD price using current exchange rate
 * - DTO->Entity: Persists core product data without currency information
 * - Maintains numeric formatting consistency across conversions
 *
 * @see UsdConvertService For exchange rate retrieval
 */
@Component("productDtoConverter")
public class ProductDtoConverter {

    @Autowired
    private UsdConvertService usdConvertService;

    /**
     * Converts Product entity to API-ready DTO with formatted prices
     * @param entity Source entity from database
     * @param usdRate Current EUR->USD exchange rate (typically from {@link UsdConvertService#findUSDRate()})
     * @return DTO with prices rounded to 2 decimal places
     * @throws ArithmeticException If usdRate produces invalid decimal formatting
     */
    public ProductDto convert(final Product entity, final Double usdRate) {
        DecimalFormat df = new DecimalFormat("#.##");
        ProductDto productDto = new ProductDto();
        productDto.setId(entity.getId());
        productDto.setCode(entity.getCode());
        productDto.setName(entity.getName());
        productDto.setPriceEur(entity.getPriceEur());
        productDto.setPriceUsd(Double.valueOf(df.format( entity.getPriceEur() * usdRate)));
        productDto.setAvailable(entity.getAvailable());
        return productDto;
    }

    /**
     * Converts Product DTO to persistence-ready entity
     * @param dto API input containing new product data
     * @return Entity with core fields populated
     * @apiNote Ignores USD price field as it's calculated dynamically
     */
    public Product convert(final ProductDto dto) {
        Product product = new Product();
        product.setCode(dto.getCode());
        product.setName(dto.getName());
        product.setPriceEur(dto.getPriceEur());
        product.setAvailable(dto.getAvailable());
        return product;
    }

    /**
     * Batch converts entities to DTOs using a shared exchange rate
     * @param entities Products from database
     * @param usdRate Rate applied to all conversions (null-safe)
     * @return List of DTOs with uniform currency formatting
     * @see #convert(Product, Double) For individual conversion logic
     */
    public List<ProductDto> convertToDto(final List<Product> entities, final Double usdRate) {
        return entities.stream().map(e -> convert(e, usdRate)).toList();
    }

    /**
     * Batch converts DTOs to entities for bulk operations
     * @param dtos Products from API input
     * @return List of entities ready for persistence
     * @see #convert(ProductDto) For individual conversion logic
     */
    public List<Product> convertToEntity(final List<ProductDto> dtos) {
        return dtos.stream().map(this::convert).toList();
    }
}
