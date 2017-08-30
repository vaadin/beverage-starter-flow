package com.vaadin.flow.demo.helloworld;

import com.vaadin.flow.html.Div;
import com.vaadin.flow.router.View;

public class ReviewCategoryComponent extends Div implements View {
    public ReviewCategoryComponent() {
        setText("Review categories: good, bad");
    }
}
