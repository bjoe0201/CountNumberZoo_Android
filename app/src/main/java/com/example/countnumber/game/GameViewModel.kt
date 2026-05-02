package com.example.countnumber.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countnumber.data.ALL_ANIMALS
import com.example.countnumber.data.Animal
import com.example.countnumber.data.GameSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var settings: GameSettings
    private var onSpeakNumber: ((Int) -> Unit)? = null
    private var onSpeakFeedback: ((Boolean) -> Unit)? = null

    fun init(
        gameSettings: GameSettings,
        speakNumber: (Int) -> Unit,
        speakFeedback: (Boolean) -> Unit
    ) {
        settings = gameSettings
        onSpeakNumber = speakNumber
        onSpeakFeedback = speakFeedback
        _uiState.value = GameUiState(totalRounds = gameSettings.rounds)
        startNextRound()
    }

    private fun startNextRound() {
        val state = _uiState.value
        if (state.currentRound >= state.totalRounds) {
            _uiState.value = state.copy(isGameOver = true)
            return
        }
        val count = Random.nextInt(1, settings.maxCount + 1)
        val animal = ALL_ANIMALS.random()
        val animals = List(count) { animal }
        val answers = generateAnswers(count, settings.maxCount)
        _uiState.value = state.copy(
            currentRound = state.currentRound + 1,
            round = RoundState(
                animals = animals,
                count = count,
                answers = answers,
                correctAnswer = count
            ),
            showFeedback = false
        )
    }

    fun tapAnimal(index: Int) {
        val round = _uiState.value.round ?: return
        if (round.selectedAnswer != null) return
        val newTapped = round.tappedIndices + index
        val newRound = round.copy(tappedIndices = newTapped)
        _uiState.value = _uiState.value.copy(round = newRound)
        if (settings.voiceEnabled) {
            onSpeakNumber?.invoke(newTapped.size)
        }
    }

    fun selectAnswer(answer: Int) {
        val state = _uiState.value
        val round = state.round ?: return
        if (round.selectedAnswer != null) return

        val isCorrect = answer == round.correctAnswer
        val newRound = round.copy(selectedAnswer = answer, isCorrect = isCorrect)
        val newCorrect = if (isCorrect) state.correctCount + 1 else state.correctCount
        val newWrong = if (!isCorrect) state.wrongCount + 1 else state.wrongCount
        val newScore = max(0, settings.rounds * settings.maxCount * (newCorrect - newWrong))

        _uiState.value = state.copy(
            round = newRound,
            correctCount = newCorrect,
            wrongCount = newWrong,
            score = newScore,
            showFeedback = true
        )

        viewModelScope.launch {
            if (settings.voiceEnabled) {
                onSpeakNumber?.invoke(answer)
                delay(900)
                onSpeakFeedback?.invoke(isCorrect)
            }
            delay(1500)
            startNextRound()
        }
    }

    private fun generateAnswers(correct: Int, maxCount: Int): List<Int> {
        val distractors = mutableSetOf<Int>()
        var attempts = 0
        while (distractors.size < 4 && attempts < 200) {
            attempts++
            val delta = if (maxCount <= 20) Random.nextInt(1, 6) else max(1, (maxCount * 0.1).toInt())
            val sign = if (Random.nextBoolean()) 1 else -1
            val candidate = correct + sign * delta
            if (candidate > 0 && candidate != correct && candidate <= maxCount + delta) {
                distractors.add(candidate)
            }
        }
        var fill = 1
        while (distractors.size < 4) {
            if (fill != correct && !distractors.contains(fill)) distractors.add(fill)
            fill++
        }
        return (distractors.take(4) + correct).shuffled()
    }
}

