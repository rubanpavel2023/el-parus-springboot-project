package com.example.el_parus_springboot_project.Repositories;

import com.example.el_parus_springboot_project.Entity.Cart.Cart;
import com.example.el_parus_springboot_project.Entity.Cart.CartArticleMap;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {


    @Query(value = "SELECT c.id AS cart_id, " +
            "c.created_time, " +
            "cam.name, " +
            "cam.size, " +
            "cam.article, " +
            "cam.quantity, " +
            "cam.price_per_unit, " +
            "(cam.quantity * cam.price_per_unit) AS total_price_position " +
            "FROM carts c " +
            "JOIN cart_articles_map cam ON cam.cart_id = c.id",
            nativeQuery = true)
    List<Map<String, Object>> getCartsForAdmin();


    @Query(value = "SELECT c.session_id, SUM(cam.quantity * cam.price_per_unit) AS sessionTotal " +
            "FROM cart_articles_map cam " +
            "JOIN carts c ON cam.cart_id = c.id " +
            "GROUP BY c.session_id ORDER BY c.session_id",
            nativeQuery = true)
    List<Map<String, Object>> getSessionTotals();


    @Query(value = "SELECT SUM(cam.quantity * cam.price_per_unit) FROM cart_articles_map cam",
            nativeQuery = true)
    Double getTotalAmount();


    @Query("SELECT c FROM Cart c WHERE c.sessionId = :sessionId")
    Cart findBySessionId(@Param("sessionId") String sessionId);


    @Query("SELECT c.cartArticleMaps FROM Cart c WHERE c.sessionId = :sessionId")
    List<CartArticleMap> findCartItemsBySessionId(@Param("sessionId") String sessionId);

    @Query("SELECT c FROM Cart  c WHERE c.createdTime < :expirationTime")
    List<Cart> findExpiredCartItems(@Param("expirationTime") LocalDateTime expirationTime);



}










  /*  @Query("SELECT c FROM Cart c JOIN FETCH c.cartArticleMaps WHERE c.sessionId = :sessionId")
    Cart findBySessionIdWithItems(@Param("sessionId") String sessionId);*/



