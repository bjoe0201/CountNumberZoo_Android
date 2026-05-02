package com.example.countnumber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.countnumber.data.AppLanguage
import com.example.countnumber.data.GameSettings
import com.example.countnumber.data.LeaderboardRepository
import com.example.countnumber.game.GameUiState
import com.example.countnumber.game.GameViewModel
import com.example.countnumber.tts.TtsManager
import com.example.countnumber.ui.screens.GameScreen
import com.example.countnumber.ui.screens.HomeScreen
import com.example.countnumber.ui.screens.LanguageScreen
import com.example.countnumber.ui.screens.ResultScreen
import com.example.countnumber.ui.screens.SettingsScreen
import com.example.countnumber.ui.theme.CountNumberTheme

enum class Screen { LANGUAGE, HOME, SETTINGS, GAME, RESULT }

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: TtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ttsManager = TtsManager(this)
        enableEdgeToEdge()

        val repository = LeaderboardRepository(this)

        setContent {
            CountNumberTheme {
                var screen by remember { mutableStateOf(Screen.LANGUAGE) }
                var settings by remember { mutableStateOf(GameSettings()) }
                var lastGameState by remember { mutableStateOf(GameUiState()) }
                val gameViewModel: GameViewModel = viewModel()

                when (screen) {
                    Screen.LANGUAGE -> LanguageScreen { lang ->
                        settings = settings.copy(language = lang)
                        screen = Screen.HOME
                    }

                    Screen.HOME -> HomeScreen(
                        language = settings.language,
                        onSpeakAnimal = { animal, lang ->
                            val name = when (lang) {
                                AppLanguage.CHINESE  -> animal.nameChinese
                                AppLanguage.ENGLISH  -> animal.nameEnglish
                                AppLanguage.JAPANESE -> animal.nameJapanese
                            }
                            ttsManager.speakText(name, lang)
                        },
                        onStart = {
                            gameViewModel.init(
                                gameSettings = settings,
                                speakNumber = { num ->
                                    ttsManager.speak(num, settings.language)
                                },
                                speakFeedback = { correct ->
                                    ttsManager.speakFeedback(correct, settings.language)
                                }
                            )
                            screen = Screen.GAME
                        },
                        onSettings = { screen = Screen.SETTINGS },
                        onLeaderboard = { screen = Screen.RESULT },
                        onChangeLanguage = { screen = Screen.LANGUAGE }
                    )

                    Screen.SETTINGS -> SettingsScreen(
                        settings = settings,
                        onSettingsChanged = { settings = it },
                        onBack = { screen = Screen.HOME },
                        leaderboardRepository = repository
                    )

                    Screen.GAME -> GameScreen(
                        viewModel = gameViewModel,
                        settings = settings,
                        onGameOver = {
                            lastGameState = gameViewModel.uiState.value
                            screen = Screen.RESULT
                        }
                    )

                    Screen.RESULT -> ResultScreen(
                        gameState = lastGameState,
                        language = settings.language,
                        repository = repository,
                        onPlayAgain = {
                            gameViewModel.init(
                                gameSettings = settings,
                                speakNumber = { num ->
                                    ttsManager.speak(num, settings.language)
                                },
                                speakFeedback = { correct ->
                                    ttsManager.speakFeedback(correct, settings.language)
                                }
                            )
                            screen = Screen.GAME
                        },
                        onHome = { screen = Screen.HOME }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}
