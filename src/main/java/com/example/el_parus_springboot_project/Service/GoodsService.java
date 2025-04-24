package com.example.el_parus_springboot_project.Service;

import com.example.el_parus_springboot_project.Entity.Goods.Goods;
import com.example.el_parus_springboot_project.Entity.Goods.SizeQuantity;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository.GoodsRepository;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository.SizeQuantityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    private final GoodsRepository goodsRepository;
    private final SizeQuantityRepository sizeQuantityRepository;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository, SizeQuantityRepository sizeQuantityRepository) {
        this.goodsRepository = goodsRepository;
        this.sizeQuantityRepository = sizeQuantityRepository;
    }

    //FOR ADMIN -> STOCKROOM
    public ResponseEntity<Map<String, Object>> getGoodsWithSizesByArticle(String article) {
        try {
            Goods goods = goodsRepository.findByArticle(article);
            Map<String, Object> result = new HashMap<>();
            result.put("id", goods.getId());
            result.put("article", goods.getArticle());
            result.put("name", goods.getName());
            result.put("category", goods.getCategory());
            result.put("price", goods.getPrice());
            result.put("currency", goods.getCurrency());
            List<SizeQuantity> sizes = sizeQuantityRepository.findByGoodsArticle(article);
            result.put("sizes", sizes);
            return ResponseEntity.ok(result);
        } catch (DataAccessException ex) {
            System.err.println("Error while accessing database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Database error"));
        }
    }


    public ResponseEntity<List<Map<String, Object>>> getAllGoodsWithSizes() {
        try {
            List<Goods> goodsList = goodsRepository.findAllSortedByCategory();
            List<Map<String, Object>> result = new ArrayList<>();
            for (Goods goods : goodsList) {
                Map<String, Object> goodsData = new HashMap<>();
                goodsData.put("id", goods.getId());
                goodsData.put("article", goods.getArticle());
                goodsData.put("name", goods.getName());
                goodsData.put("category", goods.getCategory());
                goodsData.put("price", goods.getPrice());
                goodsData.put("currency", goods.getCurrency());
                List<Map<String, Integer>> sizes = sizeQuantityRepository.findByGoodsId(goods.getId())
                        .stream()
                        .map(sizeQuantity -> Map.of(
                                "size", sizeQuantity.getSize(),
                                "quantity", sizeQuantity.getQuantity()
                        ))
                        .collect(Collectors.toList());
                goodsData.put("sizes", sizes);
                result.add(goodsData);
            }
            return ResponseEntity.ok(result);
        } catch (DataAccessException ex) {
            System.err.println("Error while accessing database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Map.of("message", "Database error")));
        }
    }


    public ResponseEntity<Map<String, String>> saveGoods(Goods goods) {
        try {
            validateGoods(goods);
            goodsRepository.save(goods);
            return ResponseEntity.ok(Map.of("message", "Goods added successfully!!"));
        } catch (IllegalArgumentException ex) {
            System.err.println("Validation error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Incorrect data"));
        } catch (DataAccessException ex) {
            System.err.println("Error while working with database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "failed to add goods."));
        }
    }

    public void validateGoods(Goods goods) {
        validateArticle(goods.getArticle());
        validatePrice(goods.getPrice());
    }

    private void validateArticle(String article) {
        if (article == null || article.trim().isEmpty() || !article.matches("^[A-Z0-9]{1,12}$")) {
            throw new IllegalArgumentException("Invalid article: Must be 1-12 characters, uppercase letters or numbers.");
        }
    }

    private void validatePrice(Double price) {
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Invalid price: Must be a positive number.");
        }
    }


    @DeleteMapping("/id/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deleteGoodsById(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            if (!goodsRepository.existsById(id)) {
                response.put("message", "Goods with ID " + id + " not found");
                return ResponseEntity.ok(response);
            }
            sizeQuantityRepository.deleteByGoodsId(id);
            goodsRepository.deleteById(id);
            response.put("message", "Goods with ID No " + id + " deleted successfully!");
            return ResponseEntity.ok(response);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Database error: failed to delete goods."));
        }
    }

    /*public Goods getGoodsByArticleAndSize(String article, Integer size) {
        return .findByArticleAndSize(article, size );
    }*/


}



