package com.example.countnumber.data

enum class LayoutMode { GRID, SCATTERED }

enum class VoiceMode { NONE, NUMBER, NUMBER_WITH_ANIMAL }

data class GameSettings(
    val rounds: Int = 5,
    val maxCount: Int = 10,
    val voiceMode: VoiceMode = VoiceMode.NUMBER,
    val layoutMode: LayoutMode = LayoutMode.GRID,
    val language: AppLanguage = AppLanguage.CHINESE
)
