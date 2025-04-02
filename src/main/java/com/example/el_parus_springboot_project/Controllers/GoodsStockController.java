package com.example.el_parus_springboot_project.Controllers;

import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}

