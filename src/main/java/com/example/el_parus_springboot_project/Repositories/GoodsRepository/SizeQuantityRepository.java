package com.example.el_parus_springboot_project.Repositories.GoodsRepository;

import com.example.el_parus_springboot_project.Entity.Goods.Goods;
import com.example.el_parus_springboot_project.Entity.Goods.SizeQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface SizeQuantityRepository extends JpaRepository<SizeQuantity, Long> {
    List<SizeQuantity> findByGoodsArticleAndGoodsCategory(String article, String category);


    @Modifying
    @Transactional
    @Query("DELETE FROM SizeQuantity sq WHERE sq.goods.id = :goodsId")
    void deleteByGoodsId(@Param("goodsId") Long goodsId);

    List<SizeQuantity> findByGoodsArticle(String article);


    @Modifying
    @Query("DELETE FROM SizeQuantity sq WHERE sq.goods = :goods")
    void deleteByGoods(@Param("goods") Goods goods);


    List<SizeQuantity> findByGoodsId(Long goodsId);

    @Query("SELECT sq FROM SizeQuantity sq WHERE sq.goods = :goods AND sq.size = :size")
    SizeQuantity findByGoodsAndSize(Goods goods, Integer size);

    @Query("SELECT sq FROM SizeQuantity sq WHERE sq.goods.article = :article AND sq.size = :size")
    SizeQuantity findByArticleAndSize(@Param("article") String article, @Param("size") Integer size);


}




