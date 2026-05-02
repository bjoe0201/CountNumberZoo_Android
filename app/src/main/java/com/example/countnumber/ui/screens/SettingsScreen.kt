package com.example.countnumber.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countnumber.data.AppLanguage
import com.example.countnumber.data.AppStrings
import com.example.countnumber.data.GameSettings
import com.example.countnumber.data.LayoutMode
import com.example.countnumber.ui.theme.BackgroundColor
import com.example.countnumber.ui.theme.OrangePeel
import com.example.countnumber.ui.theme.PrimaryColor
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit,
    onBack: () -> Unit
) {
    val lang = settings.language

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        AppStrings.settings(lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rounds
            SettingsCard {
                Text(
                    "${AppStrings.rounds(lang)}: ${settings.rounds}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Slider(
                    value = settings.rounds.toFloat(),
                    onValueChange = { onSettingsChanged(settings.copy(rounds = it.roundToInt())) },
                    valueRange = 3f..20f,
                    steps = 16
                )
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("3", fontSize = 14.sp, color = Color.Gray)
                    Text("20", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Max Count
            SettingsCard {
                Text(
                    "${AppStrings.maxCount(lang)}: ${settings.maxCount}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Slider(
                    value = settings.maxCount.toFloat(),
                    onValueChange = { onSettingsChanged(settings.copy(maxCount = it.roundToInt())) },
                    valueRange = 5f..100f,
                    steps = 18
                )
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("5", fontSize = 14.sp, color = Color.Gray)
                    Text("100", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Voice toggle
            SettingsCard {
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    Text(AppStrings.voice(lang), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Switch(
                        checked = settings.voiceEnabled,
                        onCheckedChange = { onSettingsChanged(settings.copy(voiceEnabled = it)) }
                    )
                }
            }

            // Layout toggle
            SettingsCard {
                Text(AppStrings.layout(lang), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LayoutMode.values().forEach { mode ->
                        val selected = settings.layoutMode == mode
                        Button(
                            onClick = { onSettingsChanged(settings.copy(layoutMode = mode)) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) PrimaryColor else Color.LightGray
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (mode == LayoutMode.GRID) AppStrings.grid(lang)
                                       else AppStrings.scattered(lang),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Language switcher
            SettingsCard {
                Text(AppStrings.language(lang), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        AppLanguage.CHINESE to "中文",
                        AppLanguage.ENGLISH to "English",
                        AppLanguage.JAPANESE to "日本語"
                    ).forEach { (l, label) ->
                        val selected = settings.language == l
                        Button(
                            onClick = { onSettingsChanged(settings.copy(language = l)) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) OrangePeel else Color.LightGray
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(label, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}
