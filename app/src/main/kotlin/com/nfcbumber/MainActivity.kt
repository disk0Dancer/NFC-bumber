package com.nfcbumber

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nfcbumber.presentation.cardlist.CardListScreen
import com.nfcbumber.presentation.cardlist.CardListViewModel
import com.nfcbumber.presentation.scan.ScanCardScreen
import com.nfcbumber.presentation.scan.ScanCardViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for NFC Bumber application.
 * Entry point for the app UI and NFC handling.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private var scanViewModel: ScanCardViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize NFC adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        
        // Check if device supports NFC
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available on this device", Toast.LENGTH_LONG).show()
        } else if (nfcAdapter?.isEnabled == false) {
            Toast.makeText(this, "NFC is disabled. Please enable it in settings", Toast.LENGTH_LONG).show()
        }
        
        // Create pending intent for NFC foreground dispatch
        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
        
        setContent {
            NfcBumberTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(
                        onScanViewModelCreated = { scanViewModel = it }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Enable foreground dispatch for NFC
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        // Disable foreground dispatch
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        
        // Handle NFC tag detection
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            tag?.let {
                // Pass tag to scan view model if on scan screen
                scanViewModel?.onTagDetected(it)
            }
        }
    }
}

@Composable
fun MainNavigation(
    onScanViewModelCreated: (ScanCardViewModel) -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "cardList") {
        composable("cardList") {
            val viewModel: CardListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val selectedCardId by viewModel.selectedCardId.collectAsState()

            CardListScreen(
                uiState = uiState,
                selectedCardId = selectedCardId,
                onCardSelect = viewModel::selectCard,
                onAddCard = { navController.navigate("scan") },
                onDeleteCard = viewModel::deleteCard,
                onRefresh = viewModel::refresh
            )
        }

        composable("scan") {
            val viewModel: ScanCardViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            // Notify activity about view model
            LaunchedEffect(viewModel) {
                onScanViewModelCreated(viewModel)
            }

            // Clear view model reference when leaving screen
            DisposableEffect(Unit) {
                onDispose {
                    onScanViewModelCreated(null as? ScanCardViewModel ?: return@onDispose)
                    viewModel.resetState()
                }
            }

            ScanCardScreen(
                uiState = uiState,
                onSaveCard = viewModel::saveCard,
                onDismiss = {
                    viewModel.resetState()
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun NfcBumberTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
