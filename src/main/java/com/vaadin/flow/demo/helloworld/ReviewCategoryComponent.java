package com.vaadin.flow.demo.helloworld;

import com.vaadin.flow.html.Label;
import com.vaadin.flow.router.View;
import com.vaadin.flow.starter.app.backend.Category;
import com.vaadin.flow.starter.app.backend.CategoryService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

public class ReviewCategoryComponent extends VerticalLayout implements View {
    public ReviewCategoryComponent() {
        getElement().getStyle().set("maxWidth", "500px");

        CategoryService service = CategoryService.createDemoCategoryService();
        List<Category> categories = service.findCategory("");
        for (Category category : categories) {
            // TODO get the actual review count for each category once ReviewService is fixed
            addRow(category.getCategoryName(), 42);
        }
    }

    private void addRow(String categoryName, int reviewCount) {
        HorizontalLayout layout = new HorizontalLayout();
        Label name = new Label(categoryName);
        Label counter = new Label(String.valueOf(reviewCount));
        Button editButton = new Button("Edit");
        layout.add(name, counter, editButton);
        layout.setWidth("100%");
        layout.getStyle().set("border", "1px solid #9E9E9E");
        layout.setSpacing(true);
        add(layout);
    }
}
