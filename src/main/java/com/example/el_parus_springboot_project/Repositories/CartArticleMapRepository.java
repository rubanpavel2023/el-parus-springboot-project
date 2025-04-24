/*package com.example.el_parus_springboot_project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartArticleMapRepository extends JpaRepository<CartArticleMap, Long> {

    @Query("SELECT cam FROM CartArticleMap cam WHERE cam.cart = :cart")
    List<CartArticleMap> findByCart(@Param("cart") Cart cart);

    @Query("SELECT cam FROM CartArticleMap cam WHERE cam.cart.sessionId = :sessionId")
    List<CartArticleMap> findBySessionId(@Param("sessionId") String sessionId);

    @Query("SELECT cam FROM CartArticleMap cam WHERE cam.cart.sessionId = :sessionId")
    CartArticleMap findByCartSessionId(@Param("sessionId") String sessionId);

    @Query("SELECT cam FROM CartArticleMap cam WHERE cam.cart = :cart AND cam.article = :article AND cam.size = :size")
    CartArticleMap findByCartAndArticleAndSize(Cart cart, String article, Integer size);

    @Query("SELECT cam FROM CartArticleMap cam WHERE cam.article = :article AND cam.size = :size AND cam.cart.sessionId = :sessionId")
    CartArticleMap findByArticleAndSizeAndSessionId(@Param("article") String article, @Param("size") Integer size, @Param("sessionId") String sessionId);
}*/







