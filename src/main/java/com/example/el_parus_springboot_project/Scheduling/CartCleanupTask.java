package com.example.el_parus_springboot_project.Scheduling;

import com.example.el_parus_springboot_project.Entity.CartItem;
import com.example.el_parus_springboot_project.Entity.Goods;
import com.example.el_parus_springboot_project.Repositories.CartItemRepository;
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
    private CartItemRepository cartItemRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Scheduled(fixedRate = 90000)
    @Transactional
    public void cleanUpOldCartItems() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(15);
        List<CartItem> expiredItems = cartItemRepository.findExpiredCartItems(expirationTime);

        for (CartItem item : expiredItems) {
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






