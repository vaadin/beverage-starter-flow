package com.vaadin.starter.beveragebuddy;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.html.testbench.H2Element;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.testbench.TestBenchElement;


public class ReviewsListIT extends AbstractViewTest {

    @Test
    public void reviewList_buttonLumoThemed() {
        TestBenchElement reviewsList = $("reviews-list").first();
        ButtonElement newButton = reviewsList.$(ButtonElement.class).first();
        assertThemePresentOnElement(newButton, Lumo.class);
    }

    @Test
    public void reviewList_numberOfReviewsIsCorrect() {
        TestBenchElement reviewsList = $("reviews-list").first();

        WebElement header = reviewsList.$(H2Element.class).id("header");
        String reviewsCountText = header
                .findElement(By.tagName("span")).getText();
        int reviewsCount = Integer.valueOf(
                reviewsCountText.substring(0, reviewsCountText.indexOf(' ')));

        List<DivElement> reviewElements = reviewsList.$(DivElement.class)
                .attributeContains("class", "review").all();

        Assert.assertEquals(
                "The number of rendered reviews must be equal to the actual number of the reviews.",
                reviewsCount, reviewElements.size());
    }

}
