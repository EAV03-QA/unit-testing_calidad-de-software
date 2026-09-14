package com.saucedemo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saucedemo.model.CartItem;
import com.saucedemo.model.Product;
import com.saucedemo.repository.CartItemRepository;
import com.saucedemo.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock 
    private ProductRepository productRepository;

    @InjectMocks 
    private CartService cartService;

    @Test 
    void getCart_ProductInCart_returnCartItems() {
        var cartItems = List.of(new CartItem(
            "session1", 
            new Product("code1", "Product1", "Description1", 10.0, "image1.jpg"),
            2), new CartItem(
            "session1",
            new Product("code2", "Product2", "Description2", 20.0, "image2.jpg"),
            1));

        when(cartItemRepository.findBySessionId("session1")).thenReturn(cartItems);
        
        List<CartItem> result = cartService.getCart("session1");

        assertFalse(result.isEmpty());
        assertEquals(cartItems, result);
    }

    @Test
    void getCart_ProductNotInCart_returnEmptyList() {
        when(cartItemRepository.findBySessionId("session1")).thenReturn(List.of());
        
        List<CartItem> result = cartService.getCart("session1");

        assertTrue(result.isEmpty());
    }

    @Test 
    void addToCart_ProductNotInCart_ReturnsNewCartItem() {
        Long productId = 1L;
        var product = new Product("code1", "Product1", "Description1", 10.0, "image1.jpg");
        product.setId(productId);
        var cartItem = new CartItem("session1", product, 1);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findBySessionIdAndProductId("session1", 1L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.addToCart("session1", product.getId(), 1);

        assertNotNull(result);
        assertEquals(1, result.getQuantity());
        assertEquals(cartItem, result);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test 
    void addToCart_ProductInCart_ReturnsCartItem() {
        Long productId = 1L;
        var product = new Product("code1", "Product1", "Description1", 10.0, "image1.jpg");
        product.setId(productId);
        var cartItem = new CartItem("session1", product, 1);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findBySessionIdAndProductId("session1", 1L)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.addToCart("session1", product.getId(), 1);

        assertNotNull(result);
        assertEquals(2, result.getQuantity());
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void addToCart_ProductDoesNotExist_ThrowsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            NoSuchElementException.class, 
            () -> cartService.addToCart("session1", 1L, 1)
        );
        assertNotNull(exception);
    }

    @Test 
    void updateQuantity_ProductInCart_ReturnsUpdatedCartItem() {
        Long itemId = 1L;
        var product = new Product("code1", "Product1", "Description1", 10.0, "image1.jpg");
        var cartItem = new CartItem("session1", product, 1);
        cartItem.setId(itemId);

        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.updateQuantity(itemId, 3);

        assertNotNull(result);
        assertEquals(3, result.getQuantity());
        verify(cartItemRepository).save(any(CartItem.class));
    }
    @Test 
    void updateQuantity_ProductNotInCart_ThrowsException() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(
            NoSuchElementException.class, 
            () -> cartService.updateQuantity(1L, 3)
        );
        assertNotNull(exception);
    }
    @Test 
    void removeItem_ValidItemId_DeletesItem() {
        // Arrange
        Long itemId = 1L;

        // Act
        cartService.removeItem(itemId);

        // Assert
        verify(cartItemRepository).deleteById(itemId);
    }
}
