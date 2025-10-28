# Release Notes - v0.1.0

## 🎉 First Beta Release - Sprint 1 & 2 Complete

This is the first functional beta release of NFC Card Emulator, featuring complete NFC card scanning and management capabilities.

## 🚀 What's New

### Sprint 1: Foundation & Core Infrastructure ✅
- **Multi-module Clean Architecture** with separate app, data, domain, and presentation layers
- **Room Database** with AES-256-GCM encryption for secure card storage
- **Hilt Dependency Injection** for better testability and modularity
- **Android Keystore** integration for cryptographic key management
- **Repository Pattern** with coroutines and Flow for reactive data access

### Sprint 2: NFC Card Reading ✅
- **Universal NFC Card Scanner** supporting multiple card types:
  - ISO-DEP (ISO 14443-4) - Smart cards
  - MIFARE Classic - Access control cards
  - MIFARE Ultralight - Simple cards
  - NFC-A/B/F/V - Various NFC standards
- **Material Design 3 UI** with smooth animations
- **Horizontal Card Slider** for easy card browsing
- **Card Management**:
  - Scan and save NFC cards
  - Custom card names and colors
  - View card details (UID, ATS, type)
  - Delete cards with confirmation
- **Smart Features**:
  - Auto-generated card names based on type
  - Duplicate card detection
  - Usage tracking (usage count, last used)
  - 9 preset color options

## 🔧 Technical Improvements

### Android 16 Compatibility
- **Updated to API 35** (Android 16) for latest platform support
- **Maintains backward compatibility** to Android 8.0 (API 26)
- **Optimized for Pixel 7 Pro** and modern Android devices

### Build Configuration
- **Gradle 8.2** with Kotlin DSL
- **Kotlin 1.9.0** for latest language features
- **Jetpack Compose** for modern declarative UI
- **Hilt 2.48** for dependency injection

### Security
- **AES-256-GCM encryption** for all card data
- **Android Keystore** for secure key storage
- **No cloud backup** of sensitive card information
- **EncryptedSharedPreferences** for app settings

## 📱 Features

### Card Scanning
1. Tap the + button on the main screen
2. Hold your NFC card near the device
3. App automatically detects and reads the card
4. Review card details (UID, ATS, type)
5. Enter a custom name and choose a color
6. Save the card to your collection

### Card Management
- **Visual Card Slider**: Swipe through your cards horizontally
- **Selection**: Tap any card to view details
- **Details View**: See technical information like UID, ATS, card type
- **Delete**: Remove unwanted cards with confirmation
- **Statistics**: Track usage count for each card

## 🎨 UI/UX
- **Material Design 3** following Google's latest design language
- **Smooth Animations**: Pulsing NFC icon during scanning
- **Empty State Guidance**: Clear instructions for new users
- **Error Handling**: User-friendly error messages
- **Color Customization**: 9 vibrant colors for card personalization

## 📋 System Requirements

### Minimum Requirements
- **Android 8.0 (API 26)** or higher
- **NFC Hardware** required
- **HCE Support** (Host Card Emulation capable device)

### Recommended
- **Android 16 (API 35)** for full feature support
- **Google Pixel 7 Pro** or equivalent modern device
- **At least 50MB** free storage space

## 🔐 Privacy & Security

- ✅ **No Internet Permission** - App works completely offline
- ✅ **Encrypted Storage** - All card data encrypted at rest
- ✅ **No Analytics** - No tracking or data collection
- ✅ **Open Source** - Full transparency, inspect the code
- ✅ **Local Only** - All data stays on your device

## ⚠️ Known Limitations

### Not Yet Implemented
- **HCE Emulation**: Card emulation feature coming in Sprint 3
- **Biometric Lock**: Security enhancement planned for v1.1
- **Backup/Restore**: Local backup feature planned for v2.0
- **Widgets**: Home screen widgets planned for v2.0

### Important Notes
- This is a **BETA** release for testing purposes
- **Do NOT use with payment cards** (credit/debit cards)
- Legal use only with your own cards
- Card emulation (HCE) not yet functional

## 🐛 Bug Fixes

This is the first release, so no bug fixes yet. Please report issues on GitHub!

## 📖 Documentation

- [README](../README.md) - Project overview and quick start
- [Sprint 1 Completion](./SPRINT1_COMPLETION.md) - Infrastructure details
- [Sprint 2 Completion](./SPRINT2_COMPLETION.md) - NFC reading implementation
- [Development Roadmap](./ROADMAP.md) - Future plans
- [Architecture](./architecture/c4-model.md) - System architecture

## 🔜 Coming Next

### Sprint 3: HCE Emulation (In Progress)
- Host Card Emulation service
- Terminal interaction
- Foreground-only emulation
- Haptic feedback on card read

### v1.1 Features
- Biometric authentication
- Enhanced card customization
- Transaction history

## 🙏 Acknowledgments

- **disk0Dancer** - Project creator and maintainer
- **GitHub Copilot** - AI-assisted development
- **Android Open Source Community** - Libraries and tools

## 📥 Installation

### Via Obtainium (Recommended)
1. Install [Obtainium](https://github.com/ImranR98/Obtainium/releases/latest)
2. Add this repository: `https://github.com/disk0Dancer/NFC-bumber`
3. Obtainium will auto-update the app

### Direct APK
1. Download `app-release.apk` from this release
2. Enable "Install from unknown sources" in Android settings
3. Install the APK file
4. Grant NFC permissions when prompted

## 🔗 Links

- **GitHub**: https://github.com/disk0Dancer/NFC-bumber
- **Issues**: https://github.com/disk0Dancer/NFC-bumber/issues
- **Documentation**: https://github.com/disk0Dancer/NFC-bumber/tree/main/docs

## ⚖️ License

BSD 3-Clause License - See [LICENSE](../LICENSE) for details

---

**Full Changelog**: https://github.com/disk0Dancer/NFC-bumber/compare/v0.0.2...v0.1.0

**Release Date**: October 28, 2025
**Version Code**: 2
**Min SDK**: 26 (Android 8.0)
**Target SDK**: 35 (Android 16)
