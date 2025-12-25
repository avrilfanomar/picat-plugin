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

## IDE Integration with MCP Tools

This project has MCP (Model Context Protocol) servers enabled that provide IntelliJ IDEA-specific tools for working with the codebase. **Always prefer MCP tools over command-line equivalents** when available.

### Available MCP Tool Categories

**IntelliJ MCP Server** (`mcp__Intellij_mcp__*`):
- File operations: `get_file_text_by_path`, `create_new_file`, `replace_text_in_file`
- File search: `find_files_by_name_keyword`, `find_files_by_glob`, `search_in_files_by_text`, `search_in_files_by_regex`
- Project structure: `get_project_modules`, `get_project_dependencies`, `get_all_open_file_paths`, `list_directory_tree`
- Code intelligence: `get_symbol_info`, `get_file_problems`, `rename_refactoring`, `reformat_file`
- Execution: `execute_run_configuration`, `get_run_configurations`, `execute_terminal_command`

**PSI MCP Server** (`mcp__jetbrain-psi-mcp-server__*`):
- Symbol navigation: `find-symbol`, `find-definition`, `find-usages`
- Code structure: `get-references`, `get-inheritors`, `get-supers`, `get-containing-context`
- Module analysis: `get-module-info`, `dump-all-module-info`, `fuzzy-search-module`

**IDE MCP Server** (`mcp__ide__*`):
- Diagnostics: `getDiagnostics`

### When to Use MCP Tools

**ALWAYS use MCP tools for:**
1. **Reading files**: `get_file_text_by_path` instead of `cat`, `head`, `tail`
2. **Searching code**: `search_in_files_by_text` or `search_in_files_by_regex` instead of `grep`, `rg`, `find`
3. **Finding files**: `find_files_by_name_keyword` or `find_files_by_glob` instead of `find`, `ls -R`
4. **File editing**: `replace_text_in_file` for targeted changes
5. **Code navigation**: `find-symbol`, `find-definition`, `find-usages` for understanding code relationships
6. **Getting diagnostics**: `getDiagnostics` and `get_file_problems` for analyzing errors/warnings
7. **Refactoring**: `rename_refactoring` for safe symbol renaming across the codebase
8. **Running tests**: `execute_run_configuration` for test execution
9. **Project structure**: `list_directory_tree` for exploring directory contents
10. **Symbol information**: `get_symbol_info` for understanding code at specific positions

**Use command-line tools for:**
- Git operations: `git status`, `git diff`, `git log`, `git commit`
- Gradle builds: `./gradlew build`, `./gradlew test`, `./gradlew detekt`
- Grammar generation: `./gradlew generateParser`, `./gradlew generateLexer`
- System operations that don't involve reading/modifying source code

### Examples

**Reading a file:**
```
✅ PREFERRED: Use get_file_text_by_path with pathInProject="src/main/kotlin/MyFile.kt"
❌ AVOID: cat src/main/kotlin/MyFile.kt
```

**Finding files:**
```
✅ PREFERRED: Use find_files_by_name_keyword with nameKeyword="Parser"
❌ AVOID: find . -name "*Parser*"
```

**Searching code:**
```
✅ PREFERRED: Use search_in_files_by_text with searchText="PicatAtom"
❌ AVOID: grep -r "PicatAtom" src/
```

**Finding symbol definitions:**
```
✅ PREFERRED: Use find-symbol with symbol_name="PicatAtom"
❌ AVOID: grep -r "class PicatAtom" or manual file searches
```

**Getting diagnostics:**
```
✅ PREFERRED: Use get_file_problems with filePath="src/main/kotlin/MyFile.kt"
❌ AVOID: ./gradlew build (for just checking errors in one file)
```

**Renaming symbols:**
```
✅ PREFERRED: Use rename_refactoring to safely rename across all usages
❌ AVOID: Manual find-replace operations that might miss references
```

### Benefits of MCP Tools

1. **Faster**: MCP tools use IntelliJ's indexes and don't require spawning shell processes
2. **More accurate**: Leverage IDE's semantic understanding of code structure
3. **Project-aware**: Automatically respect project boundaries and .gitignore rules
4. **Type-safe**: Work with PSI (Program Structure Interface) for precise code operations
5. **Safe refactoring**: Tools like `rename_refactoring` update all references correctly
6. **Better diagnostics**: Get real-time IDE analysis instead of waiting for full builds

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

1. **Prefer MCP tools over command-line**: Always use IntelliJ MCP tools for file operations, code search, and navigation (see "IDE Integration with MCP Tools" section)
2. **Never modify generated code**: All changes to parsing must go through grammar files (`Picat.bnf` or `_PicatLexer.flex`)
3. **Always regenerate after grammar changes**: Use `./gradlew generateParser generateLexer` and commit results
4. **Detekt is mandatory**: All issues must be resolved before finalizing tasks
5. **Test coverage matters**: Write tests for new grammar rules and language features
6. **Error recovery**: Add proper error recovery in grammar for robust parsing with malformed code
7. **Cache expensive operations**: Use `PicatPsiCache` for repeated PSI operations
8. **Safe PSI traversal**: Use `PsiTreeUtil` methods for safe navigation

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
