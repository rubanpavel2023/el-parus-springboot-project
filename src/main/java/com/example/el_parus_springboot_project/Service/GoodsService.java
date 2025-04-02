package com.example.el_parus_springboot_project.Service;

import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class GoodsService {

    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }


    public Goods getGoodsByArticle(String article) {
        return goodsRepository.findByArticle(article);
    }


    public List<Goods> getAllGoods() {
        return goodsRepository.findAll();
    }

}




