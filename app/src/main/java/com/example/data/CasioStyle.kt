package com.example.data

/**
 * Casio-style textbook input helpers.
 *
 * Fractions and radicals are inserted as templates with the cursor placed
 * inside the placeholder, so a complete number or expression can be typed
 * inside them (e.g. \frac{10}{2}, \sqrt{123}, \sqrt[n]{x+y}).
 */
object CasioStyle {
    data class Template(val text: String, val cursorOffset: Int)

    fun fraction(mode: OutputMode): Template {
        return if (mode == OutputMode.LATEX) {
            Template("\\frac{}{}", 6)
        } else {
            Template("( )⁄( )", 1)
        }
    }

    fun radical(index: String?, mode: OutputMode): Template {
        if (mode == OutputMode.LATEX) {
            val text = if (index.isNullOrEmpty()) "\\sqrt{}" else "\\sqrt[$index]{}"
            return Template(text, text.indexOf("{}") + 1)
        }
        val glyph = when (index) {
            null, "" -> "√"
            "3" -> "∛"
            "4" -> "∜"
            else -> "$index√"
        }
        val text = "$glyph()"
        return Template(text, glyph.length + 1)
    }

    fun templateFor(
        symbolId: String,
        settings: KeyboardLayoutSettings,
        shifted: Boolean = false
    ): Template? {
        return when (symbolId) {
            "calc_frac" -> fraction(settings.outputMode)
            "calc_nth_root" -> radical("n", settings.outputMode)
            "calc_sqrt" -> radical(if (shifted) "3" else null, settings.outputMode)
            else -> null
        }
    }

    fun indexForVariant(label: String): String? = when (label) {
        "∛", "\\sqrt[3]{}" -> "3"
        "∜", "\\sqrt[4]{}" -> "4"
        else -> null
    }

    sealed interface Segment {
        data class Plain(val text: String) : Segment
        data class Fraction(val numerator: String, val denominator: String) : Segment
        data class Radical(val index: String?, val content: String) : Segment
        data class NthRoot(val index: String, val content: String) : Segment
    }

    /** Splits an expression into plain text, fractions and radicals for Natural Display rendering. */
    fun parse(source: String): List<Segment> {
        data class Hit(val start: Int, val end: Int, val segment: Segment)

        val hits = mutableListOf<Hit>()
        val latexFraction = Regex("""\\frac\{([^{}]*(?:\{[^{}]*\}[^{}]*)*)\}\{([^{}]*(?:\{[^{}]*\}[^{}]*)*)\}""")
        latexFraction.findAll(source).forEach {
            hits += Hit(
                it.range.first,
                it.range.last + 1,
                Segment.Fraction(it.groupValues[1], it.groupValues[2])
            )
        }

        val plainFraction = Regex("""(${mathToken})\s*/\s*(${mathToken})""")
        plainFraction.findAll(source).forEach {
            val precededByCommand = source.getOrNull(it.range.first - 1)?.let { c -> c == '\\' || c == '/' } ?: false
            if (!precededByCommand) {
                hits += Hit(
                    it.range.first,
                    it.range.last + 1,
                    Segment.Fraction(it.groupValues[1].trim(), it.groupValues[2].trim())
                )
            }
        }

        val latexRoot = Regex("""\\sqrt(?:\[([^\]]*)\])?\{([^{}]*(?:\{[^{}]*\}[^{}]*)*)\}""")
        latexRoot.findAll(source).forEach {
            val index = it.groupValues[1]
            val content = it.groupValues[2]
            hits += Hit(
                it.range.first,
                it.range.last + 1,
                if (index.isEmpty()) Segment.Radical(null, content) else Segment.NthRoot(index, content)
            )
        }

        val unicodeRoot = Regex("""(∛|∜|√)([A-Za-z0-9πeθλμ+\-.]*(?:\([^)]*\))?)""")
        unicodeRoot.findAll(source).forEach {
            val index = when (it.groupValues[1]) {
                "∛" -> "3"
                "∜" -> "4"
                else -> null
            }
            if (it.groupValues[2].isNotEmpty()) {
                hits += Hit(
                    it.range.first,
                    it.range.last + 1,
                    if (index == null) Segment.Radical(null, it.groupValues[2])
                    else Segment.NthRoot(index, it.groupValues[2])
                )
            }
        }

        val accepted = mutableListOf<Hit>()
        for (hit in hits.sortedWith(compareBy<Hit> { it.start }.thenByDescending { it.end - it.start })) {
            if (accepted.none { hit.start < it.end && it.start < hit.end }) accepted += hit
        }

        val result = mutableListOf<Segment>()
        var cursor = 0
        for (hit in accepted.sortedBy { it.start }) {
            if (hit.start > cursor) result += Segment.Plain(source.substring(cursor, hit.start))
            result += hit.segment
            cursor = hit.end
        }
        if (cursor < source.length) result += Segment.Plain(source.substring(cursor))
        return result
    }

    private val mathToken = """(?:\\sqrt(?:\[[^\]]*\])?\{[^}]*\}|\\frac\{[^}]*\}\{[^}]*\}|[A-Za-z0-9πeθλμ∂∇∫∈∞±×·²³⁰¹⁴⁵⁶⁷⁸⁹_.-]+(?:\([^)]*\))?)"""
    private val fractionRegex = Regex("""(${mathToken})\s*/\s*(${mathToken})""")
    private val sqrtRegex = Regex("""√([A-Za-z0-9πeθλμ+\-.]*(?:\([^)]*\))?)""")
    private val cbrtRegex = Regex("""∛([A-Za-z0-9πeθλμ+\-.]*(?:\([^)]*\))?)""")
    private val fourthRootRegex = Regex("""∜([A-Za-z0-9πeθλμ+\-.]*(?:\([^)]*\))?)""")

    /**
     * Converts plain-text divisions and roots to LaTeX textbook style so that
     * copied output matches what a Casio natural display shows:
     * "10/2 = 5" -> "\frac{10}{2} = 5", "x/y" -> "\frac{x}{y}",
     * "√π / 2" -> "\frac{\sqrt{π}}{2}".
     */
    fun toLatexFractions(source: String): String {
        var out = sqrtRegex.replace(source) { "\\sqrt{${it.groupValues[1]}}" }
        out = cbrtRegex.replace(out) { "\\sqrt[3]{${it.groupValues[1]}}" }
        out = fourthRootRegex.replace(out) { "\\sqrt[4]{${it.groupValues[1]}}" }
        return fractionRegex.replace(out) { match ->
            "\\frac{${match.groupValues[1].trim()}}{${match.groupValues[2].trim()}}"
        }
    }
}
