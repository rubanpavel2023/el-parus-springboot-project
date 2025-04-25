package com.example.el_parus_springboot_project.Controllers.GoodsController;

import com.example.el_parus_springboot_project.Service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsStockController {

    private final GoodsService goodsService;

    @Autowired
    public GoodsStockController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllGoodsWithSizes() {
        return goodsService.getAllGoodsWithSizes();
    }

    @GetMapping("/article/{article}")
    public ResponseEntity<Map<String, Object>> getGoodsByArticle(@PathVariable String article) {
        return goodsService.getGoodsWithSizesByArticle(article);
    }


    @DeleteMapping("/id/{id}")
    public ResponseEntity<Map<String, String>> deleteGoodsById(@PathVariable Long id) {
        return goodsService.deleteGoodsById(id);

    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Map<String, String>> deleteAllCartsBuyers() {
        return goodsService.deleteAllGoods();
    }

}
