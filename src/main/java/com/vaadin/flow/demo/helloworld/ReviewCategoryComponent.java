package com.vaadin.flow.demo.helloworld;

import java.util.List;

import com.vaadin.flow.html.Label;
import com.vaadin.flow.router.View;
import com.vaadin.flow.starter.app.backend.Category;
import com.vaadin.flow.starter.app.backend.CategoryService;
import com.vaadin.flow.starter.app.backend.Review;
import com.vaadin.flow.starter.app.backend.ReviewService;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ReviewCategoryComponent extends VerticalLayout implements View {

    private final CategoryService categoryService = CategoryService.getInstance();
    private final ReviewService reviewService = ReviewService.getInstance();
    private TextField filter;
    private VerticalLayout categories = new VerticalLayout();

    public ReviewCategoryComponent() {
        getElement().getStyle().set("maxWidth", "500px");

        addSearchBar();
        add(categories);
        updateView();
    }

    private void updateView() {
        categories.removeAll();
        List<Category> categories = categoryService.findCategory(filter.getValue());
        for (Category category : categories) {
            List<Review> reviewsInCategory = reviewService.findReview(category.getCategoryName());
            int reviewCount = reviewsInCategory.stream()
                    .map(Review::getTestTimes)
                    .reduce(0, (i, j) -> i + j);
            addRow(category.getCategoryName(), reviewCount);
        }
    }

    private void addSearchBar() {
        HorizontalLayout layout = new HorizontalLayout();
        filter = new TextField("", "Search");
        filter.addValueChangeListener(e -> updateView());
        Button newButton = new Button("+ New Category");
        layout.add(filter, newButton);

        layout.setWidth("100%");
        layout.setSpacing(true);
        add(layout);
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
        categories.add(layout);
    }
}
