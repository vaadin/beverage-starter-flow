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
package com.vaadin.flow.demo.helloworld;

import java.util.List;

import com.vaadin.annotations.Convert;
import com.vaadin.annotations.EventHandler;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.ModelItem;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.helloworld.ReviewsView.ReviewsModel;
import com.vaadin.flow.demo.helloworld.converters.LocalDateToStringConverter;
import com.vaadin.flow.demo.helloworld.converters.LongToStringConverter;
import com.vaadin.flow.router.View;
import com.vaadin.flow.starter.app.backend.Review;
import com.vaadin.flow.starter.app.backend.ReviewService;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;

/**
 * Simple template example.
 */
@Tag("reviews-view")
@HtmlImport("frontend://ReviewsView.html")
public class ReviewsView extends PolymerTemplate<ReviewsModel> implements View {

    @Id("filterText")
    private TextField filterText;
    @Id("addReview")
    private Button addReview;
    @Id("notification")
    private PaperToast notification;

    public static interface ReviewsModel extends TemplateModel {
        @Convert(value = LongToStringConverter.class, path = "id")
        @Convert(value = LocalDateToStringConverter.class, path = "testDate")
        @Convert(value = LongToStringConverter.class, path = "reviewCategory.categoryId")
        void setReviews(List<Review> reviews);

    }

    private ReviewForm reviewForm = new ReviewForm(this);
    ReviewService reviews = ReviewService.getInstance();

    public ReviewsView() {
        filterText.setPlaceholder("Find a review...");
        filterText.addValueChangeListener(e -> updateList());

        addReview.setText("Add new review");
        addReview.addClickListener(e -> addReviewClicked());

        updateList();
    }

    private void addReviewClicked() {
        reviewForm.clear();
        openForm();
    }

    @EventHandler
    private void edit(@ModelItem Review review) {
        openForm();
        reviewForm.bindReview(review);
    }

    public void updateList() {
        getModel().setReviews(reviews.findReviews(filterText.getValue()));
    }

    private void openForm() {
        getElement().getParent().appendChild(reviewForm.getElement());
        reviewForm.open();
    }

    public void showMessage() {
        notification.show("Your reviews have been modified.");
    }
}
