package com.example.el_parus_springboot_project.Service.SizeQuantityService;

import com.example.el_parus_springboot_project.Entity.Goods.Goods;
import com.example.el_parus_springboot_project.Entity.Goods.SizeQuantity;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository.GoodsRepository;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository.SizeQuantityRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class SizeQuantityService {

    private final GoodsRepository goodsRepository;
    private final SizeQuantityRepository sizeQuantityRepository;

    public SizeQuantityService(GoodsRepository goodsRepository, SizeQuantityRepository sizeQuantityRepository) {
        this.goodsRepository = goodsRepository;
        this.sizeQuantityRepository = sizeQuantityRepository;
    }

    @Transactional
    public ResponseEntity<Map<String, String>> addSizes(SizeQuantityRequest request) {
        try {
            Goods goods = goodsRepository.findByArticleAndCategory(request.getArticle(), request.getCategory());
            if (goods == null) {
                return ResponseEntity.ok(Map.of("message", "Goods not in the database"));
            }
            if (containsNegative(request.getSizes())) {
                return ResponseEntity.ok(Map.of("message", "Invalid quantity for size. Must be a positive number"));
            }
            sizeQuantityRepository.deleteByGoods(goods);
            for (SizeQuantityDto sizeQuantityDto : request.getSizes()) {
                SizeQuantity newSize = new SizeQuantity();
                newSize.setGoods(goods);
                newSize.setSize(sizeQuantityDto.getSize());
                newSize.setQuantity(sizeQuantityDto.getQuantity());
                newSize.setCategory(request.getCategory());
                sizeQuantityRepository.save(newSize);
            }
            return ResponseEntity.ok(Map.of("message", "Sizes updated successfully!!"));
        } catch (DataAccessException ex) {
            System.err.println("Error while accessing database: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Database error"));
        }
    }


    public boolean containsNegative(List<SizeQuantityDto> sizesAndQuantity) {
        return sizesAndQuantity.stream().anyMatch(sq -> sq.getQuantity() < 0);
    }

    public SizeQuantity getGoodsByArticleAndSize(String article, Integer size) {
        return sizeQuantityRepository.findByArticleAndSize(article, size );
    }

    public ResponseEntity<String> saveGoods(SizeQuantity goods) {
        try {
           // validateGoods(goods);
            sizeQuantityRepository.save(goods);
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




}

