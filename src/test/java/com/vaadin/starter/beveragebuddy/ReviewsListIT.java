package com.vaadin.starter.beveragebuddy;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class ReviewsListIT extends AbstractViewTest {

    public ReviewsListIT() {
        super("", By.tagName("reviews-list"));
    }

    @Test
    public void reviewList_numberOfReviewsIsCorrect() {
        List<WebElement> headerElements = findInShadowRoot(getRootElement(),
                By.id("header"));
        Assert.assertEquals("There must be one and only one header element.", 1,
                headerElements.size());

        String reviewsCountText = headerElements.get(0)
                .findElement(By.tagName("span")).getText();
        int reviewsCount = Integer.valueOf(
                reviewsCountText.substring(0, reviewsCountText.indexOf(' ')));

        List<WebElement> reviewElements = findInShadowRoot(getRootElement(),
                By.className("review"));
        Assert.assertEquals(
                "The number of rendered reviews must be equal to the actual number of the reviews.",
                reviewsCount, reviewElements.size());
    }

}
