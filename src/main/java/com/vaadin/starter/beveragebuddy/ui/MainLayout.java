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

import com.vaadin.flow.router.HasChildView;
import com.vaadin.flow.router.View;
import com.vaadin.router.RouterLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.html.Anchor;
import com.vaadin.ui.html.Label;
import com.vaadin.ui.layout.HorizontalLayout;
import com.vaadin.ui.layout.VerticalLayout;

/**
 * The main view contains a simple label element and a template element.
 */
@HtmlImport("context://styles.html")
public class MainLayout extends VerticalLayout
        implements RouterLayout, HasChildView {

    private View child;

    public MainLayout() {
        Label label = new Label("Beverage Buddy");
        label.addClassName("title");
        label.addClassName("toolbar-item");

        Anchor reviews = new Anchor("/", "Reviews List");
        reviews.setId("reviews-link");
        reviews.addClassName("link");
        Anchor categories = new Anchor("categories", "Categories List");
        categories.setId("categories-link");
        categories.addClassName("link");

        HorizontalLayout viewSelector = new HorizontalLayout(reviews, categories);
        HorizontalLayout toolbar = new HorizontalLayout(label, viewSelector);

        viewSelector.addClassName("toolbar-item");
        toolbar.addClassName("toolbar");
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.BASELINE);
        addClassName("main-layout");
        setAlignItems(Alignment.CENTER);
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
