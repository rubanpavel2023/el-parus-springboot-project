package com.example.el_parus_springboot_project.Service.CartService;


import com.example.el_parus_springboot_project.Entity.Cart.Cart;
import com.example.el_parus_springboot_project.Entity.Cart.CartArticleMap;
import com.example.el_parus_springboot_project.Entity.Goods.SizeQuantity;
import com.example.el_parus_springboot_project.Repositories.CartRepository;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository.GoodsRepository;
import com.example.el_parus_springboot_project.Service.SizeQuantityService.SizeQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CartCleanupTask {

    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private SizeQuantityService sizeQuantityService;

    @Autowired
    private GoodsRepository goodsRepository;


    @Scheduled(fixedRate = 120000)
    @Transactional
    public void cleanUpOldCartItems() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(5);
        List<Cart> expiredItems = cartRepository.findExpiredCartItems(expirationTime);

        for (Cart cart : expiredItems) {
            try {
                for (CartArticleMap item : cart.getCartArticleMaps()) {
                    SizeQuantity goods = sizeQuantityService.getGoodsByArticleAndSize(item.getArticle(), item.getSize());

                    if (goods != null) {
                        goods.setQuantity(goods.getQuantity() + item.getQuantity());
                        sizeQuantityService.saveGoods(goods);
                    }
                }
                cartRepository.delete(cart);
            } catch (DataAccessException ex) {
                System.err.println("Error while working with database: " + ex.getMessage());
            }
        }
    }

}





