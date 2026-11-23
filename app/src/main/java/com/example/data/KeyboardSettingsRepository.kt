package com.example.data

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.nablaSettingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "nabla_keys_layout_settings"
)

class KeyboardSettingsRepository(private val context: Context) {

    private object Keys {
        val OUTPUT_MODE = stringPreferencesKey("output_mode")
        val CONSTANT_INSERT_MODE = stringPreferencesKey("constant_insert_mode")
        val COLUMNS_PER_ROW = intPreferencesKey("columns_per_row")
        val KEY_HEIGHT_PRESET = stringPreferencesKey("key_height_preset")
        val KEY_CORNER_PRESET = stringPreferencesKey("key_corner_preset")
        val COLORWAY = stringPreferencesKey("colorway")
        val DEFAULT_DECK = stringPreferencesKey("default_deck")
        val ENABLED_DECKS = stringPreferencesKey("enabled_decks")
        val PINNED_SYMBOL_IDS = stringPreferencesKey("pinned_symbol_ids")
        val CUSTOM_SYMBOLS = stringPreferencesKey("custom_symbols_v1")
        val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val SHOW_KEY_POPUP = booleanPreferencesKey("show_key_popup")
        val SHOW_LATEX_HINTS = booleanPreferencesKey("show_latex_hints")
        val AUTO_SPACE_OPERATORS = booleanPreferencesKey("auto_space_operators")
        val SHOW_PINNED_BAR = booleanPreferencesKey("show_pinned_bar")
        val SHOW_NUMBER_ROW = booleanPreferencesKey("show_number_row")
    }

    val settingsFlow: Flow<KeyboardLayoutSettings> = context.nablaSettingsDataStore.data.map { prefs ->
        val outputMode = prefs[Keys.OUTPUT_MODE]?.let { name ->
            OutputMode.entries.firstOrNull { it.name == name }
        } ?: OutputMode.UNICODE

        val constantMode = prefs[Keys.CONSTANT_INSERT_MODE]?.let { name ->
            ConstantInsertMode.entries.firstOrNull { it.name == name }
        } ?: ConstantInsertMode.SYMBOL

        val columns = (prefs[Keys.COLUMNS_PER_ROW] ?: 7).coerceIn(6, 9)

        val heightPreset = prefs[Keys.KEY_HEIGHT_PRESET]?.let { name ->
            KeyHeightPreset.entries.firstOrNull { it.name == name }
        } ?: KeyHeightPreset.BALANCED

        val cornerPreset = prefs[Keys.KEY_CORNER_PRESET]?.let { name ->
            KeyCornerPreset.entries.firstOrNull { it.name == name }
        } ?: KeyCornerPreset.TACTILE

        val colorway = prefs[Keys.COLORWAY]?.let { name ->
            KeyboardColorway.entries.firstOrNull { it.name == name }
        } ?: KeyboardColorway.OBSIDIAN

        val enabledDecks = prefs[Keys.ENABLED_DECKS]?.split(",")
            ?.mapNotNull { token -> KeyDeck.entries.firstOrNull { it.name == token } }
            ?.toSet()
            ?.takeIf { it.isNotEmpty() }
            ?: KeyDeck.entries.toSet()

        val defaultDeck = prefs[Keys.DEFAULT_DECK]?.let { name ->
            KeyDeck.entries.firstOrNull { it.name == name && it in enabledDecks }
        } ?: enabledDecks.first()

        val pinnedIds = prefs[Keys.PINNED_SYMBOL_IDS]?.let { raw ->
            if (raw.isBlank()) emptyList() else raw.split(",").filter { it.isNotBlank() }
        } ?: KeyboardLayoutSettings.DEFAULT_PINNED_IDS

        val customSymbols = decodeCustomSymbols(prefs[Keys.CUSTOM_SYMBOLS])

        KeyboardLayoutSettings(
            outputMode = outputMode,
            constantInsertMode = constantMode,
            columnsPerRow = columns,
            keyHeightPreset = heightPreset,
            keyCornerPreset = cornerPreset,
            colorway = colorway,
            defaultDeck = defaultDeck,
            enabledDecks = enabledDecks,
            pinnedSymbolIds = pinnedIds,
            customSymbols = customSymbols,
            hapticEnabled = prefs[Keys.HAPTIC_ENABLED] ?: true,
            showKeyPopup = prefs[Keys.SHOW_KEY_POPUP] ?: true,
            showLatexHints = prefs[Keys.SHOW_LATEX_HINTS] ?: true,
            autoSpaceOperators = prefs[Keys.AUTO_SPACE_OPERATORS] ?: false,
            showPinnedBar = prefs[Keys.SHOW_PINNED_BAR] ?: true,
            showNumberRowOnQwerty = prefs[Keys.SHOW_NUMBER_ROW] ?: true
        )
    }

    suspend fun setOutputMode(mode: OutputMode) {
        context.nablaSettingsDataStore.edit { it[Keys.OUTPUT_MODE] = mode.name }
    }

    suspend fun setConstantInsertMode(mode: ConstantInsertMode) {
        context.nablaSettingsDataStore.edit { it[Keys.CONSTANT_INSERT_MODE] = mode.name }
    }

    suspend fun setColumnsPerRow(columns: Int) {
        context.nablaSettingsDataStore.edit { it[Keys.COLUMNS_PER_ROW] = columns.coerceIn(6, 9) }
    }

    suspend fun setKeyHeightPreset(preset: KeyHeightPreset) {
        context.nablaSettingsDataStore.edit { it[Keys.KEY_HEIGHT_PRESET] = preset.name }
    }

    suspend fun setKeyCornerPreset(preset: KeyCornerPreset) {
        context.nablaSettingsDataStore.edit { it[Keys.KEY_CORNER_PRESET] = preset.name }
    }

    suspend fun setColorway(colorway: KeyboardColorway) {
        context.nablaSettingsDataStore.edit { it[Keys.COLORWAY] = colorway.name }
    }

    suspend fun setDefaultDeck(deck: KeyDeck) {
        context.nablaSettingsDataStore.edit { it[Keys.DEFAULT_DECK] = deck.name }
    }

    suspend fun toggleDeckEnabled(deck: KeyDeck) {
        context.nablaSettingsDataStore.edit { prefs ->
            val current = prefs[Keys.ENABLED_DECKS]?.split(",")
                ?.mapNotNull { token -> KeyDeck.entries.firstOrNull { it.name == token } }
                ?.toMutableSet()
                ?: KeyDeck.entries.toMutableSet()

            if (deck in current) {
                if (current.size > 1) {
                    current.remove(deck)
                }
            } else {
                current.add(deck)
            }
            prefs[Keys.ENABLED_DECKS] = KeyDeck.entries.filter { it in current }.joinToString(",") { it.name }
            val activeDefault = prefs[Keys.DEFAULT_DECK]
            if (activeDefault == null || current.none { it.name == activeDefault }) {
                prefs[Keys.DEFAULT_DECK] = current.first().name
            }
        }
    }

    suspend fun togglePinnedSymbol(symbolId: String) {
        context.nablaSettingsDataStore.edit { prefs ->
            val list = prefs[Keys.PINNED_SYMBOL_IDS]?.let { raw ->
                if (raw.isBlank()) mutableListOf() else raw.split(",").filter { it.isNotBlank() }.toMutableList()
            } ?: KeyboardLayoutSettings.DEFAULT_PINNED_IDS.toMutableList()

            if (symbolId in list) {
                list.remove(symbolId)
            } else {
                list.add(symbolId)
            }
            prefs[Keys.PINNED_SYMBOL_IDS] = list.joinToString(",")
        }
    }

    suspend fun movePinnedSymbol(symbolId: String, direction: Int) {
        context.nablaSettingsDataStore.edit { prefs ->
            val list = prefs[Keys.PINNED_SYMBOL_IDS]?.let { raw ->
                if (raw.isBlank()) mutableListOf() else raw.split(",").filter { it.isNotBlank() }.toMutableList()
            } ?: KeyboardLayoutSettings.DEFAULT_PINNED_IDS.toMutableList()

            val index = list.indexOf(symbolId)
            val target = index + direction
            if (index >= 0 && target in list.indices) {
                val item = list.removeAt(index)
                list.add(target, item)
                prefs[Keys.PINNED_SYMBOL_IDS] = list.joinToString(",")
            }
        }
    }

    suspend fun addCustomSymbol(label: String, unicode: String, latex: String, description: String) {
        val cleanLabel = label.trim().ifEmpty { unicode.trim().take(6) }
        val cleanUnicode = unicode.ifEmpty { cleanLabel }
        val cleanLatex = latex.trim().ifEmpty { cleanUnicode }
        val cleanDesc = description.trim().ifEmpty { "Custom macro ($cleanLabel)" }
        val newSymbol = ScientificSymbol(
            id = "custom_${System.currentTimeMillis()}",
            label = cleanLabel,
            unicode = cleanUnicode,
            latex = cleanLatex,
            name = cleanDesc,
            deck = KeyDeck.CALCULUS,
            isCustom = true
        )
        context.nablaSettingsDataStore.edit { prefs ->
            val existing = decodeCustomSymbols(prefs[Keys.CUSTOM_SYMBOLS]).toMutableList()
            existing.add(newSymbol)
            prefs[Keys.CUSTOM_SYMBOLS] = encodeCustomSymbols(existing)

            val pinned = prefs[Keys.PINNED_SYMBOL_IDS]?.let { raw ->
                if (raw.isBlank()) mutableListOf() else raw.split(",").filter { it.isNotBlank() }.toMutableList()
            } ?: KeyboardLayoutSettings.DEFAULT_PINNED_IDS.toMutableList()
            pinned.add(0, newSymbol.id)
            prefs[Keys.PINNED_SYMBOL_IDS] = pinned.joinToString(",")
        }
    }

    suspend fun removeCustomSymbol(symbolId: String) {
        context.nablaSettingsDataStore.edit { prefs ->
            val existing = decodeCustomSymbols(prefs[Keys.CUSTOM_SYMBOLS]).filterNot { it.id == symbolId }
            prefs[Keys.CUSTOM_SYMBOLS] = encodeCustomSymbols(existing)

            val pinned = prefs[Keys.PINNED_SYMBOL_IDS]?.let { raw ->
                if (raw.isBlank()) mutableListOf() else raw.split(",").filter { it.isNotBlank() }.toMutableList()
            } ?: KeyboardLayoutSettings.DEFAULT_PINNED_IDS.toMutableList()
            pinned.remove(symbolId)
            prefs[Keys.PINNED_SYMBOL_IDS] = pinned.joinToString(",")
        }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        context.nablaSettingsDataStore.edit { it[Keys.HAPTIC_ENABLED] = enabled }
    }

    suspend fun setShowKeyPopup(enabled: Boolean) {
        context.nablaSettingsDataStore.edit { it[Keys.SHOW_KEY_POPUP] = enabled }
    }

    suspend fun setShowLatexHints(enabled: Boolean) {
        context.nablaSettingsDataStore.edit { it[Keys.SHOW_LATEX_HINTS] = enabled }
    }

    suspend fun setAutoSpaceOperators(enabled: Boolean) {
        context.nablaSettingsDataStore.edit { it[Keys.AUTO_SPACE_OPERATORS] = enabled }
    }

    suspend fun setShowPinnedBar(enabled: Boolean) {
        context.nablaSettingsDataStore.edit { it[Keys.SHOW_PINNED_BAR] = enabled }
    }

    suspend fun setShowNumberRowOnQwerty(enabled: Boolean) {
        context.nablaSettingsDataStore.edit { it[Keys.SHOW_NUMBER_ROW] = enabled }
    }

    suspend fun resetToDefaults() {
        context.nablaSettingsDataStore.edit { it.clear() }
    }

    private fun encodeCustomSymbols(list: List<ScientificSymbol>): String {
        return list.joinToString("\n") { s ->
            listOf(s.id, s.label, s.unicode, s.latex, s.name)
                .joinToString("|") { encodeB64(it) }
        }
    }

    private fun decodeCustomSymbols(raw: String?): List<ScientificSymbol> {
        if (raw.isNullOrBlank()) return emptyList()
        return raw.lineSequence().mapNotNull { line ->
            val parts = line.split("|")
            if (parts.size < 5) return@mapNotNull null
            runCatching {
                ScientificSymbol(
                    id = decodeB64(parts[0]),
                    label = decodeB64(parts[1]),
                    unicode = decodeB64(parts[2]),
                    latex = decodeB64(parts[3]),
                    name = decodeB64(parts[4]),
                    deck = KeyDeck.CALCULUS,
                    isCustom = true
                )
            }.getOrNull()
        }.toList()
    }

    private fun encodeB64(value: String): String =
        Base64.encodeToString(value.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)

    private fun decodeB64(encoded: String): String =
        String(Base64.decode(encoded, Base64.NO_WRAP), Charsets.UTF_8)
}
