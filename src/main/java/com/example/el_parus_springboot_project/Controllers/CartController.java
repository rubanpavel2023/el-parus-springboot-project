package com.example.el_parus_springboot_project.Controllers;

import com.example.el_parus_springboot_project.Entity.CartItem;
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
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody CartItem item, HttpSession session) {
        String sessionId = session.getId();
        Map<String, Object> result = cartService.addToCart(item, sessionId);

        if (result.get("message").toString().contains("Incorrect") ||
                result.get("message").toString().contains("exceeded")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/getCartItems")
    @ResponseBody
    public ResponseEntity<List<CartItem>> getCartItems(HttpSession session) {
        String sessionId = session.getId();
        List<CartItem> cartItems = cartService.getCartItems(sessionId);

        if (cartItems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cartItems);
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

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getCartData() {
        return cartService.getCartData();
    }

    @GetMapping("/total-amount")
    public ResponseEntity<Double> getTotalAmount() {
        return cartService.calculateTotalAmount();
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Map<String, String>> deleteAllCartsBuyers() {
        return ResponseEntity.ok(cartService.deleteAllCartsCustomers());
    }
}













