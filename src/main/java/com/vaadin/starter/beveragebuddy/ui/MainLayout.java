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

import com.vaadin.annotations.HtmlImport;
import com.vaadin.flow.html.Label;
import com.vaadin.flow.router.HasChildView;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * The main view contains a simple label element and a template element.
 */
@HtmlImport("context://styles.html")
public class MainLayout extends VerticalLayout implements HasChildView {

    private View child;

    public MainLayout() {
        // This is just a simple label created via Elements API
        Label label = new Label("Beverage Buddy");
        label.addClassName("title");
        label.addClassName("toolbar-item");

        RouterLink reviews = new RouterLink("Reviews", ReviewsList.class);
        reviews.setId("reviews-link");
        reviews.addClassName("link");
        RouterLink categories = new RouterLink("Categories",
                CategoriesList.class);
        categories.setId("categories-link");
        categories.addClassName("link");

        HorizontalLayout viewSelector = new HorizontalLayout(reviews, categories);
        HorizontalLayout toolbar = new HorizontalLayout(label, viewSelector);

        viewSelector.addClassName("toolbar-item");
        toolbar.addClassName("toolbar");
        toolbar.setSpacing(true);
        toolbar.setDefaultComponentAlignment(Alignment.BASELINE);
        addClassName("main-layout");
        setDefaultComponentAlignment(Alignment.CENTER);
        add(toolbar);
    }

    @Override
    public void setChildView(View childView) {
        // Update what we show whenever the sub view changes
        if (child != null) {
            this.remove((Component) child);
        }

        if (childView != null) {
            add((Component) childView);
        }
        child = childView;
    }
}
