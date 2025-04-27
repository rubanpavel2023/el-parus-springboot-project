package com.example.el_parus_springboot_project.Controllers;

import com.example.el_parus_springboot_project.Entity.Order;
import com.example.el_parus_springboot_project.Repositories.OrderRepository;
import com.example.el_parus_springboot_project.Service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        String sessionId = session.getId();
        return orderService.placeOrder(orderData, sessionId);
    }


    //FOR ADMIN -> VIEW ORDERS

    @GetMapping("/phone/{phone}")
    public ResponseEntity<List<?>> getOrdersByPhone(@PathVariable String phone) {
        return orderService.getOrdersByPhone(phone);
    }

    @GetMapping("/status/reserved")
    public ResponseEntity<List<?>> getOrdersByStatusReserved() {
        return orderService.getOrdersByStatusReserved();
    }

    @GetMapping("/status/completed")
    public ResponseEntity<List<?>> getOrdersByStatusCompleted() {
        return orderService.getOrdersByStatusCompleted();
    }

    //FOR ADMIN -> CLOSING ORDERS
    @GetMapping("/phone/{phone}/completed")
    public ResponseEntity<List<?>> getOrdersByPhoneOnlyReserved(@PathVariable String phone) {
        return orderService.getOrdersByPhoneAndStatusReserved(phone);
    }

    @PutMapping("/{id}/completed")
    public ResponseEntity<Map<String, String>> markOrderAsCompleted(@PathVariable("id") Long orderId) {
        return orderService.updateStatusOrderToCompleted(orderId);
    }
    //____________________________________________________________________
   //FOR ADMIN -> DELETING ORDERS
    @GetMapping("/phone/{phone}/reserved")
    public ResponseEntity<List<?>> getOrdersByPhoneAndStatusReserved(@PathVariable String phone) {
        return orderService.getOrdersByPhoneAndStatusReserved(phone);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<Map<String, String>> deleteOrderByPhoneAndStatusReserved(@PathVariable("orderId") Long orderId) {
        return orderService.deleteOrderWithStatusReservedById(orderId);
    }

    @DeleteMapping("/delete/completed")
    public ResponseEntity<Map<String, String>> deleteCompletedOrders() {
        return orderService.deleteOrderByStatusCompleted();
    }

    @GetMapping("/load-date-range")
    public ResponseEntity<Map<String, Object>> loadOrdersByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return orderService.getOrdersByStatusCompletedForPeriod(startDate, endDate);
    }


}



