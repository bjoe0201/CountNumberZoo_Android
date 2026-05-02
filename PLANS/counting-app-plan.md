# Plan: 動物有幾隻？ (How Many Animals?) - Counting App for Kids

## Context

Building a preschool counting game app for children ages 1-6. The project is a fresh Android scaffold (Kotlin + Jetpack Compose + Material3). The app lets children count animals displayed on screen, then select the correct number from multiple choices.

**User decisions:**
- Leaderboard requires name input (supports multiple kids sharing one device)
- Animal layout: both grid and scattered modes, toggled in settings
- Language selection at app launch (affects ALL UI text + TTS voice)
- Home screen with animated animals running around + app title "動物數數"
- `LeaderboardEntry` includes `playerName: String`
- `GameSettings` includes `layoutMode: LayoutMode` (GRID / SCATTERED)
- `AppLanguage` enum (CHINESE / ENGLISH / JAPANESE) stored in DataStore

---

## Architecture Overview

**Navigation:** Simple state-based (`when(screen)`) — no Jetpack Navigation library needed for 4 screens.

**State Management:** Single `GameViewModel` with `StateFlow` for reactive UI updates.

**TTS:** Android built-in `TextToSpeech` API (no extra dependency).

**Persistence:** DataStore Preferences for leaderboard.

---

## File Structure

```
com/example/countnumber/
├── MainActivity.kt                 (navigation host, TTS lifecycle)
├── data/
│   ├── Animal.kt                   (20 animals: emoji + names in 3 languages)
│   ├── GameSettings.kt             (rounds, maxCount, voice, layout settings)
│   ├── GameResult.kt               (LeaderboardEntry model)
│   ├── LeaderboardRepository.kt    (DataStore persistence)
│   └── AppStrings.kt               (all UI strings in 3 languages)
├── game/
│   ├── GameState.kt                (RoundState, GameUiState)
│   └── GameViewModel.kt            (game logic, round management)
├── tts/
│   └── TtsManager.kt              (TextToSpeech wrapper)
├── ui/
│   ├── screens/
│   │   ├── LanguageScreen.kt       (first-time language selection)
│   │   ├── HomeScreen.kt          (animated animals + title + start)
│   │   ├── SettingsScreen.kt       (pre-game config)
│   │   ├── GameScreen.kt          (main gameplay)
│   │   └── ResultScreen.kt        (score + leaderboard)
│   ├── components/
│   │   ├── AnimalGrid.kt          (tappable animal grid)
│   │   ├── AnswerButtons.kt       (5 number choices)
│   │   └── AnimatedAnimal.kt      (single animated animal for home screen)
│   └── theme/
│       ├── Color.kt                (child-friendly bright colors)
│       ├── Type.kt                 (large fonts)
│       └── Theme.kt               (force light, vibrant theme)
```

---

## Dependencies to Add

In `gradle/libs.versions.toml`:
- `lifecycle-viewmodel-compose` (2.10.0) — ViewModel in Compose
- `datastore-preferences` (1.1.4) — leaderboard persistence

No other new dependencies required. TTS is in Android SDK.

---

## App Flow (4 Screens)

### 0. Language Selection Screen (APP 啟動第一個畫面)
- 全螢幕，簡潔可愛風格
- 3 個大按鈕選擇語言：「中文」/「English」/「日本語」
- 選擇後，整個 APP 介面文字 + TTS 語音都切換為該語言
- 語言選擇存入 DataStore，下次啟動自動跳過（但可在設定中切換）

### 1. Home Screen (主畫面/開始畫面)
- **主標題**: "動物數數" (或對應語言: "Animal Counting" / "どうぶつかぞえ") — 大字、彩色、可愛字型
- **背景動畫**: 20 隻動物 emoji 在畫面上隨機亂跑、跳動、裝可愛（使用 Compose Animation）
  - 動物隨機出現在不同位置
  - 有簡單的移動動畫（左右晃動、上下跳動、旋轉等）
  - 營造活潑有趣的視覺效果
- **開始遊戲按鈕**: 大按鈕 "開始！" / "Start!" / "スタート！"
- **設定按鈕**: 齒輪圖示，進入設定畫面
- **排行榜按鈕**: 獎盃圖示，查看排行榜

### 2. Settings Screen (設定畫面)
- Rounds slider: 3~20 (題數)
- Max count slider: 5~100 (最多幾隻)
- Voice toggle (on/off) 語音開關
- Animal layout toggle: 整齊格子 (grid) / 隨機散佈 (scattered)
- Language switcher: 可切換語言（中文/English/日本語）
- 返回按鈕

### 3. Game Screen (遊戲畫面)
- Top: round progress "第 X/Y 題" / "Round X/Y" / "だい X/Y もん"
- Center (~60%): FlowRow/Grid of tappable animal emojis (size scales down for large counts)
- Tapped animals get visual marker (colored circle/checkmark)
- Each tap triggers TTS of the count number in selected language
- Bottom (~30%): 5 large answer buttons (1 correct + 4 distractors)
- Feedback overlay: "答對了！"/"Correct!"/"せいかい！" or "不對哦！"/"Wrong!"/"ざんねん！" with TTS, auto-advance after 1.5s

### 4. Result Screen (結果畫面)
- Score display (large)
- Correct/Wrong stats with visual indicators
- Name input field (for leaderboard entry)
- Top-10 leaderboard (persisted via DataStore) — shows name, score, date
- "再玩一次"/"Play Again"/"もういっかい" and "回主畫面"/"Home"/"ホーム" buttons

---

## Key Logic

### Scoring
`score = rounds × maxCount × (correctCount - wrongCount)`, clamped to minimum 0.

### Answer Distractors
Generate 4 wrong answers near the correct value (±1~5 for small numbers, ±10% for large), ensure all distinct and positive.

### Animal Display (up to 100)
- ≤20: emoji size 48dp
- 21-50: emoji size 36dp
- 51-100: emoji size 28dp, scrollable grid

### TTS Manager
- Init in `MainActivity.onCreate`, cleanup in `onDestroy`
- `speak(number: Int, locale: Locale)` for counting
- `speakFeedback(correct: Boolean)` for answer feedback

---

## 20 Animals

| # | Emoji | 中文 | 日本語 | English |
|---|-------|------|--------|---------|
| 1 | 🐶 | 狗 | いぬ | Dog |
| 2 | 🐱 | 貓 | ねこ | Cat |
| 3 | 🐰 | 兔子 | うさぎ | Rabbit |
| 4 | 🐻 | 熊 | くま | Bear |
| 5 | 🐼 | 熊貓 | パンダ | Panda |
| 6 | 🐷 | 豬 | ぶた | Pig |
| 7 | 🐮 | 牛 | うし | Cow |
| 8 | 🐸 | 青蛙 | かえる | Frog |
| 9 | 🐵 | 猴子 | さる | Monkey |
| 10 | 🦁 | 獅子 | ライオン | Lion |
| 11 | 🐯 | 老虎 | とら | Tiger |
| 12 | 🐔 | 雞 | にわとり | Chicken |
| 13 | 🐧 | 企鵝 | ペンギン | Penguin |
| 14 | 🐳 | 鯨魚 | くじら | Whale |
| 15 | 🐘 | 大象 | ぞう | Elephant |
| 16 | 🦒 | 長頸鹿 | キリン | Giraffe |
| 17 | 🐑 | 羊 | ひつじ | Sheep |
| 18 | 🐴 | 馬 | うま | Horse |
| 19 | 🦋 | 蝴蝶 | ちょうちょ | Butterfly |
| 20 | 🐢 | 烏龜 | かめ | Turtle |

---

## Implementation Order

1. Add dependencies (`libs.versions.toml` + `app/build.gradle.kts`)
2. Create data models (`Animal.kt`, `GameSettings.kt`, `GameResult.kt`, `AppStrings.kt`)
3. Update theme (bright child-friendly colors, large fonts, force light mode)
4. Create `TtsManager.kt`
5. Build `LanguageScreen.kt` (first-time language picker)
6. Build `HomeScreen.kt` with animated animals and title
7. Build `SettingsScreen.kt`
8. Create `GameViewModel.kt` with round logic
9. Build `GameScreen.kt` with `AnimalGrid` and `AnswerButtons`
10. Add feedback overlay and TTS integration
11. Create `LeaderboardRepository.kt` with DataStore
12. Build `ResultScreen.kt` with leaderboard
13. Wire navigation in `MainActivity.kt`
14. Polish: animations, edge cases, UI refinement

---

## Critical Files to Modify

- `gradle/libs.versions.toml` — add new dependencies
- `app/build.gradle.kts` — add new implementation lines
- `app/src/main/java/com/example/countnumber/MainActivity.kt` — complete rewrite as nav host
- `app/src/main/java/com/example/countnumber/ui/theme/Color.kt` — child-friendly palette
- `app/src/main/java/com/example/countnumber/ui/theme/Type.kt` — large fonts
- `app/src/main/java/com/example/countnumber/ui/theme/Theme.kt` — force light theme

## Multi-Language UI (AppStrings.kt)

不使用 Android resource 多語系，改用自訂 `AppStrings` 物件，根據選擇的語言回傳對應文字：

```kotlin
enum class AppLanguage { CHINESE, ENGLISH, JAPANESE }

object AppStrings {
    fun appTitle(lang: AppLanguage) = when(lang) {
        CHINESE -> "動物數數"
        ENGLISH -> "Animal Counting"
        JAPANESE -> "どうぶつかぞえ"
    }
    fun start(lang: AppLanguage) = when(lang) { ... }
    fun correct(lang: AppLanguage) = when(lang) { ... }
    fun wrong(lang: AppLanguage) = when(lang) { ... }
    // ... all UI strings
}
```

---

## Verification

1. Build: `./gradlew assembleDebug` — should compile without errors
2. Run on emulator/device (API 31+):
   - Settings screen renders with all controls
   - Slider values update correctly
   - Game starts and shows correct number of animals
   - Tapping animals increments counter with TTS
   - Answer buttons appear with 1 correct + 4 wrong
   - Correct/wrong feedback plays
   - Result screen shows stats and persists to leaderboard
3. Unit test: ViewModel logic for round generation, score calculation, distractor generation
