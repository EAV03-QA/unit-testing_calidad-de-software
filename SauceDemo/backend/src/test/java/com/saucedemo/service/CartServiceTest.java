package com.saucedemo.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    void getCart_ProductExists_returnCartItems() {
        var cartItems = List.of(new CartItem(
            "session1", 
            new Product("code1", "Product 1", "Description 1", 10.0, "image1.jpg"),
            2), new CartItem(
            "session1",
            new Product("code2", "Product 2", "Description 2", 20.0, "image2.jpg"),
            1));

        when(cartItemRepository.findBySessionId("session1")).thenReturn(cartItems);
        
        List<CartItem> result = cartService.getCart("session1");

        assertFalse(result.isEmpty());
        assertEquals(cartItems, result);
    }
}
