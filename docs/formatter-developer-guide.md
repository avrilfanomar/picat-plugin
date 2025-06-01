# Picat Code Formatter Developer Guide

## Architecture Overview

The Picat code formatter is built on top of IntelliJ's formatting framework. It consists of several key components that work together to format Picat code according to configurable style settings.

## Key Components

### 1. PicatFormattingModelBuilder

The `PicatFormattingModelBuilder` class is the entry point for the formatter. It implements the `FormattingModelBuilder` interface and is responsible for creating a formatting model for Picat code.

```kotlin
class PicatFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val element = formattingContext.psiElement
        val settings = formattingContext.codeStyleSettings

        val spacingBuilder = PicatSpacingBuilder(settings).getSpacingBuilder()
        val blockFactory = PicatBlockFactory(settings, spacingBuilder)

        val rootBlock = blockFactory.createBlock(element.node)

        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            rootBlock,
            settings
        )
    }
}
```

This class is registered with the IntelliJ platform via the `LanguageFormatting` extension point, which allows the IDE to find the appropriate formatter for Picat files.

### 2. PicatSpacingBuilder

The `PicatSpacingBuilder` class defines spacing rules for Picat language elements. It uses IntelliJ's `SpacingBuilder` to create a set of rules that determine the spacing between different elements in the code.

```kotlin
class PicatSpacingBuilder(settings: CodeStyleSettings) {
    private val spacingBuilder: SpacingBuilder

    init {
        // Define spacing rules based on settings
    }

    fun getSpacingBuilder(): SpacingBuilder {
        return spacingBuilder
    }
}
```

The spacing rules are based on the code style settings defined in `PicatCodeStyleSettings`.

### 3. PicatCodeStyleSettings

The `PicatCodeStyleSettings` class defines customizable code style settings for Picat. It extends IntelliJ's `CustomCodeStyleSettings` class and provides properties for various formatting options.

```kotlin
class PicatCodeStyleSettings(settings: CodeStyleSettings) : CustomCodeStyleSettings("PicatCodeStyleSettings", settings) {
    // Define code style settings
}
```

These settings are exposed to the user through the IDE's code style settings UI.

### 4. PicatBlockFactory

The `PicatBlockFactory` class is responsible for creating `PicatBlock` instances based on AST nodes. It helps to separate the block creation logic from the block implementation.

```kotlin
class PicatBlockFactory(
    private val settings: CodeStyleSettings,
    private val spacingBuilder: SpacingBuilder
) {
    fun createBlock(node: ASTNode, wrap: Wrap? = null, alignment: Alignment? = null): PicatBlock {
        // Create a PicatBlock for the given node
    }

    fun createChildBlocks(node: ASTNode): List<Block> {
        // Create child blocks for the given parent node
    }
}
```

### 5. PicatBlock

The `PicatBlock` class is the core of the formatter. It extends IntelliJ's `AbstractBlock` class and is responsible for determining indentation, spacing, and child block creation.

```kotlin
class PicatBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val settings: CodeStyleSettings,
    private val spacingBuilder: SpacingBuilder? = null,
    private val blockFactory: PicatBlockFactory? = null
) : AbstractBlock(node, wrap, alignment) {
    override fun buildChildren(): List<Block> {
        // Build child blocks
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        // Determine spacing between child blocks
    }

    override fun getIndent(): Indent? {
        // Determine indentation for this block
    }

    override fun getChildIndent(): Indent? {
        // Determine indentation for child blocks
    }
}
```

### 6. PicatFormattingUtils

The `PicatFormattingUtils` class provides utility functions for formatting, such as checking if a node is an operator, finding siblings, and determining if a node is inside a specific construct.

```kotlin
object PicatFormattingUtils {
    fun isOperator(elementType: IElementType): Boolean {
        // Check if the element type is an operator
    }

    fun findPreviousNonWhitespaceSibling(node: ASTNode): ASTNode? {
        // Find the previous non-whitespace sibling
    }

    fun isInsideListComprehension(node: ASTNode): Boolean {
        // Check if the node is inside a list comprehension
    }

    // Other utility methods
}
```

### 7. PicatLanguageCodeStyleSettingsProvider

The `PicatLanguageCodeStyleSettingsProvider` class provides code style settings to the IDE. It defines which settings are available for Picat and provides a code sample for the settings preview.

```kotlin
class PicatLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage(): Language {
        return PicatLanguage
    }

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        // Define which settings to show in the UI
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        // Provide a code sample for the settings preview
    }
}
```

## Formatting Process

The formatting process follows these steps:

1. The IDE requests a formatting model for a Picat file by calling `PicatFormattingModelBuilder.createModel()`.
2. The `PicatFormattingModelBuilder` creates a `PicatSpacingBuilder` and a `PicatBlockFactory`.
3. The `PicatBlockFactory` creates a root `PicatBlock` for the file's AST.
4. The root `PicatBlock` builds its child blocks recursively, creating a tree of blocks that mirrors the AST.
5. The IDE uses this block tree to format the code, applying indentation and spacing rules.

## Indentation Logic

The indentation logic is primarily implemented in the `PicatBlock.shouldIndentChildren()` and `PicatBlock.getChildIndent()` methods. The `shouldIndentChildren()` method determines if a block should indent its children, based on the block's type and content.

Key indentation rules include:

- Indent children after rule operators (`=>` and `?=>`)
- Indent children after block keywords (`then`, `else`)
- Indent children in block structures (if-then-else, foreach, while, etc.)
- Indent children in rule bodies, predicate bodies, and function bodies
- Special indentation for list comprehensions and constraint expressions

## Spacing Logic

The spacing logic is implemented in the `PicatSpacingBuilder` class and the `PicatBlock.getSpacing()` method. The `PicatSpacingBuilder` defines general spacing rules based on code style settings, while the `PicatBlock.getSpacing()` method handles special cases.

Key spacing rules include:

- Spacing around operators (assignment, arithmetic, logical, etc.)
- Spacing around Picat-specific operators (rule, constraint, term comparison, etc.)
- Spacing around punctuation (commas, colons, etc.)
- Spacing within parentheses, brackets, and braces
- Line breaks after rule operators, block keywords, and dots

## Extending the Formatter

To extend the formatter with new features:

1. **Add new code style settings**: Add new properties to `PicatCodeStyleSettings` and expose them in `PicatLanguageCodeStyleSettingsProvider`.

2. **Add new spacing rules**: Add new rules to `PicatSpacingBuilder` or enhance the `PicatBlock.getSpacing()` method.

3. **Improve indentation logic**: Enhance the `PicatBlock.shouldIndentChildren()` and `PicatBlock.getChildIndent()` methods.

4. **Add utility methods**: Add new utility methods to `PicatFormattingUtils` for handling specific formatting requirements.

5. **Add tests**: Add new test cases to `PicatFormattingTest` to verify the new functionality.

## Testing

The formatter is tested using the `PicatFormattingTest` class, which extends IntelliJ's `BasePlatformTestCase`. The tests verify that the formatter correctly formats various Picat code constructs according to the code style settings.

To add a new test:

1. Create a test method in `PicatFormattingTest`.
2. Define the input code and expected formatted output.
3. Call the `doFormatTest()` method to verify that the formatter produces the expected output.

```kotlin
@Test
fun testNewFeature() {
    val code = """
        // Input code
    """.trimIndent()

    val expected = """
        // Expected formatted output
    """.trimIndent()

    doFormatTest(code, expected)
}
```

## Debugging

To debug the formatter:

1. **Enable logging**: Add logging statements to the formatter classes to track the formatting process.

2. **Use the `FormattingModelDumper`**: Use the `FormattingModelDumper.dumpFormattingModelToString()` method to dump the formatting model to a string for inspection.

3. **Use the `testSimpleFormatting()` test**: This test prints the original and formatted code, which can be useful for debugging.

4. **Set breakpoints**: Set breakpoints in the formatter classes and run the tests in debug mode to step through the code.

## Common Issues and Solutions

### Incorrect Indentation

If code is not indented correctly:

- Check the `shouldIndentChildren()` method to ensure it correctly identifies blocks that should indent their children.
- Check the `getChildIndent()` method to ensure it returns the correct indent type.
- Verify that the AST structure is as expected.

### Incorrect Spacing

If spacing is not applied correctly:

- Check the spacing rules in `PicatSpacingBuilder`.
- Check the `getSpacing()` method in `PicatBlock` for special cases.
- Verify that the code style settings are correctly applied.

### Formatting Not Applied

If formatting is not applied to certain code constructs:

- Check that the AST nodes for those constructs are correctly identified.
- Verify that the formatter is registered for the Picat language.
- Check for any exceptions during the formatting process.

## Conclusion

The Picat code formatter is a complex system that relies on IntelliJ's formatting framework. By understanding its architecture and components, you can extend and improve it to better support the Picat language and provide a better user experience.

For more information about IntelliJ's formatting framework, see the [IntelliJ Platform SDK Documentation](https://plugins.jetbrains.com/docs/intellij/code-formatting.html).
