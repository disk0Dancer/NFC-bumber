# Functional Requirements - NFC Card Emulator

## Overview
This document specifies the functional requirements for the NFC Card Emulator Android application. Requirements are organized by feature area and prioritized using MoSCoW method (Must have, Should have, Could have, Won't have).

---

## FR-1: NFC Card Reading

### FR-1.1: Physical Card Detection [MUST HAVE]
**Description**: The system must detect when a physical NFC card is brought near the device's NFC reader.

**Acceptance Criteria**:
- System detects card within 1 second of proximity
- Works with device screen on or off
- Provides visual feedback when card is detected
- Supports ISO 14443-4 Type A and Type B cards

**Priority**: P0 (Critical)

---

### FR-1.2: Card Data Extraction [MUST HAVE]
**Description**: The system must extract and store essential card data from physical NFC cards.

**Data to Extract**:
- Card UID (Unique Identifier)
- ATS (Answer To Select) bytes
- Historical bytes
- Card type/technology
- SAK (Select Acknowledge)

**Acceptance Criteria**:
- Successfully reads data from 95% of supported cards
- Validates data integrity before storage
- Handles malformed data gracefully
- Logs unsupported card types

**Priority**: P0 (Critical)

---

### FR-1.3: Card Type Recognition [SHOULD HAVE]
**Description**: The system should automatically identify the type of NFC card being scanned.

**Supported Card Types**:
- MIFARE Classic 1K/4K
- MIFARE Ultralight
- MIFARE DESFire
- ISO-DEP (ISO 14443-4)
- Generic NFC-A/B

**Acceptance Criteria**:
- Correct card type identification in 90% of cases
- Shows "Unknown" for unrecognized types
- Displays card type to user after scan
- Stores card type metadata

**Priority**: P1 (High)

---

### FR-1.4: Scan Error Handling [MUST HAVE]
**Description**: The system must handle scanning errors gracefully and guide user to retry.

**Error Types**:
- Card moved away during read
- Unsupported card type
- Read timeout
- NFC disabled
- Hardware error

**Acceptance Criteria**:
- Shows clear error message for each error type
- Provides actionable resolution steps
- Allows immediate retry
- Logs errors for debugging

**Priority**: P0 (Critical)

---

## FR-2: Card Storage & Management

### FR-2.1: Card Persistence [MUST HAVE]
**Description**: The system must securely store digitized card data for later emulation.

**Storage Requirements**:
- Encrypted database using Room
- Unique identifier for each card
- Metadata: name, color, date added, last used
- Maximum 50 cards per user

**Acceptance Criteria**:
- Card data survives app restart
- Encrypted at rest using Android Keystore
- Fast retrieval (<100ms)
- No data loss on app updates

**Priority**: P0 (Critical)

---

### FR-2.2: Card Naming [MUST HAVE]
**Description**: Users must be able to assign friendly names to saved cards.

**Acceptance Criteria**:
- Default name: "Card {number}" (e.g., "Card 1")
- User can rename during save or after
- Name length: 1-30 characters
- Supports Unicode (emoji, Cyrillic, etc.)
- Duplicate names allowed
- Name displayed prominently in UI

**Priority**: P0 (Critical)

---

### FR-2.3: Card Customization [SHOULD HAVE]
**Description**: Users should be able to customize card appearance with colors and icons.

**Customization Options**:
- Color selection (8-12 preset colors)
- Optional icon/emoji
- Card preview in list

**Acceptance Criteria**:
- Visual distinction between cards
- Color persists across sessions
- Default colors assigned automatically
- Easy to change in card details

**Priority**: P1 (High)

---

### FR-2.4: Card Deletion [MUST HAVE]
**Description**: Users must be able to delete saved cards.

**Acceptance Criteria**:
- Accessible from card details screen
- Requires confirmation dialog
- Permanently removes card data
- Updates UI immediately
- Cannot be undone
- If deleted card was selected, auto-select another or show empty state

**Priority**: P0 (Critical)

---

### FR-2.5: Card List Display [MUST HAVE]
**Description**: The system must display all saved cards in an organized, browsable list.

**Display Requirements**:
- Horizontal slider/carousel layout
- Card preview showing: name, icon, color
- Last used timestamp
- Sort order: last used, then date added
- Empty state when no cards

**Acceptance Criteria**:
- Smooth scrolling (60fps)
- Loads quickly even with 50 cards
- Clear visual hierarchy
- Supports swipe gestures
- Accessible with screen readers

**Priority**: P0 (Critical)

---

## FR-3: Card Emulation (HCE)

### FR-3.1: Host Card Emulation Service [MUST HAVE]
**Description**: The system must emulate the selected virtual card when near an NFC terminal.

**Technical Requirements**:
- Implements Android HostApduService
- Responds to ISO 14443-4 APDU commands
- Emulates card UID and ATS
- Works when app is in foreground

**Acceptance Criteria**:
- Terminal cannot distinguish virtual from physical card
- Response time <100ms for APDU commands
- Successfully completes 95% of transactions
- Works with various terminal types

**Priority**: P0 (Critical)

---

### FR-3.2: Card Selection for Emulation [MUST HAVE]
**Description**: Users must be able to select which card to emulate.

**Selection Behavior**:
- Only one card active at a time
- Selection persists across app sessions
- Clear visual indicator of selected card
- Selected card is used for HCE

**Acceptance Criteria**:
- Tap card in slider to select
- Highlighted/bordered selected card
- Selection remembered on app restart
- Fast switching (<0.5 seconds)

**Priority**: P0 (Critical)

---

### FR-3.3: Foreground Requirement [MUST HAVE]
**Description**: HCE must only work when the app is in the foreground for security.

**Acceptance Criteria**:
- HCE service active only when app visible
- Notification shown when emulation is active
- Automatic pause when app backgrounded
- Resume when app returns to foreground
- Clear user communication about requirement

**Priority**: P0 (Critical)

---

### FR-3.4: Transaction Logging [SHOULD HAVE]
**Description**: The system should log successful emulation transactions for user reference.

**Log Information**:
- Date and time
- Card used
- Terminal type (if identifiable)
- Success/failure status

**Acceptance Criteria**:
- Last 100 transactions stored
- Accessible in card details
- Optional: export to file
- No sensitive terminal data stored

**Priority**: P1 (High)

---

### FR-3.5: Emulation Feedback [SHOULD HAVE]
**Description**: The system should provide feedback when card is being read by terminal.

**Feedback Types**:
- Haptic vibration
- Brief visual notification
- Sound (optional, user preference)

**Acceptance Criteria**:
- Immediate feedback (<100ms)
- Non-intrusive
- Configurable in settings
- Works in silent mode (for haptic/visual)

**Priority**: P1 (High)

---

## FR-4: User Interface

### FR-4.1: Main Screen [MUST HAVE]
**Description**: The app must have a clear, intuitive main screen for card management.

**UI Elements**:
- Card slider/carousel
- "Add Card" button
- Settings icon
- Current card indicator

**Acceptance Criteria**:
- Loads in <1 second
- Touch targets minimum 48dp
- Follows Material Design guidelines
- Supports light and dark themes
- Landscape orientation support

**Priority**: P0 (Critical)

---

### FR-4.2: Card Scanning Screen [MUST HAVE]
**Description**: The app must provide a dedicated screen for scanning physical cards.

**UI Elements**:
- Scanning animation/indicator
- Instructions text
- NFC icon/graphic
- Cancel button
- Progress feedback

**Acceptance Criteria**:
- Clear instructions for first-time users
- Real-time scan status updates
- Visual feedback when card detected
- Easy to cancel operation
- Tips for positioning phone

**Priority**: P0 (Critical)

---

### FR-4.3: Card Details Screen [MUST HAVE]
**Description**: The app must provide a detailed view of each saved card.

**Information Displayed**:
- Card name (editable)
- Card type
- UID (with copy button)
- ATS/historical bytes (technical view)
- Date added
- Last used
- Usage count
- Edit/Delete buttons

**Acceptance Criteria**:
- All card data visible
- Easy to edit name/color
- Technical details collapsible
- Copy UID to clipboard
- Confirmation before delete

**Priority**: P0 (Critical)

---

### FR-4.4: Settings Screen [SHOULD HAVE]
**Description**: The app should have a settings screen for user preferences.

**Settings Options**:
- Default card selection
- Emulation feedback (haptic, sound)
- Theme selection (light/dark/system)
- Security options (biometric lock)
- About/Help
- Version information

**Acceptance Criteria**:
- Standard Android settings UI
- Changes take effect immediately
- Settings persist across sessions
- Clear descriptions for each option

**Priority**: P1 (High)

---

### FR-4.5: First-Time Tutorial [COULD HAVE]
**Description**: The app could show a brief tutorial on first launch.

**Tutorial Content**:
- Welcome message
- Key features overview
- How to scan first card
- How to emulate
- Privacy information

**Acceptance Criteria**:
- Skippable
- Only shows once
- 3-5 screens maximum
- Clear visual design
- "Don't show again" option

**Priority**: P2 (Medium)

---

## FR-5: Security & Privacy

### FR-5.1: Data Encryption [MUST HAVE]
**Description**: All sensitive card data must be encrypted at rest.

**Encryption Requirements**:
- AES-256 encryption
- Android Keystore for key management
- EncryptedSharedPreferences for settings
- No plaintext card data in logs

**Acceptance Criteria**:
- Card data unreadable without app
- Keys stored in secure hardware (if available)
- Survives app uninstall/reinstall (keys regenerated)
- No data leakage in logs or backups

**Priority**: P0 (Critical)

---

### FR-5.2: Biometric Authentication [SHOULD HAVE]
**Description**: The app should support optional biometric authentication to access cards.

**Biometric Options**:
- Fingerprint
- Face recognition
- PIN fallback

**Acceptance Criteria**:
- Optional (disabled by default)
- Configurable in settings
- Authentication required on app launch
- 30-second timeout after backgrounding
- Graceful fallback for unsupported devices

**Priority**: P1 (High)

---

### FR-5.3: No Network Access [MUST HAVE]
**Description**: The app must function entirely offline without internet access.

**Acceptance Criteria**:
- No INTERNET permission in manifest
- All data stored locally
- No telemetry or analytics
- No crash reporting to external services
- Complete privacy

**Priority**: P0 (Critical)

---

### FR-5.4: Secure Storage [MUST HAVE]
**Description**: Card data must be stored securely against extraction attempts.

**Security Measures**:
- SQLCipher for database encryption
- Certificate pinning for future cloud features
- Root detection (warning only)
- Debugger detection (warning only)

**Acceptance Criteria**:
- Database file encrypted
- Cannot be read by file managers
- Cannot be extracted via ADB (device encrypted)
- Warning shown on rooted devices

**Priority**: P0 (Critical)

---

## FR-6: Permissions & System Integration

### FR-6.1: NFC Permission [MUST HAVE]
**Description**: The app must request and use NFC permission.

**Acceptance Criteria**:
- Declares NFC permission in manifest
- Requests at runtime with rationale
- Handles permission denial gracefully
- Links to system settings if denied permanently

**Priority**: P0 (Critical)

---

### FR-6.2: Foreground Service [MUST HAVE]
**Description**: The app must use a foreground service for active HCE.

**Acceptance Criteria**:
- Shows persistent notification during emulation
- Notification tappable to return to app
- Notification shows which card is active
- Service stops when app backgrounded

**Priority**: P0 (Critical)

---

### FR-6.3: NFC State Detection [MUST HAVE]
**Description**: The app must detect when NFC is disabled and prompt user.

**Acceptance Criteria**:
- Detects NFC on/off state
- Shows banner when NFC is off
- Provides button to open NFC settings
- Re-checks when app returns to foreground

**Priority**: P0 (Critical)

---

## FR-7: Error Handling & Reliability

### FR-7.1: Crash Recovery [MUST HAVE]
**Description**: The app must recover gracefully from crashes without data loss.

**Acceptance Criteria**:
- No data corruption on crash
- Database transactions atomic
- App restarts cleanly
- User notified if recovery fails

**Priority**: P0 (Critical)

---

### FR-7.2: Compatibility Checking [SHOULD HAVE]
**Description**: The app should check device compatibility on first launch.

**Checks**:
- NFC hardware present
- HCE supported (Android 4.4+)
- Sufficient storage available

**Acceptance Criteria**:
- Clear error if device incompatible
- Links to supported devices list
- Graceful exit if critical features missing

**Priority**: P1 (High)

---

### FR-7.3: Error Logging [SHOULD HAVE]
**Description**: The app should log errors for debugging without compromising privacy.

**Acceptance Criteria**:
- Logs stored locally only
- No sensitive data in logs
- Logs rotated to limit size
- Optional: user can export logs for support

**Priority**: P1 (High)

---

## FR-8: Help & Support

### FR-8.1: In-App Help [SHOULD HAVE]
**Description**: The app should provide contextual help and FAQs.

**Help Topics**:
- How to scan a card
- How to use emulated card
- Troubleshooting NFC issues
- Supported card types
- Privacy policy

**Acceptance Criteria**:
- Accessible from settings
- Searchable
- Offline content
- Regular updates

**Priority**: P1 (High)

---

### FR-8.2: Feedback Mechanism [COULD HAVE]
**Description**: The app could allow users to submit feedback or bug reports.

**Acceptance Criteria**:
- Feedback form in settings
- Optional email collection
- Attach logs option
- No automatic data collection

**Priority**: P2 (Medium)

---

## FR-9: Future Features (Not in MVP)

### FR-9.1: Cloud Backup [WON'T HAVE IN V1]
**Description**: Allow users to backup cards to cloud storage.

**Priority**: P3 (Future)

---

### FR-9.2: Card Sharing [WON'T HAVE IN V1]
**Description**: Share cards between users via NFC or QR code.

**Priority**: P3 (Future)

---

### FR-9.3: Multiple Card Emulation [WON'T HAVE IN V1]
**Description**: Rotate between multiple cards automatically.

**Priority**: P3 (Future)

---

### FR-9.4: Wear OS Support [WON'T HAVE IN V1]
**Description**: Companion app for smartwatches.

**Priority**: P3 (Future)

---

## Requirements Traceability Matrix

| Requirement ID | Feature | Priority | Sprint |
|---------------|---------|----------|--------|
| FR-1.1 | Card Detection | P0 | 1 |
| FR-1.2 | Data Extraction | P0 | 1 |
| FR-1.3 | Type Recognition | P1 | 2 |
| FR-1.4 | Error Handling | P0 | 1 |
| FR-2.1 | Card Storage | P0 | 1 |
| FR-2.2 | Card Naming | P0 | 1 |
| FR-2.3 | Customization | P1 | 2 |
| FR-2.4 | Card Deletion | P0 | 2 |
| FR-2.5 | Card List | P0 | 1 |
| FR-3.1 | HCE Service | P0 | 2 |
| FR-3.2 | Card Selection | P0 | 1 |
| FR-3.3 | Foreground Req | P0 | 2 |
| FR-3.4 | Transaction Log | P1 | 3 |
| FR-3.5 | Emulation Feedback | P1 | 3 |
| FR-4.1 | Main Screen | P0 | 1 |
| FR-4.2 | Scan Screen | P0 | 1 |
| FR-4.3 | Details Screen | P0 | 2 |
| FR-4.4 | Settings Screen | P1 | 2 |
| FR-5.1 | Data Encryption | P0 | 1 |
| FR-5.2 | Biometric Auth | P1 | 3 |
| FR-5.3 | No Network | P0 | 1 |
| FR-5.4 | Secure Storage | P0 | 1 |

---

## Acceptance Test Plan

Each functional requirement will be validated through:

1. **Unit Tests**: Individual component functionality
2. **Integration Tests**: Component interaction
3. **UI Tests**: User interface automation
4. **Manual Tests**: Real device testing with physical cards
5. **Security Tests**: Penetration testing, encryption validation

**Definition of Done**:
- All acceptance criteria met
- Unit tests pass (>80% coverage)
- Integration tests pass
- Manual testing completed
- Code reviewed
- Documentation updated
