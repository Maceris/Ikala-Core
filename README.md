Ikala Core
=====

These is a library of utilities that are intended to provide functionality that are commonly needed in various applications to make development much easier and more efficient. This includes:

* An event management system which allows for an event system that is very simple and easy to use. 
* A plugin system that allows you to dynamically load/unload, and enable/disable plugins at run time. This allows you to split up functionality into bundles to easily handle the lifecycle of each part. It also integrates with the event system which allows better decoupling of different APIs.
* A logging system which integrates with the event system and all core library functionality. It is fairly simple but provides logging levels and has a simple API.
* Some simple utilities to make tasks easier such as loading resources, getting system properties, and handling files.

The `dist` directory contains output jars for the project.

For more information about what this provides and how to use the components, please check out the wiki on github.
