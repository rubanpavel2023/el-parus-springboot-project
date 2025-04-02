package com.example.el_parus_springboot_project.Controllers.AssortmentControllers;

import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GoodsControllerGucci {

    @Autowired
    private GoodsRepository goodsRepository;

    @GetMapping("/mens-shoes-gucci")
    public String showMensShoes(Model model) {
        List<Goods> goods = goodsRepository.findByCategory("GUCCI Men's Shoes");
        model.addAttribute("goods", goods);
        return "gucciMensShoes";
    }

    @GetMapping("/womens-shoes-gucci")
    public String showWomensShoes(Model model) {
        List<Goods> goods = goodsRepository.findByCategory("GUCCI Women's Shoes");
        model.addAttribute("goods", goods);
        return "gucciWomensShoes";
    }
}