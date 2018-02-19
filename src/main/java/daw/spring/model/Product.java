package daw.spring.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    private long id;
    private String description;
    private double cost;
    private ProductType type;
    private String img;

    public Product() {
    }

    public Product(long id, String description, double cost, ProductType type, String img) {
        this.id = id;
        this.description = description;
        this.cost = cost;
        this.type = type;
        this.img = img;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public enum ProductType {
        BLIND, LIGHT
    }


}

