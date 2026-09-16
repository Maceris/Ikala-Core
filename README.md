Ikala Core
=====
# Info
This is a framework for building modular plugin-based applications. This includes:

* An event management system which allows for an event system that is very simple and easy to use. 
* A plugin system that allows you to dynamically load/unload, and enable/disable plugins at run time. This allows you to split up functionality into bundles to easily handle the life-cycle of each independently. It also integrates with the event and scripting systems which allows better decoupling of different APIs.
* Some simple utilities to make tasks easier such as loading resources, fetching system properties, and handling files.

For more information about what this provides and how to use the components, please check out the wiki on GitHub.


# Building

To build the project, run `./gradlew clean build`. This should build the project, run the tests, and generate jars for distribution (under `build\libs`).

## Parser

Some of the scripting language parser is generated with antlr. See `docs` for the g4 files, when antlr is regenerated
the scripts will need to be regenerated like

```bash
java -jar antlr-4.13.2-complete.jar -Dlanguage=Java IkalaScriptLexer.g4 IkalaScriptParser.g4
```

# Editing
To set up a project for Eclipse you can run `./gradlew eclipse`, or for IntelliJ you can run `./gradlew idea`.

# FAQ

**Why not use OSGI?**

This library offers modular code and the ability to hot-swap code in and out of a running application without the need for a restart.
That sounds a lot like OSGI, so why should I use this instead, when OSGI has more features and support?

The primary reason is simplicity. OSGI renders development quite complex, and requires a large initial investment to use.
This library is designed to be simple to understand and use, allowing even beginner programmers to quickly create and use plugins.

We sacrifice some of the more advanced features for ease of use, reduced configuration, lower overhead, and simple implementation.
