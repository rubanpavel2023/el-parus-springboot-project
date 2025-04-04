package com.example.el_parus_springboot_project.Repositories;

import com.example.el_parus_springboot_project.Entity.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findBySessionId(String sessionId);

    List<Cart> findByArticle(String article);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.sessionId = :sessionId")
    void deleteBySessionId(@Param("sessionId") String sessionId);

    Cart findByArticleAndSessionId(String article, String sessionId);

    @Query("SELECT c FROM Cart c WHERE c.createdTime < :expirationTime")
    List<Cart> findExpiredCartItems(@Param("expirationTime") LocalDateTime expirationTime);

    //for admin
    @Query(value = "SELECT name, size, article, quantity, price_per_unit, created_time, " +
            "(quantity * price_per_unit) AS totalPricePosition " + "FROM cart " + "ORDER BY created_time",
            nativeQuery = true)
    List<Map<String, Object>> getCarts();

    @Query(value = "SELECT session_id, SUM(quantity * price_per_unit) AS sessionTotal " +
            "FROM cart " + "GROUP BY session_id ORDER BY session_id",
            nativeQuery = true)
    List<Map<String, Object>> getSessionTotals();


    @Query(value = "SELECT SUM(quantity * price_per_unit) AS totalAmount FROM cart",
            nativeQuery = true)
    Double getTotalAmount();


}


