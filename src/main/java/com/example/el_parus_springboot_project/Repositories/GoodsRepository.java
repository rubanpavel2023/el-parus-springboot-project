package com.example.el_parus_springboot_project.Repositories;

import com.example.el_parus_springboot_project.Entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findByCategory(String category);
    Goods findByArticle(String article);
    Goods findByNameAndArticle(String brand, String article);


}
