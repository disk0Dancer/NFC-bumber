# Contributing to NFC Card Emulator

Thank you for your interest in contributing to NFC Card Emulator! This guide will help you get started.

## Code of Conduct

This project adheres to a code of conduct that all contributors are expected to follow. Please be respectful and constructive in all interactions.

## Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/NFC-bumber.git
   cd NFC-bumber
   ```
3. **Set up the development environment** following the [Project Setup Guide](./docs/guides/project-setup.md)
4. **Create a feature branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Making Changes

### Before You Start

- Check existing [issues](https://github.com/disk0Dancer/NFC-bumber/issues) and [pull requests](https://github.com/disk0Dancer/NFC-bumber/pulls)
- For major changes, open an issue first to discuss your proposed changes
- Read our [Coding Style Guide](./docs/guides/coding-style-guide.md)

### Development Workflow

1. **Write your code** following the coding standards
2. **Write tests** for your changes (target ≥80% coverage)
3. **Run local checks**:
   ```bash
   # Format code
   ./gradlew ktlintFormat
   
   # Run linters
   ./gradlew ktlintCheck detekt
   
   # Run tests
   ./gradlew test
   
   # Build the app
   ./gradlew assembleDebug
   ```
4. **Commit your changes** using [Conventional Commits](https://www.conventionalcommits.org/):
   ```bash
   git commit -m "feat: add card search functionality"
   git commit -m "fix: resolve NFC timeout issue"
   git commit -m "docs: update installation guide"
   ```

## Submitting Changes

1. **Push your branch** to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
2. **Open a Pull Request** on GitHub
3. **Fill out the PR template** completely
4. **Wait for review** - maintainers will review your PR and may request changes
5. **Address feedback** - make any requested changes and push updates
6. **Merge** - once approved, your PR will be merged!

## Commit Message Guidelines

We follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types
- **feat**: New feature
- **fix**: Bug fix
- **docs**: Documentation changes
- **style**: Code style changes (formatting, no logic change)
- **refactor**: Code refactoring
- **test**: Adding or updating tests
- **chore**: Build process, dependency updates, etc.
- **perf**: Performance improvements

### Examples
```
feat(scan): add support for MIFARE Ultralight cards
fix(hce): resolve timeout issue with slow terminals
docs(readme): update installation instructions
test(repository): add unit tests for CardRepository
chore(deps): update Compose to version 1.5.4
```

## Code Style

- Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use our [Coding Style Guide](./docs/guides/coding-style-guide.md)
- Run `./gradlew ktlintFormat` to auto-format code
- Ensure `./gradlew ktlintCheck detekt` passes

## Testing

- Write unit tests for all new code
- Maintain test coverage ≥80%
- Run `./gradlew test` before submitting
- Include integration tests for critical paths
- Test on real devices when possible (especially for NFC features)

## Documentation

- Update documentation for any user-facing changes
- Add KDoc comments for public APIs
- Update README.md if needed
- Keep docs/ folder current

## Pull Request Process

1. **Ensure all checks pass** (CI/CD pipeline must be green)
2. **Get at least one approval** from a maintainer
3. **Address all review comments**
4. **Keep your PR focused** - one feature/fix per PR
5. **Keep commits clean** - squash if needed before merge

## What to Contribute

### Good First Issues
Look for issues labeled `good first issue` - these are suitable for newcomers.

### Priority Areas
- Bug fixes (especially critical ones)
- Performance improvements
- Documentation improvements
- Test coverage improvements
- Accessibility enhancements
- Translations (i18n)

### Feature Requests
- Discuss in an issue first
- Ensure it aligns with project goals
- Follow the roadmap when possible

## Questions?

- **General questions**: [GitHub Discussions](https://github.com/disk0Dancer/NFC-bumber/discussions)
- **Bug reports**: [GitHub Issues](https://github.com/disk0Dancer/NFC-bumber/issues)
- **Security issues**: Email the maintainer directly (see README)

## License

By contributing, you agree that your contributions will be licensed under the BSD 3-Clause License.

## Recognition

Contributors will be recognized in the project README and release notes. Thank you for your contributions! 🎉
