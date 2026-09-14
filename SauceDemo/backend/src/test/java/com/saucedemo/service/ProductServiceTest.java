package com.saucedemo.service;

import com.saucedemo.model.Product;
import com.saucedemo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProductById_WhenIdExists_ReturnProduct() {

        Long productId = 11L;
        // Arrange 
        Product expectedProduct = new Product(
                "PRODUCT-001", 
                "Laptop Gamer", 
                "Laptop de alto rendimiento", 
                "16GB RAM, 1TB SSD", 
                1500.00, 
                "https://laptop.com/image.jpg"
        );
        expectedProduct.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(expectedProduct));

        // Act
        Optional<Product> actualProduct = productService.getProductById(productId);

        
        // Assert
        assertTrue(actualProduct.isPresent());
        assertEquals(expectedProduct, actualProduct.get());
        
        assertEquals(productId, actualProduct.get().getId());
        assertEquals("Laptop Gamer", actualProduct.get().getName());
        assertEquals(1500.00, actualProduct.get().getPrice());
        
        verify(productRepository, times(1)).findById(productId);
    }
}
