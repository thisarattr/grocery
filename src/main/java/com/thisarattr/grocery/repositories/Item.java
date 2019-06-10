package com.thisarattr.grocery.repositories;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item", indexes = {
        @Index(columnList = "name", name = "idx_item_name")
})
public class Item extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "made_in")
    private String madeIn;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "categories")
    @ManyToMany(cascade = { CascadeType.DETACH })
    @JoinTable(
        name = "item_category",
        joinColumns = { @JoinColumn(name = "item_id") },
        inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    Set<Category> categories = new HashSet<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public void setMadeIn(String madeIn) {
        this.madeIn = madeIn;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
