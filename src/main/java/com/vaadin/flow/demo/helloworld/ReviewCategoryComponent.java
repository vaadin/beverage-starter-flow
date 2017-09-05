package com.vaadin.flow.demo.helloworld;

import java.util.List;

import com.vaadin.flow.html.H2;
import com.vaadin.flow.html.Label;
import com.vaadin.flow.router.View;
import com.vaadin.flow.starter.app.backend.Category;
import com.vaadin.flow.starter.app.backend.CategoryService;
import com.vaadin.flow.starter.app.backend.Review;
import com.vaadin.flow.starter.app.backend.ReviewService;
import com.vaadin.generated.paper.dialog.GeneratedPaperDialog;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public final class ReviewCategoryComponent extends VerticalLayout implements View {

    private final transient CategoryService categoryService = CategoryService.getInstance();
    private final transient ReviewService reviewService = ReviewService.getInstance();
    private TextField filter;
    private VerticalLayout categoryLayout = new VerticalLayout();
    private TextField newCategoryNameInput;
    private final GeneratedPaperDialog dialog = new GeneratedPaperDialog();
    private final PaperToast notification = new PaperToast();

    public ReviewCategoryComponent() {
        initView();

        addSearchBar();
        add(categoryLayout);

        updateView();
    }

    private void initView() {
        getElement().getStyle().set("maxWidth", "500px");
        notification.setBackgroundColor("blue");
        add(dialog, notification);
    }

    private void updateView() {
        categoryLayout.removeAll();
        List<Category> categories = categoryService.findCategory(filter.getValue());
        for (Category category : categories) {
            List<Review> reviewsInCategory = reviewService.findReview(category.getCategoryName());
            int reviewCount = reviewsInCategory.stream()
                    .mapToInt(Review::getTestTimes)
                    .sum();
            addRow(category.getCategoryName(), reviewCount);
        }
    }

    private void addSearchBar() {
        HorizontalLayout layout = new HorizontalLayout();
        filter = new TextField("", "Search");
        filter.addValueChangeListener(e -> updateView());
        Button newButton = new Button("+ New Category");
        newButton.addClickListener(e -> displayNewCategoryDialog());
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
        categoryLayout.add(layout);
    }

    private void displayNewCategoryDialog() {
        initDialog("Add New Category", "Category Name");
        dialog.open();
    }

    private void initDialog(String title, String text) {
        dialog.removeAll();
        newCategoryNameInput = new TextField(text);
        Button saveButton = new Button("Save");
        saveButton.getElement().setAttribute("dialog-confirm", true);
        saveButton.getElement().setAttribute("autofocus", true);
        saveButton.addClickListener(e -> handleNewCategoryRequest());
        Button cancelButton = new Button("Cancel");
        cancelButton.getElement().setAttribute("dialog-dismiss", true);
        HorizontalLayout buttonBar = new HorizontalLayout(saveButton, cancelButton);
        buttonBar.setClassName("buttons");
        VerticalLayout layout = new VerticalLayout(new H2(title), newCategoryNameInput, buttonBar);
        dialog.add(layout);
        dialog.setModal(true);
    }

    private void handleNewCategoryRequest() {
        String newCategoryName = newCategoryNameInput.getValue().trim();

        if (newCategoryName.isEmpty()) {
            notification.show("Category name must not be empty");
        } else if (categoryService.findCategory("").stream()
                .anyMatch(c -> c.getCategoryName().equalsIgnoreCase(newCategoryName))) {
            notification.show("Category already exists");
        } else {
            Category newCategory = new Category();
            newCategory.setCategoryName(newCategoryName);
            categoryService.saveCategory(newCategory);

            notification.show("Category successfully added");
            updateView();
        }
    }
}
