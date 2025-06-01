# Picat Code Formatter User Guide

## Introduction

The Picat plugin for IntelliJ IDEA includes a powerful code formatter that helps you maintain consistent code style in your Picat projects. This guide explains how to use the formatter and customize its settings to match your preferred coding style.

## Using the Formatter

### Format Current File

To format the current file:

1. Open a Picat file (`.pi` extension)
2. Press **Ctrl+Alt+L** (Windows/Linux) or **Cmd+Alt+L** (Mac)
3. The entire file will be formatted according to your code style settings

### Format Selection

To format only a selected portion of code:

1. Select the code you want to format
2. Press **Ctrl+Alt+L** (Windows/Linux) or **Cmd+Alt+L** (Mac)
3. Only the selected code will be formatted

### Format on Save

You can configure the IDE to automatically format your code when you save a file:

1. Go to **Settings/Preferences > Tools > Actions on Save**
2. Check the **Reformat code** option
3. Optionally, select **All file types** or **Only these file types** and include `.pi` in the list

## Customizing Formatter Settings

### Accessing Code Style Settings

1. Go to **Settings/Preferences > Editor > Code Style > Picat**
2. You'll see several tabs for different aspects of code style:
   - **Tabs and Indents**
   - **Spaces**
   - **Wrapping and Braces**
   - **Blank Lines**
   - **Other**

### Common Settings

#### Indentation

In the **Tabs and Indents** tab:

- **Use tab character**: Whether to use tabs or spaces for indentation
- **Tab size**: The size of a tab character
- **Indent**: The size of each indentation level
- **Continuation indent**: The size of indentation for continuation lines

#### Spacing

In the **Spaces** tab, you can configure spacing around:

- **Operators**: Assignment, logical, equality, relational, additive, multiplicative
- **Picat-specific operators**: Rule operators, constraint operators, term comparison operators
- **Punctuation**: Commas, colons
- **Parentheses, brackets, and braces**
- **Keywords**

#### Line Breaks and Wrapping

In the **Wrapping and Braces** tab:

- **Line breaks after rule operators**
- **Line breaks after block keywords**
- **Line breaks after dots**
- **Keep line breaks**
- **Wrap long lines**
- **Maximum line length**

#### Alignment

Also in the **Wrapping and Braces** tab:

- **Align multiline parameters**
- **Align multiline arguments**
- **Align consecutive assignments**
- **Align list elements**

### Examples of Settings Impact

#### Spacing Around Operators

With spacing around operators enabled:
```picat
X = 10 + 20 * 30
```

With spacing around operators disabled:
```picat
X=10+20*30
```

#### Line Breaks After Rule Operators

With line breaks after rule operators enabled:
```picat
factorial(N) = N * factorial(N - 1) =>
    N > 0.
```

With line breaks after rule operators disabled:
```picat
factorial(N) = N * factorial(N - 1) => N > 0.
```

#### Indentation in Control Structures

With proper indentation:
```picat
if X > 10 then
    println("X is greater than 10")
else
    println("X is not greater than 10")
end
```

## Sharing Code Style Settings

### Exporting Settings

To export your code style settings:

1. Go to **Settings/Preferences > Editor > Code Style**
2. Click the gear icon next to the scheme selector
3. Select **Export > IntelliJ IDEA code style XML**
4. Choose a location to save the settings file

### Importing Settings

To import code style settings:

1. Go to **Settings/Preferences > Editor > Code Style**
2. Click the gear icon next to the scheme selector
3. Select **Import Scheme > IntelliJ IDEA code style XML**
4. Select the settings file to import

## Troubleshooting

### Formatter Not Working

If the formatter doesn't seem to be working:

1. Make sure the file has a `.pi` extension
2. Check that the Picat plugin is enabled in **Settings/Preferences > Plugins**
3. Try restarting the IDE

### Unexpected Formatting

If the code is not formatted as expected:

1. Check your code style settings
2. Make sure there are no syntax errors in your code
3. Try formatting a smaller section of code to isolate the issue

### Reporting Issues

If you encounter bugs or have suggestions for improving the formatter:

1. Check the [issue tracker](https://github.com/avrilfanomar/picat-plugin/issues) to see if the issue has already been reported
2. If not, create a new issue with:
   - A description of the problem
   - A code sample that demonstrates the issue
   - Your expected formatting result
   - Your actual formatting result

## Conclusion

The Picat code formatter is a powerful tool that can help you maintain consistent code style across your projects. By customizing the formatter settings to match your preferences, you can ensure that your code is always formatted exactly the way you want it.

For more detailed information about the formatter's implementation and capabilities, see the [Formatter Documentation](formatter.md).