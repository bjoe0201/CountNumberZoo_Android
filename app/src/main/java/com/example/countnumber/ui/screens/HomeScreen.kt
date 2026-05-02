package com.example.countnumber.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countnumber.data.ALL_ANIMALS
import com.example.countnumber.data.AppLanguage
import com.example.countnumber.data.AppStrings
import com.example.countnumber.data.APP_VERSION
import com.example.countnumber.ui.components.FloatingAnimal
import com.example.countnumber.ui.theme.BackgroundColor
import com.example.countnumber.ui.theme.GrassGreen
import com.example.countnumber.ui.theme.OrangePeel
import com.example.countnumber.ui.theme.PrimaryColor
import com.example.countnumber.ui.theme.SunshineYellow

@Composable
fun HomeScreen(
    language: AppLanguage,
    onStart: () -> Unit,
    onSettings: () -> Unit,
    onLeaderboard: () -> Unit,
    onChangeLanguage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE3F2FD), BackgroundColor, Color(0xFFFFF9C4))
                )
            )
    ) {
        // Animated background animals
        ALL_ANIMALS.forEachIndexed { index, animal ->
            FloatingAnimal(emoji = animal.emoji, index = index)
        }

        // Version label — bottom right corner
        Text(
            text = "v$APP_VERSION",
            fontSize = 12.sp,
            color = Color(0xFF888888),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        )

        // Foreground UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App title
            Text(
                text = AppStrings.appTitle(language),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor,
                modifier = Modifier
                    .background(
                        Color.White.copy(alpha = 0.7f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            )

            Spacer(Modifier.height(48.dp))

            // Start button
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(72.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GrassGreen)
            ) {
                Text(
                    text = AppStrings.start(language),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Settings button
                Button(
                    onClick = onSettings,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePeel),
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
                    Text(
                        text = " ${AppStrings.settings(language)}",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }

                // Leaderboard button
                Button(
                    onClick = onLeaderboard,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SunshineYellow),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text(text = "\uD83C\uDFC6", fontSize = 20.sp)
                    Text(
                        text = " ${AppStrings.leaderboard(language)}",
                        fontSize = 18.sp,
                        color = Color(0xFF5D4037)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Change language button
            Button(
                onClick = onChangeLanguage,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.8f)
                ),
                modifier = Modifier.height(44.dp)
            ) {
                Text(text = "\uD83C\uDF0D ", fontSize = 16.sp)
                Text(
                    text = AppStrings.changeLanguage(language),
                    fontSize = 16.sp,
                    color = Color(0xFF555555)
                )
            }
        }
    }
}
