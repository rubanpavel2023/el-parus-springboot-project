package com.example.el_parus_springboot_project.Controllers;

import com.example.el_parus_springboot_project.Entity.Order;
import com.example.el_parus_springboot_project.Repositories.OrderRepository;
import com.example.el_parus_springboot_project.Service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/placeOrder")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody Map<String, Object> orderData, HttpSession session) {
        try {

            String sessionId = session.getId();
            Map<String, Object> response = orderService.placeOrder(orderData, sessionId);

            return ResponseEntity.ok(Map.of(
                    "message", "Your order has been successfully placed!",
                    "order", response
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Error while placing your order(: " + e.getMessage()
            ));
        }
    }

    //for admin
    @GetMapping("/all")
    public ResponseEntity<List<?>> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<List<?>> getOrdersByPhone(@PathVariable String phone) {
        return orderService.getOrdersByPhone(phone);
    }

    @GetMapping("/status/reserved")
    public ResponseEntity<List<?>> getOrdersByStatusReserved() {
        return orderService.getOrdersByStatusReserved();
    }

    @GetMapping("/phone/{phone}/reserved")
    public ResponseEntity<List<?>> getOrdersByPhoneAndStatusReserved(@PathVariable String phone) {
        return orderService.getOrdersByPhoneAndStatusReserved(phone);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<Map<String, String>> deleteOrderByPhoneAndStatusReserved(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderService.deleteOrderWithStatusReservedById(orderId));
    }

    @GetMapping("/status/completed")
    public ResponseEntity<List<?>> getOrdersByStatusCompleted() {
        return orderService.getOrdersByStatusCompleted();
    }


    @DeleteMapping("/delete/completed")
    public ResponseEntity<Map<String, String>> deleteCompletedOrders() {
        return ResponseEntity.ok(orderService.deleteOrderByStatusCompleted());
    }

    @GetMapping("/phone/{phone}/completed")
    public ResponseEntity<List<?>> getOrdersByPhoneOnlyReserved(@PathVariable String phone) {
        return orderService.getOrdersByPhoneAndStatusReserved(phone);
    }

    @PutMapping("/{id}/completed")
    public ResponseEntity<Map<String, String>> markOrderAsCompleted(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.updateStatusOrderToCompleted(orderId));
    }


}



