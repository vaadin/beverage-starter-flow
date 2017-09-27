package com.vaadin.starter.beveragebuddy.ui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.ui.textfield.TextField;

/**
 * A dialog for editing {@link Category} objects.
 */
public class CategoryEditorDialog extends AbstractEditorDialog<Category> {

    private final TextField categoryNameField = new TextField("Category Name");

    public CategoryEditorDialog(BiConsumer<Category, Operation> itemSaver,
            Consumer<Category> itemDeleter) {
        super("Category", itemSaver, itemDeleter);

        addNameField();
    }

    private void addNameField() {
        getFormLayout().add(categoryNameField);

        getBinder().forField(categoryNameField)
                .withConverter(String::trim, String::trim)
                .withValidator(name -> name.length() >= 3,
                        "Category name must contain at least 3 printable characters")
                .withValidator(
                        name -> CategoryService.getInstance().findCategories(name).size() == 0,
                        "Category name must be unique")
                .bind(Category::getName, Category::setName);
    }

    @Override
    protected void confirmDelete() {
        int reviewCount = ReviewService.getInstance().findReviews(
                getCurrentItem().getName()).size();
        String additionalMessage = reviewCount == 0 ? "" :
                "Deleting the category will mark the associated reviews as \"undefined\". "
                        + "You may link the reviews to other categories on the edit page.";
        openConfirmationDialog("Delete Category \"" + getCurrentItem().getName() + "\"?",
                "There are " + reviewCount + " reviews associated with this category.",
                additionalMessage);
    }
}
