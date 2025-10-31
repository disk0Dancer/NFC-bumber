# NFC AID Extraction and Emulation Improvements

## Overview

This document describes the improvements made to the NFC card emulation system to enable proper card usage by extracting and utilizing Application Identifiers (AIDs) from scanned cards.

## Problem

The app could successfully read NFC cards and door access keys, but users were unable to use them effectively with terminals and readers. This was because:

1. **No AID Extraction**: The app wasn't extracting AIDs (Application Identifiers) from scanned cards
2. **Generic AIDs Only**: Only hardcoded generic AIDs were registered in the HCE (Host Card Emulation) configuration
3. **Terminal Compatibility**: Terminals and readers expect specific AIDs that match the original card's applications

## Solution

### 1. AID Field Added to Data Model

**Changes:**
- Added `aids: List<String>` field to `Card` domain model
- Added `aids: String` field to `CardEntity` (stored as comma-separated values)
- Updated `NfcCardData` to include discovered AIDs

**Database Migration:**
- Database version bumped from 1 to 2
- Migration added to add `aids` column to existing cards table

### 2. AID Discovery During Card Scanning

**Implementation in `NfcReaderService.kt`:**

When scanning an ISO-DEP card, the app now probes common AIDs by sending SELECT commands:

```kotlin
private fun discoverAids(isoDep: IsoDep): List<String> {
    val discoveredAids = mutableListOf<String>()
    
    // Probe common AIDs for access control, transit, and payment systems
    val commonAids = listOf(
        "F0010203040506",      // Generic/Default AID
        "A0000000031010",      // Visa
        "A0000000041010",      // Mastercard
        "D2760000850100",      // MIFARE DESFire
        "D2760000850101",      // MIFARE DESFire EV1
        "315449432E494341",    // Transit card (STIC.ICA)
        "A000000618",          // Access control
        "A00000061701",        // Access control (HID)
        // ... and more
    )
    
    // Try each AID and check if card responds with success
    for (aid in commonAids) {
        val selectCommand = buildSelectApdu(aid)
        val response = isoDep.transceive(selectCommand)
        
        if (response.isSuccessful()) {
            discoveredAids.add(aid)
        }
    }
    
    return discoveredAids
}
```

### 3. Expanded HCE Configuration

**Updated `apduservice.xml`:**

Added comprehensive list of common AIDs for various card types:

- **Payment AIDs**: Visa, Mastercard, generic payment
- **Access Control AIDs**: HID, generic access control
- **Transit AIDs**: Common transport card systems
- **MIFARE DESFire AIDs**: Used in many access control systems

```xml
<aid-group android:description="@string/aid_group_description" android:category="other">
    <!-- Generic/Default AIDs -->
    <aid-filter android:name="F0010203040506"/>
    <aid-filter android:name="F04E4643424D42455200"/>
    
    <!-- Payment AIDs -->
    <aid-filter android:name="A0000000031010"/>
    <aid-filter android:name="A0000000041010"/>
    
    <!-- MIFARE DESFire AIDs (access control) -->
    <aid-filter android:name="D2760000850100"/>
    <aid-filter android:name="D2760000850101"/>
    
    <!-- Access control AIDs -->
    <aid-filter android:name="A000000618"/>
    <aid-filter android:name="A00000061701"/>
    
    <!-- And more... -->
</aid-group>
```

### 4. AID Validation in Emulation Service

**Updated `NfcEmulatorService.kt`:**

The emulation service now validates that incoming SELECT commands request AIDs that the card actually supports:

```kotlin
private fun handleSelectCommand(commandApdu: ByteArray): ByteArray {
    val card = getSelectedCard()
    
    // Extract requested AID from SELECT command
    val requestedAid = extractAidFromCommand(commandApdu)
    
    // Check if card supports this AID
    if (card.aids.isNotEmpty() && !card.aids.contains(requestedAid)) {
        Log.w(TAG, "Card does not support requested AID: $requestedAid")
        return SW_FILE_NOT_FOUND
    }
    
    // Return ATS and success if AID is supported
    return card.ats + SW_SUCCESS
}
```

## Benefits

### For Users

1. **Improved Compatibility**: Cards now work with more terminals and readers
2. **Accurate Emulation**: Only AIDs that the original card supports are emulated
3. **Better Debugging**: Logs show which AIDs are discovered and used

### For Developers

1. **Extensible**: Easy to add more AIDs to probe for
2. **Diagnostic Information**: AIDs are stored and visible for troubleshooting
3. **Future Features**: AID information enables advanced features like:
   - Card type detection
   - Application-specific handling
   - Terminal compatibility checking

## Technical Details

### AID Format

AIDs are hexadecimal strings representing Application Identifiers defined by ISO/IEC 7816-4:
- Length: 5-16 bytes
- Format: Hex string (e.g., "F0010203040506")
- Structure: RID (5 bytes) + PIX (0-11 bytes)

### SELECT APDU Command Format

```
CLA  INS  P1  P2  Lc  [AID bytes]
00   A4   04  00  XX  [XX bytes]
```

### Success Response

A successful SELECT returns:
```
[Response data] 90 00
```
Where `90 00` is the success status word (SW1 SW2).

## Limitations

### Android HCE Constraints

1. **Static AID Registration**: AIDs must be declared in `apduservice.xml` - they cannot be registered dynamically at runtime
2. **Category Restrictions**: Payment AIDs in the "payment" category require special handling
3. **Priority**: Only one app can handle a given AID at a time

### Card Type Limitations

1. **ISO-DEP Only**: AID extraction currently only works for ISO-DEP cards
2. **MIFARE Classic/Ultralight**: These card types don't use AIDs in the same way
3. **Proprietary Protocols**: Some cards use proprietary protocols that may not respond to standard SELECT commands

## Future Improvements

1. **User-Defined AIDs**: Allow users to manually add AIDs that weren't auto-discovered
2. **AID Learning Mode**: Record AIDs from failed terminal interactions
3. **Cloud AID Database**: Share discovered AIDs between users (with privacy controls)
4. **Card Type Detection**: Use AIDs to automatically detect card type (transit, access control, etc.)
5. **Application-Specific Features**: Enable/disable specific card applications

## Testing

To test AID discovery:

1. Scan a card with known AIDs (e.g., transit card, access card)
2. Check logs for "Discovered AID: XXX" messages
3. View card details to see stored AIDs
4. Test emulation with original terminal

## References

- [ISO/IEC 7816-4](https://www.iso.org/standard/54550.html) - Smart Card Standard
- [Android HCE Documentation](https://developer.android.com/guide/topics/connectivity/nfc/hce)
- [EMV Book 1](https://www.emvco.com/specifications/) - Payment Card Specifications
- [PC/SC Workgroup](https://www.pcscworkgroup.com/) - Smart Card Standards

## Migration Guide

For existing users:

1. **Automatic Migration**: Database will automatically add `aids` column with empty default
2. **Re-scan Cards**: Users should re-scan their cards to discover AIDs
3. **Backward Compatible**: Cards without AIDs will continue to work with generic emulation

## Troubleshooting

### Card Won't Work with Terminal

1. Check if AIDs were discovered during scanning (view logs)
2. Verify the terminal's expected AID is in the card's AID list
3. Try re-scanning the card to rediscover AIDs
4. Check if the AID is registered in `apduservice.xml`

### No AIDs Discovered

This can happen if:
- Card uses a non-standard protocol
- Card doesn't respond to SELECT commands
- Card requires authentication before SELECT
- Card is MIFARE Classic/Ultralight (they don't use ISO-DEP AIDs)

**Solution**: The card will still work with generic emulation, but may have reduced compatibility.

## Conclusion

These improvements significantly enhance the app's ability to emulate NFC cards by:
- Extracting actual AIDs from scanned cards
- Validating AID requests during emulation
- Supporting a wide range of card types and systems

This makes the app much more useful for real-world scenarios like door access, transit cards, and other applications.
