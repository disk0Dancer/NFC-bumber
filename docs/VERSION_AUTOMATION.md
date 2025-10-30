# Automatic Version Management

## Overview

This document describes the automated version management system that extracts version numbers from git tags, eliminating manual version updates and ensuring consistency between releases and builds.

## Problem

Previously, version management was manual and error-prone:

1. **Hardcoded Versions**: `versionCode` and `versionName` were hardcoded in `app/build.gradle.kts`
2. **Manual Updates**: Developers had to remember to update versions before each release
3. **Inconsistencies**: Version in code (2.0.0) didn't match git tags (v0.1.2)
4. **Build Artifacts**: APK filenames didn't reliably reflect the actual version

## Solution

### Automatic Version Extraction from Git Tags

**Implementation in `app/build.gradle.kts`:**

```kotlin
// Function to extract version from git tag
fun getVersionFromGit(): Pair<Int, String> {
    return try {
        val gitTag = Runtime.getRuntime()
            .exec("git describe --tags --abbrev=0")
            .inputStream.bufferedReader().readText().trim()
        
        // Parse version tag (e.g., "v1.2.3" or "1.2.3")
        val versionMatch = Regex("""v?(\d+)\.(\d+)\.(\d+)""").find(gitTag)
        if (versionMatch != null) {
            val (major, minor, patch) = versionMatch.destructured
            
            // Calculate versionCode as major*10000 + minor*100 + patch
            val versionCode = major.toInt() * 10000 + minor.toInt() * 100 + patch.toInt()
            val versionName = "$major.$minor.$patch"
            
            Pair(versionCode, versionName)
        } else {
            // Fallback if no valid tag found
            Pair(1, "0.1.0")
        }
    } catch (e: Exception) {
        // Fallback to default version if git command fails
        println("Warning: Could not read git tag, using default version. Error: ${e.message}")
        Pair(1, "0.1.0")
    }
}

val (appVersionCode, appVersionName) = getVersionFromGit()

android {
    defaultConfig {
        versionCode = appVersionCode
        versionName = appVersionName
        // ...
    }
}
```

### Version Code Calculation

The version code is calculated using a formula that ensures:
- Monotonically increasing values (required by Android)
- Clear relationship to semantic version
- Support for up to version 209.99.99 (Android's max version code is 2,100,000,000)

**Formula:**
```
versionCode = (major × 10000) + (minor × 100) + patch
```

**Examples:**
| Version Tag | versionCode | versionName |
|-------------|-------------|-------------|
| v0.1.0      | 100         | 0.1.0       |
| v0.1.2      | 102         | 0.1.2       |
| v1.0.0      | 10000       | 1.0.0       |
| v2.3.5      | 20305       | 2.3.5       |
| v10.15.99   | 101599      | 10.15.99    |
| v209.99.99  | 2099999     | 209.99.99   |

## Benefits

### For Developers

1. **No Manual Updates**: Versions are automatically extracted from git tags
2. **Single Source of Truth**: Git tags are the only place version is defined
3. **Consistency**: APKs, app info, and git tags always match
4. **Less Errors**: No risk of forgetting to update version numbers

### For Release Process

1. **Simplified Workflow**: 
   ```bash
   git tag v1.2.3
   git push --tags
   ./gradlew assembleRelease
   ```
2. **Correct APK Names**: Output APK is named `Wolle-1.2.3.apk`
3. **CI/CD Integration**: GitHub Actions automatically uses correct version
4. **Obtainium Compatibility**: Version extraction works perfectly with Obtainium

### For Users

1. **Clear Version Numbers**: App version matches release tag
2. **Reliable Updates**: Update managers like Obtainium can track versions correctly
3. **No Confusion**: No mismatches between download version and app version

## Usage

### Creating a New Release

1. **Commit your changes:**
   ```bash
   git add .
   git commit -m "feat: new feature"
   ```

2. **Create a git tag with the new version:**
   ```bash
   git tag v1.2.3
   ```

3. **Push the tag to GitHub:**
   ```bash
   git push --tags
   ```

4. **Build the release (locally or via CI/CD):**
   ```bash
   ./gradlew assembleRelease
   ```

The APK will be automatically named `Wolle-1.2.3.apk` and will report version `1.2.3` in Android settings.

### Version Tag Format

The system supports flexible tag formats:

✅ **Supported:**
- `v1.2.3` (preferred)
- `1.2.3`
- `v0.1.0`
- `v10.99.999`

❌ **Not Supported:**
- `version-1.2.3`
- `v1.2` (must have three parts)
- `1.2.3-beta` (pre-release tags need special handling)

### Pre-release Versions

For pre-release versions (alpha, beta, RC), you can:

1. **Use version suffix in tag:**
   ```bash
   git tag v1.2.3-beta1
   ```
   The version will be extracted as `1.2.3` (suffix ignored)

2. **Add versionNameSuffix in debug build:**
   ```kotlin
   debug {
       versionNameSuffix = "-debug"
   }
   ```

## Fallback Behavior

### No Git Tags

If no git tags exist, the system falls back to:
- `versionCode = 1`
- `versionName = "0.1.0"`

A warning is printed to the build log:
```
Warning: Could not read git tag, using default version. Error: [error message]
```

### Invalid Tag Format

If a tag exists but doesn't match the expected format:
- Falls back to default version (0.1.0)
- Prints warning message
- Build continues successfully

### Git Not Available

If git is not in the PATH or not available:
- Falls back to default version
- Build continues without errors
- Useful for clean builds without git history

## Integration with CI/CD

### GitHub Actions

The existing `.github/workflows/ci-cd.yml` workflow automatically:

1. Checks out with full git history (`fetch-depth: 0`)
2. Builds release APK when tags are pushed
3. Creates GitHub release with correct version
4. Uploads APK with correct filename

**Workflow excerpt:**
```yaml
- uses: actions/checkout@v4
  with:
    fetch-depth: 0  # Important: fetch all history and tags

- name: Build release APK
  if: startsWith(github.ref, 'refs/tags/')
  run: ./gradlew assembleRelease

- name: Create Release
  if: startsWith(github.ref, 'refs/tags/')
  uses: softprops/action-gh-release@v1
  with:
    files: app/build/outputs/apk/release/Wolle-*.apk
```

### Local Development

During local development:
- The current git tag is used
- If no tag, falls back to 0.1.0
- Debug builds get `-debug` suffix

## Troubleshooting

### Build Shows Wrong Version

**Problem:** Build shows version 0.1.0 even though tags exist

**Solutions:**
1. Fetch tags: `git fetch --tags`
2. Check you're on the right commit: `git log --oneline`
3. Verify tag exists: `git tag -l`
4. Clean build: `./gradlew clean`

### Version Code Conflict

**Problem:** "Version code X already exists"

**Cause:** Two different semantic versions produce the same version code

**Solution:** This shouldn't happen with the formula used, but if it does:
- Ensure tags follow semantic versioning
- Check for duplicate or overlapping tags
- Manually adjust version if needed (not recommended)

### CI/CD Build Fails

**Problem:** CI/CD can't determine version

**Solutions:**
1. Verify `fetch-depth: 0` in checkout action
2. Check that tags are pushed: `git push --tags`
3. Look at CI logs for git errors

## Best Practices

### Semantic Versioning

Follow [Semantic Versioning 2.0.0](https://semver.org/):

- **MAJOR** (X.0.0): Breaking changes
- **MINOR** (0.X.0): New features, backward compatible
- **PATCH** (0.0.X): Bug fixes, backward compatible

### Tagging Strategy

1. **Development**: Work on main/feature branches without tags
2. **Release Preparation**: Test thoroughly
3. **Release**: Create tag only when ready to release
4. **Hotfix**: Create new patch version tag

### Version Incrementing

| Change Type | Version Update | Example |
|-------------|---------------|---------|
| Bug fix | Increment patch | 1.2.3 → 1.2.4 |
| New feature | Increment minor | 1.2.3 → 1.3.0 |
| Breaking change | Increment major | 1.2.3 → 2.0.0 |

### Git Tag Management

**Create tag:**
```bash
git tag v1.2.3
git tag -a v1.2.3 -m "Release version 1.2.3"  # With message
```

**Delete tag (if needed):**
```bash
git tag -d v1.2.3              # Delete local
git push origin :refs/tags/v1.2.3  # Delete remote
```

**List tags:**
```bash
git tag -l
git describe --tags            # Show current tag with offset
```

## Migration from Manual Versions

### For Existing Projects

If you're migrating from hardcoded versions:

1. **Determine current version** in build.gradle.kts
2. **Create matching git tag:**
   ```bash
   git tag v[current-version]
   git push --tags
   ```
3. **Remove hardcoded versions** (already done in this project)
4. **Test build** to verify version extraction works

### Update Workflow

Old workflow:
1. Edit build.gradle.kts
2. Update versionCode
3. Update versionName
4. Commit changes
5. Build APK
6. Create git tag
7. Create release

New workflow:
1. Commit changes
2. Create git tag with version
3. Push tag (CI/CD builds automatically)

## Future Enhancements

Potential improvements to the version system:

1. **Pre-release Suffix**: Automatic `-alpha`, `-beta`, `-rc` suffixes
2. **Build Number**: Add build number for non-tagged commits
3. **Commit Count**: Include commit count since last tag
4. **Version Manifest**: Generate version.txt with build info
5. **Changelog Generation**: Auto-generate changelog from commits between tags

## References

- [Semantic Versioning](https://semver.org/)
- [Android Versioning](https://developer.android.com/studio/publish/versioning)
- [Git Tags](https://git-scm.com/book/en/v2/Git-Basics-Tagging)
- [Gradle Build Scripts](https://docs.gradle.org/current/userguide/writing_build_scripts.html)

## Conclusion

Automatic version management from git tags provides:
- ✅ Single source of truth for versions
- ✅ No manual version updates needed
- ✅ Consistency between code, builds, and releases
- ✅ Simplified release process
- ✅ Better developer experience

This system ensures that version numbers are always accurate and reduces the risk of version-related errors in the release process.
