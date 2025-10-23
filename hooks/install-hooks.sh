#!/bin/sh
#
# Install Git hooks
#

echo "Installing Git hooks..."

cp hooks/pre-commit .git/hooks/pre-commit

chmod +x .git/hooks/pre-commit

echo "✅ Pre-commit hook installed successfully!"
echo ""
echo "The hook will automatically run static code analysis before each commit."
echo "To bypass (not recommended): git commit --no-verify"

