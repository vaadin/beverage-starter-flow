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
import com.vaadin.annotations.Exclude;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.helloworld.ReviewsView.ReviewsModel;
import com.vaadin.flow.router.View;
import com.vaadin.flow.starter.app.backend.Review;
import com.vaadin.flow.starter.app.backend.ReviewService;

import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Simple template example.
 */
@Tag("reviews-view")
@HtmlImport("frontend://ReviewsView.html")
public class ReviewsView extends PolymerTemplate<ReviewsModel> implements View {

    /**
     * Template model which defines the single "name" property.
     */
 public static interface ReviewsModel extends TemplateModel {
        
        //Review getCurrentReview();
        //void setCurrentReview(Review currentReview);
        @Exclude("id")
        @Convert(value=LocalDateToStringConverter.class, path="testDate")
        void setReviews(List<Review> reviews);
        
    }

    public ReviewsView() {
        
        List<Review> reviews = ReviewService.createDemoReviewService();
        getModel().setReviews(reviews);
        
    }
}
