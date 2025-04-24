package com.example.el_parus_springboot_project.Entity.Cart;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", unique = true, nullable = false)
    private String sessionId;

    @Column(nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartArticleMap> cartArticleMaps = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public List<CartArticleMap> getCartArticleMaps() {
        return cartArticleMaps;
    }

    public void setCartArticleMaps(List<CartArticleMap> cartArticleMaps) {
        this.cartArticleMaps = cartArticleMaps;
    }
}