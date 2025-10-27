# DevOps Practices

This directory contains all DevOps and CI/CD configurations for the NFC Card Emulator project.

## Overview

Our DevOps setup is designed to be **economical** and efficient, minimizing GitHub Actions minutes while maintaining code quality.

## Workflows

### 🔧 Main CI/CD Pipeline (`android-ci.yml`)

**Triggers**: Push to `main` or `develop`, Pull Requests to these branches

**What it does**:
- ✅ Runs ktlint code formatting checks
- ✅ Runs Detekt static analysis
- ✅ Executes unit tests
- ✅ Builds debug APK
- ✅ Uploads artifacts (only on main branch)

**Optimizations**:
- Uses Gradle caching to speed up builds (saves ~2-3 minutes per run)
- Cancels previous runs when new commits are pushed
- Runs jobs sequentially to minimize parallel runner usage
- 30-minute timeout to prevent runaway jobs
- Only uploads artifacts on main branch to save storage
- Only uploads reports on failure

**Estimated cost**: ~5-8 minutes per run

### ⚡ Quick PR Checks (`pr-checks.yml`)

**Triggers**: Pull Request opened, synchronized, or reopened

**What it does**:
- ✅ Code style check (ktlint)
- ✅ Compilation check
- ✅ Adds comment on failure

**Optimizations**:
- Very lightweight (only essential checks)
- 15-minute timeout
- Cancels previous runs per PR

**Estimated cost**: ~2-3 minutes per run

### 🏷️ Auto Labeling (`auto-label.yml`)

**Triggers**: Pull Request opened, reopened, or synchronized

**What it does**:
- Automatically labels PRs based on changed files
- Helps organize and categorize contributions

**Estimated cost**: <1 minute per run

### 🧹 Stale Issues (`stale.yml`)

**Triggers**: Weekly schedule (Monday 00:00 UTC) or manual

**What it does**:
- Marks issues/PRs as stale after 60 days of inactivity
- Closes stale issues/PRs after 14 days
- Keeps issue tracker clean and organized

**Optimizations**:
- Runs only once per week
- Processes max 30 items per run
- Exempts critical issues

**Estimated cost**: <1 minute per week

## Cost Optimization Strategies

### 1. **Caching**
- Gradle dependencies cached between runs
- Build cache preserved
- Saves 2-3 minutes per build

### 2. **Selective Triggering**
- Only runs on important branches (`main`, `develop`)
- Pull requests trigger lightweight checks
- Scheduled jobs run weekly, not daily

### 3. **Concurrency Control**
- Cancels outdated workflow runs automatically
- Prevents wasting minutes on superseded commits

### 4. **Artifact Management**
- Only uploads artifacts on `main` branch
- 7-day retention period (vs default 90 days)
- Only uploads reports on failure

### 5. **Timeouts**
- All jobs have reasonable timeouts
- Prevents runaway workflows

### 6. **Sequential Execution**
- Runs checks sequentially, not in parallel
- Uses single runner instead of multiple

### 7. **Shallow Clones**
- Uses `fetch-depth: 1` for faster checkouts
- Only fetches latest commit

## Estimated Monthly Cost

Based on GitHub Actions free tier (2,000 minutes/month for public repos):

### Regular Usage (10 PRs/month, 20 commits to main/develop)
- Main CI: 20 commits × 8 min = 160 minutes
- PR Checks: 10 PRs × 3 runs × 3 min = 90 minutes
- Auto Label: 30 runs × 0.5 min = 15 minutes
- Stale Issues: 4 runs × 1 min = 4 minutes

**Total: ~270 minutes/month** (13.5% of free tier)

### Heavy Usage (30 PRs/month, 50 commits to main/develop)
- Main CI: 50 commits × 8 min = 400 minutes
- PR Checks: 30 PRs × 4 runs × 3 min = 360 minutes
- Auto Label: 90 runs × 0.5 min = 45 minutes
- Stale Issues: 4 runs × 1 min = 4 minutes

**Total: ~810 minutes/month** (40.5% of free tier)

**Result**: Even with heavy usage, you stay well within the free tier! 🎉

## Issue & PR Templates

### Issue Templates
- **Bug Report**: Structured bug reporting with device and card info
- **Feature Request**: Consistent feature proposal format
- **Config**: Directs users to discussions and documentation

### Pull Request Template
- Comprehensive checklist
- Type of change classification
- Testing requirements
- Documentation reminders

## Automation Features

### Dependabot
- **Gradle dependencies**: Weekly checks
- **GitHub Actions**: Monthly checks
- Groups related updates to reduce PR count
- Configured in `dependabot.yml`

### Auto-Labeling
- Automatically categorizes PRs by changed files
- Labels: documentation, dependencies, tests, UI, etc.
- Configured in `labeler.yml`

### Stale Issue Management
- Keeps issue tracker clean
- Gives contributors 74 days total before closing
- Respects pinned/critical issues

## Code Owners

The `CODEOWNERS` file automatically requests review from:
- Repository owner for all changes
- Specific owners for sensitive areas (security, HCE)

## Security

See `SECURITY.md` for:
- Vulnerability reporting process
- Security best practices
- Supported versions

## Contributing

See `CONTRIBUTING.md` for:
- Development workflow
- Commit message conventions
- Pull request process
- Code style guidelines

## Monitoring

### What we track:
- ✅ Build success rate
- ✅ Test pass rate
- ✅ Code quality metrics (via ktlint/Detekt)
- ✅ Artifact generation

### What we DON'T track:
- ❌ User analytics (no telemetry)
- ❌ Personal information
- ❌ Deployment metrics (no automated deployment)

## Local Development

Before pushing, run locally to save CI minutes:

```bash
# Format code
./gradlew ktlintFormat

# Run all checks
./gradlew ktlintCheck detekt test

# Build APK
./gradlew assembleDebug
```

## Future Improvements

Potential additions (when needed):
- [ ] Release automation workflow
- [ ] Changelog generation
- [ ] APK signing for releases
- [ ] Device testing on Firebase Test Lab (limited runs)
- [ ] Code coverage reporting
- [ ] Performance benchmarking

## Tips for Contributors

1. **Run checks locally first** - saves CI minutes and gets faster feedback
2. **Keep PRs focused** - smaller PRs run faster and are easier to review
3. **Use draft PRs** - draft PRs don't trigger some workflows
4. **Squash commits** - fewer commits = fewer workflow runs

## Questions?

- **Workflows not working?** Check the Actions tab for logs
- **Need to adjust triggers?** Edit the `on:` section in workflow files
- **Want to add a workflow?** Follow existing patterns for consistency

---

**Remember**: These practices are optimized for economy. The goal is to maintain high code quality while staying within GitHub's free tier. 💰✨
