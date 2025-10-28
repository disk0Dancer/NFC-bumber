# Release Process

This document describes how to create releases for NFC Card Emulator.

## Creating a Release

Releases are automatically built and published when you create a version tag.

### Prerequisites

- All changes committed and pushed to `main` or `develop`
- Tests passing
- Ready for release

### Steps

1. **Create a version tag:**

```bash
# Tag the commit
git tag -a v1.0.0 -m "Release version 1.0.0"

# Push the tag to GitHub
git push origin v1.0.0
```

2. **GitHub Actions will automatically:**
   - Build the release APK
   - Generate a changelog from commits since the last tag
   - Create a GitHub Release
   - Upload the APK as a release asset

3. **The release will be available at:**
   - https://github.com/disk0Dancer/Wolle/releases

## Version Numbering

Follow [Semantic Versioning](https://semver.org/):

- **v1.0.0** - Major release
- **v1.1.0** - Minor release (new features)
- **v1.0.1** - Patch release (bug fixes)

Pre-release versions:
- **v1.0.0-alpha.1** - Alpha release
- **v1.0.0-beta.1** - Beta release
- **v1.0.0-rc.1** - Release candidate

## Release Types

### Regular Release
- Tag format: `v1.0.0`
- Marked as latest release
- Stable and production-ready

### Pre-release
- Tag format: `v1.0.0-alpha.1`, `v1.0.0-beta.1`, `v1.0.0-rc.1`
- Marked as pre-release
- For testing and early adopters

## Obtainium Compatibility

Once a release is published:
- Obtainium will automatically detect new versions
- Users who added the app via Obtainium will receive update notifications
- APK files must be attached to the release (automated via workflow)

## Manual Release (If Needed)

If you need to create a release manually:

1. Build the release APK:
```bash
./gradlew assembleRelease
```

2. Go to [Releases](https://github.com/disk0Dancer/Wolle/releases)
3. Click "Draft a new release"
4. Choose a tag (or create a new one)
5. Fill in release notes
6. Upload the APK from `app/build/outputs/apk/release/`
7. Publish release

## Changelog

The automated release workflow generates a changelog from commit messages. To make this useful:

- Use [Conventional Commits](https://www.conventionalcommits.org/)
- Write clear, descriptive commit messages
- Example:
  ```
  feat: add card search functionality
  fix: resolve NFC timeout issue
  docs: update installation guide
  ```

## APK Signing (Future)

Currently, releases are unsigned. To add signing:

1. Create a keystore:
```bash
keytool -genkey -v -keystore release.keystore -alias wolle -keyalg RSA -keysize 2048 -validity 10000
```

2. Add secrets to GitHub:
   - `KEYSTORE_FILE` - Base64 encoded keystore
   - `KEYSTORE_PASSWORD` - Keystore password
   - `KEY_ALIAS` - Key alias
   - `KEY_PASSWORD` - Key password

3. Update the release workflow to sign APKs

## Rollback

If a release has issues:

1. Delete the problematic release from GitHub
2. Fix the issues
3. Create a new patch release (e.g., v1.0.1)

## Testing Releases

Before creating a public release:

1. Create a pre-release (e.g., v1.0.0-beta.1)
2. Test thoroughly
3. Gather feedback
4. Create the final release when ready

## Release Checklist

- [ ] All tests passing
- [ ] Documentation updated
- [ ] CHANGELOG.md updated (if manual release)
- [ ] Version number follows semantic versioning
- [ ] Tag created and pushed
- [ ] Release published successfully
- [ ] APK downloads and installs correctly
- [ ] Release notes are clear and complete

## Distribution

After release:
- **Obtainium**: Automatically distributed to users who added the app
- **GitHub**: Available in Releases section
- **Direct**: Users can download APK directly

## Monitoring

After release, monitor:
- Download counts
- Issue reports
- User feedback
- Crash reports (if analytics enabled)

---

For questions about releases, see:
- [CONTRIBUTING.md](../CONTRIBUTING.md)
- [GitHub Discussions](https://github.com/disk0Dancer/Wolle/discussions)
