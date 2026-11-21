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

# Running

The project can be run like this: `java -jar Ikala-Core-1.0.0.jar <arguments>` (where 1.0.0 is your jar version).
See `--help` for more arguments after the jar.

## Folders

The framework uses three kinds of folder (see `AppPaths`):

- **Install**: the folder holding the Ikala-Core jar, with `plugins/` and `libs/` next to it. It is only read while
  running, so the jar can be started from any working directory.
- **Home**: one per user per machine, for everything the application writes: `settings/` (config overrides and other
  user settings), `state/`, `logs/`, `reports/`, `screenshots/` and anything plugins add. The cache is a separate folder.
- **Instance** (optional): a folder given with `--instance`, such as the save a server hosts. Its logs go in
  `<instance>/logs`.

The application's name comes from `app.yml` in the install folder (`name: Lotomation`), and picks the default Home and
cache folders:

| OS      | Home                                                         | Cache                                                    |
|---------|--------------------------------------------------------------|----------------------------------------------------------|
| Windows | `%APPDATA%\<name>`                                           | `%LOCALAPPDATA%\<name>\cache`                            |
| Linux   | `$XDG_DATA_HOME/<name>` (`~/.local/share/<name>`), lower case | `$XDG_CACHE_HOME/<name>` (`~/.cache/<name>`), lower case |
| macOS   | `~/Library/Application Support/<name>`                       | `~/Library/Caches/<name>`                                |

Without `app.yml` the name is `Ikala`. A file named `portable` in the install folder keeps the Home and cache in
`<install>/home` instead. The `--home`, `--cache` and `--instance` options, or the properties below, override them.

Each plugin's shipped files are in a folder named after it, next to its jar: `config/`, `data/` and `scripts/`.
A config is read from the copy in the jar, then the shipped `config/` file, then the user's changes in
`<home>/settings/plugins/<plugin>/`, each overriding the one before. Saving a config writes only what differs from
the defaults, to the Home. So users can see every setting, the defaults are written beside their file as
`config.defaults.yml` (from `config.yml`), with the jar's comments and any values the install ships. It is rewritten
whenever the defaults change and is never read. The last loaded version of each plugin is kept in
`<home>/state/plugin-versions.yml`.

## Dependency versions

Entries in `dependencies` and `soft-dependencies` in `plugin.yml` can follow the plugin name with version bounds:

```yaml
dependencies:
  - Ikala-Graphics
  - Factory-Core >=0.1.0 <0.2.0
soft-dependencies:
  - Factory-Server >=0.0.1-alpha
```

Each bound is `>=`, `>`, `<=`, `<` or `=` (or no operator, for exactly that version) and a full semantic version, and
a version must meet every bound. Versions compare by semantic versioning precedence, so a pre-release comes before its
release (`1.0.0-alpha` does not meet `>=1.0.0`), and build metadata is ignored. A dependency whose version isn't
accepted stops the plugin from loading, as a missing one does. A soft dependency that is present with a version that
isn't accepted does too, since the plugin would otherwise use a version it doesn't support; a soft dependency that
isn't present is still fine.

## System properties

These are set with `-D` before `-jar`, for example
`java -Dikala.logLevel=TRACE -Dikala.plugins.loadOrderSeed=12345 -jar Ikala-Core-1.0.0.jar`.

| Property                      | Default | What it does                                                                                                                                                                                                                                                                                                                                                                                                         |
|-------------------------------|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `ikala.home`                  | by OS   | The Home folder, the same as `--home`. A moved Home keeps its cache in `<home>/cache` unless `ikala.cache` is also set.                                                                                                                                                                                                                                                                                               |
| `ikala.cache`                 | by OS   | The cache folder, the same as `--cache`.                                                                                                                                                                                                                                                                                                                                                                             |
| `ikala.instance`              | none    | The instance folder, the same as `--instance`.                                                                                                                                                                                                                                                                                                                                                                       |
| `ikala.install`               | jar     | The install folder. It defaults to the folder holding the jar, or the working directory when running from classes, as in tests.                                                                                                                                                                                                                                                                                      |
| `ikala.logDir`                | Home    | The log folder. The launcher sets it to `<home>/logs`, or `<instance>/logs` with an instance, unless it is set already. Tools and tests that start without the launcher log to `logs` in the working directory.                                                                                                                                                                                                      |
| `ikala.logLevel`              | `DEBUG` | The log level for `com.ikalagaming` loggers, such as `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR` or `OFF`. Other libraries' log levels aren't affected. Read by `log4j2.xml` at startup, so it applies from the first log line.                                                                                                                                                                                        |
| `ikala.plugins.loadOrderSeed` | random  | The seed used to shuffle the order plugins load in (dependencies still load before the plugins that need them), so no plugin can rely on an order by accident. Each run logs its seed at the debug level, as `Shuffling plugin load order with seed ...`; pass that seed back to reproduce a problem that only happens in that order. It must be a whole number; anything else is ignored and a random seed is used. |


# Editing
To set up a project for Eclipse you can run `./gradlew eclipse`, or for IntelliJ you can run `./gradlew idea`.

# FAQ

**Why not use OSGI?**

This library offers modular code and the ability to hot-swap code in and out of a running application without the need for a restart.
That sounds a lot like OSGI, so why should I use this instead, when OSGI has more features and support?

The primary reason is simplicity. OSGI renders development quite complex, and requires a large initial investment to use.
This library is designed to be simple to understand and use, allowing even beginner programmers to quickly create and use plugins.

We sacrifice some of the more advanced features for ease of use, reduced configuration, lower overhead, and simple implementation.
