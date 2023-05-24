# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Ability to add stages to run as part of the core loop
- Utility for reading in a file as a string
- Cancelable events
- Custom scripting language
- New methods to the Plugin class, including a breaking change of requiring a name

### Changed
- Updated build system to gradle
- Change the main threads classloader to be able to read from plugin jars
- Allow registering of classes with the scripting engine to expose methods
- Synchronized singleton access methods

### Removed
- Task manager

## [0.4.0] - 2021-09-08
### Added
- gson bundled in for use in plugins
- Slf4j logger backed by log4j2

### Changed
- Bundle library javadocs into javadoc output jar
- Stop sending logs through the event system

### Removed
- Logging package

## [0.3.0] - 2021-08-31
### Added
- Plugin folder utilities
- Licenses bundled into builds
- Command line entry point with help text
- Plugin management from command line
- AVL Tree Map data structure

### Changed
- Disabling and unloading plugins recursively

### Removed
- ArrayOperations util
- Permissions package (moved to a plugin)

## [0.2.0] - 2021-08-02
### Added
- Cyclic plugin dependency resolution
- Changed plugin yaml file entries
- Event testing utility
- Changelog

### Changed
- Plugin dependencies now load first, and besides that load order is randomized
- Plugin management is now done using plugin names, not plugin objects directly

## [0.1.0] - 2021-06-10
### Added
- Added Lombok, bundled in for use in plugins
- Logger factory, log annotations

### Changed
- Made Plugin an abstract class, added default methods
- Versioned jar output
- Standardized library folder structure
- Extract name and versions out of plugins

### Removed
- Console GUI

## [0.0.1] - 2019-05-03
### Added
- Event system
- Logging
- Scripting Engine class
- Utilities
	- Array operations
	- Binary Tree
	- File utils
	- Safe resource loading
	- System properties
- Permissions
- Localization

### Changed
- Switched to Semantic Versioning