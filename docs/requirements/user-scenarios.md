# User Scenarios - NFC Card Emulator

## Primary User Personas

### Persona 1: Urban Commuter (Алексей)
- **Age**: 28
- **Occupation**: Software Developer
- **Tech Savvy**: High
- **Use Case**: Daily public transport card, office access card
- **Pain Points**: Carrying multiple cards, forgot card at home
- **Goals**: Consolidate all cards in phone, quick access

### Persona 2: Business Traveler (Мария)
- **Age**: 35
- **Occupation**: Sales Manager
- **Tech Savvy**: Medium
- **Use Case**: Hotel key cards, loyalty cards
- **Pain Points**: Cards get demagnetized, wallet is bulky
- **Goals**: Reliable digital wallet, easy card management

### Persona 3: Student (Дмитрий)
- **Age**: 20
- **Occupation**: University Student
- **Tech Savvy**: High
- **Use Case**: Campus access card, cafeteria card
- **Pain Points**: Losing cards, replacement costs
- **Goals**: Never lose access, backup cards

## Core User Scenarios

### Scenario 1: First-Time Setup

**Context**: User just installed the app for the first time

**Steps**:
1. User opens the NFC Card Emulator app
2. System shows welcome screen with brief tutorial
3. User sees permissions request for NFC access
4. User grants NFC permission
5. System shows empty card list with "Add Card" button
6. User is ready to scan their first card

**Success Criteria**:
- User understands app purpose
- Permissions are granted
- User knows how to add first card

**Alternative Flow**:
- If NFC not supported: Show error message and exit
- If NFC disabled: Prompt user to enable in settings
- If permissions denied: Explain why needed, allow retry

---

### Scenario 2: Scanning and Adding a New Card

**Context**: User wants to digitize their physical transit card

**Steps**:
1. User taps "Add Card" or "+" button
2. System opens scan screen with instructions
3. User holds physical card to phone's NFC reader
4. System detects card and reads UID/ATS data
5. System shows preview of scanned data
6. User enters a friendly name (e.g., "Metro Card")
7. User optionally selects card color/icon
8. User taps "Save"
9. System encrypts and stores card data
10. Card appears in main list

**Success Criteria**:
- Card is successfully read
- Card is saved to database
- Card appears in slider
- User sees success confirmation

**Alternative Flows**:
- **Card read error**: Show retry option with tips
- **Unsupported card**: Show error explaining card type not supported
- **Duplicate card**: Warn user, allow saving with different name
- **User cancels**: Return to main screen without saving

**Error Handling**:
- Timeout after 30 seconds if no card detected
- Clear error messages for unsupported cards
- Validation for empty card name

---

### Scenario 3: Selecting a Card for Emulation

**Context**: User wants to use their virtual metro card

**Steps**:
1. User opens the app
2. System displays card slider with all saved cards
3. User swipes through cards to find "Metro Card"
4. User taps on the card to select it
5. System highlights selected card
6. Card is now active for emulation
7. Visual indicator shows card is ready

**Success Criteria**:
- Selected card is visually distinct
- System remembers selection across app restarts
- Only one card can be active at a time

**Alternative Flows**:
- **No cards saved**: Show empty state with "Add Card" prompt
- **Last used card**: System auto-selects last used card on app start

---

### Scenario 4: Using Emulated Card at Terminal

**Context**: User approaches metro turnstile with phone

**Steps**:
1. User has "Metro Card" selected in app
2. User keeps app open (in foreground)
3. User taps phone to metro turnstile
4. NFC terminal sends APDU commands
5. HCE service responds with virtual card data
6. Terminal accepts card and opens gate
7. App shows brief success notification
8. System updates "last used" timestamp

**Success Criteria**:
- Terminal successfully reads virtual card
- Terminal cannot distinguish from physical card
- Transaction completes in <2 seconds
- User has seamless experience

**Alternative Flows**:
- **App in background**: Show notification to open app
- **No card selected**: Prompt user to select card
- **Multiple taps needed**: Provide haptic feedback
- **Terminal error**: Log for debugging

**Error Handling**:
- If terminal doesn't respond: User retries
- If wrong card type: User selects correct card
- If app crashes: Graceful recovery on restart

---

### Scenario 5: Managing Multiple Cards

**Context**: User has 5 saved cards (metro, work, gym, library, parking)

**Steps**:
1. User opens app and sees card slider
2. User swipes through cards horizontally
3. Each card shows name, icon, and last used date
4. User can quickly switch between cards
5. Current selection is highlighted
6. User taps "Metro Card" for morning commute
7. Later, user switches to "Work Badge" for office

**Success Criteria**:
- Smooth scrolling through cards
- Clear visual hierarchy
- Fast card switching (<1 second)
- Persistent selection

**UX Considerations**:
- Card preview shows essential info
- Smooth animations
- Clear selected state
- Easy to scan with thumb

---

### Scenario 6: Viewing and Editing Card Details

**Context**: User wants to rename their "Card 1" to "Library Card"

**Steps**:
1. User long-presses on "Card 1" in slider
2. System opens card details screen
3. User sees card information:
   - Name: "Card 1"
   - Type: ISO-DEP
   - UID: 04:AB:CD:EF
   - Date Added: 2024-01-15
   - Last Used: 2024-01-20
4. User taps "Edit" button
5. User changes name to "Library Card"
6. User selects new color (blue)
7. User taps "Save"
8. System updates card
9. Updated card appears in slider

**Success Criteria**:
- Easy access to edit mode
- Clear display of card metadata
- Changes are saved immediately
- Validation for required fields

**Alternative Flows**:
- **View-only mode**: Technical details not editable
- **Delete card**: Confirmation dialog before deletion

---

### Scenario 7: Deleting a Card

**Context**: User no longer needs their old gym card

**Steps**:
1. User opens "Gym Card" details
2. User scrolls to bottom
3. User taps "Delete Card" button
4. System shows confirmation dialog:
   - "Delete Gym Card?"
   - "This action cannot be undone"
5. User confirms deletion
6. System removes card from database
7. Card disappears from slider
8. If it was selected, system selects another card or shows empty state

**Success Criteria**:
- Confirmation prevents accidental deletion
- Card is completely removed
- UI updates immediately
- No orphaned data

---

### Scenario 8: App Security - Biometric Lock

**Context**: User wants to protect their cards with fingerprint

**Steps**:
1. User opens Settings
2. User toggles "Require Authentication"
3. System prompts for biometric enrollment
4. User authenticates with fingerprint
5. Setting is enabled
6. Next app launch requires fingerprint
7. User authenticates to access cards

**Success Criteria**:
- Biometric option available on supported devices
- PIN fallback for devices without biometrics
- Secure storage of preference
- Clear security indicators

**Alternative Flows**:
- **No biometric enrolled**: Prompt to enroll in system settings
- **Failed authentication**: Allow retry with limit
- **Locked out**: Fallback to PIN or app uninstall/reinstall

---

### Scenario 9: Troubleshooting - Card Not Reading

**Context**: Terminal doesn't recognize user's virtual card

**Steps**:
1. User tries to tap phone at terminal
2. Terminal doesn't respond
3. User opens app to check
4. Card shows as selected (correct card)
5. User tries again with phone positioned differently
6. Still doesn't work
7. User checks Settings > Help
8. Help shows:
   - "Ensure app is in foreground"
   - "Check NFC positioning on your device"
   - "Verify card type compatibility"
9. User realizes app was in background
10. User keeps app open, tries again
11. Success!

**Success Criteria**:
- Clear troubleshooting guide
- Visual indicators of NFC status
- Device-specific NFC positioning tips
- Link to FAQ/support

---

### Scenario 10: Backup and Recovery (Future Feature)

**Context**: User gets new phone and wants to transfer cards

**Steps**:
1. On old phone, user opens Settings
2. User taps "Backup Cards"
3. System exports encrypted card data
4. User saves to cloud/email/file
5. On new phone, user installs app
6. User taps "Restore Cards"
7. User selects backup file
8. User authenticates
9. System imports cards
10. All cards appear in slider

**Success Criteria**:
- Encrypted backup
- Easy transfer process
- No data loss
- Works across Android versions

---

## User Journey Map

### First Week with App

**Day 1: Discovery & Setup**
- Install app from Play Store
- Grant permissions
- Read tutorial

**Day 2: First Card**
- Scan metro card
- Name it "Metro"
- Try at turnstile successfully
- Excitement! 😊

**Day 3: Add More Cards**
- Scan work badge
- Scan gym card
- Organize with colors

**Day 4: Daily Use**
- Use metro card morning
- Use work badge at office
- Forget physical cards at home
- App saves the day! 🎉

**Day 5-7: Power User**
- Switch between cards smoothly
- Rename cards for clarity
- Delete old test cards
- Recommend to friends

---

## Edge Cases & Error Scenarios

### Edge Case 1: Rapid Card Switching
- User switches cards rapidly while near terminal
- System ensures only selected card responds
- No partial transactions

### Edge Case 2: Phone Battery Dies During Transaction
- Transaction fails gracefully
- Terminal times out
- No data corruption
- User uses physical card backup

### Edge Case 3: Multiple NFC Apps
- User has multiple HCE apps installed
- Android shows payment app selector
- User selects NFC Card Emulator as default
- Smooth experience thereafter

### Edge Case 4: Card with Encryption/Authentication
- User tries to scan encrypted card (e.g., credit card)
- System detects encryption
- Shows error: "This card type uses encryption and cannot be cloned"
- Provides explanation for security reasons

### Edge Case 5: NFC Disabled Mid-Use
- User disables NFC while app is open
- App detects NFC state change
- Shows prominent warning banner
- Provides quick link to enable NFC

---

## Success Metrics

### User Satisfaction
- 95% successful first scan within 3 attempts
- <2 seconds average card selection time
- <3% terminal rejection rate
- 90% user retention after 1 week

### Performance
- App launch time: <1 second
- Card scan time: <3 seconds
- Card switch time: <0.5 seconds
- HCE response time: <100ms

### Usage Patterns
- Average cards per user: 3-5
- Daily active usage: 2-4 times
- Peak usage: Morning & evening commute
- Feature most used: Card slider

---

## Accessibility Considerations

### Visual
- High contrast mode support
- Large text support
- Card colors with patterns (not just color)
- Screen reader descriptions

### Motor
- Large tap targets (48dp minimum)
- No time-based interactions
- Alternative to swipe gestures
- Voice control support

### Cognitive
- Simple, clear language
- Consistent navigation
- Visual feedback for all actions
- Error messages with solutions

---

## Privacy & User Trust

### Transparency
- Clear explanation of data storage
- No cloud sync by default (offline-first)
- No analytics without consent
- Open source potential

### User Control
- Easy card deletion
- Export card data
- Clear data option
- No ads, no tracking

### Security Communication
- Explain encryption in simple terms
- Show security indicators
- Regular security updates
- Responsible disclosure policy
