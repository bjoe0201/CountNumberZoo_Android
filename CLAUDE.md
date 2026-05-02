# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run a single unit test
./gradlew :app:testDebugUnitTest --tests "com.example.countnumber.ExampleUnitTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean
```

## Architecture & Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material3
- **Min SDK**: 31, **Target/Compile SDK**: 36
- **Build system**: Gradle with Kotlin DSL (`.kts`), version catalog at `gradle/libs.versions.toml`
- **AGP**: 9.1.1, **Kotlin**: 2.2.10, **Compose BOM**: 2026.02.01
- **State management**: `GameViewModel` + `StateFlow`
- **Navigation**: Simple `when(screen)` state-based (no Jetpack Navigation library)
- **Persistence**: AndroidX DataStore Preferences (leaderboard)
- **TTS**: Android built-in `TextToSpeech` API

## Dependencies

Defined in `gradle/libs.versions.toml`:

| Alias | Artifact | Purpose |
|-------|----------|---------|
| `androidx.lifecycle.viewmodel.compose` | lifecycle-viewmodel-compose 2.10.0 | ViewModel in Compose |
| `androidx.datastore.preferences` | datastore-preferences 1.1.4 | Leaderboard persistence |
| `androidx.compose.material.icons.extended` | material-icons-extended | Settings / back icons |

## Project Structure

```
app/src/main/java/com/example/countnumber/
├── MainActivity.kt                  — nav host (Screen enum), TTS lifecycle
├── data/
│   ├── Animal.kt                    — 20 animals (emoji + names in ZH/EN/JA)
│   ├── AppStrings.kt                — all UI strings in 3 languages
│   ├── AppVersion.kt                — APP_VERSION constant (shown on Home screen)
│   ├── GameSettings.kt              — GameSettings data class + LayoutMode enum
│   ├── GameResult.kt                — LeaderboardEntry model
│   └── LeaderboardRepository.kt    — DataStore read/write, top-10
├── game/
│   ├── GameState.kt                 — RoundState, GameUiState
│   └── GameViewModel.kt             — round logic, scoring, distractor generation
├── tts/
│   └── TtsManager.kt               — TextToSpeech wrapper (speak number / feedback)
├── ui/
│   ├── screens/
│   │   ├── LanguageScreen.kt        — first-launch language picker (ZH / EN / JA)
│   │   ├── HomeScreen.kt            — animated animals background + nav buttons
│   │   ├── SettingsScreen.kt        — sliders + toggles (rounds, maxCount, voice, layout, language)
│   │   ├── GameScreen.kt            — main gameplay + feedback overlay
│   │   └── ResultScreen.kt          — score, emoji name picker, leaderboard
│   ├── components/
│   │   ├── AnimalGrid.kt            — BoxWithConstraints auto-sizing tappable grid
│   │   ├── AnswerButtons.kt         — 5 colored answer buttons
│   │   └── AnimatedAnimal.kt        — floating emoji for HomeScreen background
│   └── theme/
│       ├── Color.kt                 — child-friendly bright palette + AnswerColors list
│       ├── Type.kt                  — large SansSerif typography
│       └── Theme.kt                 — forced light theme (no dynamic color)
```

## App Flow (5 Screens)

```
Screen.LANGUAGE → Screen.HOME ⇄ Screen.SETTINGS
                      ↓
                 Screen.GAME → Screen.RESULT → Screen.HOME
                                     ↑
                              (Play Again loops back to GAME)
```

- **LANGUAGE**: Full-screen picker; stored in `GameSettings.language`; accessible again via "切換語言" button on Home
- **HOME**: 20 floating animated animals; Start / Settings / Leaderboard / Change Language
- **SETTINGS**: Rounds (3–20), MaxCount (5–100), Voice toggle, Layout (Grid/Scattered), Language switcher
- **GAME**: `AnimalGrid` fills available space via `BoxWithConstraints`; tap animals → TTS count; tap answer → TTS number then TTS feedback after 900 ms; auto-advance after 1500 ms
- **RESULT**: Score + stats; emoji animal name picker (up to 8 emojis from 20 animals, repeatable, backspace); DataStore leaderboard top-10

## Key Logic

### Scoring
`score = rounds × maxCount × (correctCount - wrongCount)`, clamped to minimum 0.

### Distractor Generation
4 wrong answers near the correct value: `±1–5` for small numbers, `±10%` for large.

### Animal Grid Sizing (`AnimalGrid.kt`)
Uses `BoxWithConstraints` to compute `cellSize = min(availableWidth/cols, availableHeight/rows)`, capped at 96 dp, minimum 28 dp. Vertical scroll only enabled when cells would be < 24 dp.

### TTS on Answer Selection (`GameViewModel.selectAnswer`)
1. Speak selected number immediately
2. Delay 900 ms
3. Speak correct/wrong feedback
4. Delay 1500 ms → advance to next round

### Player Name (Emoji Picker)
`LeaderboardEntry.playerName` stores a string of emoji characters (e.g. `🐶🐱🐰`). Up to 8 emojis, all 20 animals available, repeatable.

## Multi-Language Support

All UI text goes through `AppStrings` object (no Android string resources). `AppLanguage` enum: `CHINESE / ENGLISH / JAPANESE`. TTS locale mapping: ZH → `Locale.TRADITIONAL_CHINESE`, EN → `Locale.ENGLISH`, JA → `Locale.JAPANESE`.

## Theme

Forced light mode only — no dark theme, no dynamic color. Child-friendly palette defined in `Color.kt` (`SkyBlue`, `GrassGreen`, `SunshineYellow`, `OrangePeel`, etc.).

---

## ⚠️ Version Update Checklist

每次發佈新版本時，**必須同步更新以下所有地方**，缺一不可：

| # | 檔案 | 位置 | 說明 |
|---|------|------|------|
| 1 | `app/build.gradle.kts` | `versionCode` | 整數，每版 +1（例：4） |
| 2 | `app/build.gradle.kts` | `versionName` | 語意版號字串（例："1.0.3"） |
| 3 | `app/src/main/java/com/example/countnumber/data/AppVersion.kt` | `APP_VERSION` | 與 `versionName` 相同（例："1.0.3"），顯示在主畫面右下角 |
| 4 | `README.md` | 第一段 `**版本 Version：vX.X.X**` | 更新顯示版號 |
| 5 | `CHANGELOG.md` | 頂部新增 `## [X.X.X] — YYYY-MM-DD` 區塊 | 記錄本版本變更內容 |

### 更新後的發佈流程

```bash
# 1. 更新上方5個地方後，構建 release APK
./gradlew clean assembleRelease -x test

# 2. 提交所有變更
git add -A
git commit -m "release: vX.X.X — <簡短說明>"

# 3. 推送到 GitHub
git push origin main

# 4. 建立並推送 tag
git tag -a vX.X.X -m "Release vX.X.X"
git push origin vX.X.X

# 5. 刪除舊 release（保留最新）
gh release delete vX.X.X-1 --yes
git push origin :refs/tags/vX.X.X-1   # 若需要也刪除舊 tag

# 6. 建立新 release 並上傳 APK
gh release create vX.X.X "app/build/outputs/apk/release/app-release-unsigned.apk" \
  --title "動物數數 vX.X.X — <標題>" \
  --notes "<繁體中文 release 說明>"
```

