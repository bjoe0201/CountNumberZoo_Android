package com.example.countnumber.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countnumber.data.Animal
import com.example.countnumber.data.LayoutMode
import com.example.countnumber.ui.theme.SkyBlue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimalGrid(
    animals: List<Animal>,
    tappedIndices: Set<Int>,
    layoutMode: LayoutMode,
    onTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val count = animals.size

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val availW = maxWidth
        val availH = maxHeight

        val cols = when {
            count == 1 -> 1
            count <= 4 -> count
            count <= 6 -> 3
            count <= 10 -> 5
            count <= 20 -> 5
            count <= 50 -> 8
            else -> 10
        }
        val rows = (count + cols - 1) / cols

        // Padding per cell
        val cellPadding = 4.dp

        // Max cell size constrained by both width and height
        val cellByWidth: Dp = (availW / cols) - cellPadding * 2
        val cellByHeight: Dp = (availH / rows) - cellPadding * 2
        val cellSize: Dp = cellByWidth
            .coerceAtMost(cellByHeight)
            .coerceAtMost(96.dp)
            .coerceAtLeast(28.dp)

        val fontSize = (cellSize.value * 0.65f).sp

        val needsScroll = (cellByHeight < 24.dp)

        FlowRow(
            modifier = if (needsScroll) {
                Modifier.verticalScroll(rememberScrollState())
            } else {
                Modifier
            },
            maxItemsInEachRow = cols,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            animals.forEachIndexed { index, animal ->
                val isTapped = index in tappedIndices
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(cellPadding)
                        .size(cellSize)
                        .clip(CircleShape)
                        .background(
                            if (isTapped) SkyBlue.copy(alpha = 0.3f) else Color.Transparent
                        )
                        .border(
                            width = if (isTapped) 2.dp else 0.dp,
                            color = if (isTapped) SkyBlue else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { onTap(index) }
                ) {
                    Text(text = animal.emoji, fontSize = fontSize)
                }
            }
        }
    }
}
