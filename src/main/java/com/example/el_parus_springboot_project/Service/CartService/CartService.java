package com.example.el_parus_springboot_project.Service.CartService;

import com.example.el_parus_springboot_project.Entity.Cart.Cart;
import com.example.el_parus_springboot_project.Entity.Cart.CartArticleMap;
import com.example.el_parus_springboot_project.Entity.Goods.Goods;
import com.example.el_parus_springboot_project.Entity.Goods.SizeQuantity;
import com.example.el_parus_springboot_project.Repositories.CartRepository;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository.GoodsRepository;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository.SizeQuantityRepository;
import com.example.el_parus_springboot_project.Service.SizeQuantityService.SizeQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SizeQuantityService sizeQuantityService;

    @Autowired
    private SizeQuantityRepository sizeQuantityRepository;


    @Transactional
    public ResponseEntity<Map<String, Object>> addToCart(List<CartArticleMap> items, String sessionId) {

        try {

            if (items == null || items.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "no goods"));
            }

            Goods goods = goodsRepository.findByArticle(items.getFirst().getArticle());
            SizeQuantity sizeQuantity = sizeQuantityRepository.findByGoodsAndSize(goods, items.getFirst().getSize());
            if (sizeQuantity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Size " + items.getFirst().getSize() + " for the goods " + items.getFirst().getArticle() + " not found"));
            }

            Integer startAvailableQuantity = sizeQuantity.getQuantity();
            if (startAvailableQuantity < items.getFirst().getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Not enough goods. Available: " + startAvailableQuantity));
            }

            sizeQuantity.setQuantity(startAvailableQuantity - items.getFirst().getQuantity());
            Cart cart = cartRepository.findBySessionId(sessionId);
            if (cart == null) {
                Cart myNewCart = new Cart();
                myNewCart.setSessionId(sessionId);
                myNewCart.setCreatedTime(LocalDateTime.now());

                CartArticleMap newItem = new CartArticleMap(myNewCart, items.getFirst().getName(), items.getFirst().getPricePerUnit(), items.getFirst().getArticle(), items.getFirst().getSize(), items.getFirst().getQuantity());
                myNewCart.getCartArticleMaps().add(newItem);

                cartRepository.save(myNewCart);
            } else {
                CartArticleMap existingItem = cart.getCartArticleMaps().stream()
                        .filter(cam -> cam.getArticle().equals(items.getFirst().getArticle())
                                && cam.getSize().equals(items.getFirst().getSize()))
                        .findFirst()
                        .orElse(null);

                if (existingItem == null) {
                    CartArticleMap newMyCartArticleMap = new CartArticleMap(cart, items.getFirst().getName(), items.getFirst().getPricePerUnit(), items.getFirst().getArticle(), items.getFirst().getSize(), items.getFirst().getQuantity());
                    cart.getCartArticleMaps().add(newMyCartArticleMap);
                } else {
                    existingItem.setQuantity(existingItem.getQuantity() + items.getFirst().getQuantity());
                }
                cart.setCreatedTime(LocalDateTime.now());
                cartRepository.save(cart);
            }

            sizeQuantityRepository.save(sizeQuantity);

            return ResponseEntity.ok(Map.of("message", "The goods has been successfully added to your cart!"));
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error"));
        }
    }


    public ResponseEntity<List<CartArticleMap>> getCartBySession(String sessionId) {
        return ResponseEntity.ok(cartRepository.findCartItemsBySessionId(sessionId));
    }


    @Transactional
    public ResponseEntity<Map<String, Object>> clearCart(String sessionId) {
        try {
            List<CartArticleMap> cartItems = getCartBySessionForService(sessionId);
            for (CartArticleMap item : cartItems) {
                SizeQuantity goods = sizeQuantityService.getGoodsByArticleAndSize(item.getArticle(), item.getSize());
                if (goods != null) {
                    goods.setQuantity(goods.getQuantity() + item.getQuantity());
                    sizeQuantityService.saveGoods(goods);
                }
            }
            clearCartBySessionId(sessionId);
            return ResponseEntity.ok(Map.of("message", "The cart has been emptied successfully"));
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error"));
        }
    }

    public List<CartArticleMap> getCartBySessionForService(String sessionId) {
        return cartRepository.findCartItemsBySessionId(sessionId);
    }


    public void clearCartBySessionId(String sessionId) {
        Cart cart = cartRepository.findBySessionId(sessionId);
        if (cart != null) {
            cartRepository.delete(cart);
        }
    }

    //FOR ADMIN -> ACTIVE CARTS
    @Transactional
    public ResponseEntity<Map<String, Object>> getAllDataCarts() {
        try {
            List<Map<String, Object>> cartItems = cartRepository.getCartsForAdmin();
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

//_________________________________________________________________________________

    @Transactional
    public Map<String, String> deleteAllCartsCustomers() {
        try {
            List<Cart> cartsFromDBase = cartRepository.findAll();
            if (cartsFromDBase.isEmpty()) {
                return Map.of("message", "No carts found in the database to delete");
            }
            for (Cart cart : cartsFromDBase) {
                List<CartArticleMap> itemsCart = cart.getCartArticleMaps();
                for (CartArticleMap item : itemsCart) {
                    SizeQuantity goods = sizeQuantityService.getGoodsByArticleAndSize(item.getArticle(), item.getSize());
                    if (goods != null) {
                        goods.setQuantity(goods.getQuantity() + item.getQuantity());
                        sizeQuantityService.saveGoods(goods);
                        cartRepository.delete(cart);
                    }
                }
            }
            return Map.of("message", "All carts in the database have been successfully deleted. " +
                    "Stock updated successfully");
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return Map.of("message", "Database error: unable to delete ");

        }
    }
}



