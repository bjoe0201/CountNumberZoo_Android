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
import androidx.compose.runtime.remember
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
        val cellPadding = 4.dp

        if (layoutMode == LayoutMode.SCATTERED) {
            // ── Scattered: pre-allocate 2–3× slots, randomly place animals ──

            // multiplier: more empty space for small counts
            val multiplier = when {
                count <= 5  -> 3
                count <= 15 -> 3
                else        -> 2
            }
            val totalSlots = count * multiplier

            // columns for the enlarged virtual grid
            val cols = when {
                totalSlots <= 6  -> totalSlots
                totalSlots <= 15 -> 5
                totalSlots <= 24 -> 6
                totalSlots <= 40 -> 8
                else             -> 10
            }
            val rows = (totalSlots + cols - 1) / cols

            // Cell size based on the bigger virtual grid
            val cellByWidth: Dp  = (availW / cols) - cellPadding * 2
            val cellByHeight: Dp = (availH / rows) - cellPadding * 2
            val cellSize: Dp = cellByWidth
                .coerceAtMost(cellByHeight)
                .coerceAtMost(96.dp)
                .coerceAtLeast(24.dp)
            val fontSize = (cellSize.value * 0.65f).sp

            // Pick `count` random slot indices from [0, totalSlots).
            // Re-randomise whenever the animal type or count changes (new round).
            val animalKey = animals.firstOrNull()?.emoji ?: ""
            val slotToAnimalIdx: Map<Int, Int> = remember(count, animalKey) {
                (0 until totalSlots)
                    .shuffled()
                    .take(count)
                    .sorted()
                    .mapIndexed { animalIdx, slotIdx -> slotIdx to animalIdx }
                    .toMap()
            }

            val needsScroll = cellByHeight < 24.dp

            FlowRow(
                modifier = if (needsScroll) Modifier.verticalScroll(rememberScrollState())
                           else Modifier,
                maxItemsInEachRow = cols,
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                repeat(totalSlots) { slotIdx ->
                    val animalIdx = slotToAnimalIdx[slotIdx]
                    if (animalIdx != null) {
                        // ── Filled slot: real tappable animal ──
                        val animal = animals[animalIdx]
                        val isTapped = animalIdx in tappedIndices
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(cellPadding)
                                .size(cellSize)
                                .clip(CircleShape)
                                .background(
                                    if (isTapped) SkyBlue.copy(alpha = 0.3f)
                                    else Color.Transparent
                                )
                                .border(
                                    width = if (isTapped) 2.dp else 0.dp,
                                    color = if (isTapped) SkyBlue else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { onTap(animalIdx) }
                        ) {
                            Text(text = animal.emoji, fontSize = fontSize)
                        }
                    } else {
                        // ── Empty slot: invisible placeholder ──
                        Box(
                            modifier = Modifier
                                .padding(cellPadding)
                                .size(cellSize)
                        )
                    }
                }
            }

        } else {
            // ── Grid: compact, fills available space evenly ──

            val cols = when {
                count == 1  -> 1
                count <= 4  -> count
                count <= 6  -> 3
                count <= 10 -> 5
                count <= 20 -> 5
                count <= 50 -> 8
                else        -> 10
            }
            val rows = (count + cols - 1) / cols

            val cellByWidth: Dp  = (availW / cols) - cellPadding * 2
            val cellByHeight: Dp = (availH / rows) - cellPadding * 2
            val cellSize: Dp = cellByWidth
                .coerceAtMost(cellByHeight)
                .coerceAtMost(96.dp)
                .coerceAtLeast(28.dp)
            val fontSize = (cellSize.value * 0.65f).sp

            val needsScroll = cellByHeight < 24.dp

            FlowRow(
                modifier = if (needsScroll) Modifier.verticalScroll(rememberScrollState())
                           else Modifier,
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
                                if (isTapped) SkyBlue.copy(alpha = 0.3f)
                                else Color.Transparent
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
}
