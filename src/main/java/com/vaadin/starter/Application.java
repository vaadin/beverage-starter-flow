package com.vaadin.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.connect.auth.server.EnableVaadinConnectOAuthServer;

/**
 * Spring boot starter class.
 */
@SpringBootApplication
@EnableVaadinConnectOAuthServer
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
