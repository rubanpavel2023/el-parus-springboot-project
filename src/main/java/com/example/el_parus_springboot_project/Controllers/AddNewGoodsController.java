package com.example.el_parus_springboot_project.Controllers;

import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/goods")
public class AddNewGoodsController {

    private final GoodsService goodsService;

    @Autowired
    public AddNewGoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("/addNewGoodsPage")
    public String showAddGoodsPage() {
        return "Admin/addNewGoodsPage";
    }

    @ResponseBody
    @PostMapping("/addNewGoods")
    public ResponseEntity<String> addNewGoods(@RequestBody Goods goods) {
        return goodsService.saveGoods(goods);
    }
}






