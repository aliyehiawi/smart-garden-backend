# Static Code Analysis

This project uses automated static code analysis tools to maintain code quality and catch potential bugs early.

## Tools Used

### 1. Checkstyle

**Purpose:** Enforces Java coding standards and style consistency

**What it checks:**

- Naming conventions (classes, methods, variables)
- Code formatting (whitespace, indentation, line length)
- Import organization
- Code structure and complexity
- Common style violations

### 2. SpotBugs

**Purpose:** Detects potential bugs and code smells

**What it checks:**

- Null pointer dereferences
- Resource leaks
- Infinite loops
- Dead code
- Security vulnerabilities
- Equals/hashCode violations
- Incorrect API usage

## Installation

No installation required! The tools are configured as Gradle plugins and will be downloaded automatically on first run.

**Requirements:**

- Java 21
- Gradle wrapper (included in project)

## Running Static Analysis

### Run All Checks

Execute all static analysis tools (Checkstyle + SpotBugs):

```bash
./gradlew codeQuality
```

### Run Individual Tools

**Checkstyle only:**

```bash
./gradlew checkstyleMain checkstyleTest
```

**SpotBugs only:**

```bash
./gradlew spotbugsMain spotbugsTest
```

### Run as Part of Build

Static analysis runs automatically with the standard build:

```bash
./gradlew check
```

## Viewing Reports

After running analysis, HTML reports are generated:

### Checkstyle Reports

- Main code: `build/reports/checkstyle/main.html`
- Test code: `build/reports/checkstyle/test.html`

### SpotBugs Reports

- Main code: `build/reports/spotbugs/main.html`
- Test code: `build/reports/spotbugs/test.html`

## Understanding Results

### Checkstyle Violations

Checkstyle reports show:

- File and line number
- Violation description
- Severity (warning/error)

**Example:**

```
Line 42: Method name 'DoSomething' must match pattern '^[a-z][a-zA-Z0-9]*$'
```

**Fix:** Rename method to follow camelCase convention: `doSomething`

### SpotBugs Bugs

SpotBugs reports show:

- Bug pattern name
- Priority (High/Medium/Low)
- Description and explanation
- Location in code

**Example:**

```
Possible null pointer dereference in UserService.findByUsername()
```

**Fix:** Add null check before dereferencing the variable

## Configuration

### Checkstyle Configuration

Located at: `config/checkstyle/checkstyle.xml`

**Key rules:**

- Maximum line length: 150 characters
- Maximum method length: 150 lines
- Maximum parameters: 7
- Maximum cyclomatic complexity: 10
- Enforces naming conventions
- Requires braces on all blocks

### SpotBugs Configuration

Configured in: `build.gradle`

**Settings:**

- Effort level: Maximum
- Report level: Medium and High priority bugs
- Fails build on violations

## Suppressing Violations

### Checkstyle Suppression

Use `@SuppressWarnings` annotation:

```java
@SuppressWarnings("checkstyle:MethodName")
public void Some_Legacy_Method() {
    // ...
}
```

### SpotBugs Suppression

Use `@SuppressFBWarnings` annotation:

```java
@SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
public void someMethod() {
    // ...
}
```

**Note:** Only suppress violations when absolutely necessary and document why.

## Integration with Build

Static analysis is integrated into the build process:

1. **Local Development:** Run `./gradlew check` before committing
2. **Pre-commit Hooks:** Automatically runs before commits (see CONTRIBUTING.md)
3. **CI/CD:** Runs on every pull request and merge

## Fixing Common Violations

### Checkstyle

**Unused imports:**

```java
// Remove or use the import
import java.util.List;  // Remove if not used
```

**Line too long:**

```java
// Split into multiple lines
String message = "This is a very long string that exceeds " +
                 "the maximum line length";
```

**Missing braces:**

```java
// Add braces even for single statements
if (condition) {
    doSomething();
}
```

### SpotBugs

**Potential null pointer:**

```java
// Add null check
if (user != null) {
    String name = user.getName();
}
```

**Resource leak:**

```java
// Use try-with-resources
try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
    // Use reader
}
```

## Exit Codes

- `0` - No violations found, analysis passed
- `Non-zero` - Violations found, build fails

## Troubleshooting

### Build Fails Due to Checkstyle

1. Check the HTML report at `build/reports/checkstyle/main.html`
2. Fix the violations listed
3. Re-run `./gradlew checkstyleMain`

### Build Fails Due to SpotBugs

1. Check the HTML report at `build/reports/spotbugs/main.html`
2. Review and fix the bugs
3. Re-run `./gradlew spotbugsMain`
