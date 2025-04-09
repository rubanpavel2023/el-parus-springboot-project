package com.example.el_parus_springboot_project.Service;

import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class GoodsService {

    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }


    public Goods getGoodsByArticle(String article) {
        return goodsRepository.findByArticle(article);
    }


    public List<Goods> getAllGoods() {
        return goodsRepository.findAllSortedByCategory();
    }

    //add new goods

    public ResponseEntity<String> saveGoods(Goods goods) {
        try {
            validateGoods(goods);
            goodsRepository.save(goods);
            return ResponseEntity.ok("Goods added successfully!");
        } catch (IllegalArgumentException ex) {
            System.err.println("Validation error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation error: " + ex.getMessage());
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error: failed to add goods.");
        }
    }

    public void validateGoods(Goods goods) {
        validateArticle(goods.getArticle());
        validateSize(goods.getSize());
        validateQuantity(goods.getQuantity());
        validatePrice(goods.getPrice());
    }

    private void validateArticle(String article) {
        if (article == null || article.trim().isEmpty() || !article.matches("^[A-Z0-9]{1,12}$")) {
            throw new IllegalArgumentException("Invalid article: Must be 1-12 characters, uppercase letters or numbers.");
        }
    }

    private void validateSize(Integer size) {
        if (size == null || size < 1 || size > 55) {
            throw new IllegalArgumentException("Invalid size: Must be an integer between 1 and 55.");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Invalid quantity: Must be a positive integer.");
        }
    }

    private void validatePrice(Double price) {
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Invalid price: Must be a positive number.");
        }
    }

}



