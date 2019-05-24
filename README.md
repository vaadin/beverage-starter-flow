[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin-flow/Lobby#?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# Beverage Buddy App Starter for Vaadin Flow
:coffee::tea::sake::baby_bottle::beer::cocktail::tropical_drink::wine_glass:

This is a Vaadin platform Java example application, used to demonstrate features of the Vaadin Flow framework.

The easiest way of using it is via [https://vaadin.com/start](https://vaadin.com/start/v10-simple-ui) - you can choose the package naming you want.

The Starter demonstrates the core Vaadin Flow concepts:
* Building UIs in Java with Components based on [Vaadin components](https://vaadin.com/components/browse), such as `TextField`, `Button`, `ComboBox`, `DatePicker`, `VerticalLayout` and `Grid` (see `CategoriesList`)
* [Creating forms with `Binder`](https://github.com/vaadin/free-starter-flow/blob/master/documentation/using-binder-in-review-editor-dialog.asciidoc) (`ReviewEditorDialog`)
* Making reusable Components on server side with `Composite` (`AbstractEditorDialog`)
* [Creating a Component based on a HTML Template](https://github.com/vaadin/free-starter-flow/blob/master/documentation/polymer-template-based-view.asciidoc) (`ReviewsList`)
  * This template can be opened and edited with [the Vaadin Designer](https://vaadin.com/designer)
* [Creating Navigation with the Router API](https://github.com/vaadin/free-starter-flow/blob/master/documentation/using-annotation-based-router-api.asciidoc) (`MainLayout`, `ReviewsList`, `CategoriesList`)

## Prerequisites

The project can be imported into the IDE of your choice, with Java 8 installed, as a Maven project.

But additionally you need `node.js` installed in your System, and available in your `PATH`.
See the [Node page](https://nodejs.org/en/) for the installation instructions.

## Dependencies

Dependencies are managed by the Flow framework and flow-maven-plugin.

## Running the Project in Developer Mode

1. Run `mvn jetty:run`
2. Wait for the application to start
3. Open http://localhost:8080/ to view the application

Note that there are two files created in the project structure automatically:

* `webpack.config.js` is used to configure webpack target folders and transpilation to es5
* `package.json` contains project meta-data and is used to manage the project dependencies

## Production Mode

1. Run `mvn package -Pproduction` to get the artifact.
2. Deploy the `target/beveragebuddy-1.0-SNAPSHOT.war`.
If you want to run the production build using the Jetty plugin, use `mvn jetty:run-exploded` after you've build the artifact in the production mode
and navigate to the http://localhost:8080/ page.

## Documentation

Brief introduction to the application parts can be found from the `documentation` folder. For Vaadin documentation for Java users, see [Vaadin.com/docs](https://vaadin.com/docs/v10/flow/Overview.html).

## Adding new templates

To add a new template or a style to the project create the JavaScript module in the `./frontend` directory.

Then in the PolymerTemplate using the P3 element add the `JsModule` annotation e.g. `@JsModule("./src/views/reviewslist/reviews-list.js")`

### Branching information
* `master` the latest version of the starter, using the latest platform snapshot
* `v10` the version for Vaadin Platform 10
* `v11` the version for Vaadin Platform 11
* `v12` the version for Vaadin Platform 12
* `v13` the version for Vaadin Platform 13

