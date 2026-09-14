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

    @Test
    void getAllProducts_ReturnListOfProducts() {
        // Arrange
        Product product1 = new Product(
                "PROD-002",
                "iPhone 18",
                "Chip potente y carga rápida",
                " chip A20 Pro, 64GB de almacenamiento, cámara de 108MP",
                5000.00,
                "https://iphone.jpg"
        );
        product1.setId(1L);

        Product product2 = new Product(
                "PROD-003",
                "Mouse Inalámbrico",
                "Mouse ergonómico",
                "Batería recargable, RGB",
                50.00,
                "https://mouse.jpg"
        );
        product2.setId(2L);

        List<Product> expectedProducts = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.getAllProducts();

        // Assert
        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());

        
        assertEquals(1L, actualProducts.get(0).getId());
        assertEquals("PROD-002", actualProducts.get(0).getCode());
        assertEquals("iPhone 18", actualProducts.get(0).getName());
        assertEquals(5000.00, actualProducts.get(0).getPrice());

        
        assertEquals(2L, actualProducts.get(1).getId());
        assertEquals("PROD-003", actualProducts.get(1).getCode());
        assertEquals("Mouse Inalámbrico", actualProducts.get(1).getName());
        assertEquals(50.00, actualProducts.get(1).getPrice());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_WhenIdDoesNotExist_ShouldReturnEmptyOptional() {
        // Arrange
        Long nonExistentId = 3L;
        
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<Product> actualProduct = productService.getProductById(nonExistentId);

        // Assert
        assertTrue(actualProduct.isEmpty()); 
        verify(productRepository, times(1)).findById(nonExistentId);
    }
}
