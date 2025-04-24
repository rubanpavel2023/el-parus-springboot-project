package com.example.el_parus_springboot_project.Repositories.GoodsRepository;

import com.example.el_parus_springboot_project.Entity.Goods.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

    List<Goods> findByCategory(String category);
    @Query("SELECT g FROM Goods g WHERE g.article = :article")
    Goods findByArticle(String article);

    @Query("SELECT g FROM Goods g ORDER BY g.category, g.id ASC")
    List<Goods> findAllSortedByCategory();

    Goods findByArticleAndCategory(String article, String category);






}
