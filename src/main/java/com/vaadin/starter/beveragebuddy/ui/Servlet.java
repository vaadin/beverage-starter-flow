package com.vaadin.starter.beveragebuddy.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

/*** The main servlet for the application. */

@WebServlet(urlPatterns = "/*", name = "UIServlet", asyncSupported = true)
@VaadinServletConfiguration(usingNewRouting = true, productionMode = false)
public class Servlet extends VaadinServlet {
}
