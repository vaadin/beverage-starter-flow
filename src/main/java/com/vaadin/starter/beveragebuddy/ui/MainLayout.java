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
import com.vaadin.router.RouterLink;
import com.vaadin.ui.Component;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.html.Label;
import com.vaadin.ui.icon.Icon;
import com.vaadin.ui.icon.VaadinIcons;
import com.vaadin.ui.layout.HorizontalLayout;
import com.vaadin.ui.layout.VerticalLayout;

/**
 * The main layout contains the top toolbar with the view selector buttons,
 * and the child views below that.
 */
@HtmlImport("context://styles.html")
public class MainLayout extends VerticalLayout
        implements RouterLayout, HasChildView {

    private View child;

    public MainLayout() {
        Label label = new Label("Beverage Buddy");
        label.addClassName("title");
        label.addClassName("toolbar-item");
        HorizontalLayout titleBar = new HorizontalLayout(
                new Icon(VaadinIcons.HANDS_UP), label);

        RouterLink reviews = new RouterLink("Reviews", ReviewsList.class);
        reviews.setId("reviews-link");
        reviews.addClassName("link");
        RouterLink categories = new RouterLink("Categories",
                CategoriesList.class);
        categories.setId("categories-link");
        categories.addClassName("link");

        HorizontalLayout logoReview = new HorizontalLayout(
                new Icon(VaadinIcons.ARCHIVES), reviews);
        HorizontalLayout logoCategory = new HorizontalLayout(
                new Icon(VaadinIcons.LIST), categories);
        HorizontalLayout viewSelector = new HorizontalLayout(logoReview,
                logoCategory);
        HorizontalLayout toolbar = new HorizontalLayout(titleBar, viewSelector);

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
