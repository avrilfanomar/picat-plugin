# Picat Code Formatter Documentation

This document provides comprehensive documentation for the Picat code formatter included in the Picat IntelliJ plugin.

## Overview

The Picat code formatter automatically formats Picat code according to configurable code style settings. It handles all Picat-specific language constructs, including rule definitions, constraint expressions, term comparison operators, list comprehensions, and control structures.

## Code Style Settings

The formatter provides a wide range of configurable settings that can be accessed through the IDE's settings dialog (File > Settings > Editor > Code Style > Picat).

### Spacing

#### Operators

- **Assignment Operators**: Controls spacing around assignment operators (`=`, `:=`).
- **Logical Operators**: Controls spacing around logical operators (`&&`, `||`, `!`).
- **Equality Operators**: Controls spacing around equality operators (`==`, `!=`, `!==`, `===`).
- **Relational Operators**: Controls spacing around relational operators (`<`, `<=`, `=<`, `>`, `>=`).
- **Additive Operators**: Controls spacing around additive operators (`+`, `-`, `++`).
- **Multiplicative Operators**: Controls spacing around multiplicative operators (`*`, `/`, `//`, `**`, `mod`).

#### Picat-specific Operators

- **Rule Operators**: Controls spacing around rule operators (`=>`, `?=>`).
- **Constraint Operators**: Controls spacing around constraint operators (`#=`, `#!=`, etc.).
- **Term Comparison Operators**: Controls spacing around term comparison operators (`@<`, `@=<`, etc.).
- **Range Operator**: Controls spacing around the range operator (`..`).
- **Type Constraint Operator**: Controls spacing around the type constraint operator (`::`).
- **Bitwise Operators**: Controls spacing around bitwise operators (`/\`, `\/`, `<<`, `>>`).

#### Punctuation

- **Before Comma**: Controls spacing before commas.
- **After Comma**: Controls spacing after commas.
- **Around Colon**: Controls spacing around colons.

#### Parentheses, Brackets, and Braces

- **Within Parentheses**: Controls spacing within parentheses.
- **Within Brackets**: Controls spacing within brackets.
- **Within Braces**: Controls spacing within braces.

#### Keywords

- **After Control Keywords**: Controls spacing after control keywords (if, foreach, etc.).

### Line Breaks

- **Keep Line Breaks**: Controls whether existing line breaks should be preserved.
- **Keep Blank Lines in Code**: Controls how many consecutive blank lines to keep.
- **Line Break After Rule Operators**: Controls whether a line break should be inserted after rule operators.
- **Line Break After Block Keywords**: Controls whether a line break should be inserted after block keywords.
- **Line Break After Dot**: Controls whether a line break should be inserted after a dot.
- **Line Break After End Keyword**: Controls whether a line break should be inserted after the end keyword.

### Alignment

- **Align Multiline Parameters**: Controls whether parameters in multiline parameter lists should be aligned.
- **Align Multiline Arguments**: Controls whether arguments in multiline argument lists should be aligned.
- **Align Consecutive Assignments**: Controls whether consecutive assignments should be aligned.
- **Align List Elements**: Controls whether elements in lists should be aligned.

### Wrapping

- **Wrap Long Lines**: Controls whether long lines should be wrapped.
- **Maximum Line Length**: Controls the maximum line length before wrapping.
- **Prefer Parameters Wrap**: Controls whether parameters should be wrapped when a line is too long.

### Special Formatting

- **Special Else-If Treatment**: Controls whether else-if statements should receive special formatting.
- **Indent Case From Switch**: Controls whether case statements should be indented from switch statements.
- **Special List Comprehension Formatting**: Controls whether list comprehensions should receive special formatting.

## Examples

### Rule Definitions

```picat
% Regular rule with => operator
factorial(0) = 1.
factorial(N) = N * factorial(N-1) => N > 0.

% Backtrackable rule with ?=> operator
solve_sudoku ?=>
    Sudoku = new_array(9, 9),
    Sudoku :: 1..9,
    % ... more code ...
    solve(Sudoku),
    print_sudoku(Sudoku).

solve_sudoku => println("No solution found").
```

### Constraint Expressions

```picat
% Constraint operators example
foreach(I in 1..9)
    all_different([Sudoku[I,J] : J in 1..9]),
    all_different([Sudoku[J,I] : J in 1..9])
end,

% Type constraint example
Sudoku :: 1..9,
```

### Term Comparison Operators

```picat
% Term comparison operators example
foreach(I in 1..9, J in 1..9)
    if Sudoku[I,J] @> 5 then
        println("Value at " ++ I ++ "," ++ J ++ " is greater than 5")
    end
end,
```

### List Comprehensions

```picat
% List comprehension example
List = [X : X in 1..10, X mod 2 == 0],
```

### Control Structures

```picat
% If-then-else example
if Z > 25 then
    println("Z is greater than 25")
else
    println("Z is not greater than 25")
end,

% Foreach example
foreach(I in 1..5)
    println(Arr[I])
end,

% While example
while(I < 10)
    println(I),
    I := I + 1
end,

% Try-catch example
try
    X = 10 / 0
catch
    println("Division by zero")
end,
```

### Bitwise Operators

```picat
% Bitwise operators example
Bits1 = 0b1010,
Bits2 = 0b1100,
BitwiseAnd = Bits1 /\ Bits2,
BitwiseOr = Bits1 \/ Bits2,
ShiftLeft = Bits1 << 2,
ShiftRight = Bits1 >> 1,
```

### Multi-line Parameters

```picat
% Multi-line parameters example
complex_function(
    Parameter1,
    Parameter2,
    Parameter3
),
```

### Consecutive Assignments

```picat
% Example of consecutive assignments
initialize =>
    X1 = 1,
    LongVariableName = 2,
    Y = 300,
    VeryLongVariableName = 4000.
```

## Troubleshooting

If you encounter any issues with the formatter, try the following:

1. Make sure your code is syntactically correct. The formatter may not work correctly with code that contains syntax errors.
2. Try adjusting the code style settings to better match your preferences.
3. If the formatter is not formatting a specific construct correctly, check if there's a setting that controls that construct.
4. If you find a bug or have a feature request, please report it on the plugin's issue tracker.

## Contributing

If you'd like to contribute to the Picat code formatter, please see the [contributing guidelines](CONTRIBUTING.md).