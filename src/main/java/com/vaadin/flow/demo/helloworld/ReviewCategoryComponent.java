package com.vaadin.flow.demo.helloworld;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.flow.html.H2;
import com.vaadin.flow.html.Label;
import com.vaadin.flow.router.View;
import com.vaadin.flow.starter.app.backend.Category;
import com.vaadin.flow.starter.app.backend.CategoryService;
import com.vaadin.flow.starter.app.backend.Review;
import com.vaadin.flow.starter.app.backend.ReviewService;
import com.vaadin.generated.paper.dialog.GeneratedPaperDialog;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;

public final class ReviewCategoryComponent extends VerticalLayout implements View {

    private final transient CategoryService categoryService = CategoryService.getInstance();
    private final transient ReviewService reviewService = ReviewService.getInstance();

    private final TextField filter = new TextField("", "Search");
    private final VerticalLayout categoryLayout = new VerticalLayout();

    private final GeneratedPaperDialog dialog = new GeneratedPaperDialog();
    private final H2 titleField = new H2();
    private final TextField newCategoryNameInput = new TextField("Category Name");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private Registration registration;
    private Binder<Category> binder;
    private Category currentCategory;

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
        initDialog();

        add(dialog, notification);
    }

    private void initDialog() {
        saveButton.getElement().setAttribute("dialog-confirm", true);
        saveButton.getElement().setAttribute("autofocus", true);
        cancelButton.getElement().setAttribute("dialog-dismiss", true);
        HorizontalLayout buttonBar = new HorizontalLayout(saveButton, cancelButton);
        buttonBar.setClassName("buttons");
        VerticalLayout layout = new VerticalLayout(titleField, newCategoryNameInput, buttonBar);
        dialog.add(layout);
        dialog.setModal(true);

        binder = new Binder<>(Category.class);
        binder.forField(newCategoryNameInput)
                .withConverter(String::trim, String::trim)
                .withValidator(name -> name.length() >= 3,
                        "Category name must contain at least 3 printable characters")
                .withValidator(name -> categoryService.findCategories(name).size() == 0,
                        "Category name must be unique")
                .bind(Category::getCategoryName, Category::setCategoryName);
    }

    private void updateView() {
        categoryLayout.removeAll();
        List<Category> categories = categoryService.findCategories(filter.getValue());
        for (Category category : categories) {
            List<Review> reviewsInCategory = reviewService.findReviews(category.getCategoryName());
            int reviewCount = reviewsInCategory.stream()
                    .mapToInt(Review::getTestTimes)
                    .sum();
            addRow(category, reviewCount);
        }
    }

    private void addSearchBar() {
        HorizontalLayout layout = new HorizontalLayout();
        filter.addValueChangeListener(e -> updateView());
        Button newButton = new Button("+ New Category");
        newButton.addClickListener(
                e -> displayDialog(new Category(), "Add"));
        layout.add(filter, newButton);

        layout.setWidth("100%");
        layout.setSpacing(true);
        add(layout);
    }

    private void addRow(Category category, int reviewCount) {
        HorizontalLayout layout = new HorizontalLayout();
        Label name = new Label(category.getCategoryName());
        Label counter = new Label(String.valueOf(reviewCount));
        Button editButton = new Button("Edit");
        editButton.addClickListener(
                e -> displayDialog(category, "Edit"));
        layout.add(name, counter, editButton);
        layout.setWidth("100%");
        layout.getStyle().set("border", "1px solid #9E9E9E");
        layout.setSpacing(true);
        categoryLayout.add(layout);
    }

    private void displayDialog(Category category, String operationName) {
        currentCategory = category;
        titleField.setText(operationName + " New Category");
        if (registration != null) {
            registration.remove();
        }
        registration = saveButton.addClickListener(e -> handleSave(operationName));
        binder.readBean(currentCategory);
        dialog.open();
    }

    private void handleSave(String operationName) {
        try {
            binder.writeBean(currentCategory);
            categoryService.saveCategory(currentCategory);

            notification.show("Category successfully " + operationName.toLowerCase() + "ed");
            updateView();
        } catch (ValidationException ex) {
            notification.show(ex.getValidationErrors().stream()
                    .map(ValidationResult::getErrorMessage)
                    .collect(Collectors.joining("; ")));
        }
    }
}
