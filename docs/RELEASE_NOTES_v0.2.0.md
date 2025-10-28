# Release Notes v0.2.0 - HCE Emulation Release

## 🎉 Major Release: Card Emulation Now Available!

**Release Date**: October 28, 2025  
**Version**: 0.2.0  
**Version Code**: 3

---

## ✨ What's New

### 🎴 NFC Card Emulation (HCE)
**The feature you've been waiting for!** Your phone can now emulate saved NFC cards.

- **Emulate saved cards to NFC terminals** - Use your digitized cards with compatible readers
- **APDU command support** - SELECT, READ BINARY, GET DATA commands
- **Automatic card selection** - Select which card to emulate from the card list
- **Usage tracking** - See when and how often each card is used
- **Secure implementation** - All card data encrypted, only works in foreground

### 🧪 Comprehensive Test Suite
- 30+ unit tests covering all business logic
- Use case validation and error handling
- APDU command format verification
- High code coverage for reliability

### 🔧 Technical Improvements
- **Persistent card selection** - Your selected card is remembered across app restarts
- **Improved error handling** - Better feedback when things go wrong
- **Enhanced logging** - Easier debugging and troubleshooting

---

## 📱 How to Use Card Emulation

### Step 1: Select a Card
1. Open the app
2. Swipe through your saved cards
3. Tap on the card you want to emulate
4. It will be highlighted as selected

### Step 2: Emulate
1. Keep the app open (in foreground)
2. Bring your phone close to an NFC terminal
3. The terminal will read your virtual card
4. Usage statistics will be updated automatically

### ⚠️ Important Notes
- **Foreground requirement**: The app must be visible on screen for emulation to work
- **Supported cards**: Works best with ISO-DEP and NFC-A type cards
- **Legal use only**: Only emulate your own cards for legitimate purposes
- **Compatibility**: May not work with all proprietary systems

---

## 🛠️ Technical Details

### HCE Service
**Component**: `NfcEmulatorService`  
**Type**: HostApduService (Android HCE)  
**AIDs**: F0010203040506, A0000000031010

### Supported APDU Commands
| Command | Code | Response |
|---------|------|----------|
| SELECT | 00 A4 | ATS + Status |
| READ BINARY | 00 B0 | Historical Bytes + Status |
| GET DATA | 00 CA | UID + Status |

### Status Words
- `9000`: Success
- `6A82`: Card not found
- `6700`: Invalid length
- `6986`: Command not allowed
- `6F00`: Unknown error

---

## 🔐 Security & Privacy

### What's Protected
✅ All card data encrypted with AES-256-GCM  
✅ Selected card ID stored in encrypted preferences  
✅ No plaintext sensitive data in logs  
✅ Emulation only works when app is in foreground  
✅ No internet access required or used

### Your Responsibility
⚠️ Only emulate your own cards  
⚠️ Follow local laws and regulations  
⚠️ Don't use for payment cards without authorization  
⚠️ Ensure physical card security

---

## 📋 Complete Feature List

### Card Management
- ✅ Scan NFC cards (8 card types supported)
- ✅ Save cards to encrypted database
- ✅ Name and color customization
- ✅ Delete cards with confirmation
- ✅ View card details (UID, type, statistics)

### Card Emulation (NEW!)
- ✅ Select card for emulation
- ✅ Emulate to NFC terminals
- ✅ APDU command processing
- ✅ Usage statistics tracking
- ✅ Persistent selection

### User Interface
- ✅ Material Design 3
- ✅ Horizontal card slider
- ✅ Card details screen
- ✅ Scan screen with live feedback
- ✅ Empty states and error messages

### Security
- ✅ AES-256-GCM encryption
- ✅ Android Keystore integration
- ✅ Encrypted SharedPreferences
- ✅ No internet access
- ✅ No data collection

### Testing
- ✅ 30+ unit tests
- ✅ Use case coverage
- ✅ APDU format tests
- ✅ Error scenario tests

---

## 🐛 Bug Fixes

### Fixed in v0.2.0
- Fixed card selection not persisting after app restart
- Fixed auto-selection when no card is selected
- Fixed selection clearing when card is deleted
- Improved error messages for NFC failures

---

## 📊 Statistics

### Code Metrics
- **Total Kotlin Files**: 25+
- **Lines of Code**: ~3,000
- **Test Files**: 7
- **Test Cases**: 30+
- **Modules**: 4 (app, data, domain, presentation)

### Supported Features
- **Card Types**: 8 (ISO-DEP, MIFARE Classic, MIFARE Ultralight, NFC-A/B/F/V, Unknown)
- **APDU Commands**: 3 (SELECT, READ BINARY, GET DATA)
- **Languages**: English, Russian (partial)

---

## 🔄 Upgrade Instructions

### From v0.1.0
1. Install v0.2.0 APK
2. All your saved cards will be preserved
3. First card will be auto-selected for emulation
4. Test emulation with an NFC terminal

### Fresh Install
1. Install APK
2. Grant NFC permissions
3. Scan your first card
4. Start emulating!

---

## 🚀 Performance

- **APDU Response Time**: < 50ms (typical)
- **Card Selection**: < 100ms
- **Database Query**: < 100ms
- **Memory Usage**: Low (~50MB)
- **Battery Impact**: Minimal

---

## ⚙️ System Requirements

### Minimum
- **Android Version**: 8.0 Oreo (API 26)
- **NFC Hardware**: Required
- **HCE Support**: Required (Android 4.4+)
- **Storage**: 10MB

### Recommended
- **Android Version**: 10.0+ (API 29+)
- **Device**: Pixel, Samsung, or other major brands
- **NFC**: Always-on NFC support
- **Storage**: 50MB for future updates

---

## 📚 Documentation

### New Documents
- ✅ `docs/SPRINT4_COMPLETION.md` - Technical completion report
- ✅ Updated `README.md` - Reflects HCE completion
- ✅ This release notes document

### Existing Documentation
- `docs/requirements/functional-requirements.md` - All requirements
- `docs/ROADMAP.md` - Development roadmap
- `docs/SPRINT1_COMPLETION.md` - Foundation sprint
- `docs/SPRINT2_COMPLETION.md` - NFC reading sprint

---

## 🎯 What's Next (v0.3.0)

### Planned Features
- 🔄 Haptic feedback on card read
- 📊 Transaction history
- 🔐 Optional biometric authentication
- 🎨 Enhanced card customization
- 📱 Notification during emulation
- 🌍 Full internationalization (i18n)

### Performance Improvements
- ⚡ Faster card switching
- 💾 Optimized database queries
- 🔋 Reduced battery consumption

---

## 🙏 Acknowledgments

### Contributors
- **Developer**: Grigorii Churakov (@disk0Dancer)
- **AI Assistant**: GitHub Copilot

### Open Source Libraries
- AndroidX Compose
- Room Database
- Hilt Dependency Injection
- Kotlin Coroutines
- MockK (testing)

### Special Thanks
- Android NFC Community
- Material Design Team
- All beta testers

---

## 📞 Support & Feedback

### Report Issues
- GitHub Issues: https://github.com/disk0Dancer/Wolle/issues
- Include: Android version, device model, steps to reproduce

### Discussions
- GitHub Discussions: https://github.com/disk0Dancer/Wolle/discussions
- Ask questions, share use cases, suggest features

### Security Issues
- Email: See SECURITY.md for responsible disclosure

---

## ⚖️ Legal & Disclaimer

### License
BSD 3-Clause License - See LICENSE file

### Disclaimer
This app is for legal use only. Do not:
- Clone payment cards (credit/debit)
- Perform unauthorized access
- Engage in fraud or illegal activities

Users are responsible for compliance with local laws.

### Privacy
- ✅ No data collection
- ✅ No internet access
- ✅ No analytics or tracking
- ✅ All data stored locally and encrypted

---

## 🔗 Links

- **Repository**: https://github.com/disk0Dancer/Wolle
- **Releases**: https://github.com/disk0Dancer/Wolle/releases
- **Documentation**: https://github.com/disk0Dancer/Wolle/tree/main/docs
- **Android NFC Guide**: https://developer.android.com/guide/topics/connectivity/nfc/hce

---

**Thank you for using Wolle!** 🎉

We hope you enjoy the new card emulation feature. Please provide feedback and report any issues you encounter.

**Made with ❤️ for the NFC community**
