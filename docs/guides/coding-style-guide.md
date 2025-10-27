# Coding Style Guide - NFC Card Emulator

## Overview

This document defines the coding standards and best practices for the NFC Card Emulator Android project. Following these guidelines ensures consistency, readability, and maintainability across the codebase.

---

## Language and Platform

### Primary Language: Kotlin

- **Version**: Kotlin 1.9.20+
- **Style**: Official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **Philosophy**: Prefer Kotlin idioms over Java patterns

### Prohibited

- Java files (except for compatibility with legacy code if absolutely necessary)
- Kotlin-Java interop unless required by libraries

---

## Code Formatting

### Automatic Formatting

Use **ktlint** for consistent code formatting:

```bash
# Format all code
./gradlew ktlintFormat

# Check formatting
./gradlew ktlintCheck
```

### Key Formatting Rules

**Indentation**
```kotlin
// Use 4 spaces, no tabs
class CardRepository {
    fun getAllCards(): List<Card> {
        return database
            .cardDao()
            .getAllCards()
    }
}
```

**Line Length**
- Maximum: 120 characters
- Prefer 100 characters when possible

**Blank Lines**
```kotlin
class CardManager {
    private val repository: CardRepository  // ✓
                                           // ✓ Blank line
    fun loadCards() {                      // ✓
        // Function body
    }
                                           // ✓ Blank line between functions
    fun saveCard(card: Card) {
        // Function body
    }
}
```

**Imports**
```kotlin
// Organize imports alphabetically
import android.content.Context
import androidx.room.Database
import com.nfcbumber.data.model.Card
import kotlinx.coroutines.flow.Flow

// Avoid wildcard imports (*)
import android.view.*  // ✗ Bad
import android.view.View  // ✓ Good
```

---

## Naming Conventions

### Classes and Objects

```kotlin
// PascalCase for classes
class CardRepository

// PascalCase for objects
object DatabaseConfig

// PascalCase for interfaces
interface CardDao

// Descriptive, specific names
class NfcCardEmulatorService  // ✓ Good
class Service                  // ✗ Too generic
```

### Functions

```kotlin
// camelCase for functions
fun loadCards()
fun saveCard(card: Card)

// Boolean functions: use is/has/can prefix
fun isCardValid(): Boolean
fun hasNfcSupport(): Boolean
fun canEmulateCard(): Boolean

// Avoid redundant prefixes
class CardRepository {
    fun getCards()  // ✗ Redundant 'get'
    fun cards()     // ✓ Concise
    
    // Exception: getters/setters that match convention
    fun getSelectedCard(): Card?  // ✓ When needed for clarity
}
```

### Variables and Properties

```kotlin
// camelCase for variables
val cardList: List<Card>
var selectedCard: Card?

// Constants: SCREAMING_SNAKE_CASE
const val MAX_CARDS = 50
const val DEFAULT_TIMEOUT_MS = 30000

// Private properties: no special prefix
class CardManager {
    private val repository: CardRepository  // ✓ Good
    private val _repository: CardRepository  // ✗ Avoid underscore (unless for backing property)
    
    // Backing properties: use underscore
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()  // ✓ Good pattern
}
```

### Packages

```kotlin
// All lowercase, no underscores
package com.nfcbumber.data.repository  // ✓
package com.nfcbumber.data.Repository  // ✗
package com.nfcbumber.data_repository  // ✗
```

### XML Resources

```xml
<!-- activity_*.xml for activities -->
activity_main.xml
activity_scan_card.xml

<!-- fragment_*.xml for fragments -->
fragment_card_details.xml
fragment_settings.xml

<!-- item_*.xml for list items -->
item_card.xml
item_transaction.xml

<!-- layout_*.xml for reusable layouts -->
layout_empty_state.xml
layout_card_preview.xml

<!-- Strings: snake_case -->
<string name="app_name">NFC Card Emulator</string>
<string name="scan_card_title">Scan Card</string>
<string name="error_nfc_disabled">NFC is disabled</string>

<!-- IDs: snake_case -->
<Button
    android:id="@+id/btn_scan_card"
    ... />
```

---

## Code Structure

### File Organization

**Order of declarations in a class:**

1. Companion object
2. Properties (public → private)
3. Init blocks
4. Constructors
5. Public functions
6. Internal functions
7. Private functions
8. Nested classes

```kotlin
class CardRepository(
    private val database: CardDatabase,
    private val cryptoService: CryptoService
) {
    companion object {
        private const val TAG = "CardRepository"
    }
    
    // Properties
    private val cardDao = database.cardDao()
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()
    
    // Init block
    init {
        loadCards()
    }
    
    // Public functions
    suspend fun getAllCards(): List<Card> {
        return cardDao.getAllCards()
    }
    
    suspend fun insertCard(card: Card) {
        val encrypted = encryptCard(card)
        cardDao.insert(encrypted)
    }
    
    // Private functions
    private fun encryptCard(card: Card): Card {
        // Implementation
    }
    
    // Nested classes
    sealed class Result {
        data class Success(val cards: List<Card>) : Result()
        data class Error(val message: String) : Result()
    }
}
```

### File Naming

- One class per file (except nested classes)
- File name matches class name
- `CardRepository.kt` for `CardRepository` class

---

## Kotlin Best Practices

### Prefer Immutability

```kotlin
// Use val over var
val card = Card(...)  // ✓
var card = Card(...)  // ✗ (unless truly mutable)

// Use immutable collections
val cards: List<Card> = listOf(...)  // ✓
val cards: MutableList<Card> = mutableListOf(...)  // ✗ (expose mutable)

// Return immutable types
fun getCards(): List<Card>  // ✓
fun getCards(): MutableList<Card>  // ✗
```

### Null Safety

```kotlin
// Avoid nullable types when possible
class CardManager {
    var selectedCard: Card? = null  // ✗ If avoidable
    
    // Better: use sealed class or Result type
    sealed class Selection {
        data class Selected(val card: Card) : Selection()
        object None : Selection()
    }
}

// Use safe calls and elvis operator
val name = card?.name ?: "Unknown"  // ✓
val name = if (card != null) card.name else "Unknown"  // ✗ Verbose

// Avoid !! (non-null assertion)
val uid = card!!.uid  // ✗ Can crash
val uid = card?.uid ?: return  // ✓ Safe

// Exception: when you're certain (after check)
if (card != null) {
    val uid = card.uid  // ✓ Smart cast
}
```

### Data Classes

```kotlin
// Use data classes for DTOs and models
data class Card(
    val id: String,
    val uid: ByteArray,
    val name: String,
    val type: CardType,
    val dateAdded: Long
) {
    // Override equals/hashCode for ByteArray
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        if (id != other.id) return false
        if (!uid.contentEquals(other.uid)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + uid.contentHashCode()
        return result
    }
}

// Use regular class when behavior is complex
class CardManager {
    // Complex logic
}
```

### Sealed Classes

```kotlin
// Use sealed classes for restricted hierarchies
sealed class CardResult {
    data class Success(val card: Card) : CardResult()
    data class Error(val message: String, val cause: Throwable?) : CardResult()
    object Loading : CardResult()
}

// Use sealed interface for more flexibility (Kotlin 1.5+)
sealed interface ViewState {
    object Loading : ViewState
    data class Success(val data: List<Card>) : ViewState
    data class Error(val message: String) : ViewState
}
```

### Extension Functions

```kotlin
// Use extensions for utility functions
fun ByteArray.toHexString(): String {
    return joinToString("") { "%02X".format(it) }
}

// Usage
val uid = card.uid.toHexString()

// Keep extensions focused and in appropriate files
// CardExtensions.kt
fun Card.displayName(): String = name.ifEmpty { "Card ${id.take(4)}" }

// Don't overuse extensions for everything
class Card {
    fun displayName(): String = name.ifEmpty { "Card ${id.take(4)}" }  // ✓ If core behavior
}
```

### Scope Functions

```kotlin
// Use appropriate scope function

// let: for null checks and transformation
card?.let { 
    saveCard(it)
}

// apply: for object configuration
val card = Card(id = "1").apply {
    name = "Metro Card"
    color = Color.Blue
}

// also: for side effects
val card = createCard().also {
    logger.log("Created card: ${it.id}")
}

// run: for executing a block and returning result
val name = card.run {
    if (name.isEmpty()) "Default" else name
}

// with: for calling multiple functions on an object
with(card) {
    println(name)
    println(type)
    println(uid.toHexString())
}
```

### Coroutines

```kotlin
// Use coroutines for async operations
class CardRepository {
    
    // Suspend functions for async operations
    suspend fun loadCards(): List<Card> = withContext(Dispatchers.IO) {
        database.cardDao().getAllCards()
    }
    
    // Use appropriate dispatchers
    suspend fun encryptData(data: ByteArray): ByteArray = withContext(Dispatchers.Default) {
        cryptoService.encrypt(data)  // CPU-intensive
    }
    
    // Use Flow for streams
    fun observeCards(): Flow<List<Card>> {
        return database.cardDao().observeAllCards()
    }
}

// ViewModel: launch in viewModelScope
class CardViewModel(
    private val repository: CardRepository
) : ViewModel() {
    
    fun loadCards() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val cards = repository.loadCards()
                _state.value = State.Success(cards)
            } catch (e: Exception) {
                _state.value = State.Error(e.message ?: "Unknown error")
            }
        }
    }
}
```

---

## Android Best Practices

### MVVM Architecture

```kotlin
// Model: Data layer
data class Card(val id: String, val name: String)

// Repository: Data source abstraction
interface CardRepository {
    suspend fun getAllCards(): List<Card>
    suspend fun insertCard(card: Card)
}

// ViewModel: Business logic and UI state
class CardViewModel(
    private val repository: CardRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadCards() {
        viewModelScope.launch {
            // Business logic
        }
    }
    
    sealed interface UiState {
        object Loading : UiState
        data class Success(val cards: List<Card>) : UiState
        data class Error(val message: String) : UiState
    }
}

// View: UI (Compose)
@Composable
fun CardScreen(viewModel: CardViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (uiState) {
        is UiState.Loading -> LoadingView()
        is UiState.Success -> CardList((uiState as UiState.Success).cards)
        is UiState.Error -> ErrorView((uiState as UiState.Error).message)
    }
}
```

### Dependency Injection (Hilt)

```kotlin
// Application class
@HiltAndroidApp
class NfcBumberApplication : Application()

// Module
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CardDatabase {
        return Room.databaseBuilder(
            context,
            CardDatabase::class.java,
            "card_database"
        ).build()
    }
    
    @Provides
    fun provideCardDao(database: CardDatabase): CardDao {
        return database.cardDao()
    }
}

// Inject in ViewModel
@HiltViewModel
class CardViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel()

// Inject in Activity/Fragment
@AndroidEntryPoint
class MainActivity : ComponentActivity()
```

### Resource Management

```kotlin
// Use string resources
// strings.xml
<string name="scan_card">Scan Card</string>
<string name="card_name_hint">Enter card name</string>

// In Compose
@Composable
fun ScanButton() {
    Button(onClick = { /*...*/ }) {
        Text(stringResource(R.string.scan_card))
    }
}

// Use dimension resources
// dimens.xml
<dimen name="padding_standard">16dp</dimen>

// In Compose
@Composable
fun CardItem() {
    Card(
        modifier = Modifier.padding(dimensionResource(R.dimen.padding_standard))
    )
}
```

### Jetpack Compose

```kotlin
// Composable naming: PascalCase
@Composable
fun CardList(cards: List<Card>) {
    LazyColumn {
        items(cards) { card ->
            CardItem(card = card)
        }
    }
}

// Extract subcomposables
@Composable
fun CardItem(
    card: Card,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        // Content
    }
}

// Use preview annotations
@Preview(showBackground = true)
@Composable
fun CardItemPreview() {
    NfcBumberTheme {
        CardItem(
            card = Card(
                id = "1",
                name = "Metro Card",
                type = CardType.MIFARE_CLASSIC,
                uid = byteArrayOf()
            )
        )
    }
}

// State hoisting
@Composable
fun CardScreen(viewModel: CardViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    CardScreenContent(
        uiState = uiState,
        onCardClick = { card -> viewModel.selectCard(card) },
        onAddClick = { viewModel.navigateToScan() }
    )
}

@Composable
fun CardScreenContent(
    uiState: UiState,
    onCardClick: (Card) -> Unit,
    onAddClick: () -> Unit
) {
    // Stateless UI
}
```

---

## Documentation

### KDoc Comments

```kotlin
/**
 * Repository for managing NFC card data.
 *
 * This repository handles all card-related operations including
 * reading, writing, and querying cards from the local database.
 *
 * @property database The Room database instance
 * @property cryptoService Service for encrypting/decrypting card data
 */
class CardRepository(
    private val database: CardDatabase,
    private val cryptoService: CryptoService
) {
    
    /**
     * Retrieves all cards from the database.
     *
     * @return List of all saved cards, empty if none exist
     * @throws DatabaseException if database access fails
     */
    suspend fun getAllCards(): List<Card> {
        return database.cardDao().getAllCards()
    }
    
    /**
     * Inserts a new card into the database.
     *
     * Card data is automatically encrypted before storage.
     *
     * @param card The card to insert
     * @throws DatabaseException if insertion fails
     * @throws CryptoException if encryption fails
     */
    suspend fun insertCard(card: Card) {
        // Implementation
    }
}
```

### Inline Comments

```kotlin
// Use inline comments sparingly for complex logic
fun processApdu(command: ByteArray): ByteArray {
    // Extract command type from first byte
    val cla = command[0]
    val ins = command[1]
    
    // Handle SELECT command (ISO 7816-4)
    if (ins == 0xA4.toByte()) {
        return handleSelectCommand(command)
    }
    
    // Handle READ BINARY command
    if (ins == 0xB0.toByte()) {
        return handleReadCommand(command)
    }
    
    // Return "Command not supported" status
    return byteArrayOf(0x6D.toByte(), 0x00.toByte())
}

// Avoid obvious comments
val name = card.name  // Get the card name ✗ Obvious

// Use TODO comments for pending work
fun shareCard(card: Card) {
    // TODO: Implement card sharing via NFC P2P
    throw NotImplementedError("Card sharing not yet implemented")
}
```

---

## Testing

### Test Naming

```kotlin
class CardRepositoryTest {
    
    // Pattern: `should<ExpectedBehavior>When<StateUnderTest>`
    @Test
    fun `should return all cards when database is not empty`() {
        // Test
    }
    
    @Test
    fun `should throw exception when card encryption fails`() {
        // Test
    }
    
    // Alternative pattern: `<methodName>_<scenario>_<expectedResult>`
    @Test
    fun getAllCards_emptyDatabase_returnsEmptyList() {
        // Test
    }
}
```

### Test Structure (Given-When-Then)

```kotlin
@Test
fun `should insert card successfully when data is valid`() {
    // Given
    val card = Card(
        id = "1",
        name = "Test Card",
        uid = byteArrayOf(0x01, 0x02, 0x03),
        type = CardType.MIFARE_CLASSIC,
        dateAdded = System.currentTimeMillis()
    )
    
    // When
    runBlocking {
        repository.insertCard(card)
    }
    
    // Then
    val cards = runBlocking { repository.getAllCards() }
    assertEquals(1, cards.size)
    assertEquals("Test Card", cards[0].name)
}
```

### Mock Usage

```kotlin
@Test
fun `should handle database error gracefully`() {
    // Given
    val mockDao = mockk<CardDao>()
    coEvery { mockDao.getAllCards() } throws SQLException("Database error")
    
    val repository = CardRepository(mockDatabase, mockCrypto)
    
    // When/Then
    assertFailsWith<DatabaseException> {
        runBlocking { repository.getAllCards() }
    }
}
```

---

## Performance

### Avoid Unnecessary Object Creation

```kotlin
// Bad: Creates new list on every call
fun getCards(): List<Card> {
    return cards.map { it.copy() }  // ✗
}

// Good: Return immutable reference
fun getCards(): List<Card> {
    return cards  // ✓
}
```

### Use Sequences for Large Collections

```kotlin
// Use sequences for chained operations on large collections
val activeCards = cards
    .asSequence()  // ✓
    .filter { it.isActive }
    .map { it.name }
    .toList()

// Without sequence (creates intermediate lists)
val activeCards = cards
    .filter { it.isActive }  // ✗ Creates intermediate list
    .map { it.name }          // ✗ Creates another intermediate list
```

### Lazy Initialization

```kotlin
class CardManager {
    // Lazy initialization for expensive objects
    private val database by lazy {
        Room.databaseBuilder(context, CardDatabase::class.java, "cards").build()
    }
    
    private val cryptoService by lazy {
        CryptoService(context)
    }
}
```

---

## Security

### Sensitive Data Handling

```kotlin
// Never log sensitive data
class CardRepository {
    suspend fun insertCard(card: Card) {
        Log.d(TAG, "Inserting card: ${card.uid.toHexString()}")  // ✗ Sensitive data
        Log.d(TAG, "Inserting card: ${card.id}")  // ✓ Only ID
        
        // Clear sensitive data after use
        val decrypted = decrypt(card.uid)
        try {
            processCard(decrypted)
        } finally {
            decrypted.fill(0)  // ✓ Clear memory
        }
    }
}

// Use secure storage for keys
class CryptoService {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    
    // Never hardcode keys
    private const val KEY = "my_secret_key"  // ✗ Bad
}
```

---

## Error Handling

### Use Result/sealed classes

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// Usage
suspend fun loadCards(): Result<List<Card>> {
    return try {
        val cards = database.cardDao().getAllCards()
        Result.Success(cards)
    } catch (e: Exception) {
        Result.Error(e)
    }
}
```

### Specific Exceptions

```kotlin
// Create specific exceptions
class CardNotFoundException(message: String) : Exception(message)
class CardEncryptionException(message: String, cause: Throwable?) : Exception(message, cause)

// Use when appropriate
fun getCardById(id: String): Card {
    return cards.find { it.id == id }
        ?: throw CardNotFoundException("Card not found: $id")
}
```

---

## Tools and Automation

### Pre-commit Hook

Create `.git/hooks/pre-commit`:

```bash
#!/bin/sh
# Run ktlint before commit
./gradlew ktlintCheck
if [ $? -ne 0 ]; then
    echo "ktlint check failed. Run './gradlew ktlintFormat' to fix."
    exit 1
fi
```

### IDE Configuration

**Android Studio Settings** (File > Settings):

- **Editor > Code Style > Kotlin**: Import Kotlin style guide
- **Editor > Inspections**: Enable all Kotlin inspections
- **Editor > File and Code Templates**: Set file headers

---

## Checklist

Before submitting code, ensure:

- [ ] Code follows naming conventions
- [ ] ktlint formatting applied
- [ ] No compiler warnings
- [ ] All tests pass
- [ ] New code has tests (≥80% coverage)
- [ ] Public APIs documented with KDoc
- [ ] No sensitive data logged
- [ ] No hardcoded strings (use resources)
- [ ] Null safety considered
- [ ] Performance optimized
- [ ] Security best practices followed

---

## References

- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)
- [Effective Kotlin](https://kt.academy/book/effectivekotlin)
- [Jetpack Compose Guidelines](https://developer.android.com/jetpack/compose/api-guidelines)

---

## Questions?

If you have questions about coding standards or best practices:
1. Check this guide first
2. Review existing codebase for examples
3. Ask in GitHub Discussions
4. Propose changes via PR to this guide
