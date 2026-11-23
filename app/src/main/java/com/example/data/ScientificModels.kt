package com.example.data

enum class KeyDeck(val shortLabel: String, val displayName: String) {
    CALCULUS("Calc", "Advanced Calculus"),
    CONSTANTS("Const", "Constants"),
    GREEK("αβγ", "Greek Alphabet"),
    LOGIC("∀∈ℝ", "Sets & Logic"),
    SCRIPTS("xⁿ", "Scripts & Ops"),
    QWERTY("ABC", "QWERTY + Math")
}

enum class OutputMode(val label: String) {
    UNICODE("Unicode"),
    LATEX("LaTeX")
}

enum class ConstantInsertMode(val label: String, val description: String) {
    SYMBOL("Symbol", "Insert glyph (e.g. ℏ or \\hbar)"),
    VALUE("Value", "Insert numerical value (e.g. 1.05457e-34)"),
    BOTH("Symbol = Value", "Insert assignment (e.g. ℏ = 1.05457e-34)")
}

enum class KeyboardColorway(val displayName: String) {
    OBSIDIAN("Obsidian Copper"),
    BLUEPRINT("Blueprint Cyan"),
    CHALK("Paper Chalk"),
    EMERALD_LAB("Phosphor Lab")
}

enum class KeyHeightPreset(val displayName: String, val heightDp: Int) {
    COMPACT("Compact", 40),
    BALANCED("Balanced", 47),
    TALL("Tall", 55)
}

enum class KeyCornerPreset(val displayName: String, val radiusDp: Int) {
    CRISP("Crisp", 4),
    TACTILE("Tactile", 9),
    PILL("Rounded", 16)
}

data class SymbolVariant(
    val label: String,
    val unicode: String,
    val latex: String,
    val description: String = ""
)

data class ScientificSymbol(
    val id: String,
    val label: String,
    val unicode: String,
    val latex: String,
    val name: String,
    val deck: KeyDeck,
    val numericValue: String? = null,
    val units: String? = null,
    val shiftedLabel: String? = null,
    val shiftedUnicode: String? = null,
    val shiftedLatex: String? = null,
    val variants: List<SymbolVariant> = emptyList(),
    val isCustom: Boolean = false
)

data class FormulaTemplate(
    val id: String,
    val shortLabel: String,
    val title: String,
    val unicode: String,
    val latex: String,
    val domain: String
)

data class KeyboardLayoutSettings(
    val outputMode: OutputMode = OutputMode.UNICODE,
    val constantInsertMode: ConstantInsertMode = ConstantInsertMode.SYMBOL,
    val columnsPerRow: Int = 7,
    val keyHeightPreset: KeyHeightPreset = KeyHeightPreset.BALANCED,
    val keyCornerPreset: KeyCornerPreset = KeyCornerPreset.TACTILE,
    val colorway: KeyboardColorway = KeyboardColorway.OBSIDIAN,
    val defaultDeck: KeyDeck = KeyDeck.CALCULUS,
    val enabledDecks: Set<KeyDeck> = KeyDeck.entries.toSet(),
    val pinnedSymbolIds: List<String> = DEFAULT_PINNED_IDS,
    val customSymbols: List<ScientificSymbol> = emptyList(),
    val hapticEnabled: Boolean = true,
    val showKeyPopup: Boolean = true,
    val showLatexHints: Boolean = true,
    val autoSpaceOperators: Boolean = false,
    val showPinnedBar: Boolean = true,
    val showNumberRowOnQwerty: Boolean = true
) {
    companion object {
        val DEFAULT_PINNED_IDS = listOf(
            "calc_int",
            "calc_partial",
            "calc_nabla",
            "calc_sum",
            "calc_inf",
            "calc_lim",
            "const_pi",
            "const_hbar",
            "calc_arrow",
            "calc_dx"
        )
    }
}
