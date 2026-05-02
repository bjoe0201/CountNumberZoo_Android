package com.example.countnumber.data

enum class LayoutMode { GRID, SCATTERED }

data class GameSettings(
    val rounds: Int = 5,
    val maxCount: Int = 10,
    val voiceEnabled: Boolean = true,
    val layoutMode: LayoutMode = LayoutMode.GRID,
    val language: AppLanguage = AppLanguage.CHINESE
)
