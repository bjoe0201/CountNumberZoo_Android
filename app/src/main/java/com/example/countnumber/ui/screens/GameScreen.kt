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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var showExitDialog by remember { mutableStateOf(false) }

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

                // "有幾隻？" row with exit button on the right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = AppStrings.howMany(lang),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Button(
                        onClick = { showExitDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                        modifier = Modifier.height(36.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = when (lang) {
                                AppLanguage.CHINESE -> "退出測試"
                                AppLanguage.ENGLISH -> "Quit"
                                AppLanguage.JAPANESE -> "終了"
                            },
                            fontSize = 13.sp,
                            color = Color(0xFF555555)
                        )
                    }
                }

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

        // Exit confirmation dialog
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = {
                    Text(
                        text = when (lang) {
                            AppLanguage.CHINESE -> "退出測試"
                            AppLanguage.ENGLISH -> "Quit Game"
                            AppLanguage.JAPANESE -> "ゲームを終了"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = when (lang) {
                            AppLanguage.CHINESE -> "確定要退出測試嗎？\n目前的進度將不會被記錄。"
                            AppLanguage.ENGLISH -> "Are you sure you want to quit?\nYour current progress will not be saved."
                            AppLanguage.JAPANESE -> "本当にゲームを終了しますか？\n現在の進捗は記録されません。"
                        },
                        fontSize = 15.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            onGameOver()
                        }
                    ) {
                        Text(
                            text = when (lang) {
                                AppLanguage.CHINESE -> "確定退出"
                                AppLanguage.ENGLISH -> "Quit"
                                AppLanguage.JAPANESE -> "終了する"
                            },
                            color = WrongRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text(
                            text = when (lang) {
                                AppLanguage.CHINESE -> "繼續遊戲"
                                AppLanguage.ENGLISH -> "Keep Playing"
                                AppLanguage.JAPANESE -> "続ける"
                            },
                            color = PrimaryColor
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp)
            )
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
