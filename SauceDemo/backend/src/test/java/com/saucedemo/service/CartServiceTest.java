package com.saucedemo.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void shouldReturnCartItemsWhenProductExists() {
        CartItem cartItem = new CartItem(
            "session1", 
            new Product("code1", "Product 1", "Description 1", 10.0, "image1.jpg"),
            2);

        when(cartItemRepository.findBySessionId("session1")).thenReturn(List.of(cartItem));
    
        List<CartItem> cartItems = cartService.getCart("session1");

        assertEquals(1, cartItems.size());
        assertEquals("Product 1", cartItems.get(0).getProduct().getName());
        assertEquals(2, cartItems.get(0).getQuantity());
    }
}
