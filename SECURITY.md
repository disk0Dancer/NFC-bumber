# Security Policy

## Supported Versions

Currently supported versions of NFC Card Emulator:

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | :white_check_mark: |
| < 1.0   | :x:                |

## Reporting a Vulnerability

We take the security of NFC Card Emulator seriously. If you discover a security vulnerability, please follow these steps:

### Do NOT:
- Open a public issue on GitHub
- Discuss the vulnerability publicly before it's fixed

### Do:
1. **Email the maintainer** directly at the email address listed in the repository
2. **Include the following information**:
   - Type of vulnerability
   - Affected versions
   - Steps to reproduce
   - Potential impact
   - Suggested fix (if you have one)

### What to Expect:
- **Initial response**: Within 48 hours
- **Status update**: Within 7 days
- **Fix timeline**: Varies based on severity
  - Critical: 7-14 days
  - High: 14-30 days
  - Medium: 30-60 days
  - Low: Next planned release

### After the Fix:
- You'll be credited in the security advisory (unless you prefer to remain anonymous)
- A security advisory will be published on GitHub
- Users will be notified to update

## Security Best Practices

### For Users:
- Always use the latest version of the app
- Enable biometric authentication in settings
- Only add cards you personally own
- Never share your encrypted card data
- Be aware of your surroundings when using NFC emulation

### For Developers:
- Follow the [Coding Style Guide](./docs/guides/coding-style-guide.md)
- Never log sensitive data (UIDs, card data)
- Use Android Keystore for all cryptographic keys
- Encrypt all card data with AES-256
- Run security tests before submitting PRs
- Keep dependencies up to date (use Dependabot)

## Known Security Considerations

### NFC Security:
- This app is designed for **non-payment cards** only
- Payment card cloning is illegal and not supported
- Card emulation only works when app is in foreground
- No card data is transmitted over the internet

### Data Security:
- All card data is encrypted using AES-256
- Encryption keys are stored in Android Keystore
- No plaintext card data is stored on disk
- Optional biometric authentication available

### App Security:
- ProGuard/R8 obfuscation enabled in release builds
- No hardcoded secrets or API keys
- Root/debugging warnings (non-blocking)
- Regular security audits recommended

## Security Features

- ✅ AES-256 encryption for all card data
- ✅ Android Keystore integration
- ✅ Biometric authentication support
- ✅ Foreground-only HCE
- ✅ No internet permissions
- ✅ Open source for security audits

## Disclaimer

This application is provided "as is" without warranty of any kind. Users are responsible for:
- Complying with local laws regarding NFC card usage
- Using the app only with cards they own
- Understanding the security implications of card emulation

**NOT for use with:**
- Credit or debit cards
- Payment cards of any kind
- Cards you don't own
- Accessing secure areas without authorization

## Third-Party Dependencies

We regularly update dependencies to patch security vulnerabilities. Current security-critical dependencies:
- AndroidX Security Library
- Android Biometric API
- Room Database (with encryption)

Security updates are monitored via:
- GitHub Dependabot
- Android Security Bulletins
- CVE databases

## Responsible Disclosure

We follow responsible disclosure practices:
1. Vulnerability reported privately
2. Fix developed and tested
3. Security patch released
4. Public disclosure after users have time to update
5. Credit to reporter (if desired)

## Contact

For security issues only: [Create a security advisory](https://github.com/disk0Dancer/NFC-bumber/security/advisories/new)

For general issues: [GitHub Issues](https://github.com/disk0Dancer/NFC-bumber/issues)
