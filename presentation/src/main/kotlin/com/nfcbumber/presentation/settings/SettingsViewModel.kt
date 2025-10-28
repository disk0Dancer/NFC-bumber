package com.nfcbumber.presentation.settings

import androidx.lifecycle.ViewModel
import com.nfcbumber.data.security.SecureStorage
import com.nfcbumber.domain.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val secureStorage: SecureStorage
) : ViewModel() {

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_DYNAMIC_COLOR = "dynamic_color"
    }

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _dynamicColor = MutableStateFlow(loadDynamicColor())
    val dynamicColor: StateFlow<Boolean> = _dynamicColor.asStateFlow()

    private fun loadThemeMode(): ThemeMode {
        val value = secureStorage.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(value)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    }

    private fun loadDynamicColor(): Boolean {
        return secureStorage.getBoolean(KEY_DYNAMIC_COLOR, true)
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        secureStorage.putString(KEY_THEME_MODE, mode.name)
    }

    fun setDynamicColor(enabled: Boolean) {
        _dynamicColor.value = enabled
        secureStorage.putBoolean(KEY_DYNAMIC_COLOR, enabled)
    }
}
