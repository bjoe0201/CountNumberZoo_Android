package com.example.countnumber.game

import com.example.countnumber.data.Animal

data class RoundState(
    val animals: List<Animal>,
    val count: Int,
    val answers: List<Int>,
    val correctAnswer: Int,
    val tappedIndices: Set<Int> = emptySet(),
    val selectedAnswer: Int? = null,
    val isCorrect: Boolean? = null
)

data class GameUiState(
    val currentRound: Int = 0,
    val totalRounds: Int = 5,
    val round: RoundState? = null,
    val score: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val isGameOver: Boolean = false,
    val showFeedback: Boolean = false
)
