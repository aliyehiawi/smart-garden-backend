# Static Code Analysis Guide

This project enforces code quality through automated static analysis tools. All code must pass Checkstyle and SpotBugs checks before being committed.

## Quick Reference

```bash
# Run all static analysis checks
./gradlew codeQuality

# Run specific checks
./gradlew checkstyleMain checkstyleTest
./gradlew spotbugsMain spotbugsTest

# View reports
open build/reports/checkstyle/main.html
open build/reports/spotbugs/main.html
```

---

## Tools Overview

### Checkstyle - Code Style Verification

**Purpose:** Enforces consistent Java coding standards

**Checks:**
- Naming conventions (camelCase, PascalCase)
- Code formatting (line length, indentation)
- Import organization
- Cyclomatic complexity
- Method/class length limits

**Configuration:** `config/checkstyle/checkstyle.xml`

### SpotBugs - Bug Pattern Detection

**Purpose:** Identifies potential bugs and security issues

**Checks:**
- Null pointer risks
- Resource leaks
- Security vulnerabilities
- Thread safety issues
- Performance problems
- Incorrect API usage

**Configuration:** `config/spotbugs/excludeFilter.xml`

---

## Prerequisites

No installation needed! Tools are configured as Gradle plugins.

**Requirements:**
- Java 21
- Gradle wrapper (included)

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

## Pre-commit Hooks

Pre-commit hooks automatically run static code analysis before allowing commits, ensuring code quality is maintained.

### Installation

#### Option 1: Automatic Installation (Recommended)

```bash
./hooks/install-hooks.sh
```

#### Option 2: Manual Installation

Copy the hook file to your local Git hooks directory:

```bash
cp hooks/pre-commit .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit  # Linux/Mac only
```

### How It Works

1. When you run `git commit`, the pre-commit hook triggers automatically
2. The hook runs `./gradlew codeQuality` to check for violations
3. **If violations found:** Commit is blocked, and you see error reports
4. **If no violations:** Commit proceeds normally

### Example Output

**Success:**
```
Running static code analysis...
✅ Code quality checks passed!
[feature/my-branch abc123] feat: add new feature
```

**Failure:**
```
Running static code analysis...
❌ Code quality checks failed!
Please fix the violations before committing.

View reports:
  - Checkstyle: build/reports/checkstyle/main.html
  - SpotBugs:   build/reports/spotbugs/main.html
```

### Bypassing the Hook (Not Recommended)

In rare cases where you need to commit despite violations:

```bash
git commit --no-verify
```

**Warning:** Only use `--no-verify` for work-in-progress commits that will be fixed before merging.

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

**Exclude Filter:** `config/spotbugs/excludeFilter.xml`

Excludes false positives for:
- Lombok-generated code (Builder classes)
- DTOs with intentional data exposure
- Entity classes with JPA requirements
- MapStruct-generated implementations

## Suppressing Violations

### Checkstyle Suppression

Use inline suppression comments:

```java
// CHECKSTYLE.OFF: MethodName - Reason for suppression
public void Some_Legacy_Method() {
    // ...
}
// CHECKSTYLE.ON: MethodName
```

**Example from project:**
```java
// CHECKSTYLE.OFF: MethodName - Spring Data JPA requires underscore for nested property navigation (garden.id)
java.util.List<Device> findByGarden_Id(Long gardenId);
// CHECKSTYLE.ON: MethodName
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
2. **Pre-commit Hooks:** Automatically runs before commits (see Pre-commit Hooks section above)
3. **CI/CD:** Runs on every pull request and merge

**Recommended Workflow:**

```bash
# 1. Make code changes
# 2. Run analysis manually
./gradlew codeQuality

# 3. Fix any violations
# 4. Commit (hook will verify automatically)
git commit -m "feat: add new feature"
```

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

---

## Related Documentation

- **[README](README.md)** - Project overview and setup
- **[Contributing Guidelines](README.md#contributing)** - Code quality requirements for contributors
- **[Testing Guide](TESTING_GUIDE.md)** - API testing with Postman
- **[Tutorial](docs/tutorials/tutorial.md)** - Learn the API step-by-step
