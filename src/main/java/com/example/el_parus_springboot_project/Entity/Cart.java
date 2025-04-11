package com.example.el_parus_springboot_project.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int size;
    private String article;
    private int quantity;

    @Column(name = "price_per_unit")
    private double pricePerUnit;

    @Column(name = "session_Id")
    private String sessionId;

    @Column(nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    @ElementCollection
    @CollectionTable(name = "cart_articles_map", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "article")
    @Column(name = "quantity")
    private Map<String, Integer> articlesMap = new HashMap<>();;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
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

    public Map<String, Integer> getArticlesMap() {
        return articlesMap;
    }

    public void setArticlesMap(Map<String, Integer> articlesMap) {
        this.articlesMap = articlesMap;
    }


}