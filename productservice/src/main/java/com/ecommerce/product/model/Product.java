package com.ecommerce.product.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "products")



public class Product {
    @Id
    private String id;
    private String name;
    private String brand;
    private String category;
    private String description;
    private double price;
    private int stock;
    private String seller;
    private String imageUrl;
    private boolean active; 

}
