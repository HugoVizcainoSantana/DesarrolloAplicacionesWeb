package daw.spring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Product {

    @id
    private long id;
    private String description;
    private long cost = -1;
    private ProductType type;



    public Product() {
    }

    public Product(String description, long cost, ProductType type) {
        this.description = description;
        this.cost = cost;
        this.type = type;
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

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public enum ProductType{
        BLIND, LIGHT
    }
}

