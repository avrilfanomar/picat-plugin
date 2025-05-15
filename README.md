# Picat Language Support for IntelliJ IDEA

![Build](https://github.com/avrilfanomar/picat-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.avrilfanomar.picatplugin.svg)](https://plugins.jetbrains.com/plugin/com.github.avrilfanomar.picatplugin)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.avrilfanomar.picatplugin.svg)](https://plugins.jetbrains.com/plugin/com.github.avrilfanomar.picatplugin)

## Description

The Picat Plugin provides support for the [Picat programming language](http://picat-lang.org/) in IntelliJ IDEA. Picat is a multi-paradigm programming language that integrates logic-based and function-based programming with constraint solving capabilities.

## Features

### Implemented Features

- **Basic Language Support**:
  - File type recognition for `.pi` files
  - Picat language definition
  - Custom file icon

- **Syntax Highlighting**:
  - Highlighting for keywords, operators, strings, numbers, comments, etc.
  - Customizable colors through the IDE's color settings page
  - Support for 'basic' module functions and operators

- **Run Configuration**:
  - Configure the path to the Picat executable in Settings | Tools | Picat
  - Run Picat programs directly from the IDE
  - Run the current file directly from the editor context menu or Run menu
  - Console output handling
  - Run line markers in the editor gutter

### Planned Features

- **Parser and PSI Structure**:
  - Grammar definition for Picat
  - Parser implementation
  - Program Structure Interface (PSI) for code understanding

- **Code Completion**:
  - Completion for built-in predicates and functions
  - Completion for user-defined predicates

- **Code Navigation**:
  - Reference resolution
  - Go to declaration
  - Find usages

- **Code Formatting**:
  - Automatic code formatting
  - Customizable formatting options

- **Structure View**:
  - Hierarchical view of predicates, modules, etc.

- **Debugging Support**:
  - Breakpoints
  - Step-by-step debugging

## Implementation Status

This plugin is currently in early development. The basic language support, syntax highlighting, and run configuration are implemented, but more advanced features are still in progress. You can now configure the path to the Picat executable and run Picat programs directly from the IDE.

<!-- Plugin description -->
Picat Plugin provides support for the Picat programming language in IntelliJ IDEA. Features include syntax highlighting, file type recognition, run configuration for Picat programs, and more to come.

Picat is a multi-paradigm programming language that integrates logic-based and function-based programming with constraint solving capabilities.

You can configure the path to the Picat executable in Settings | Tools | Picat and run Picat programs directly from the IDE.
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Picat Language Support"</kbd> >
  <kbd>Install</kbd>

- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/com.github.avrilfanomar.picatplugin) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/com.github.avrilfanomar.picatplugin/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/avrilfanomar/picat-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
