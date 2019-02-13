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

But additionally you need `node.js` and `npm` installed in your System.

## Polymer Versions

There is a work in progress to support both Polymer 2.0 and Polymer 3.0 in this application.
Since each version requires different JavaScript packaging system, we can refer as `bower mode` to Polymer 2.0, or `npm mode` for Polymer 3.0.

We include some hacks to make the project work with `npm` in the meanwhile Vaadin that Flow is fully prepared for it.
Some of those workarounds include customized copies of flow classes, or steps that the user needs to perform manually.

## Templates

The project has a template that needs to be accessible in server-side, there is no tooling so far to maintain synchronized Polymer 2.0 and 3.0 versions of the template, hence we need to maintain two versions of the file: `src/main/webapp/frontend/src/reviewlist/reviews-list.html` and `src/main/webapp/frontend/src/reviewlist/reviews-list.js`

## Dependencies

In `bower` mode, dependencies are managed by the flow framework, but in `npm` we still need to manually edit the `src/main/webapp/package.json` and `src/main/webapp/main.js` to add and import new dependencies respectively.

## Running the Project

1. when in the `src/main/webapp` folder run `npm install`
2. To run devmode you have two options:
  - Run the project in Polymer 3.0 mode by executing `mvn jetty:run`
  - Otherwise, to run it in Polymer 2.0 mode use `mvn -Dvaadin.bower.mode jetty:run`
3. Wait for the application to start
4. Open http://localhost:8080/ to view the application

## Production Mode

### Packaging and running for Polymer 2.0

1. Run the command `mvn package -Pproduction-mode` to get the artifact.
2. Deploy the generated `target/beveragebuddy-1.0-SNAPSHOT.war` file in your production server, and run  with the `vaadin.bower.mode` and `vaadin.productionMode` properties set.

### Packaging and running for Polymer 2.0

1. When in the `src/main/webapp` folder run `npm install`
2. Generate the client bundle by executing `node_modules/.bin/webpack`
3. Run `mvn package` to get the artifact.
2. Deploy the  `target/beveragebuddy-1.0-SNAPSHOT.war`, and run with the `vaadin.productionMode` propertie set.

## Documentation

Brief introduction to the application parts can be found from the `documentation` folder. For Vaadin documentation for Java users, see [Vaadin.com/docs](https://vaadin.com/docs/v10/flow/Overview.html).

### Branching information
* `2.0` the work in progress for the future version of Vaadin (14)
* `master` the latest version of the starter, using the latest platform snapshot
* `v10` the version for Vaadin 10
* `v11` the version for Vaadin 11
* `v12` the version for Vaadin 12
* `v13` the version for Vaadin 13

