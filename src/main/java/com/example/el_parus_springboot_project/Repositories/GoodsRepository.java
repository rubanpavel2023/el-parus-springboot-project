package com.example.el_parus_springboot_project.Repositories;

import com.example.el_parus_springboot_project.Entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

    List<Goods> findByCategory(String category);
    Goods findByArticle(String article);

    @Query("SELECT g FROM Goods g ORDER BY g.category, g.id ASC")
    List<Goods> findAllSortedByCategory();



}
