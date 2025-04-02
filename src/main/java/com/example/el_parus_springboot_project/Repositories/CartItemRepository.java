package com.example.el_parus_springboot_project.Repositories;

import com.example.el_parus_springboot_project.Entity.CartItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findBySessionId(String sessionId);


    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.sessionId = :sessionId")
    void deleteBySessionId(@Param("sessionId") String sessionId);

    CartItem findByArticleAndSessionId(String article, String sessionId);

    List<CartItem> findByArticle(String article);

    @Query("SELECT c FROM CartItem c WHERE c.createdTime < :expirationTime")
    List<CartItem> findExpiredCartItems(@Param("expirationTime") LocalDateTime expirationTime);

//for admin
    @Query(value = "SELECT name, size, article, quantity, price_per_unit, created_time, " +
        "(quantity * price_per_unit) AS totalPricePosition " + "FROM cart_items " + "ORDER BY created_time",
        nativeQuery = true)
    List<Map<String, Object>> getCartItems();

    @Query(value = "SELECT session_id, SUM(quantity * price_per_unit) AS sessionTotal " +
            "FROM cart_items " + "GROUP BY session_id ORDER BY session_id",
            nativeQuery = true)
    List<Map<String, Object>> getSessionTotals();


    @Query(value = "SELECT SUM(quantity * price_per_unit) AS totalAmount FROM cart_items",
            nativeQuery = true)
    Double getTotalAmount();






}


