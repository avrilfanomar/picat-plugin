# Picat Plugin for IntelliJ IDEA

This plugin provides support for the [Picat](http://picat-lang.org/) programming language in IntelliJ IDEA.
<!-- Plugin description -->

        Support for the Picat programming language in IntelliJ IDEA.

        <p>Picat is a multi-paradigm programming language that integrates logic-based and function-based programming with constraint solving capabilities.</p>

        <h3>Features:</h3>
        <ul>
            <li>Syntax highlighting for Picat files (.pi)</li>
            <li>Default color themes for Picat elements (customizable per IDE scheme)</li>
            <li>Code formatting with customizable settings</li>
            <li>Run configuration for Picat programs</li>
            <li>Live templates</li>
            <li>Autocompletion</li>
            <li>Navigate | Symbol for predicates/functions</li>
            <li>Quick Documentation (Ctrl+Q) for symbols</li>
            <li>Navigation Bar breadcrumbs for rules</li>
            <li>Spellchecking limited to comments and string literals</li>
            <li>Code folding</li>
            <li>File type recognition</li>
            <li>Custom file icon</li>
        </ul>

        <p>This plugin is licensed under the <a href="https://github.com/avrilfanomar/picat-plugin/blob/main/LICENSE">Apache License 2.0</a>.</p>

<!-- Plugin description end -->

## Features

- Syntax highlighting for Picat files
- Code formatting with customizable settings
- Structure view for Picat files
- Code completion
- Navigation to declarations
- Find usages
- Rename refactoring
- Code folding (rule bodies, multi-clause groups)
- Navigation Bar breadcrumbs
- Quick Documentation (Ctrl+Q)
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

The plugin includes a powerful code formatter for Picat that can be customized to match your preferred coding style. To
format your code:

- Use the keyboard shortcut: Ctrl+Alt+L (Windows/Linux) or Cmd+Alt+L (Mac)
- Or select Code > Reformat Code from the menu

To customize the formatting settings:

1. Go to File > Settings > Editor > Code Style > Picat
2. Adjust the settings to match your preferences
3. Click "Apply" or "OK"

### Structure View

The Structure View shows the structure of your Picat file, including predicates, functions, and rules. To open the
Structure View:

- Use the keyboard shortcut: Alt+7 (Windows/Linux) or Cmd+7 (Mac)
- Or select View > Tool Windows > Structure from the menu

### Live Templates

Common Picat constructs are available as live templates (type the key and press Tab):

- if: if-then block
- ife: if-then-else block
- ifel: if-elseif-else block
- foreach: foreach loop (foreach (X in L) ... end)
- while: while loop (while (Cond) ... end)
- loopw: loop ... while pattern
- try: try-catch block
- tryf: try-catch-finally block

### Code Completion

The plugin provides code completion for Picat keywords, built-in predicates, and user-defined predicates and functions.
To use code completion:

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

## Development

- Prerequisites:
    - JDK 21
    - Gradle 9.0 (use the provided wrapper: ./gradlew)
- Build the plugin and run tests: ./gradlew build
- Run tests: ./gradlew test
- Static code analysis (Detekt): ./gradlew detekt
    - Reports are generated in build/reports/detekt/
- Run the IDE with the plugin: ./gradlew runIde

This project uses GrammarKit/JFlex to generate the parser and lexer from grammars:

- Parser is generated from `src/main/grammars/Picat.bnf` into `src/main/gen` as
  `com.github.avrilfanomar.picatplugin.language.parser.PicatParser` and related PSI/element types.
- Lexer is generated from `src/main/grammars/_PicatLexer.flex` into `src/main/gen` as
  `com.github.avrilfanomar.picatplugin.language.parser._PicatLexer`.

Important:

- Do not manually modify generated files under `src/main/gen`, as well as `_PicatLexer.flex`. Changes to parsing
  behavior must be done in `Picat.bnf`, then the sources regenerated.
- Commit regenerated sources as part of your change if you modify the grammar definitions.

Regeneration instructions (local development):

- Gradle tasks for generation are provided and included in the build by default (./gradlew generateParser
  generateLexer); alternatively, use IDE generators as below.
- Lexer: Use "Run JFlex Generator" on `_PicatLexer.flex` (Tools | JFlex | Run JFlex Generator) targeting
  `src/main/grammars`.
- Parser/PSI: Open the project in IntelliJ IDEA with GrammarKit installed, open `Picat.bnf`, and use "Generate Parser
  Code" (Tools | Grammar-Kit | Generate Parser Code) targeting `src/main/gen`.

The canonical rule: PicatParser is generated from BNF and should not be manually modified.

## Contributing

Contributions are welcome! Please follow the Development guidelines above and ensure all tests and Detekt pass before
submitting a PR.

## License

This plugin is licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for details.

## Acknowledgements

- This plugin was developed by Andrii Andriichuk, including parts generated by Jetbrains Junie, Claude AI, OpenAI
  ChatGPT and Google Jules.
