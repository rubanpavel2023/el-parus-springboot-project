package com.example.el_parus_springboot_project.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerPhone;

    @Column(name = "items_description", length = 15000)
    private String itemsDescription;

    private double totalPrice;
    private LocalDateTime orderDate;

    @Column(name = "status", columnDefinition = "VARCHAR(50) DEFAULT 'reserved'")
    private String status = "reserved";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getItemsDescription() {
        return itemsDescription;
    }

    public void setItemsDescription(String itemsDescription) {
        this.itemsDescription = itemsDescription;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", itemsDescription='" + itemsDescription + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                '}';
    }
}



