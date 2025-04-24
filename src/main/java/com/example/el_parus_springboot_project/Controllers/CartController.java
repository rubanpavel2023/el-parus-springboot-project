package com.example.el_parus_springboot_project.Controllers;

import com.example.el_parus_springboot_project.Entity.Cart.CartArticleMap;
import com.example.el_parus_springboot_project.Service.CartService.CartRequest;
import com.example.el_parus_springboot_project.Service.CartService.CartService;
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
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody CartRequest request, HttpSession session) {
        String sessionId = session.getId();
        return cartService.addToCart(request.getItems(), sessionId);
    }


    @GetMapping("/getCartItems")
    @ResponseBody
    public ResponseEntity<List<CartArticleMap>> getCartItems(HttpSession session) {
        String sessionId = session.getId();
        return cartService.getCartBySession(sessionId);

    }

    @Transactional
    @PostMapping("/clearCart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clearCart(HttpSession session) {
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
