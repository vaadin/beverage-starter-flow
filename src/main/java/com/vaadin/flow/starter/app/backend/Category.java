package com.vaadin.flow.starter.app.backend;

import java.io.Serializable;

public class Category implements Serializable, Cloneable {

    private Long categoryId;

    private String categoryName = "";

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
    public Category clone() throws CloneNotSupportedException {
        return (Category) super.clone();
    }
}
