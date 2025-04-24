package com.example.el_parus_springboot_project.Controllers.AssortmentControllers;

import com.example.el_parus_springboot_project.Entity.Goods.Goods;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GoodsControllerChanel {

    @Autowired
    private GoodsRepository goodsRepository;

    @GetMapping("/mens-shoes-chanel")
    public String showMensShoes(Model model) {
        List<Goods> goods = goodsRepository.findByCategory("CHANEL Men's Shoes");
        model.addAttribute("goods", goods);
        return "chanelMensShoes";
    }

    @GetMapping("/womens-shoes-chanel")
    public String showWomensShoes(Model model) {
        List<Goods> goods = goodsRepository.findByCategory("CHANEL Women's Shoes");
        model.addAttribute("goods", goods);
        return "chanelWomensShoes";
    }
}
