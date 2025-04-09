package com.example.el_parus_springboot_project.Controllers;

import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public List<Goods> getAllGoods() {
        return goodsService.getAllGoods();
    }

    @GetMapping("/article/{article}")
    public Goods getGoodsByArticle(@PathVariable String article) {
        return goodsService.getGoodsByArticle(article);
    }


    @DeleteMapping("/id/{id}")
    public ResponseEntity<Map<String, String>> deleteGoodsById(@PathVariable Long id) {
        return goodsService.deleteGoodsById(id);

    }
}
