# Picat Plugin for IntelliJ IDEA

This plugin provides support for the [Picat](http://picat-lang.org/) programming language in IntelliJ IDEA.

<!-- Plugin description -->
The Picat Plugin for IntelliJ IDEA provides comprehensive support for the Picat programming language, a logic-based multi-paradigm language. It offers syntax highlighting, code formatting, structure view, code completion, navigation, refactoring, and more to enhance your Picat development experience.
<!-- Plugin description end -->

## Features

- Syntax highlighting for Picat files
- Code formatting with customizable settings
- Structure view for Picat files
- Code completion
- Navigation to declarations
- Find usages
- Rename refactoring
- Code folding
- Brace matching
- Comment/uncomment actions
- Basic error highlighting

## Installation

You can install the plugin from the JetBrains Plugin Repository:

1. Open IntelliJ IDEA
2. Go to File > Settings > Plugins
3. Click on "Marketplace"
4. Search for "Picat"
5. Click "Install"
6. Restart IntelliJ IDEA

## Usage

### Code Formatting

The plugin includes a powerful code formatter for Picat that can be customized to match your preferred coding style. To format your code:

- Use the keyboard shortcut: Ctrl+Alt+L (Windows/Linux) or Cmd+Alt+L (Mac)
- Or select Code > Reformat Code from the menu

To customize the formatting settings:

1. Go to File > Settings > Editor > Code Style > Picat
2. Adjust the settings to match your preferences
3. Click "Apply" or "OK"

For detailed documentation on the code formatter, see [formatting.md](docs/formatting.md).

### Structure View

The Structure View shows the structure of your Picat file, including predicates, functions, and rules. To open the Structure View:

- Use the keyboard shortcut: Alt+7 (Windows/Linux) or Cmd+7 (Mac)
- Or select View > Tool Windows > Structure from the menu

### Code Completion

The plugin provides code completion for Picat keywords, built-in predicates, and user-defined predicates and functions. To use code completion:

- Start typing and press Ctrl+Space to see completion suggestions

### Navigation

To navigate to a declaration:

- Hold Ctrl (Windows/Linux) or Cmd (Mac) and click on a predicate or function name
- Or place the cursor on a name and press Ctrl+B (Windows/Linux) or Cmd+B (Mac)

### Find Usages

To find all usages of a predicate or function:

- Right-click on a predicate or function name and select "Find Usages"
- Or place the cursor on a name and press Alt+F7

### Rename Refactoring

To rename a predicate or function:

- Right-click on a predicate or function name and select "Refactor > Rename"
- Or place the cursor on a name and press Shift+F6

## Contributing

Contributions are welcome!

### Development Tools

#### Mutation Testing

This project uses [Pitest](https://pitest.org/) for mutation testing to ensure high-quality tests. To run mutation tests:

```bash
./gradlew runMutationTests
```

For more information about mutation testing in this project, see [mutation-testing.md](docs/mutation-testing.md).

## License

This plugin is licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for details.

## Acknowledgements

- This plugin was developed by the Andrii Andriichuk and Jetbrains Junie.
