package com.example.el_parus_springboot_project.Controllers.GoodsController;

import com.example.el_parus_springboot_project.Entity.Goods.Goods;
import com.example.el_parus_springboot_project.Service.GoodsService;
import com.example.el_parus_springboot_project.Service.SizeQuantityService.SizeQuantityRequest;
import com.example.el_parus_springboot_project.Service.SizeQuantityService.SizeQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Controller
@RequestMapping("/newGoods")
public class AddNewGoodsController {

    private final GoodsService goodsService;
    private final SizeQuantityService sizeQuantityService;

    @Autowired
    public AddNewGoodsController(GoodsService goodsService, SizeQuantityService sizeQuantityService) {
        this.goodsService = goodsService;
        this.sizeQuantityService = sizeQuantityService;
    }

    @GetMapping("/addNewGoodsPage")
    public String showAddGoodsPage() {
        return "Admin/addNewGoodsPage";
    }

    @ResponseBody
    @PostMapping("/addNewGoods")
    public ResponseEntity<Map<String, String>> addNewGoods(@RequestBody Goods goods) {
        return goodsService.saveGoods(goods);
    }

    @ResponseBody
    @PostMapping("/addSizes")
    public ResponseEntity<Map<String, String>> addSizes(@RequestBody SizeQuantityRequest request) {
        return sizeQuantityService.addSizes(request);

    }
}






