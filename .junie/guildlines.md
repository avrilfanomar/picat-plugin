# Development Guidelines

## Overview
This document contains guidelines for contributing to and maintaining this project.

## Code Style
- Follow consistent formatting and naming conventions
- Write clear and concise comments
- Maintain proper documentation

## Testing
- Write unit tests for new functionality
- Don't use mocking, only fixtures and stubs where needed
- Ensure all tests pass before submitting changes
- Test edge cases and error conditions

## Version Control
- Use meaningful commit messages
- Keep commits focused and atomic
- Follow branching strategy guidelines

## Documentation
- Update documentation when making changes
- Include examples where appropriate
- Keep README and other docs up to date

## Code Coverage with Kover
- Kover is configured to generate XML coverage reports automatically
- Coverage reports are generated on check and can be found in `build/reports/kover/`
- Aim for meaningful test coverage, focusing on critical business logic
- Use `./gradlew koverHtmlReport` to generate human-readable HTML coverage reports
- Use `./gradlew koverXmlReport` to generate XML reports for CI/CD integration
- Coverage reports help identify untested code paths and improve test quality
- Review coverage reports before submitting changes to ensure adequate testing

## Static Code Analysis with Detekt
- Detekt is configured for static code analysis to maintain code quality
- Configuration is defined in `config/detekt.yml` with project-specific rules
- Baseline file at `config/baseline.xml` suppresses existing issues when introducing Detekt
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

## Changelog Management
- Follow the "Keep a Changelog" format (https://keepachangelog.com)
- Update CHANGELOG.md for all user-facing changes
- Use semantic versioning for releases (MAJOR.MINOR.PATCH)
- Organize changes under appropriate categories:
  - **Added** for new features
  - **Changed** for changes in existing functionality
  - **Deprecated** for soon-to-be removed features
  - **Removed** for now removed features
  - **Fixed** for any bug fixes
  - **Security** for vulnerability fixes
- Add entries under the `[Unreleased]` section during development
- Move entries to versioned sections when releasing
- Include dates in the format `YYYY-MM-DD` for released versions
- Write clear, concise descriptions that help users understand the impact

## Review Process
- All changes should be reviewed before merging
- Address feedback promptly and thoroughly
- Ensure code quality standards are met
