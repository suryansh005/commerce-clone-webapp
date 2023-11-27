package com.commerce.webapp.commerceclonewebapp.model.entity;

import javax.persistence.*;
import java.util.*;
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "parent" ,cascade = CascadeType.ALL,orphanRemoval = true ,fetch = FetchType.EAGER)
    private Set<Category>children = new HashSet<>();;

    @ManyToOne
    private Category parent;




    // Constructors, getters, and setters

    public Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    // ... other methods

    public void addProduct(Product product) {
        products.add(product);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

//    @PreRemove
//    public void nullifyChildForeignKey() {
//        if (children != null) {
//            for (Category childEntity : children) {
//                childEntity.setParent(null);
//            }
//        }
//    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void addChildren(Category child) {
        children.add(child);
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }
}
