package com.nfcbumber.presentation.cardlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nfcbumber.domain.util.toHexString
import com.nfcbumber.domain.model.Card

/**
 * Main screen displaying the list of cards as a horizontal slider.
 * Allows users to select, view, and add cards.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    uiState: CardListUiState,
    selectedCardId: Long?,
    searchQuery: String,
    backupState: BackupState,
    onCardSelect: (Long) -> Unit,
    onAddCard: () -> Unit,
    onDeleteCard: (Long) -> Unit,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onExportCards: (String) -> Unit,
    onImportCards: (String) -> Unit,
    onResetBackupState: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    if (showSearchBar) {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = onSearchQueryChange,
                            onClose = { 
                                showSearchBar = false
                                onSearchQueryChange("")
                            }
                        )
                    } else {
                        Text("NFC Card Emulator") 
                    }
                },
                actions = {
                    if (!showSearchBar) {
                        IconButton(onClick = { showSearchBar = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Export Cards") },
                            leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                showExportDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Import Cards") },
                            leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                showImportDialog = true
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                onNavigateToSettings()
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCard
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Card")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is CardListUiState.Loading -> {
                    LoadingContent()
                }
                is CardListUiState.Empty -> {
                    if (searchQuery.isEmpty()) {
                        EmptyContent(onAddCard = onAddCard)
                    } else {
                        EmptySearchContent(onClearSearch = { onSearchQueryChange("") })
                    }
                }
                is CardListUiState.Success -> {
                    CardListContent(
                        cards = uiState.cards,
                        selectedCardId = selectedCardId,
                        onCardSelect = onCardSelect,
                        onDeleteCard = onDeleteCard
                    )
                }
                is CardListUiState.Error -> {
                    ErrorContent(
                        message = uiState.message,
                        onRetry = onRefresh
                    )
                }
            }
        }
    }

    // Backup state dialogs
    when (backupState) {
        is BackupState.Exporting -> {
            LoadingDialog("Exporting cards...")
        }
        is BackupState.Importing -> {
            LoadingDialog("Importing cards...")
        }
        is BackupState.ExportSuccess -> {
            SuccessDialog(
                title = "Export Successful",
                message = "${backupState.cardsCount} cards exported successfully",
                onDismiss = onResetBackupState
            )
        }
        is BackupState.ImportSuccess -> {
            SuccessDialog(
                title = "Import Successful",
                message = "${backupState.imported} cards imported, ${backupState.skipped} skipped (already exist)",
                onDismiss = onResetBackupState
            )
        }
        is BackupState.Error -> {
            ErrorDialog(
                message = backupState.message,
                onDismiss = onResetBackupState
            )
        }
        BackupState.Idle -> { /* No dialog */ }
    }

    // Export dialog
    if (showExportDialog) {
        ExportDialog(
            onConfirm = { fileName ->
                onExportCards(fileName)
                showExportDialog = false
            },
            onDismiss = { showExportDialog = false }
        )
    }

    // Import dialog
    if (showImportDialog) {
        ImportDialog(
            onConfirm = { fileName ->
                onImportCards(fileName)
                showImportDialog = false
            },
            onDismiss = { showImportDialog = false }
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search cards...") },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close search")
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyContent(onAddCard: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountBox,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No Cards Yet",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the + button to scan your first NFC card",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddCard) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Card")
        }
    }
}

@Composable
private fun EmptySearchContent(onClearSearch: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No Results Found",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try adjusting your search query",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onClearSearch) {
            Text("Clear Search")
        }
    }
}

@Composable
private fun CardListContent(
    cards: List<Card>,
    selectedCardId: Long?,
    onCardSelect: (Long) -> Unit,
    onDeleteCard: (Long) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Card slider
        val listState = rememberLazyListState()
        
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(cards, key = { it.id }) { card ->
                CardItem(
                    card = card,
                    isSelected = card.id == selectedCardId,
                    onClick = { onCardSelect(card.id) }
                )
            }
        }

        // Selected card details
        selectedCardId?.let { id ->
            val selectedCard = cards.find { it.id == id }
            selectedCard?.let { card ->
                Divider()
                CardDetailsSection(
                    card = card,
                    onDelete = { onDeleteCard(card.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardItem(
    card: Card,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(card.color)
        ),
        border = if (isSelected) {
            BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
        } else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = card.name,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Column {
                Text(
                    text = card.cardType.name.replace('_', ' '),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    text = "Used ${card.usageCount} times",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun CardDetailsSection(
    card: Card,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Card Details",
            style = MaterialTheme.typography.titleLarge
        )

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DetailRow("Name", card.name)
                DetailRow("Type", card.cardType.name.replace('_', ' '))
                DetailRow("UID", card.uid.toHexString())
                card.ats?.let {
                    DetailRow("ATS", it.toHexString())
                }
                card.historicalBytes?.let {
                    DetailRow("Historical Bytes", it.toHexString())
                }
                DetailRow("Usage Count", card.usageCount.toString())
                DetailRow("Created", card.createdAt.toString())
                card.lastUsedAt?.let {
                    DetailRow("Last Used", it.toString())
                }
            }
        }

        Button(
            onClick = { showDeleteDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Delete Card")
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Card") },
            text = { Text("Are you sure you want to delete this card? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2f)
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun LoadingDialog(message: String) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = { },
        text = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Text(message)
            }
        }
    )
}

@Composable
private fun SuccessDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun ExportDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var fileName by remember { mutableStateOf("nfc_cards_backup.json") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Export Cards") },
        text = {
            Column {
                Text("Enter file name for the backup:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val downloadDir = android.os.Environment.getExternalStoragePublicDirectory(
                        android.os.Environment.DIRECTORY_DOWNLOADS
                    )
                    val filePath = "${downloadDir.absolutePath}/$fileName"
                    onConfirm(filePath)
                }
            ) {
                Text("Export")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ImportDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var fileName by remember { mutableStateOf("nfc_cards_backup.json") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Import Cards") },
        text = {
            Column {
                Text("Enter file name to import from:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val downloadDir = android.os.Environment.getExternalStoragePublicDirectory(
                        android.os.Environment.DIRECTORY_DOWNLOADS
                    )
                    val filePath = "${downloadDir.absolutePath}/$fileName"
                    onConfirm(filePath)
                }
            ) {
                Text("Import")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
