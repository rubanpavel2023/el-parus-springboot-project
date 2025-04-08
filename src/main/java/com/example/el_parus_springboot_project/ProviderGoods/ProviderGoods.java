/*package com.example.el_parus_springboot_project.ProviderGoods;

import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProviderGoods {

    private final List<Goods> goods = new ArrayList<>();


    @Autowired
    private GoodsRepository goodsRepository;

    @PostConstruct
    public void seedGoods() {
        goods.add(new Goods("Gucci", "GUC2371SHO", "GUCCI Men's Shoes", 42, 90, 200.0, "USD"));
        goods.add(new Goods("Gucci", "GUC2372SHO", "GUCCI Men's Shoes", 43, 80, 210.0, "USD"));
        goods.add(new Goods("Gucci", "GUC2373SHO", "GUCCI Men's Shoes", 44, 60, 220.0, "USD"));
        goods.add(new Goods("Gucci", "GUC2374SHO", "GUCCI Men's Shoes", 45, 50, 230.0, "USD"));

        goods.add(new Goods("Gucci", "GUC3371SHO", "GUCCI Women's Shoes", 36, 90, 240.0, "USD"));
        goods.add(new Goods("Gucci", "GUC3372SHO", "GUCCI Women's Shoes", 37, 70, 190.0, "USD"));
        goods.add(new Goods("Gucci", "GUC3373SHO", "GUCCI Women's Shoes", 38, 50, 300.0, "USD"));
        goods.add(new Goods("Gucci", "GUC3374SHO", "GUCCI Women's Shoes", 39, 60, 220.0, "USD"));

        goods.add(new Goods("CHANEL", "CHA4371SHO", "CHANEL Men's Shoes", 42, 90, 200.0, "USD"));
        goods.add(new Goods("CHANEL", "CHA4372SHO", "CHANEL Men's Shoes", 43, 80, 210.0, "USD"));
        goods.add(new Goods("CHANEL", "CHA4373SHO", "CHANEL Men's Shoes", 44, 60, 220.0, "USD"));
        goods.add(new Goods("CHANEL", "CHA4374SHO", "CHANEL Men's Shoes", 45, 50, 230.0, "USD"));

        goods.add(new Goods("CHANEL", "CHA5371SHO", "CHANEL Women's Shoes", 36, 90, 240.0, "USD"));
        goods.add(new Goods("CHANEL", "CHA5372SHO", "CHANEL Women's Shoes", 37, 70, 190.0, "USD"));
        goods.add(new Goods("CHANEL", "CHA5373SHO", "CHANEL Women's Shoes", 38, 50, 300.0, "USD"));
        goods.add(new Goods("CHANEL", "CHA5374SHO", "CHANEL Women's Shoes", 39, 60, 220.0, "USD"));

        goods.add(new Goods("M.KORS", "MKO6371SHO", "M.KORS Men's Shoes", 42, 90, 200.0, "USD"));
        goods.add(new Goods("M.KORS", "MKO6372SHO", "M.KORS Men's Shoes", 43, 80, 210.0, "USD"));
        goods.add(new Goods("M.KORS", "MKO6373SHO", "M.KORS Men's Shoes", 44, 60, 220.0, "USD"));
        goods.add(new Goods("M.KORS", "MKO6374SHO", "M.KORS Men's Shoes", 45, 50, 230.0, "USD"));

        goods.add(new Goods("M.KORS", "MKO7371SHO", "M.KORS Women's Shoes", 36, 90, 240.0, "USD"));
        goods.add(new Goods("M.KORS", "MKO7372SHO", "M.KORS Women's Shoes", 37, 70, 190.0, "USD"));
        goods.add(new Goods("M.KORS", "MKO7373SHO", "M.KORS Women's Shoes", 38, 50, 300.0, "USD"));
        goods.add(new Goods("M.KORS", "MKO7374SHO", "M.KORS Women's Shoes", 39, 60, 220.0, "USD"));

        goods.add(new Goods("BURBERRY", "BRB8371SHO", "BURBERRY Men's Shoes", 42, 90, 220.0, "USD"));
        goods.add(new Goods("BURBERRY", "BRB8372SHO", "BURBERRY Men's Shoes", 43, 80, 270.0, "USD"));
        goods.add(new Goods("BURBERRY", "BRB8373SHO", "BURBERRY Men's Shoes", 44, 60, 140.0, "USD"));
        goods.add(new Goods("BURBERRY", "BRB8374SHO", "BURBERRY Men's Shoes", 45, 50, 180.0, "USD"));

        goods.add(new Goods("BURBERRY", "BRB9371SHO", "BURBERRY Women's Shoes", 36, 90, 200.0, "USD"));
        goods.add(new Goods("BURBERRY", "BRB9372SHO", "BURBERRY Women's Shoes", 37, 70, 150.0, "USD"));
        goods.add(new Goods("BURBERRY", "BRB9373SHO", "BURBERRY Women's Shoes", 38, 50, 120.0, "USD"));
        goods.add(new Goods("BURBERRY", "BRB9374SHO", "BURBERRY Women's Shoes", 39, 60, 230.0, "USD"));


        goods.forEach(this::updateOrInsert);
    }


    private void updateOrInsert(Goods newGoods) {
        Goods existingGoods = goodsRepository.findByNameAndArticle(newGoods.getName(), newGoods.getArticle());
        if (existingGoods != null) {
            existingGoods.setName(newGoods.getName());
            existingGoods.setCategory(newGoods.getCategory());
            existingGoods.setSize(newGoods.getSize());
            existingGoods.setQuantity(newGoods.getQuantity());
            existingGoods.setPrice(newGoods.getPrice());
            existingGoods.setCurrency(newGoods.getCurrency());
            goodsRepository.save(existingGoods);
        } else {
            goodsRepository.save(newGoods);
        }
    }

}*/


