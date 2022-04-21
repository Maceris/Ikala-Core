Ikala Core
=====
# Info
This is a framework for building modular plugin-based applications. This includes:

* An event management system which allows for an event system that is very simple and easy to use. 
* A plugin system that allows you to dynamically load/unload, and enable/disable plugins at run time. This allows you to split up functionality into bundles to easily handle the lifecycle of each part. It also integrates with the event system which allows better decoupling of different APIs.
* Some simple utilities to make tasks easier such as loading resources, getting system properties, and handling files.

For more information about what this provides and how to use the components, please check out the wiki on GitHub.

# Building

To build the project, run `./gradlew clean build`. This should build the project, run the tests, and generate jars for distribution (under `build\libs`).

# Editing
To set up a project for Eclipse you can run `./gradlew eclipse`, or for IntelliJ you can run `./gradlew idea`.
