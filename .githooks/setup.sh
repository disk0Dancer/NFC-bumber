#!/bin/bash
# Setup script to install git hooks

echo "Setting up git hooks..."

# Configure git to use .githooks directory
git config core.hooksPath .githooks

if [ $? -eq 0 ]; then
    echo "✅ Git hooks installed successfully!"
    echo ""
    echo "The following hooks are now active:"
    echo "  - pre-commit: Runs build check before allowing commits"
    echo ""
    echo "To disable hooks temporarily, use: git commit --no-verify"
else
    echo "❌ Failed to install git hooks"
    exit 1
fi
