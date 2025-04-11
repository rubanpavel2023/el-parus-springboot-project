package com.example.el_parus_springboot_project.Service.CartService;

import com.example.el_parus_springboot_project.Entity.Cart;
import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Repositories.CartRepository;
import com.example.el_parus_springboot_project.Service.GoodsService;
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
    private CartRepository cartRepository;

    @Autowired
    private GoodsService goodsService;

    @Transactional
    public Map<String, Object> addToCart(Cart item, String sessionId) {
        item.setSessionId(sessionId);

        if (item.getArticle() == null || item.getQuantity() < 1 || item.getPricePerUnit() <= 0) {
            return Map.of("message", "Incorrect data for adding to cart", "errorCode", "INVALID_DATA");
        }

        Goods goods = goodsService.getGoodsByArticle(item.getArticle());
        if (goods == null) {
            return Map.of("message", "Goods not found", "errorCode", "ITEM_NOT_FOUND");
        }
        int startAvailableQuantity = goods.getQuantity();

        int totalReservedQuantity = getTotalReservedItemsInAllCartsOtherCustomers(item);
        int availableQuantity = startAvailableQuantity - totalReservedQuantity;

        Cart existingItem = getCartItemsByArticleAndBySession(item.getArticle(), sessionId);
        int currentCartQuantity = (existingItem != null) ? existingItem.getQuantity() : 0;


        if (item.getQuantity() > availableQuantity) {
            return Map.of(
                    "message", "The requested quantity exceeds the available quantity. Remaining: " + availableQuantity + " un.");

        }
        int updatedAvailableQuantity = currentCartQuantity + item.getQuantity();

        if (existingItem != null) {

            existingItem.getArticlesMap().put(
                    existingItem.getArticle(),
                    existingItem.getArticlesMap().getOrDefault(existingItem.getArticle(), 0) + item.getQuantity()
            );

            existingItem.setQuantity(updatedAvailableQuantity);
            existingItem.setCreatedTime(LocalDateTime.now());
            cartRepository.save(existingItem);
        } else {
            item.getArticlesMap().put(item.getArticle(), item.getQuantity());
            item.setQuantity(updatedAvailableQuantity);
            item.setCreatedTime(LocalDateTime.now());
            cartRepository.save(item);
        }
        int newActualAvailableQuantity = availableQuantity - item.getQuantity();
        goods.setQuantity(newActualAvailableQuantity);
        goodsService.saveGoods(goods);
        return Map.of(
                "message", "The goods has been successfully added to your cart!",
                "updatedAvailableQuantity", updatedAvailableQuantity
        );
    }

    public Cart getCartItemsByArticleAndBySession(String article, String sessionId) {
        return cartRepository.findByArticleAndSessionId(article, sessionId);
    }

    public List<Cart> getCartBySession(String sessionId) {
        return cartRepository.findBySessionId(sessionId);
    }

    @Transactional
    public ResponseEntity<String> clearCart(String sessionId) {
        List<Cart> cartItems = getCartBySession(sessionId);
        for (Cart item : cartItems) {
            Goods goods = goodsService.getGoodsByArticle(item.getArticle());
            if (goods != null) {
                goods.setQuantity(goods.getQuantity() + item.getQuantity());
                goodsService.saveGoods(goods);
            }
        }
        clearCartBySessionId(sessionId);
        return ResponseEntity.ok("The cart has been emptied successfully");
    }

    public void clearCartBySessionId(String sessionId) {
        cartRepository.deleteBySessionId(sessionId);
    }

    //for admin

    @Transactional
    public ResponseEntity<Map<String, Object>> getAllDataCarts() {
        try {
            List<Map<String, Object>> cartItems = cartRepository.getCarts();
            List<Map<String, Object>> sessionTotals = cartRepository.getSessionTotals();
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
    public ResponseEntity<Double> calculateTotalAmountForAllCarts() {
        try {
            Double totalAmount = cartRepository.getTotalAmount();
            return ResponseEntity.ok(totalAmount != null ? totalAmount : 0.0);
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    public int getTotalReservedItemsInAllCartsOtherCustomers(Cart item) {
        List<Cart> cartsWithSameArticle = cartRepository.findByArticle(item.getArticle());
        return cartsWithSameArticle.stream()
                .filter(cart -> !cart.getSessionId().equals(item.getSessionId()))
                .mapToInt(Cart::getQuantity)
                .sum();
    }

    @Transactional
    public Map<String, String> deleteAllCartsCustomers() {
        try {
            List<Cart> cartsFromDBase = cartRepository.findAll();
            if (cartsFromDBase.isEmpty()) {
                return Map.of("message", "No carts found in the database to delete");
            }
            for (Cart cart : cartsFromDBase) {
                Map<String, Integer> articlesMap = cart.getArticlesMap();
                for (Map.Entry<String, Integer> entry : articlesMap.entrySet()) {
                    String article = entry.getKey();
                    int quantity = entry.getValue();

                    Goods goodsFromDBase = goodsService.getGoodsByArticle(article);
                    if (goodsFromDBase != null) {
                        int updatedQuantity = goodsFromDBase.getQuantity() + quantity;
                        goodsFromDBase.setQuantity(updatedQuantity);
                        goodsService.saveGoods(goodsFromDBase);
                    }
                }
            }
            cartRepository.deleteAll();
            return Map.of("message", "All carts in the database have been successfully deleted. " +
                    "Stock updated successfully");
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return Map.of("message", "Database error: unable to delete ");

        }
    }

}

