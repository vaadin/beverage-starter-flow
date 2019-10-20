package com.vaadin.starter.connect;

import java.util.List;

import com.vaadin.connect.VaadinService;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;

/**
 * Simple Vaadin Connect Service definition.
 * Any {@code public} method of the class can be called via the corresponding POST request.
 * <p>
 * For the current class and method, the requests should be addressed to
 * {@literal http://localhost:8080/connect/StatusService/update} endpoint
 * where
 * <ul>
 * <li><b>http://localhost:8080</b> is the application base url</li>
 * <li><b>connect</b>  is the Vaadin Connect endpoint, refer to {@literal application.properties} to know how to override it</li>
 * <li><b>StatusService</b> is the current service class name, case insensitive</li>
 * <li><b>update</b> is the corresponding method name of the service class, case insensitive</li>
 * </ul>
 * <p>
 * <p>
 * The request body should contain a json with the parameter values for the method to be called.
 * Note that in order to successfully execute the requests, an OAuth token should be passed in each of the requests by detault.
 * <p>
 * <p>
 * Refer to {@literal README.md} for more instructions on how to acquire the token and to
 * the Vaadin Connect documentation on more complete guide how to configure the application.
 */
@VaadinService
public class ConnectServices {
    public String hello(String name) {
        return "Hello, " + name + "!";
    }

    public List<Category> categories(String filter) {
        return CategoryService.getInstance().findCategories(filter);
    }

    public List<Category> save(Category category) {
        CategoryService.getInstance().saveCategory(category);
        return CategoryService.getInstance().findCategories("");
    }

    public List<Review> reviews(String filter) {
        return ReviewService.getInstance().findReviews(filter);
    }

}
