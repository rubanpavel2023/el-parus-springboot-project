package com.example.el_parus_springboot_project.Service;

import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Repositories.CartItemRepository;
import com.example.el_parus_springboot_project.Entity.CartItem;
import com.example.el_parus_springboot_project.Entity.Order;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository;
import com.example.el_parus_springboot_project.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private GoodsRepository goodsRepository;


    public boolean isValidName(String name) {
        String namePattern = "^[A-ZА-ЯЁ][a-zа-яё-]*$";
        return name != null && Pattern.matches(namePattern, name);
    }

    public boolean isValidPhone(String phone) {
        String phonePattern = "^[0-9]+$";
        return phone != null && Pattern.matches(phonePattern, phone);
    }

    @Transactional
    public Map<String, Object> placeOrder(Map<String, Object> orderData, String sessionId) throws Exception {

        String firstName = (String) orderData.get("firstName");
        String lastName = (String) orderData.get("lastName");
        String phone = (String) orderData.get("phone");

        if (!isValidName(firstName) || !isValidName(lastName)) {
            throw new IllegalArgumentException("The first and last name must contain only letters or dashes. The first letter is capitalized");
        }
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("The phone number must contain only numbers");
        }

        List<CartItem> cartItems = cartItemRepository.findBySessionId(sessionId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty. Order cannot be placed");
        }

        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPricePerUnit())
                .sum();

        StringBuilder itemsDescription = new StringBuilder();
        for (CartItem item : cartItems) {
            itemsDescription.append(item.getName())
                    .append(" (").append(item.getArticle()).append(", ")
                    .append("Size:").append(item.getSize()).append(", ")
                    .append("Quantity: ").append(item.getQuantity()).append("); ");
        }

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomerName(firstName + " " + lastName);
        order.setCustomerPhone(phone);
        order.setItemsDescription(itemsDescription.toString());
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);

        cartItemRepository.deleteBySessionId(sessionId);

        return Map.of(
                "id", order.getId(),
                "customerName", order.getCustomerName(),
                "customerPhone", order.getCustomerPhone(),
                "itemsDescription", order.getItemsDescription(),
                "totalPrice", order.getTotalPrice(),
                "orderDate", order.getOrderDate().toString()
        );
    }

    //for admin
    @Transactional
    public ResponseEntity<List<?>> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            return ResponseEntity.ok(orders);
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Database error: failed to load "));
        }
    }


    @Transactional
    public ResponseEntity<List<?>> getOrdersByPhone(String phone) {
        try {
            List<Order> orders = orderRepository.findByCustomerPhone(phone);
            return ResponseEntity.ok(orders);
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Database error: failed to load "));
        }
    }

    @Transactional
    public ResponseEntity<List<?>> getOrdersByStatusReserved() {
        try {
            List<Order> orders = orderRepository.findByStatus("reserved");
            return ResponseEntity.ok(orders);
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Database error: failed to load "));
        }
    }

    @Transactional
    public ResponseEntity<List<?>> getOrdersByStatusCompleted() {
        try {
            List<Order> orders = orderRepository.findByStatus("completed");
            return ResponseEntity.ok(orders);
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Database error: failed to load "));
        }
    }

    @Transactional
    public Map<String, String> deleteOrderByStatusCompleted() {
        try {
            if (!orderRepository.existsByStatus("completed")) {
                return Map.of("message", "Orders with status 'Completed' are not in the database. \n" +
                        "No deletion required");
            }
            orderRepository.deleteByStatusCompleted();
            return Map.of("message", "All orders with status 'Completed' have been successfully deleted");

        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return Map.of("message", "Database error: unable to delete ");

        }
    }

    @Transactional
    public ResponseEntity<List<?>> getOrdersByPhoneAndStatusReserved(String phone) {
        try {
            List<Order> orders = orderRepository.findByCustomerPhoneWithReservedStatus(phone);
            return ResponseEntity.ok(orders);
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Database error: failed to load "));
        }
    }

    @Transactional
    public Map<String, String> updateStatusOrderToCompleted(Long id) {
        try {
            orderRepository.updateOrderStatus(id, "completed");
            return Map.of("message", "Order " + id + " successfully identified with status 'completed'");

        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return Map.of("message", "Database error: status update failed ");

        }
    }

    @Transactional
    public Map<String, String> deleteOrderWithStatusReservedById(Long id) {
        Order order = orderRepository.findById(id).get();

        Map<String, Integer> itemsMap = order.getOrderItemsMap();
        for (Map.Entry<String, Integer> entry : itemsMap.entrySet()) {
            String article = entry.getKey();
            int quantity = entry.getValue();

            Goods goodsFromDBase = goodsRepository.findByArticle(article);
            if (goodsFromDBase != null) {
                int updatedQuantity = goodsFromDBase.getQuantity() + quantity;
                goodsFromDBase.setQuantity(updatedQuantity);
                goodsRepository.save(goodsFromDBase);
            }
        }

        orderRepository.delete(order);
        return Map.of("message", "Order deleted." +
                "Stock updated successfully");
    }

}
