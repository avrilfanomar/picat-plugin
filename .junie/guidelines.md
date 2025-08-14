# Development Guidelines

## Overview

This document defines the standards for contributing to and maintaining this project.  
All code, tests, and documentation must meet these guidelines.

## Code Style

- Follow consistent **formatting, naming conventions, and code organization**.
- Write **clear, purposeful comments**—explain *why*, not *what*.
- Maintain up-to-date **documentation for all public APIs** and non-trivial logic.
- Remove all `[DEBUG_LOG]` instances from the implementation before finalizing a task (may remain in tests).
- Keep code **readable and maintainable**—prefer clarity over cleverness.

## Testing

- Provide **unit tests** for all new functionality and bug fixes.
- Avoid mocking; use **fixtures and stubs** where appropriate.
- Ensure **all tests pass** before submitting a change.

## Documentation

- Update **documentation alongside code changes**.
- Provide **examples** when documenting non-obvious behavior.
- Keep `README.md` and `plugin.xml` accurate and up to date.

## Static Code Analysis (Detekt)

- The project uses **Detekt** for static code analysis and style enforcement.
- Rules are configured in `config/detekt.yml`.
- Run `./gradlew detekt` before submitting changes.
- Reports are generated in `build/reports/detekt/`.
- **All Detekt issues must be resolved** during task finalization.
