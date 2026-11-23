package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CasioStyle
import com.example.ui.theme.MathMonoFontFamily

/**
 * Casio Natural Display style preview: fractions are drawn with a number on
 * top, a straight horizontal line in the middle and a number below it.
 * Radicals get an overbar, with the full radicand inside it.
 */
@Composable
fun CasioNaturalPreview(text: String, modifier: Modifier = Modifier) {
    val segments = remember(text) { CasioStyle.parse(text) }
    val color = MaterialTheme.colorScheme.primary
    val label = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        segments.forEach { segment ->
            when (segment) {
                is CasioStyle.Segment.Plain -> Text(
                    text = segment.text,
                    fontFamily = MathMonoFontFamily,
                    fontSize = 20.sp,
                    color = label
                )

                is CasioStyle.Segment.Fraction -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp)
                ) {
                    Text(segment.numerator, fontFamily = MathMonoFontFamily, fontSize = 16.sp, color = label)
                    Box(
                        modifier = Modifier
                            .widthIn(min = 20.dp)
                            .fillMaxWidth()
                            .height(1.5.dp)
                            .background(color)
                    )
                    Text(segment.denominator, fontFamily = MathMonoFontFamily, fontSize = 16.sp, color = label)
                }

                is CasioStyle.Segment.Radical -> Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!segment.index.isNullOrEmpty() && segment.index != "2") {
                        Text(segment.index, fontFamily = MathMonoFontFamily, fontSize = 11.sp, color = color)
                    }
                    Text("√", fontFamily = MathMonoFontFamily, fontSize = 23.sp, color = color)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(segment.content, fontFamily = MathMonoFontFamily, fontSize = 16.sp, color = label)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.5.dp)
                                .background(color)
                        )
                    }
                }

                is CasioStyle.Segment.NthRoot -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        segment.index,
                        fontFamily = MathMonoFontFamily,
                        fontSize = 11.sp,
                        color = color,
                        modifier = Modifier.padding(end = 1.dp)
                    )
                    Text("√", fontFamily = MathMonoFontFamily, fontSize = 23.sp, color = color)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(segment.content, fontFamily = MathMonoFontFamily, fontSize = 16.sp, color = label)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.5.dp)
                                .background(color)
                        )
                    }
                }
            }
        }
    }
}
