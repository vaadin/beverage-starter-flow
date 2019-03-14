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

This application supports both Polymer 2.0 and Polymer 3.0.
Since the versions require different JavaScript packaging system to be used, 
we also use the terms `bower mode` for Polymer 2.0 and `npm mode` for Polymer 3.0.
 
The workarounds left are the .js files that can be found inside `src/main/webapp/frontend/**/*.js`. These will be removed
as soon as the files are available in the webcomponents and in the right directory for the maven plugin to get. 

## Templates

The project has a template view that needs to be accessible in server-side, 
so far there is no tooling to maintain synchronized Polymer 2.0 and 3.0 versions of the template, 
hence we need to maintain two versions of the file: `src/main/webapp/frontend/src/reviewlist/reviews-list.html` and `src/main/webapp/frontend/src/reviewlist/reviews-list.js`

## Dependencies

Dependencies are managed by the flow framework and flow-maven-plugin.

## Running the Project

1. To run devmode you have two options:
  - Run the project in Polymer 3.0 mode run `mvn jetty:run`
  - Otherwise, to run it in Polymer 2.0 mode use `mvn -Dvaadin.bower.mode jetty:run`
2. Wait for the application to start
3. Open http://localhost:8080/ to view the application

## Production Mode

### Packaging and running for Polymer 2.0

1. Run the command `mvn package -Pproduction-mode` to get the artifact.
2. Deploy the generated `target/beveragebuddy-1.0-SNAPSHOT.war` file in your production server, and run  with the `vaadin.bower.mode` and `vaadin.productionMode` properties set.

### Packaging and running for Polymer 3.0

1. Install frontend dependencies by running `npm install`
2. Generate the client bundle by executing `node_modules/.bin/webpack`
3. Run `mvn package` to get the artifact.
2. Deploy the  `target/beveragebuddy-1.0-SNAPSHOT.war`, and run with the `vaadin.productionMode` propertie set.

## Documentation

Brief introduction to the application parts can be found from the `documentation` folder. For Vaadin documentation for Java users, see [Vaadin.com/docs](https://vaadin.com/docs/v10/flow/Overview.html).

## Polymer 3 development mode information

### New configuration files

* `webpack.config.js` is used to configure webpack target folders and transpilation to es5 
* `package.json` contains project meta-data and is used to manage the project dependencies
* `package-lock.json` is automatically generated for any operations where npm modifies either the node_modules tree, or package.json. It describes the exact tree that was generated, such that subsequent installs may generate identical trees, regardless of intermediate dependency updates.

NOTE: when `webpack.config.js` and `package.json` do not exists, they are automatically created by the `flow-maven-plugin`

#### Additions to the pom.xml

* The maven clean plugin addition clears the WebPack generated files on `mvn clean`
* The `fizzed-watcher-maven-plugin` is used to watch for source file changes and recompile the Java classes.
    * `exec-maven-plugin` runs the fizzed watcher so that there is no need to start it manually 
* Added profiles that are run in npm mode
    * `npm-update` profile executes the fizzer-watcher and runs the npm targets for `flow-maven-plugin`
    * `npm-install` profile will run npm install if the `node_modules` folder doesn't exist

## Adding new Polymer 3 templates

To add a new Polymer 3 template to the project create the JavaScript module in `src/main/webapp/frontend/`.

Then in the PolymerTemplate using the P3 element add the `JsModule` annotation e.g. `@JsModule("./src/views/reviewslist/reviews-list.js")` 
 
### Branching information
* `2.0` the work in progress for the future version of Vaadin Platform (14)
* `master` the latest version of the starter, using the latest platform snapshot
* `v10` the version for Vaadin Platform 10
* `v11` the version for Vaadin Platform 11
* `v12` the version for Vaadin Platform 12
* `v13` the version for Vaadin Platform 13

