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

import com.vaadin.annotations.StyleSheet;
import com.vaadin.flow.html.Div;
import com.vaadin.flow.html.H2;
import com.vaadin.flow.html.Label;
import com.vaadin.flow.router.HasChildView;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * The main view contains a simple label element and a template element.
 */
@StyleSheet("context://styles.css")
public class MainView extends Composite<Div> implements HasChildView {

    private Div container;

    public MainView() {
        // This is just a simple label created via Elements API
        Label label = new Label("Food & Drink Reviews");
        getContent().add(label);

        RouterLink beers = new RouterLink("Reviews", ReviewsTemplate.class);
        beers.setId("reviews-link");
        RouterLink categories = new RouterLink("Categories", ReviewCategoryComponent.class);
        categories.setId("categories-link");

        HorizontalLayout viewSelector = new HorizontalLayout(label, beers, categories);

        // Set up the container where sub views will be shown
        container = new Div();
        container.addClassName("container");
        VerticalLayout layout = new VerticalLayout(viewSelector, container);

        getContent().addClassName("main-view");
        getContent().add(layout);
    }

    @Override
    public void setChildView(View childView) {
        // Update what we show whenever the sub view changes
        container.removeAll();

        if (childView != null) {
            container.add(new H2(childView.getTitle(null)));
            container.add((Component) childView);
        }
    }
}
