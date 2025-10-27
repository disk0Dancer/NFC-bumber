# C4 Architecture Model - NFC Card Emulator

## Overview
This document describes the architecture of the NFC Card Emulator Android application using the C4 model (Context, Container, Component, Code).

## Level 1: System Context Diagram

```
┌─────────────────┐
│                 │
│      User       │
│  (Card Owner)   │
│                 │
└────────┬────────┘
         │
         │ Reads/Selects
         │ NFC Cards
         │
┌────────▼─────────────────────────────────┐
│                                          │
│     NFC Card Emulator Application        │
│                                          │
│  Emulates NFC cards for payment and      │
│  access control terminals                │
│                                          │
└────────┬─────────────────────────────────┘
         │
         │ Emulates NFC
         │ Protocol
         │
┌────────▼────────┐
│                 │
│  NFC Terminal   │
│  (POS/Reader)   │
│                 │
└─────────────────┘
```

### Key Elements:

**User (Card Owner)**
- Person who owns physical NFC cards
- Wants to digitize cards for convenience
- Needs simple interface to manage virtual cards

**NFC Card Emulator Application**
- Android mobile application
- Reads and digitizes physical NFC cards
- Emulates cards via Host Card Emulation (HCE)
- Provides card management UI

**NFC Terminal**
- Payment terminals (POS)
- Access control readers
- Any ISO 14443-4 compliant NFC reader
- Should not detect difference between physical and virtual cards

## Level 2: Container Diagram

```
┌──────────────────────────────────────────────────────┐
│          NFC Card Emulator (Android App)             │
│                                                      │
│  ┌──────────────────────────────────────────────┐  │
│  │         Presentation Layer (UI)               │  │
│  │  ┌────────────────┐  ┌──────────────────┐   │  │
│  │  │  Card Slider   │  │  Card Details    │   │  │
│  │  │  Activity      │  │  Fragment        │   │  │
│  │  └────────────────┘  └──────────────────┘   │  │
│  │  ┌────────────────┐  ┌──────────────────┐   │  │
│  │  │  Scan Card     │  │  Settings        │   │  │
│  │  │  Activity      │  │  Fragment        │   │  │
│  │  └────────────────┘  └──────────────────┘   │  │
│  └──────────────────────────────────────────────┘  │
│                                                      │
│  ┌──────────────────────────────────────────────┐  │
│  │         Business Logic Layer                  │  │
│  │  ┌────────────────┐  ┌──────────────────┐   │  │
│  │  │  Card Manager  │  │  NFC Reader      │   │  │
│  │  │  ViewModel     │  │  Service         │   │  │
│  │  └────────────────┘  └──────────────────┘   │  │
│  │  ┌────────────────┐  ┌──────────────────┐   │  │
│  │  │  HCE Emulator  │  │  Crypto Service  │   │  │
│  │  │  Service       │  │                  │   │  │
│  │  └────────────────┘  └──────────────────┘   │  │
│  └──────────────────────────────────────────────┘  │
│                                                      │
│  ┌──────────────────────────────────────────────┐  │
│  │         Data Layer                            │  │
│  │  ┌────────────────┐  ┌──────────────────┐   │  │
│  │  │  Card Database │  │  Secure Storage  │   │  │
│  │  │  (Room)        │  │  (EncryptedShared│   │  │
│  │  │                │  │   Preferences)   │   │  │
│  │  └────────────────┘  └──────────────────┘   │  │
│  └──────────────────────────────────────────────┘  │
│                                                      │
└──────────────────────────────────────────────────────┘
         │                            │
         │ NFC Reader                 │ HCE Protocol
         │ (Read Physical)            │ (Emulate Virtual)
         ▼                            ▼
┌─────────────────┐          ┌─────────────────┐
│  Physical Card  │          │  NFC Terminal   │
└─────────────────┘          └─────────────────┘
```

### Containers:

**Presentation Layer**
- Technology: Jetpack Compose / XML Layouts
- Responsibilities: UI rendering, user interaction
- Components: Activities, Fragments, ViewModels

**Business Logic Layer**
- Technology: Kotlin, Android Services
- Responsibilities: Card management, NFC operations, HCE
- Components: Services, Use Cases, Repositories

**Data Layer**
- Technology: Room Database, EncryptedSharedPreferences
- Responsibilities: Persistent storage, data encryption
- Components: DAOs, Entities, Repositories

## Level 3: Component Diagram

### UI Components

**CardSliderActivity**
- Main screen with card carousel
- Displays all saved cards
- Allows card selection for emulation
- Shows card preview (name, type, color)

**ScanCardActivity**
- NFC scanning interface
- Real-time scan feedback
- Card data preview before saving
- Name assignment UI

**CardDetailsFragment**
- Full card information display
- Edit card metadata (name, color, icon)
- Delete card option
- View card technical details (UID, ATS)

**SettingsFragment**
- App preferences
- Default card selection
- Security settings
- About/Help information

### Business Logic Components

**CardManagerViewModel**
- Manages card list state
- Handles card CRUD operations
- Coordinates between UI and Repository
- Implements LiveData/StateFlow for reactive UI

**NfcReaderService**
- Reads physical NFC cards
- Extracts card UID, ATS, and response data
- Validates card compatibility
- Provides structured card data

**HceEmulatorService**
- Extends HostApduService
- Emulates selected virtual card
- Responds to terminal APDU commands
- Implements ISO 14443-4 protocol

**CryptoService**
- Encrypts sensitive card data
- Manages encryption keys
- Provides secure storage helpers
- Android Keystore integration

**CardRepository**
- Interface between ViewModels and Data Layer
- Implements data access logic
- Caching strategy
- Error handling

### Data Layer Components

**CardDatabase (Room)**
- SQLite database wrapper
- Stores card entities
- Provides DAOs for queries
- Migration support

**CardDao**
- Database access object
- CRUD operations for cards
- Queries for card retrieval

**CardEntity**
- Data model for cards
- Fields: id, uid, ats, name, type, color, dateAdded
- Encrypted sensitive data

**SecureStorage**
- EncryptedSharedPreferences wrapper
- Stores app preferences
- Stores selected card ID
- Key-value secure storage

## Level 4: Code Level

### Key Classes and Interfaces

```kotlin
// Domain Model
data class NfcCard(
    val id: String,
    val uid: ByteArray,
    val ats: ByteArray?,
    val historicalBytes: ByteArray?,
    val name: String,
    val cardType: CardType,
    val color: Int,
    val dateAdded: Long,
    val lastUsed: Long?
)

enum class CardType {
    MIFARE_CLASSIC,
    MIFARE_ULTRALIGHT,
    MIFARE_DESFIRE,
    ISO_DEP,
    UNKNOWN
}

// Repository Interface
interface CardRepository {
    suspend fun getAllCards(): List<NfcCard>
    suspend fun getCardById(id: String): NfcCard?
    suspend fun insertCard(card: NfcCard)
    suspend fun updateCard(card: NfcCard)
    suspend fun deleteCard(id: String)
    suspend fun getSelectedCard(): NfcCard?
    suspend fun setSelectedCard(id: String)
}

// ViewModel
class CardManagerViewModel(
    private val repository: CardRepository
) : ViewModel() {
    
    private val _cards = MutableStateFlow<List<NfcCard>>(emptyList())
    val cards: StateFlow<List<NfcCard>> = _cards.asStateFlow()
    
    private val _selectedCard = MutableStateFlow<NfcCard?>(null)
    val selectedCard: StateFlow<NfcCard?> = _selectedCard.asStateFlow()
    
    fun loadCards()
    fun selectCard(id: String)
    fun deleteCard(id: String)
    fun updateCardName(id: String, name: String)
}

// NFC Reader
class NfcReaderService(private val context: Context) {
    fun readCard(tag: Tag): NfcCard?
    private fun extractUid(tag: Tag): ByteArray
    private fun extractAts(tag: Tag): ByteArray?
    private fun determineCardType(tag: Tag): CardType
}

// HCE Service
class NfcEmulatorService : HostApduService() {
    override fun processCommandApdu(
        commandApdu: ByteArray,
        extras: Bundle?
    ): ByteArray
    
    override fun onDeactivated(reason: Int)
    
    private fun handleSelectCommand(apdu: ByteArray): ByteArray
    private fun handleReadCommand(apdu: ByteArray): ByteArray
}
```

## Technology Stack

### Core Android
- **Language**: Kotlin 1.9+
- **Min SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 14 (API 34)
- **Build System**: Gradle with Kotlin DSL

### UI Framework
- **Option 1**: Jetpack Compose (Modern, declarative)
- **Option 2**: XML Layouts with Material Design 3

### Architecture
- **Pattern**: MVVM (Model-View-ViewModel)
- **DI**: Hilt / Koin
- **Navigation**: Jetpack Navigation Component

### Data Persistence
- **Database**: Room
- **Preferences**: EncryptedSharedPreferences
- **Serialization**: Kotlin Serialization

### NFC
- **Reader**: Android NFC API
- **Emulator**: Host Card Emulation (HCE)

### Testing
- **Unit Tests**: JUnit 5, MockK
- **UI Tests**: Espresso, Compose UI Test
- **Integration Tests**: Robolectric

### Code Quality
- **Linter**: ktlint
- **Static Analysis**: Detekt
- **Code Coverage**: JaCoCo

## Security Considerations

1. **Data Encryption**
   - All card data encrypted at rest
   - Android Keystore for key management
   - EncryptedSharedPreferences for settings

2. **NFC Security**
   - HCE service requires app to be in foreground
   - Card selection locked when emulating
   - Optional PIN/biometric authentication

3. **Permissions**
   - NFC permission required
   - Foreground service for HCE
   - No internet permission (offline-first)

4. **Code Security**
   - ProGuard/R8 obfuscation
   - Root detection
   - Anti-debugging measures

## Scalability & Performance

- **Database**: Indexed queries for fast card retrieval
- **Memory**: Efficient bitmap handling for card images
- **Battery**: Optimized NFC scanning, no background services
- **Storage**: Minimal footprint, compressed card data

## Deployment

- **Distribution**: Google Play Store
- **Updates**: OTA via Play Store
- **Versioning**: Semantic versioning (MAJOR.MINOR.PATCH)
- **CI/CD**: GitHub Actions for automated builds

## Future Enhancements

1. Cloud backup/sync
2. Multi-device support
3. Card sharing between users
4. Advanced card editing
5. NFC P2P card transfer
6. Wear OS companion app
