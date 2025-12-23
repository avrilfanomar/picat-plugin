# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an IntelliJ IDEA plugin that provides comprehensive language support for the Picat programming language (http://picat-lang.org/). Picat is a multi-paradigm language integrating logic-based and function-based programming with constraint solving capabilities.

**Key Technologies:**
- Kotlin for plugin implementation
- GrammarKit/JFlex for parser/lexer generation
- IntelliJ Platform SDK 2025.3
- JDK 21
- Gradle 9.0

## Essential Build Commands

### Development Workflow
```bash
# Full build with tests
./gradlew build

# Run tests only
./gradlew test

# Static analysis (must pass before submitting changes)
./gradlew detekt

# Run IDE with plugin loaded
./gradlew runIde

# Build plugin distribution
./gradlew buildPlugin

# Verify plugin structure and compatibility
./gradlew verifyPlugin
```

### Grammar Regeneration
```bash
# Regenerate parser from Picat.bnf
./gradlew generateParser

# Regenerate lexer from _PicatLexer.flex
./gradlew generateLexer

# Both regenerations (included in build by default)
./gradlew generateParser generateLexer
```

**Critical Rule:** Never manually modify files in `src/main/gen/`. All parser/PSI changes must be made in `src/main/grammars/Picat.bnf` (`src/main/grammars/_PicatLexer.flex` is being manually generated from `Picat.bnf`, you should prompt the used to do so if needed), then regenerated. Always commit regenerated sources when modifying grammar.

## Architecture Overview

### Core Language Implementation (`language/` package)

**Grammar & PSI Pipeline:**
1. `Picat.bnf` (BNF grammar) → generates `PicatParser.java` and PSI element classes
2. `_PicatLexer.flex` (JFlex rules) → generates `_PicatLexer.java`
3. Custom PSI mixins in `language/psi/impl/` add behavior to generated PSI
4. `PicatPsiUtil.kt` provides core utilities for PSI traversal and name/arity extraction

**Major Subsystems:**
- **Highlighting** (`language/highlighting/`): Token-based syntax highlighting + semantic annotator
- **Formatting** (`language/formatter/`): 13-component formatting engine with customizable spacing, indentation, and wrapping rules
- **References** (`language/references/`): Multi-variant reference resolution by name/arity, supports local symbols, imports, and stdlib
- **Completion** (`language/completion/`): Context-aware completion with arity hints for predicates/functions
- **Navigation** (`language/navigation/`): Symbol indexing, structure view, breadcrumb navigation
- **Documentation** (`language/documentation/`): Quick documentation provider (Ctrl+Q)

### Reference Resolution Architecture

The plugin uses a sophisticated multi-stage resolution strategy (`PicatReference.kt`):

1. **Extract context**: Identifier text and arity from usage site
2. **Search local scope**: Find matching heads in current file via `PsiTreeUtil.findChildrenOfType`
3. **Resolve imports**: Collect heads from explicitly imported modules
4. **Search stdlib**: Load stdlib modules from `/lib` directory next to Picat executable
5. **Filter and rank**: Match by name and arity, return all matching targets

This enables "Go to Definition" (Ctrl+Click) and "Find Usages" to work across files and stdlib modules.

### Standard Library Integration (`stdlib/` package)

**`PicatStdlibUtil.kt`** locates the Picat `/lib` directory next to the configured executable and resolves stdlib module files (e.g., `basic.pi`, `io.pi`).

**`PicatStdlibLibraryProvider.kt`** registers the stdlib directory as a read-only library root, enabling navigation from import statements to stdlib source files.

### Run Configuration (`run/` package)

Provides "Run Picat File" functionality with:
- Configurable Picat executable path (stored in project settings)
- Program parameters and working directory
- Validation that executable exists and is readable
- Auto-detection of run configurations from editor context
- Gutter icons for running Picat files

### Plugin Extensions (27 registered)

All extensions are registered in `src/main/resources/META-INF/plugin.xml`:
- File type, syntax highlighter, parser definition
- Code formatter, completion contributor, live templates
- Reference contributor, find usages, refactoring support
- Structure view, navigation bar, symbol indexing
- Documentation provider, annotator, code folding
- Run configuration type, line markers
- Stdlib library provider

## PSI Utilities and Conventions

**Key utilities in `PicatPsiUtil.kt`:**
- `getHeadName(element)` - Extract predicate/function name from rule head
- `getHeadArity(element)` - Get number of arguments
- `getStableSignature(element)` - Generate name/arity signature (e.g., "pred/2")
- `getModuleQualifier(element)` - Extract module qualification
- `getNameIdentifier(element)` - Find name element for refactoring

**PSI navigation patterns:**
```kotlin
// Find parent rule
val rule = PsiTreeUtil.getParentOfType(element, PicatRule::class.java)

// Find all atoms in scope
val atoms = PsiTreeUtil.findChildrenOfType(element, PicatAtom::class.java)

// Get name identifier for refactoring
val nameId = element.nameIdentifier
```

## Testing Infrastructure

**Test organization:**
- 51 test files organized by functionality
- Parser tests: golden file approach comparing `.pi` files against expected PSI output
- Formatting tests: idempotence verification (format twice, must match)
- Reference tests: resolution accuracy
- Integration tests using IntelliJ Platform `BasePlatformTestCase`

**Run tests:**
```bash
./gradlew test                    # Run all tests
./gradlew test --tests "*Parser*" # Run specific test class
```

## Live Templates

Pre-defined code templates (type and press Tab):
- `if`, `ife`, `ifel` - if-then-else variants
- `foreach`, `while`, `loopw` - loop constructs
- `try`, `tryf` - exception handling

Template definitions: `src/main/resources/liveTemplates/Picat.xml`

## Code Quality Standards

From `.junie/guidelines.md`:

1. **Detekt must pass**: Run `./gradlew detekt` before submitting changes. Reports in `build/reports/detekt/`
2. **No debug logs**: Remove all `[DEBUG_LOG]` instances before finalizing (allowed in tests)
3. **Write tests**: Unit tests required for all new features and bug fixes
4. **Avoid mocking**: Use fixtures and stubs instead
5. **Document changes**: Update README.md and plugin.xml alongside code
6. **Comment purpose**: Explain *why*, not *what*
7. **Keep it simple**: Prefer clarity over cleverness

## Settings and Configuration

**Plugin settings** (`settings/` package):
- Picat executable path configurable in "Tools → Picat" settings panel
- Stored in IntelliJ project settings
- Accessed via `PicatSettings.getInstance(project)`

**Formatter settings:**
- File → Settings → Editor → Code Style → Picat
- Customizable spacing, indentation, wrapping rules
- Default keybinding: Ctrl+Alt+L (Cmd+Alt+L on Mac)

## Important Development Rules

1. **Never modify generated code**: All changes to parsing must go through grammar files (`Picat.bnf` or `_PicatLexer.flex`)
2. **Always regenerate after grammar changes**: Use `./gradlew generateParser generateLexer` and commit results
3. **Detekt is mandatory**: All issues must be resolved before finalizing tasks
4. **Test coverage matters**: Write tests for new grammar rules and language features
5. **Error recovery**: Add proper error recovery in grammar for robust parsing with malformed code
6. **Cache expensive operations**: Use `PicatPsiCache` for repeated PSI operations
7. **Safe PSI traversal**: Use `PsiTreeUtil` methods for safe navigation

## Plugin Metadata

- **Plugin ID**: `com.github.avrilfanomar.picatplugin`
- **File Extension**: `.pi`
- **Language Name**: Picat
- **License**: Apache License 2.0

## Key Files Reference

| File | Purpose | Critical For |
|------|---------|--------------|
| `src/main/grammars/Picat.bnf` | Parser grammar | All syntax changes |
| `src/main/kotlin/language/psi/PicatPsiUtil.kt` | PSI utilities | Navigation/refactoring |
| `src/main/kotlin/language/references/PicatReference.kt` | Reference resolution | Go to Definition |
| `src/main/resources/META-INF/plugin.xml` | Extension registration | All IDE features |
| `gradle.properties` | Plugin metadata | Version/compatibility |
| `build.gradle.kts` | Build configuration | Dependencies/tasks |
