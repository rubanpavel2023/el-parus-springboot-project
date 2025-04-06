package com.example.el_parus_springboot_project.Scheduling;

import com.example.el_parus_springboot_project.Entity.Cart;
import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Repositories.CartRepository;
import com.example.el_parus_springboot_project.Repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CartCleanupTask {

    @Autowired
    private CartRepository cartItemRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanUpOldCartItems() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(1);
        List<Cart> expiredItems = cartItemRepository.findExpiredCartItems(expirationTime);

        for (Cart item : expiredItems) {
            try {
                Goods goods = goodsRepository.findByArticle(item.getArticle());
                if (goods != null) {

                    goods.setQuantity(goods.getQuantity() + item.getQuantity());
                    goodsRepository.save(goods);
                    cartItemRepository.delete(item);

                }
            } catch (Exception e) {
                System.err.println("Error while processing goods: " + item.getArticle() + ", " + e.getMessage());
            }
        }
    }
}





