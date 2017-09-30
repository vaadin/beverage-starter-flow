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

import com.vaadin.router.RouterLayout;
import com.vaadin.router.RouterLink;
import com.vaadin.router.event.AfterNavigationEvent;
import com.vaadin.router.event.AfterNavigationListener;
import com.vaadin.ui.Text;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.html.Div;
import com.vaadin.ui.html.H2;
import com.vaadin.ui.icon.Icon;
import com.vaadin.ui.icon.VaadinIcons;

/**
 * The main layout contains the header with the navigation buttons, and the
 * child views below that.
 */
@HtmlImport("frontend://styles.html")
public class MainLayout extends Div implements RouterLayout, AfterNavigationListener {

    private static final String ACTIVE_ITEM_STYLE = "main-layout__nav-item--selected";
    private RouterLink categories;
    private RouterLink reviews;

    public MainLayout() {
        H2 title = new H2("Beverage Buddy");
        title.addClassName("main-layout__title");

        reviews = new RouterLink(null, ReviewsList.class);
        reviews.add(new Icon(VaadinIcons.LIST), new Text("Reviews"));
        reviews.addClassName("main-layout__nav-item");

        categories = new RouterLink(null, CategoriesList.class);
        categories.add(new Icon(VaadinIcons.ARCHIVES), new Text("Categories"));
        categories.addClassName("main-layout__nav-item");

        Div navigation = new Div(reviews, categories);
        navigation.addClassName("main-layout__nav");

        Div header = new Div(title, navigation);
        header.addClassName("main-layout__header");
        add(header);

        addClassName("main-layout");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        // updating the active menu item based on if either of views is active
        // (note that this is triggered even for the error view)
        String segment = event.getLocation().getFirstSegment();
        boolean reviewsActive = segment.equals(reviews.getHref());
        boolean categoriesActive = segment.equals(categories.getHref());

        reviews.setClassName(ACTIVE_ITEM_STYLE, reviewsActive);
        categories.setClassName(ACTIVE_ITEM_STYLE, categoriesActive);
    }
}
