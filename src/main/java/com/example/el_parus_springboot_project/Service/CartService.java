package com.example.el_parus_springboot_project.Service;

import com.example.el_parus_springboot_project.Entity.CartItem;
import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Repositories.CartItemRepository;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Transactional
    public Map<String, Object> addToCart(CartItem item, String sessionId) {
        item.setSessionId(sessionId);

        if (item.getArticle() == null || item.getQuantity() < 1 || item.getPricePerUnit() <= 0) {
            return Map.of("message", "Incorrect data for adding to cart", "errorCode", "INVALID_DATA");
        }

        Goods goods = getGoodsByArticle(item.getArticle());
        if (goods == null) {
            return Map.of("message", "Goods not found", "errorCode", "ITEM_NOT_FOUND");
        }
        int startAvailableQuantity = goods.getQuantity();

        int totalReservedQuantityOtherBuyers = getTotalReservedItemsOtherBuyers(item);
        int availableQuantity = startAvailableQuantity - totalReservedQuantityOtherBuyers;

        CartItem existingItem = getCartItems(item.getArticle(), sessionId);
        int currentCartQuantity = (existingItem != null) ? existingItem.getQuantity() : 0;


        if (item.getQuantity() > availableQuantity) {
            return Map.of(
                    "message", "The requested quantity exceeds the available quantity. Remaining: " + availableQuantity + " un.");

        }

        int updatedAvailableQuantity = currentCartQuantity + item.getQuantity();

        if (existingItem != null) {
            existingItem.setQuantity(updatedAvailableQuantity);
            existingItem.setCreatedTime(LocalDateTime.now());
            cartItemRepository.save(existingItem);
        } else {
            item.setQuantity(updatedAvailableQuantity);
            item.setCreatedTime(LocalDateTime.now());
            cartItemRepository.save(item);
        }

        int newActualAvailableQuantity = availableQuantity - item.getQuantity();
        goods.setQuantity(newActualAvailableQuantity);
        goodsRepository.save(goods);
        return Map.of(
                "message", "The goods has been successfully added to your cart!",
                "updatedAvailableQuantity", updatedAvailableQuantity
        );
    }

    public Goods getGoodsByArticle(String article) {
        return goodsRepository.findByArticle(article);
    }

    public List<CartItem> getCartItems(String sessionId) {
        return cartItemRepository.findBySessionId(sessionId);
    }

    public CartItem getCartItems(String article, String sessionId) {
        return cartItemRepository.findByArticleAndSessionId(article, sessionId);
    }

    public int getTotalReservedItemsOtherBuyers(CartItem item) {
        return getCartItems(item.getArticle()).stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    @Transactional
    public ResponseEntity<String> clearCart(String sessionId) {
        List<CartItem> cartItems = getCartItems(sessionId);
        for (CartItem item : cartItems) {
            Goods goods = getGoodsByArticle(item.getArticle());
            if (goods != null) {
                goods.setQuantity(goods.getQuantity() + item.getQuantity());
                goodsRepository.save(goods);
            }
        }
        cartItemRepository.deleteBySessionId(sessionId);
        return ResponseEntity.ok("The cart has been emptied successfully");
    }

    //for admin

    @Transactional
    public ResponseEntity<Map<String, Object>> getCartData() {
        try {
            List<Map<String, Object>> cartItems = cartItemRepository.getCartItems();
            List<Map<String, Object>> sessionTotals = cartItemRepository.getSessionTotals();
            return ResponseEntity.ok(Map.of(
                    "cartItems", cartItems,
                    "sessionTotals", sessionTotals
            ));
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: failed to load cart data"));
        }
    }

    @Transactional
    public ResponseEntity<Double> calculateTotalAmount() {
        try {
            Double totalAmount = cartItemRepository.getTotalAmount();
            return ResponseEntity.ok(totalAmount != null ? totalAmount : 0.0);
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Transactional
    public Map<String, String> deleteAllCartsCustomers() {
        try {
            List<CartItem> cartsFromDBase = cartItemRepository.findAll();
            if (cartsFromDBase.isEmpty()) {
                return Map.of("message", "No carts found in the database to delete");
            }
            for (CartItem cart : cartsFromDBase) {
                Map<String, Integer> articlesMap = cart.getArticlesMap();
                for (Map.Entry<String, Integer> entry : articlesMap.entrySet()) {
                    String article = entry.getKey();
                    int quantity = entry.getValue();

                    Goods goodsFromDBase = goodsRepository.findByArticle(article);
                    if (goodsFromDBase != null) {
                        int updatedQuantity = goodsFromDBase.getQuantity() + quantity;
                        goodsFromDBase.setQuantity(updatedQuantity);
                        goodsRepository.save(goodsFromDBase);
                    }
                }
            }
            cartItemRepository.deleteAll();
            return Map.of("message", "All carts in the database have been successfully deleted. " +
                    "Stock updated successfully");
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return Map.of("message", "Database error: unable to delete ");

        }
    }

}


