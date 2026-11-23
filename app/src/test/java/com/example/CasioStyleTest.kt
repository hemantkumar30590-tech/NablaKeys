package com.example

import com.example.data.CasioStyle
import com.example.data.OutputMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CasioStyleTest {

    @Test
    fun `fraction template keeps cursor inside numerator`() {
        val template = CasioStyle.fraction(OutputMode.LATEX)
        assertEquals("\\frac{}{}", template.text)
        assertEquals(6, template.cursorOffset)
    }

    @Test
    fun `radical template keeps cursor inside full radicand`() {
        val template = CasioStyle.radical(null, OutputMode.LATEX)
        assertEquals("\\sqrt{}", template.text)
        assertEquals(6, template.cursorOffset)
        assertEquals("\\sqrt[n]{}", CasioStyle.radical("n", OutputMode.LATEX).text)
    }

    @Test
    fun `copy converts division to textbook fraction style`() {
        assertEquals("\\frac{10}{2} = 5", CasioStyle.toLatexFractions("10/2 = 5"))
        assertEquals("\\frac{x}{y}", CasioStyle.toLatexFractions("x/y"))
        assertEquals("\\frac{\\sqrt{π}}{2}", CasioStyle.toLatexFractions("√π / 2"))
    }

    @Test
    fun `parse finds stacked fraction segments`() {
        val segments = CasioStyle.parse("1 + 10/2 = 5")
        assertEquals(3, segments.size)
        assertTrue(segments[1] is CasioStyle.Segment.Fraction)
    }
}
