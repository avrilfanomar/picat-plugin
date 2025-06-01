# Picat Code Formatter Documentation

## Overview

The Picat code formatter is a powerful tool that automatically formats Picat code according to configurable style settings. It helps maintain consistent code style across your Picat projects, making the code more readable and maintainable.

## Features

The formatter supports the following features:

- Proper indentation of code blocks
- Configurable spacing around operators and punctuation
- Line break management for rule definitions, block statements, and more
- Special handling for Picat-specific constructs like rule operators, constraint expressions, and list comprehensions
- Customizable code style settings

## Code Style Settings

The formatter provides a wide range of configurable settings that can be adjusted in the IDE settings under **Editor > Code Style > Picat**.

### Spacing

#### Operators
- **Assignment operators (=, :=)**: Controls spacing around assignment operators
- **Logical operators (&&, ||, !)**: Controls spacing around logical operators
- **Equality operators (==, !=, ===, !==)**: Controls spacing around equality operators
- **Relational operators (<, >, <=, >=)**: Controls spacing around relational operators
- **Additive operators (+, -, ++)**: Controls spacing around additive operators
- **Multiplicative operators (*, /, //, mod)**: Controls spacing around multiplicative operators

#### Picat-specific Operators
- **Rule operators (=>, ?=>)**: Controls spacing around rule operators
- **Constraint operators (#=, #!=, etc.)**: Controls spacing around constraint operators
- **Term comparison operators (@<, @=<, etc.)**: Controls spacing around term comparison operators
- **Range operator (..)**: Controls spacing around the range operator
- **Type constraint operator (::)**: Controls spacing around the type constraint operator
- **Bitwise operators (/\, \/, <<, >>)**: Controls spacing around bitwise operators

#### Punctuation
- **Before comma**: Controls spacing before commas
- **After comma**: Controls spacing after commas
- **Around colon**: Controls spacing around colons

#### Parentheses, Brackets, and Braces
- **Within parentheses**: Controls spacing within parentheses
- **Within brackets**: Controls spacing within brackets
- **Within braces**: Controls spacing within braces

#### Keywords
- **After control keywords (if, foreach, etc.)**: Controls spacing after control keywords

### Line Breaks

- **Line break after rule operators (=> and ?=>)**: Controls whether a line break is inserted after rule operators
- **Line break after block keywords (then, else)**: Controls whether a line break is inserted after block keywords
- **Line break after dot**: Controls whether a line break is inserted after a dot
- **Line break after end keyword**: Controls whether a line break is inserted after the end keyword
- **Keep line breaks**: Controls whether existing line breaks are preserved
- **Keep blank lines in code**: Controls how many consecutive blank lines are preserved in code

### Alignment

- **Align multiline parameters**: Controls whether parameters in multiline parameter lists are aligned
- **Align multiline arguments**: Controls whether arguments in multiline argument lists are aligned
- **Align consecutive assignments**: Controls whether consecutive assignment statements are aligned
- **Align list elements**: Controls whether elements in multiline lists are aligned

### Wrapping

- **Wrap long lines**: Controls whether long lines are automatically wrapped
- **Maximum line length**: Controls the maximum allowed line length before wrapping
- **Prefer parameters wrap**: Controls whether parameters should be wrapped when possible

### Special Formatting

- **Special else-if treatment**: Controls special formatting for else-if constructs
- **Indent case from switch**: Controls indentation of case statements within switch blocks
- **Special list comprehension formatting**: Controls special formatting for list comprehensions

## Usage

The formatter can be used in several ways:

1. **Automatic formatting on save**: If enabled in the IDE settings, the code will be automatically formatted when you save a file.

2. **Manual formatting**:
   - To format the entire file: Press **Ctrl+Alt+L** (Windows/Linux) or **Cmd+Alt+L** (Mac)
   - To format a selection: Select the code and press **Ctrl+Alt+L** (Windows/Linux) or **Cmd+Alt+L** (Mac)

3. **Reformatting code**: You can also use the **Code > Reformat Code** menu option to format your code.

## Examples

### Rule Definitions

```picat
% Before formatting
factorial(0)=1.
factorial(N)=N*factorial(N-1)=>N>0.

% After formatting
factorial(0) = 1.
factorial(N) = N * factorial(N - 1) => N > 0.
```

### Control Structures

```picat
% Before formatting
if X>10 then println("X is greater than 10") else println("X is not greater than 10") end.

% After formatting
if X > 10 then
    println("X is greater than 10")
else
    println("X is not greater than 10")
end.
```

### List Comprehensions

```picat
% Before formatting
L=[X:X in 1..10,X mod 2==0].

% After formatting
L = [X : X in 1..10, X mod 2 == 0].
```

### Constraint Expressions

```picat
% Before formatting
X#=Y+Z,X#>10.

% After formatting
X #= Y + Z, X #> 10.
```

## Troubleshooting

If you encounter issues with the formatter:

1. **Incorrect formatting**: Check your code style settings and adjust them according to your preferences.

2. **Formatting not applied**: Make sure the file is recognized as a Picat file (has the `.pi` extension).

3. **Errors during formatting**: If you encounter errors during formatting, please report them to the plugin developers.

## Contributing

If you'd like to contribute to the Picat formatter, please see the [CONTRIBUTING.md](../CONTRIBUTING.md) file for details.