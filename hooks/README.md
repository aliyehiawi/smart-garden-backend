# Git Hooks

This directory contains Git hooks to automate code quality checks.

## Available Hooks

### pre-commit

Automatically runs static code analysis (Checkstyle + SpotBugs) before each commit to ensure code quality.

## Installation

### Automatic Installation

```bash
./hooks/install-hooks.sh
```

### Manual Installation

```bash
cp hooks/pre-commit .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit  # Linux/Mac only
```

## How It Works

When you commit code:

1. The pre-commit hook runs automatically
2. Executes `./gradlew codeQuality` to check for violations
3. **If violations exist:** Commit is blocked with error details
4. **If no violations:** Commit proceeds normally

## Benefits

- Catches code quality issues before they enter version control
- Ensures consistent code standards across team
- Reduces code review time by automating style checks
- Prevents bugs from being committed

## Bypassing (Emergency Only)

If you must commit despite violations:

```bash
git commit --no-verify
```

**Warning:** Only use this for work-in-progress commits that will be fixed before merging to main.

