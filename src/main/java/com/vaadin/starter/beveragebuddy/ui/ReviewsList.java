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

import java.util.List;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.Route;
import com.vaadin.router.Title;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.starter.beveragebuddy.ui.ReviewsList.ReviewsModel;
import com.vaadin.starter.beveragebuddy.ui.converters.LocalDateToStringConverter;
import com.vaadin.starter.beveragebuddy.ui.converters.LongToStringConverter;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.icon.Icon;
import com.vaadin.ui.icon.VaadinIcons;
import com.vaadin.ui.polymertemplate.EventHandler;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.ModelItem;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.TextField;

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 *
 * Implemented using a simple template.
 */
@Route(value = "", layout = MainLayout.class)
@Title("Review List")
@Tag("reviews-list")
@HtmlImport("frontend://ReviewsList.html")
public class ReviewsList extends PolymerTemplate<ReviewsModel> {

    public static interface ReviewsModel extends TemplateModel {
        @Convert(value = LongToStringConverter.class, path = "id")
        @Convert(value = LocalDateToStringConverter.class, path = "date")
        @Convert(value = LongToStringConverter.class, path = "category.id")
        void setReviews(List<Review> reviews);
    }

    @Id("filterText")
    private TextField filterText;
    @Id("addReview")
    private Button addReview;
    @Id("notification")
    private PaperToast notification;

    private ReviewEditorDialog reviewForm = new ReviewEditorDialog(
            this::saveUpdate, this::deleteUpdate);

    public ReviewsList() {
        filterText.setPlaceholder("Find a review...");
        filterText.addValueChangeListener(e -> updateList());
        filterText.addClassName("filter-field");

        addReview.setText("Add new review");
        addReview.setIcon(new Icon(VaadinIcons.PLUS));
        addReview.addClickListener(e -> openForm(new Review(),
                AbstractEditorDialog.Operation.ADD));

        updateList();

    }

    public void saveUpdate(Review review,
            AbstractEditorDialog.Operation operation) {
        ReviewService.getInstance().saveReview(review);
        updateList();
        notification.show(
                "Beverage successfully " + operation.getNameInText() + "ed.");
    }

    public void deleteUpdate(Review review) {
        ReviewService.getInstance().deleteReview(review);
        updateList();
        notification.show("Beverage successfully deleted.");
    }

    private void updateList() {
        getModel().setReviews(
                ReviewService.getInstance().findReviews(filterText.getValue()));
    }

    @EventHandler
    private void edit(@ModelItem Review review) {
        openForm(review, AbstractEditorDialog.Operation.EDIT);
    }

    private void openForm(Review review,
            AbstractEditorDialog.Operation operation) {
        // Add the form lazily as the UI is not yet initialized when
        // this view is constructed
        if (reviewForm.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(reviewForm));
        }
        reviewForm.open(review, operation);
    }

}
