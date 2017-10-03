[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin-flow/Lobby#?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# Beverage Buddy App Starter for Vaadin Flow
:coffee::tea::sake::baby_bottle::beer::cocktail::tropical_drink::wine_glass:

This is a Vaadin Flow example application, and can be used as a starting point to create your own awesome Flow applications.

The Starter demonstrates the core Flow concepts:
* Building UIs in Java with Components based on [Vaadin Elements](https://vaadin.com/elements/browse), such as `TextField`, `Button`, `ComboBox`, `DatePicker`, `VerticalLayout` and `Grid` (see `CategoriesList`)
* [Creating forms with `Binder`](https://github.com/vaadin/free-starter-flow/blob/master/documentation/using-binder-in-reviewform.asciidoc#using-binder-in-reviewform) (`ReviewForm`)
* Making reusable Components on server side with `Composite` (`AbstractEditorDialog`)
* [Creating a Component based on a HTML Template](https://github.com/vaadin/free-starter-flow/blob/master/documentation/polymer-template-based-view.asciidoc#polymer-template-based-view) (`ReviewsList`) 
* [Integrating a third-party Web Component](https://github.com/vaadin/free-starter-flow/blob/master/documentation/integrating-a-web-component.asciidoc#integrating-a-web-component) (`PaperToast`) 
* [Creating Navigation with the Router API](https://github.com/vaadin/free-starter-flow/blob/master/documentation/using-annotation-based-router-api.asciidoc#using-the-annotation-based-router-api) (`MainLayout`, `ReviewsList`, `CategoriesList`) 

## Prerequisites

The project can be imported into the IDE of your choice, with Java 8 installed, as a Maven project.

## Running the Project

1. Run using `mvn jetty:run`
2. Wait for the application to start
3. Open http://localhost:8080/ to view the application
