# Git Hooks

This directory contains git hooks for the NFC-bumber project.

## Installation

To enable the git hooks, run:

```bash
./.githooks/setup.sh
```

Or manually:

```bash
git config core.hooksPath .githooks
```

## Available Hooks

### pre-commit

Runs a build check before allowing commits. This ensures that:
- The code compiles successfully
- No build errors are introduced

The hook runs `./gradlew assembleDebug` to validate the build.

### Bypassing Hooks

If you need to bypass the hooks temporarily (e.g., for WIP commits), use:

```bash
git commit --no-verify
```

**Note:** This should only be used in exceptional cases. Always ensure builds pass before pushing to the repository.
