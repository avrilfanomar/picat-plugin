# Development Guidelines

## Overview
This document contains guidelines for contributing to and maintaining this project.

## Code Style
- Follow consistent formatting and naming conventions
- Write clear and concise comments, avoid commenting what has been changed
- Maintain proper documentation
- Remove any `[DEBUG_LOG]`/etc from the implementation (it can be kept in tests) during finalization 

## Testing
- Write unit tests for new functionality
- Don't use mocking, only fixtures and stubs where needed
- Ensure all related tests pass

## Documentation
- Update documentation when making changes
- Include examples where appropriate
- Keep README and plugin.xml up to date

## Static Code Analysis with Detekt
- Detekt is configured for static code analysis to maintain code quality
- Configuration is defined in `config/detekt.yml` with project-specific rules
- Run `./gradlew detekt` to perform static analysis on the codebase
- Detekt reports can be found in `build/reports/detekt/`
- Address Detekt findings before submitting changes to maintain code quality
- Common issues detected include:
  - Code complexity and maintainability problems
  - Potential bugs and code smells
  - Style violations and formatting inconsistencies
  - Performance anti-patterns
- Use `./gradlew detektBaseline` to update the baseline when legitimate issues are resolved
- Integrate Detekt checks into your IDE for real-time feedback during development
- Ensure code quality standards are met
