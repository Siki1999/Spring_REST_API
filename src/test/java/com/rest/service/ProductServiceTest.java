package com.rest.service;

import com.rest.converter.ProductDtoConverter;
import com.rest.dto.ProductDto;
import com.rest.entity.Product;
import com.rest.repository.ProductRepository;
import com.rest.web.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductDtoConverter productDtoConverter;

    @Mock
    private UsdConvertService usdConvertService;

    @InjectMocks
    private ProductService productService;

    private ProductDto validDto;
    private ProductDto nullDto;
    private Product validProduct;

    private Page<Product> okProductsPage;
    private ProductResponse okResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        validDto = new ProductDto(
                2,
            "P123456789",
            "Premium Widget", 
            99.99,
            149.99,
            true
        );
        
        validProduct = new Product(
            1L,
            "P123456789",
            "Premium Widget",
            99.99,
            true
        );

        okProductsPage = new PageImpl<>(List.of(validProduct, validProduct), Pageable.ofSize(1), 2);
        okResponse = new ProductResponse(Collections.emptyList(), List.of(validDto, validDto));
    }

    @Test
    void testAddProduct_Success() {
        when(productRepository.existsByCode("P123456789"))
            .thenReturn(false);
        when(productRepository.save(any(Product.class)))
            .thenReturn(validProduct);
        when(productDtoConverter.convert(any(ProductDto.class)))
            .thenReturn(validProduct);

        ProductResponse response = productService.addProduct(validDto);

        assertNotNull(response.getProducts());
        assertEquals(1, response.getProducts().size());
        assertTrue(response.getErrors().isEmpty());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testAddProduct_ProductNull() {
        ProductResponse response = productService.addProduct(nullDto);

        assertTrue(response.getProducts().isEmpty());
        assertEquals(1, response.getErrors().size());
        assertEquals("Product is null.", response.getErrors().get(0));
        verify(productRepository, never()).save(any());
    }

    @Test
    void testAddProduct_DuplicateCode() {
        when(productRepository.existsByCode("P123456789"))
            .thenReturn(true);

        ProductResponse response = productService.addProduct(validDto);

        assertTrue(response.getProducts().isEmpty());
        assertEquals(1, response.getErrors().size());
        assertEquals("Product with code " + validDto.getCode() + " already exists.", response.getErrors().get(0));
        verify(productRepository, never()).save(any());
    }

    @Test
    void testGetProduct_NotFound() {
        when(productRepository.findById(999L))
            .thenReturn(Optional.empty());

        ProductResponse response = productService.getProduct(999L);

        assertTrue(response.getProducts().isEmpty());
        assertEquals(1, response.getErrors().size());
        assertEquals("No product found.", response.getErrors().get(0));
        verify(productRepository).findById(999L);
    }

    @Test
    void testGetProduct_IdNull() {
        ProductResponse response = productService.getProduct(null);

        assertTrue(response.getProducts().isEmpty());
        assertEquals(1, response.getErrors().size());
        assertEquals("Id is null.", response.getErrors().get(0));
        verify(productRepository, never()).findById(any());
    }

    @Test
    void testGetProduct_NegativeId() {
        ProductResponse response = productService.getProduct(-9L);

        assertTrue(response.getProducts().isEmpty());
        assertEquals(1, response.getErrors().size());
        assertEquals("Id must be a positive number", response.getErrors().get(0));
        verify(productRepository, never()).findById(-9L);
    }

    @Test
    void testGetProduct_Success() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.ofNullable(validProduct));
        when(productDtoConverter.convert(any(Product.class), any(Double.class)))
                .thenReturn(validDto);
        when(usdConvertService.findUSDRate())
                .thenReturn(2.0);

        ProductResponse response = productService.getProduct(1L);

        assertFalse(response.getProducts().isEmpty());
        assertEquals(0, response.getErrors().size());
        assertEquals(validDto.getName(), response.getProducts().get(0).getName());
        verify(productRepository).findById(1L);
    }
}
