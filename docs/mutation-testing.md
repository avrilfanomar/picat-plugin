# Mutation Testing with Pitest

## Overview

This project uses [Pitest](https://pitest.org/) for mutation testing. Mutation testing is a type of software testing where specific statements in the source code are changed (mutated) to verify the quality of the test suite. If the tests fail when the code is mutated, it indicates that the tests are effective at catching errors.

## Why Mutation Testing?

Traditional code coverage metrics only tell you which lines of code are executed by your tests, not whether your tests are actually verifying the correct behavior. Mutation testing addresses this by:

1. Creating modified versions (mutants) of your code
2. Running your tests against these mutants
3. If your tests fail, the mutant is "killed" (good)
4. If your tests pass, the mutant "survives" (bad - indicates a weakness in your tests)

## Running Mutation Tests

To run mutation tests in this project, use the following Gradle command:

```bash
./gradlew runMutationTests
```

This will:
1. Run your unit tests to ensure they pass
2. Generate mutants of your code
3. Run your tests against each mutant
4. Generate a report showing which mutants were killed and which survived

## Understanding the Reports

After running mutation tests, reports can be found in `build/reports/pitest`. The HTML report provides a detailed view of:

- Mutation coverage percentage
- Line coverage percentage
- Classes that were tested
- Mutations that were applied
- Which mutations survived (indicating test weaknesses)

## Interpreting Results

- **High mutation score (>80%)**: Your tests are effective at catching errors
- **Low mutation score (<50%)**: Your tests may not be verifying the correct behavior
- **Surviving mutants**: These indicate specific areas where your tests could be improved

## Configuration

Pitest is configured in the `build.gradle.kts` file. The current configuration:

- Targets all classes in the `com.github.avrilfanomar.picatplugin` package
- Excludes generated files and UI-related classes that are hard to test
- Uses both default and stronger mutation operators
- Generates HTML and XML reports
- Uses JUnit 5 for testing

You can modify these settings in the `pitest` block in the build file.

## Tips for Improving Mutation Score

1. Focus on surviving mutants first
2. Write tests that specifically verify the behavior that was mutated
3. Use assertions that check exact values, not just null/non-null
4. Test edge cases and boundary conditions
5. Consider using property-based testing for complex logic

## Further Reading

- [Pitest Documentation](https://pitest.org/quickstart/)
- [Mutation Testing: Complete Guide](https://www.guru99.com/mutation-testing.html)
- [How to Interpret Mutation Test Results](https://blog.codecentric.de/en/2016/01/mutation-testing-how-to-interpret-mutation-test-results/)