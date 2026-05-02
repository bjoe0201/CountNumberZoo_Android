package com.example.countnumber.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countnumber.data.AppLanguage
import com.example.countnumber.data.AppStrings
import com.example.countnumber.data.GameSettings
import com.example.countnumber.game.GameViewModel
import com.example.countnumber.ui.components.AnimalGrid
import com.example.countnumber.ui.components.AnswerButtons
import com.example.countnumber.ui.theme.BackgroundColor
import com.example.countnumber.ui.theme.CorrectGreen
import com.example.countnumber.ui.theme.PrimaryColor
import com.example.countnumber.ui.theme.WrongRed

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    settings: GameSettings,
    onGameOver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = settings.language

    if (uiState.isGameOver) {
        onGameOver()
        return
    }

    val round = uiState.round ?: return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top: progress
            Column {
                Text(
                    text = AppStrings.roundProgress(lang, uiState.currentRound, uiState.totalRounds),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { uiState.currentRound.toFloat() / uiState.totalRounds },
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryColor
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = AppStrings.howMany(lang),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = AppStrings.tapToCount(lang),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Center: animal grid
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimalGrid(
                    animals = round.animals,
                    tappedIndices = round.tappedIndices,
                    layoutMode = settings.layoutMode,
                    onTap = { viewModel.tapAnimal(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Bottom: answer buttons
            Column {
                AnswerButtons(
                    answers = round.answers,
                    correctAnswer = round.correctAnswer,
                    selectedAnswer = round.selectedAnswer,
                    onSelect = { viewModel.selectAnswer(it) }
                )
            }
        }

        // Feedback overlay
        AnimatedVisibility(
            visible = uiState.showFeedback,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            val isCorrect = round.isCorrect == true
            Box(
                modifier = Modifier
                    .background(
                        color = if (isCorrect) CorrectGreen else WrongRed,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 48.dp, vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isCorrect) "\u2B50" else "\uD83D\uDE22",
                        fontSize = 48.sp
                    )
                    Text(
                        text = if (isCorrect) AppStrings.correct(lang) else AppStrings.wrong(lang),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (!isCorrect) {
                        Text(
                            text = "${round.correctAnswer}",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
