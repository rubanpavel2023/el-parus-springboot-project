package com.example.el_parus_springboot_project.Service;


import com.example.el_parus_springboot_project.Entity.Cart.CartArticleMap;
import com.example.el_parus_springboot_project.Entity.Goods.SizeQuantity;
import com.example.el_parus_springboot_project.Repositories.CartRepository;
import com.example.el_parus_springboot_project.Entity.Order;
import com.example.el_parus_springboot_project.Repositories.OrderRepository;
import com.example.el_parus_springboot_project.Service.CartService.CartService;
import com.example.el_parus_springboot_project.Service.SizeQuantityService.SizeQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartItemRepository;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SizeQuantityService sizeQuantityService;

    @Autowired
    private CartService cartService;


    @Transactional
    public ResponseEntity<Map<String, Object>> placeOrder(Map<String, Object> orderData, String sessionId) {

        try {
            String firstName = (String) orderData.get("firstName");
            String lastName = (String) orderData.get("lastName");
            String phone = (String) orderData.get("phone");

            if (!isValidName(firstName) || !isValidName(lastName)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "The first and last name must contain only letters or dashes. The first letter is capitalized"
                ));
            }
            if (!isValidPhone(phone)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "The phone number must contain only numbers"
                ));
            }

            List<CartArticleMap> cartItems = cartItemRepository.findCartItemsBySessionId(sessionId);
            if (cartItems == null || cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "Cart is empty. Order cannot be placed"
                ));
            }

            double totalPrice = cartItems.stream()
                    .mapToDouble(item -> item.getQuantity() * item.getPricePerUnit())
                    .sum();

            StringBuilder itemsDescription = new StringBuilder();
            for (CartArticleMap item : cartItems) {
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
            cartService.clearCartBySessionId(sessionId);

            return ResponseEntity.ok(Map.of(
                    "message", "Your order has been successfully placed!",
                    "order", Map.of(
                            "id", order.getId(),
                            "customerName", order.getCustomerName(),
                            "customerPhone", order.getCustomerPhone(),
                            "itemsDescription", order.getItemsDescription(),
                            "totalPrice", order.getTotalPrice(),
                            "orderDate", order.getOrderDate().toString()
                    )
            ));

        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error while placing your order: " + e.getMessage()));
        }
    }


    public boolean isValidName(String name) {
        String namePattern = "^[A-ZА-ЯЁ][a-zа-яё-]*$";
        return name != null && Pattern.matches(namePattern, name);
    }

    public boolean isValidPhone(String phone) {
        String phonePattern = "^[0-9]+$";
        return phone != null && Pattern.matches(phonePattern, phone);
    }


    //FOR ADMIN -> VIEW ORDERS
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
//_________________________________________________________________

    //FOR ADMIN -> CLOSING ORDERS
    @Transactional
    public ResponseEntity<Map<String, String>> updateStatusOrderToCompleted(Long id) {
        try {
            orderRepository.updateOrderStatus(id, "completed");
            return ResponseEntity.ok(Map.of("message", "Order " + id + " successfully identified with status 'completed'"));

        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Database error: status update failed "));

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
    //__________________________________________________________________

    //FOR ADMIN -> DELETING ORDERS
    @Transactional
    public ResponseEntity<Map<String, String>> deleteOrderByStatusCompleted() {
        try {
            if (!orderRepository.existsByStatus("completed")) {
                return ResponseEntity.ok(Map.of("message", "Orders with status 'Completed' are not in the database. \n" +
                        "No deletion required"));
            }
            orderRepository.deleteByStatusCompleted();
            return ResponseEntity.ok(Map.of("message", "All orders with status 'Completed' have been successfully deleted"));

        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Database error: unable to delete "));

        }
    }


    private List<Map<String, int[]>> parseItemsDescriptionService(String itemsDescription) {
        List<Map<String, int[]>> parsedItemsList = new ArrayList<>();
        String[] items = itemsDescription.split(";");

        for (String item : items) {
            if (item.contains("Quantity") && item.contains("Size")) {
                Map<String, int[]> itemMap = new HashMap<>();
                String article = item.substring(item.indexOf("(") + 1, item.indexOf(","));
                String sizeStr = item.substring(item.indexOf("Size:") + 5, item.indexOf(", Quantity")).trim();
                int size = Integer.parseInt(sizeStr);
                String quantityStr = item.substring(item.indexOf("Quantity:") + 9).trim().replace(")", "");
                int quantity = Integer.parseInt(quantityStr);
                itemMap.put(article, new int[]{size, quantity});
                parsedItemsList.add(itemMap);
            }
        }

        return parsedItemsList;
    }


    @Transactional
    public ResponseEntity<Map<String, String>> deleteOrderWithStatusReservedById(Long id) {
        try {
            Order order = orderRepository.findById(id).get();
            List<Map<String, int[]>> itemsMap = parseItemsDescriptionService(order.getItemsDescription());
            for (Map<String, int[]> itemOrder : itemsMap) {
                String article = itemOrder.keySet().stream().findFirst().orElse(null);
                int[] extractedValue = itemOrder.get(article);
                int size = extractedValue[0];
                int quantity = extractedValue[1];
                SizeQuantity goods = sizeQuantityService.getGoodsByArticleAndSize(article, size);
                if (goods != null) {
                    goods.setQuantity(goods.getQuantity() + quantity);
                    sizeQuantityService.saveGoods(goods);
                }
            }
            orderRepository.delete(order);
            return ResponseEntity.ok(Map.of("message", "Order deleted." +
                    "Stock updated successfully"));

        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error"));
        }
    }

    //FOR ADMIN -> SALES
    public ResponseEntity<Map<String, Object>> getOrdersByStatusCompletedForPeriod(LocalDate startDate, LocalDate endDate) {
        try {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            List<Order> ordersCompleted = orderRepository.findCompletedOrdersByDateRange(startDateTime, endDateTime);
            double totalAmount = calculateTotalAmount(ordersCompleted);
            return ResponseEntity.ok(Map.of(
                    "orders", ordersCompleted,
                    "totalAmount", totalAmount));

        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: failed to load orders data"));
        }
    }

    public static double calculateTotalAmount(List<Order> ordersCompleted) {
        double totalCompletedAmount = 0.0;

        for (Order order : ordersCompleted) {
            totalCompletedAmount += order.getTotalPrice();
        }
        return BigDecimal.valueOf(totalCompletedAmount)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}

