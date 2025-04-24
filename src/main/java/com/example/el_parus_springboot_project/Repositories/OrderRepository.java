package com.example.el_parus_springboot_project.Repositories;

import com.example.el_parus_springboot_project.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerPhone(String phone);

    List<Order> findByStatus(String status);

    boolean existsByStatus(String status);

    @Transactional
    @Modifying
    @Query("DELETE FROM Order o WHERE o.status = 'completed'")
    void deleteByStatusCompleted();

    @Query("SELECT o FROM Order o WHERE o.customerPhone = :phone AND o.status = 'reserved'")
    List<Order> findByCustomerPhoneWithReservedStatus(@Param("phone") String phone);

    @Transactional
    @Modifying
    @Query("UPDATE Order SET status = :status WHERE id = :id")
    void updateOrderStatus(@Param("id") Long id, @Param("status") String status);

}




