package com.vaadin.flow.demo.freestarter.ui;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.ValidationResult;
import com.vaadin.flow.html.H2;
import com.vaadin.flow.html.Label;
import com.vaadin.flow.router.View;
import com.vaadin.flow.demo.freestarter.backend.Category;
import com.vaadin.flow.demo.freestarter.backend.CategoryService;
import com.vaadin.flow.demo.freestarter.backend.Review;
import com.vaadin.flow.demo.freestarter.backend.ReviewService;
import com.vaadin.generated.paper.dialog.GeneratedPaperDialog;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public final class CategoriesList extends VerticalLayout implements View {

    private enum Operation {
        ADD("Add", true),
        EDIT("Edit", false);

        private final String name;
        private final boolean deleteDisabled;

        Operation(String name, boolean deleteDisabled) {
            this.name = name;
            this.deleteDisabled = deleteDisabled;
        }

        public String getName() {
            return name;
        }

        public boolean isDeleteDisabled() {
            return deleteDisabled;
        }
    }

    private final transient CategoryService categoryService = CategoryService.getInstance();
    private final transient ReviewService reviewService = ReviewService.getInstance();

    private final TextField filter = new TextField("", "Search");
    private final VerticalLayout categoryLayout = new VerticalLayout();

    private final GeneratedPaperDialog changesDialog = new GeneratedPaperDialog();
    private final H2 titleField = new H2();
    private final TextField newCategoryNameInput = new TextField("Category Name");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button deleteButton = new Button("Delete");
    private Registration registration;
    private Binder<Category> binder;
    private Category currentCategory;

    private final ConfirmationDialog confirmationDialog = new ConfirmationDialog();

    private final PaperToast notification = new PaperToast();

    public CategoriesList() {
        initView();

        addSearchBar();
        add(categoryLayout);

        updateView();
    }

    private void initView() {
        getElement().getStyle().set("maxWidth", "500px");
        notification.setBackgroundColor("blue");
        initDialog();

        add(changesDialog, notification);
    }

    private void initDialog() {
        saveButton.getElement().setAttribute("dialog-confirm", true);
        saveButton.getElement().setAttribute("autofocus", true);
        cancelButton.getElement().setAttribute("dialog-dismiss", true);
        deleteButton.addClickListener(e -> handleDelete());
        HorizontalLayout buttonBar = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        buttonBar.setClassName("buttons");
        VerticalLayout layout = new VerticalLayout(titleField, newCategoryNameInput, buttonBar);
        changesDialog.add(layout, confirmationDialog);
        changesDialog.setModal(true);

        binder = new Binder<>();
        binder.forField(newCategoryNameInput)
                .withConverter(String::trim, String::trim)
                .withValidator(name -> name.length() >= 3,
                        "Category name must contain at least 3 printable characters")
                .withValidator(
                        name -> categoryService.findCategories(name).size() == 0,
                        "Category name must be unique")
                .bind(Category::getName, Category::setName);
    }

    private void updateView() {
        categoryLayout.removeAll();
        List<Category> categories = categoryService.findCategories(filter.getValue());
        for (Category category : categories) {
            List<Review> reviewsInCategory = reviewService.findReviews(category.getName());
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
                e -> displayDialog(new Category(), Operation.ADD));
        layout.add(filter, newButton);

        layout.setWidth("100%");
        layout.setSpacing(true);
        add(layout);
    }

    private void addRow(Category category, int reviewCount) {
        HorizontalLayout layout = new HorizontalLayout();
        Label name = new Label(category.getName());
        Label counter = new Label(String.valueOf(reviewCount));
        Button editButton = new Button("Edit");
        editButton.addClickListener(
                e -> displayDialog(category, Operation.EDIT));
        layout.add(name, counter, editButton);
        layout.setWidth("100%");
        layout.getStyle().set("border", "1px solid #9E9E9E");
        layout.setSpacing(true);
        categoryLayout.add(layout);
    }

    private void displayDialog(Category category, Operation operation) {
        currentCategory = category;
        titleField.setText(operation.getName() + " New Category");
        if (registration != null) {
            registration.remove();
        }
        registration = saveButton.addClickListener(e -> handleSave(operation));
        deleteButton.setDisabled(operation.isDeleteDisabled());
        binder.readBean(currentCategory);
        changesDialog.open();
    }

    private void handleSave(Operation operation) {
        boolean isValid = binder.writeBeanIfValid(currentCategory);

        if (isValid) {
            categoryService.saveCategory(currentCategory);

            notification.show("Category successfully " + operation.getName().toLowerCase() + "ed");
            updateView();
        } else {
            BinderValidationStatus<Category> status = binder.validate();
            notification.show(status.getValidationErrors().stream()
                    .map(ValidationResult::getErrorMessage)
                    .collect(Collectors.joining("; ")));
        }
    }

    private void handleDelete() {
        int reviewCount = reviewService.findReviews(
                currentCategory.getName()).size();
        String text2 = reviewCount == 0 ? "" :
                "Deleting the category will mark the associated reviews as \"undefined\". "
                        + "You may link the reviews to other categories on the edit page.";
        confirmationDialog.open("Delete Category \"" + currentCategory.getName() + "\"?",
                "There are " + reviewCount + " reviews associated with this category.",
                text2, "Delete", currentCategory, this::deleteCategory);
    }

    private void deleteCategory(Category category) {
        List<Review> reviewsInCategory = reviewService.findReviews(category.getName());

        reviewsInCategory.forEach(review -> {
            review.setCategory(null);
            reviewService.saveReview(review);
        });
        categoryService.deleteCategory(category);

        changesDialog.close();
        notification.show("Category successfully deleted");
        updateView();
    }
}
