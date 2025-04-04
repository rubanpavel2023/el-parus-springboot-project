package com.example.el_parus_springboot_project.Controllers;

import com.example.el_parus_springboot_project.Entity.Cart;
import com.example.el_parus_springboot_project.Service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/addToCart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Cart item, HttpSession session) {
        String sessionId = session.getId();
        Map<String, Object> newCartItems = cartService.addToCart(item, sessionId);

        if (newCartItems.get("message").toString().contains("Incorrect") ||
                newCartItems.get("message").toString().contains("exceeded")) {
            return ResponseEntity.badRequest().body(newCartItems);
        }

        return ResponseEntity.ok(newCartItems);
    }

    @GetMapping("/getCartItems")
    @ResponseBody
    public ResponseEntity<List<Cart>> getCartItems(HttpSession session) {
        String sessionId = session.getId();
        List<Cart> cart = cartService.getCartBySession(sessionId);

        if (cart.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cart);
    }

    @Transactional
    @PostMapping("/clearCart")
    @ResponseBody
    public ResponseEntity<String> clearCart(HttpSession session) {
        String sessionId = session.getId();
        return cartService.clearCart(sessionId);
    }

    @GetMapping("")
    public String cartPage() {
        return "cart";
    }

    @GetMapping("/dataAllCarts")
    public ResponseEntity<Map<String, Object>> getAllDataCarts() {
        return cartService.getAllDataCarts();
    }

    @GetMapping("/total-amount")
    public ResponseEntity<Double> getTotalAmount() {
        return cartService.calculateTotalAmountForAllCarts();
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Map<String, String>> deleteAllCartsBuyers() {
        return ResponseEntity.ok(cartService.deleteAllCartsCustomers());
    }
}