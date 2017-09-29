/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.starter.beveragebuddy.ui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.vaadin.data.validator.StringLengthValidator;
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

        categoryNameField.focus();
    }

    private void addNameField() {
        getFormLayout().add(categoryNameField);

        getBinder().forField(categoryNameField)
                .withConverter(String::trim, String::trim)
                .withValidator(new StringLengthValidator(
                        "Category name must contain at least 3 printable characters",
                        3, null))
                .withValidator(
                        name -> CategoryService.getInstance()
                                .findCategories(name).size() == 0,
                        "Category name must be unique")
                .bind(Category::getName, Category::setName);
    }

    @Override
    protected void confirmDelete() {
        int reviewCount = ReviewService.getInstance()
                .findReviews(getCurrentItem().getName()).size();
        String additionalMessage = reviewCount == 0 ? ""
                : "Deleting the category will mark the associated reviews as “undefined”."
                        + "You may link the reviews to other categories on the edit page.";
        openConfirmationDialog(
                "Delete Category “" + getCurrentItem().getName() + "”?",
                "There are " + reviewCount
                        + " reviews associated with this category.",
                additionalMessage);
    }
}
