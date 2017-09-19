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
import com.vaadin.flow.html.Anchor;
import com.vaadin.flow.html.HtmlContainer;
import com.vaadin.flow.html.Label;
import com.vaadin.flow.router.HasChildView;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * The main view contains a simple label element and a template element.
 */
@HtmlImport("context://styles.html")
public class MainLayout extends VerticalLayout
        implements RouterLayout, HasChildView {

    private View child;

    public MainLayout() {
        // This is just a simple label created via Elements API
        HorizontalLayout viewSelector = new HorizontalLayout();
        Label label = new Label("Beverage Buddy");

        HtmlContainer ul = new HtmlContainer("ul");
        ul.setClassName("topnav");
        add(ul);

        ul.add(new Anchor("/", "Reviews List"));
        ul.add(new Anchor("categories", "Categories List"));

        viewSelector.add(label, ul);
        add(viewSelector);
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
