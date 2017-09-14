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
import java.util.Optional;

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
import com.vaadin.ui.AttachEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;

/**
 * This Reviews List showing the existed reviews.
 * <p>
 * Created as a single class because it is based on polymer template. The class
 * provides with one search bar and one button for adding new reviews. Review
 * list will be shown as follow in a flexbox.
 */
@Tag("reviews-view")
@HtmlImport("frontend://ReviewsView.html")
public class ReviewsView extends PolymerTemplate<ReviewsModel> implements View {

    public static interface ReviewsModel extends TemplateModel {
        @Convert(value = LongToStringConverter.class, path = "id")
        @Convert(value = LocalDateToStringConverter.class, path = "testDate")
        @Convert(value = LongToStringConverter.class, path = "reviewCategory.categoryId")
        void setReviews(List<Review> reviews);
    }

    @Id("filterText")
    private TextField filterText;
    @Id("addReview")
    private Button addReview;
    @Id("notification")
    private PaperToast notification;

    private ReviewForm reviewForm = new ReviewForm(this::saveUpdate,
            this::deleteUpdate);

    public ReviewsView() {
        filterText.setPlaceholder("Find a review...");
        filterText.addValueChangeListener(e -> updateList());

        addReview.setText("Add new review");
        addReview.addClickListener(e -> addReviewClicked());
        updateList();

    }

    /**
     * This method is called once the user saves a review.
     * <p>
     * This happens when a user adds a new review to the list or edits an
     * existed review. After the operation, a notification informs the user that
     * a new review has been saved.
     * 
     * @param review
     *            the review which is going to be saved.
     */

    public void saveUpdate(Review review) {
        ReviewService.getInstance().saveReview(review);
        updateList();
        notification.show("A new review/edit has been saved.");
    }

    /**
     * This method is invoked once the user deletes a review.
     * <p>
     * This happens when a user confirms that an existed review deletes from the
     * backend. After the operation, a notification informs the user that the
     * review has been deletes.
     * 
     * @param review
     *            the review which is going to be deleted.
     */
    public void deleteUpdate(Review review) {
        ReviewService.getInstance().deleteReview(review);
        updateList();
        notification.show("Your selected review has been deleted.");
    }

    private void updateList() {
        getModel().setReviews(
                ReviewService.getInstance().findReviews(filterText.getValue()));
    }

    @EventHandler
    private void edit(@ModelItem Review review) {
        reviewForm.openReview(Optional.of(review));
    }

    private void addReviewClicked() {
        reviewForm.openReview(Optional.empty());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getElement().getParent().appendChild(reviewForm.getElement());
    }

}
