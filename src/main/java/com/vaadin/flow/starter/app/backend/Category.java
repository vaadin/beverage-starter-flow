package com.vaadin.flow.starter.app.backend;

import java.io.Serializable;

public class Category implements Serializable {

    private Long categoryId = null;

    private String categoryName = "";

    public Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Get the value of categoryName
     *
     * @return the value of categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Set the value categoryName
     *
     * @param categoryName
     *            new value of categoryName
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Category{" + categoryName + '}';
    }
}
