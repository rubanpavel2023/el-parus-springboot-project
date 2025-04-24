package com.example.el_parus_springboot_project.Entity.Goods;

import jakarta.persistence.*;

@Entity
@Table(name = "size_quantity")
public class SizeQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer size;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "goods_id", nullable = false)
    private Goods goods;

    @Version
    private Integer version;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Goods getGoods() { return goods; }
    public void setGoods(Goods goods) { this.goods = goods; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
}