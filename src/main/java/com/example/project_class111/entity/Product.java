package com.example.project_class111.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Fetch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="product_name" , nullable = false)
    private String name;

    private String description;

    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    private String publicId;

    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id" , nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();

}
