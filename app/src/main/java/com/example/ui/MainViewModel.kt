package com.example.ui

import android.app.Application
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ConstantInsertMode
import com.example.data.FormulaTemplate
import com.example.data.KeyCornerPreset
import com.example.data.KeyDeck
import com.example.data.KeyHeightPreset
import com.example.data.KeyboardColorway
import com.example.data.KeyboardLayoutSettings
import com.example.data.KeyboardSettingsRepository
import com.example.data.OutputMode
import com.example.ime.ImeStatus
import com.example.ime.ImeStatusHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppDestination {
    STUDIO,
    VAULT,
    LAYOUT
}

enum class VaultFilter(val label: String) {
    ALL("All"),
    CALCULUS("Calculus"),
    CONSTANTS("Constants"),
    GREEK("Greek"),
    LOGIC("Sets & Logic"),
    TEMPLATES("Templates")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = KeyboardSettingsRepository(application.applicationContext)

    val settings: StateFlow<KeyboardLayoutSettings> = repository.settingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = KeyboardLayoutSettings()
    )

    private val _currentDestination = MutableStateFlow(AppDestination.STUDIO)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    private val _activeDeck = MutableStateFlow(KeyDeck.CALCULUS)
    val activeDeck: StateFlow<KeyDeck> = _activeDeck.asStateFlow()

    private val _studioValue = MutableStateFlow(
        TextFieldValue(
            text = "∫₀^∞ e^{-x²} dx = √π / 2",
            selection = TextRange("∫₀^∞ e^{-x²} dx = √π / 2".length)
        )
    )
    val studioValue: StateFlow<TextFieldValue> = _studioValue.asStateFlow()

    private val undoStack = ArrayDeque<TextFieldValue>()
    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _vaultQuery = MutableStateFlow("")
    val vaultQuery: StateFlow<String> = _vaultQuery.asStateFlow()

    private val _vaultFilter = MutableStateFlow(VaultFilter.ALL)
    val vaultFilter: StateFlow<VaultFilter> = _vaultFilter.asStateFlow()

    private val _imeStatus = MutableStateFlow(ImeStatusHelper.getStatus(application))
    val imeStatus: StateFlow<ImeStatus> = _imeStatus.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun selectDestination(destination: AppDestination) {
        _currentDestination.value = destination
        if (destination == AppDestination.LAYOUT || destination == AppDestination.STUDIO) {
            refreshImeStatus()
        }
    }

    fun selectDeck(deck: KeyDeck) {
        _activeDeck.value = deck
    }

    fun refreshImeStatus() {
        _imeStatus.value = ImeStatusHelper.getStatus(getApplication())
    }

    fun updateStudioValue(newValue: TextFieldValue) {
        _studioValue.value = newValue
    }

    fun insertIntoStudio(insertion: String) {
        val current = _studioValue.value
        pushUndo(current)
        val text = current.text
        val start = current.selection.min.coerceIn(0, text.length)
        val end = current.selection.max.coerceIn(0, text.length)
        val updated = text.substring(0, start) + insertion + text.substring(end)
        val newCursor = start + insertion.length
        _studioValue.value = TextFieldValue(
            text = updated,
            selection = TextRange(newCursor)
        )
    }

    fun insertTemplate(template: FormulaTemplate) {
        val out = if (settings.value.outputMode == OutputMode.LATEX) {
            template.latex
        } else {
            template.unicode
        }
        insertIntoStudio(out)
    }

    fun backspaceStudio() {
        val current = _studioValue.value
        val text = current.text
        val start = current.selection.min.coerceIn(0, text.length)
        val end = current.selection.max.coerceIn(0, text.length)
        if (start == 0 && end == 0) return

        pushUndo(current)
        val (newText, newCursor) = if (start != end) {
            (text.substring(0, start) + text.substring(end)) to start
        } else {
            val prevChar = text[start - 1]
            val deleteLen = if (Character.isLowSurrogate(prevChar) && start >= 2) 2 else 1
            val targetStart = (start - deleteLen).coerceAtLeast(0)
            (text.substring(0, targetStart) + text.substring(start)) to targetStart
        }
        _studioValue.value = TextFieldValue(
            text = newText,
            selection = TextRange(newCursor)
        )
    }

    fun moveStudioCursor(delta: Int) {
        val current = _studioValue.value
        val text = current.text
        val pos = current.selection.end
        val target = when {
            delta < 0 -> {
                if (pos <= 0) 0
                else if (Character.isLowSurrogate(text[pos - 1]) && pos >= 2) pos - 2
                else pos - 1
            }
            delta > 0 -> {
                if (pos >= text.length) text.length
                else if (Character.isHighSurrogate(text[pos]) && pos + 1 < text.length) pos + 2
                else pos + 1
            }
            else -> pos
        }
        _studioValue.value = current.copy(selection = TextRange(target))
    }

    fun clearStudio() {
        if (_studioValue.value.text.isEmpty()) return
        pushUndo(_studioValue.value)
        _studioValue.value = TextFieldValue("", TextRange(0))
    }

    fun undoStudio() {
        if (undoStack.isEmpty()) return
        _studioValue.value = undoStack.removeLast()
        _canUndo.value = undoStack.isNotEmpty()
    }

    private fun pushUndo(snapshot: TextFieldValue) {
        if (undoStack.size >= 30) undoStack.removeFirst()
        undoStack.addLast(snapshot)
        _canUndo.value = true
    }

    fun setVaultQuery(query: String) {
        _vaultQuery.value = query
    }

    fun setVaultFilter(filter: VaultFilter) {
        _vaultFilter.value = filter
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // Layout Settings Mutations
    fun toggleOutputMode() = viewModelScope.launch {
        val next = if (settings.value.outputMode == OutputMode.UNICODE) {
            OutputMode.LATEX
        } else {
            OutputMode.UNICODE
        }
        repository.setOutputMode(next)
    }

    fun setOutputMode(mode: OutputMode) = viewModelScope.launch {
        repository.setOutputMode(mode)
    }

    fun setConstantInsertMode(mode: ConstantInsertMode) = viewModelScope.launch {
        repository.setConstantInsertMode(mode)
    }

    fun setColumnsPerRow(columns: Int) = viewModelScope.launch {
        repository.setColumnsPerRow(columns)
    }

    fun setKeyHeightPreset(preset: KeyHeightPreset) = viewModelScope.launch {
        repository.setKeyHeightPreset(preset)
    }

    fun setKeyCornerPreset(preset: KeyCornerPreset) = viewModelScope.launch {
        repository.setKeyCornerPreset(preset)
    }

    fun setColorway(colorway: KeyboardColorway) = viewModelScope.launch {
        repository.setColorway(colorway)
    }

    fun setDefaultDeck(deck: KeyDeck) = viewModelScope.launch {
        repository.setDefaultDeck(deck)
        _activeDeck.value = deck
    }

    fun toggleDeckEnabled(deck: KeyDeck) = viewModelScope.launch {
        repository.toggleDeckEnabled(deck)
    }

    fun togglePinnedSymbol(symbolId: String) = viewModelScope.launch {
        repository.togglePinnedSymbol(symbolId)
    }

    fun movePinnedSymbol(symbolId: String, direction: Int) = viewModelScope.launch {
        repository.movePinnedSymbol(symbolId, direction)
    }

    fun addCustomSymbol(label: String, unicode: String, latex: String, description: String) =
        viewModelScope.launch {
            repository.addCustomSymbol(label, unicode, latex, description)
            showToast("Added custom key \"$label\" to Quick Bar")
        }

    fun removeCustomSymbol(symbolId: String) = viewModelScope.launch {
        repository.removeCustomSymbol(symbolId)
    }

    fun setHapticEnabled(enabled: Boolean) = viewModelScope.launch {
        repository.setHapticEnabled(enabled)
    }

    fun setShowKeyPopup(enabled: Boolean) = viewModelScope.launch {
        repository.setShowKeyPopup(enabled)
    }

    fun setShowLatexHints(enabled: Boolean) = viewModelScope.launch {
        repository.setShowLatexHints(enabled)
    }

    fun setAutoSpaceOperators(enabled: Boolean) = viewModelScope.launch {
        repository.setAutoSpaceOperators(enabled)
    }

    fun setShowPinnedBar(enabled: Boolean) = viewModelScope.launch {
        repository.setShowPinnedBar(enabled)
    }

    fun setShowNumberRowOnQwerty(enabled: Boolean) = viewModelScope.launch {
        repository.setShowNumberRowOnQwerty(enabled)
    }

    fun resetSettingsToDefaults() = viewModelScope.launch {
        repository.resetToDefaults()
        _activeDeck.value = KeyDeck.CALCULUS
        showToast("Layout settings reset to defaults")
    }
}
