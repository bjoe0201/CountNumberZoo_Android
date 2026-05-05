# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk (signed/installable)

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
│   └── TtsManager.kt               — TextToSpeech wrapper (speak number / number+animal / feedback)
├── ui/
│   ├── screens/
│   │   ├── LanguageScreen.kt        — first-launch language picker (ZH / EN / JA)
│   │   ├── HomeScreen.kt            — animated animals background + nav buttons
│   │   ├── SettingsScreen.kt        — sliders + selectors (rounds, maxCount, voice mode, layout, language)
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
- **SETTINGS**: Rounds (3–20), MaxCount (5–100), Voice mode (None / Number / Number+Animal), Layout (Grid/Scattered), Language switcher
- **GAME**: `AnimalGrid` fills available space via `BoxWithConstraints`; tap animals → TTS count (number or number+animal per `VoiceMode`); tap answer → TTS selection then TTS feedback after 900 ms; auto-advance after 1500 ms
- **RESULT**: Score + stats; emoji animal name picker (up to 8 emojis from 20 animals, repeatable, backspace); DataStore leaderboard top-10

## Key Logic

### Scoring
`score = rounds × maxCount × (correctCount - wrongCount)`, clamped to minimum 0.

### Distractor Generation
4 wrong answers near the correct value: `±1–5` for small numbers, `±10%` for large.

### Animal Grid Sizing (`AnimalGrid.kt`)
Uses `BoxWithConstraints` to compute `cellSize = min(availableWidth/cols, availableHeight/rows)`, capped at 96 dp, minimum 28 dp. Vertical scroll only enabled when cells would be < 24 dp.

### TTS on Answer Selection (`GameViewModel.selectAnswer`)
1. Speak selected number, or number + animal name, depending on `VoiceMode`
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
| 3 | `app/src/main/java/com/example/countnumber/data/AppVersion.kt` | `APP_VERSION` | 與 `versionName` 相同（例："1.0.4"），顯示在主畫面右下角 |
| 4 | `app/src/main/java/com/example/countnumber/data/AppVersion.kt` | `APP_BUILD_DATE` | 更新為發版日期（例："2026-05-03"） |
| 5 | `README.md` | 第一段 `**版本 Version：vX.X.X**` | 更新顯示版號 |
| 6 | `CHANGELOG.md` | 頂部新增 `## [X.X.X] — YYYY-MM-DD` 區塊 | 記錄本版本變更內容 |

### GitHub Releases 發佈流程（含 APK）

> 目標：每次正式發佈只保留最新 GitHub Release，並上傳**已簽章、可安裝**的 `app-release.apk`。

#### 0. 發佈前檢查

- 確認已完成上方 6 個版本位置更新。
- 確認 GitHub CLI 已登入：`gh auth status`。
- 確認目前分支是 `main`，且遠端是正確 repo：`git branch --show-current`、`git remote -v`。
- 正式公開發佈建議使用私有 release keystore：
  - 複製 `keystore.properties.example` → `keystore.properties`
  - 填入 `RELEASE_STORE_FILE`、`RELEASE_STORE_PASSWORD`、`RELEASE_KEY_ALIAS`、`RELEASE_KEY_PASSWORD`
  - **不可提交** `keystore.properties`、`.jks`、`.keystore`
- 若未設定正式 keystore，`assembleRelease` 會 fallback 使用 debug keystore 簽章；APK 可側載安裝，但只適合測試/家庭分享，不建議作為正式公開長期簽章。

#### 1. 建置並驗證 APK

```bash
# 建置已簽章、可安裝的 release APK
./gradlew clean assembleRelease -x test

# 產物必須是這個檔案
app/build/outputs/apk/release/app-release.apk
```

Windows PowerShell 檢查檔案是否存在：

```powershell
Test-Path .\app\build\outputs\apk\release\app-release.apk
Get-Item .\app\build\outputs\apk\release\app-release.apk
```

簽章驗證（Windows PowerShell）：

```powershell
$sdk = (Get-Content .\local.properties | Where-Object { $_ -like 'sdk.dir=*' } | ForEach-Object { ($_ -replace '^sdk.dir=', '') -replace '\\:', ':' -replace '\\\\', '\' })
$apksigner = Get-ChildItem -Path (Join-Path $sdk 'build-tools') -Recurse -Filter 'apksigner.bat' | Sort-Object FullName -Descending | Select-Object -First 1
& $apksigner.FullName verify --verbose --print-certs .\app\build\outputs\apk\release\app-release.apk
```

驗證結果至少要看到：

```text
Verifies
Verified using v2 scheme (APK Signature Scheme v2): true
Number of signers: 1
```

⚠️ **不要上傳** `app-release-unsigned.apk`；Android 會顯示「應用程式套件無效」或無法完成安裝。

#### 2. 測試、提交、推送

```bash
./gradlew test

git status
git add -A
git commit -m "release: vX.X.X — <簡短說明>"
git push origin main
```

若 `git status` 顯示沒有變更，代表版本 commit 可能已經存在，可直接確認 `git log -1 --oneline` 後繼續發佈。

#### 3. 建立並推送 tag

```bash
git fetch origin --tags --prune
git tag --list "vX.X.X"

# 若 tag 不存在才建立
git tag -a vX.X.X -m "Release vX.X.X"
git push origin vX.X.X
```

注意：如果 `vX.X.X` tag 已存在但指向舊 commit，先停止並確認是否要刪除/重建 tag，避免 release 綁到錯誤版本。

#### 4. 建立 GitHub Release 並上傳 APK

```bash
gh release create vX.X.X "app/build/outputs/apk/release/app-release.apk" \
  --title "動物數數 vX.X.X — <標題>" \
  --notes "<繁體中文 release 說明>" \
  --latest
```

若 Release 已存在但要替換 APK：

```bash
gh release upload vX.X.X "app/build/outputs/apk/release/app-release.apk" --clobber
```

發佈後驗證：

```bash
gh release view vX.X.X --json tagName,name,url,publishedAt,assets
gh release list --limit 100
```

Release asset 必須包含：

```text
app-release.apk
```

#### 5. 刪除舊 GitHub Releases，只保留最新

先列出所有 Releases：

```bash
gh release list --limit 100
```

確認最新 `vX.X.X` 已存在且 APK asset 正確後，再刪除舊 Release：

```bash
gh release delete v舊版本 --yes
```

範例：

```bash
gh release delete v1.0.5 --yes
```

刪除後再次確認只剩最新：

```bash
gh release list --limit 100
```

> 預設只刪除 GitHub Release，不刪除 git tag。若確定連舊 tag 也要刪除，才執行：
>
> ```bash
> git push origin :refs/tags/v舊版本
> git tag -d v舊版本
> ```

#### 6. 安裝注意事項

- 若手機已安裝同 package name 但不同簽章的舊版 APK，直接更新可能失敗；請先解除安裝舊版再安裝新版。
- 若新版 `versionCode` 小於或等於手機已安裝版本，Android 可能拒絕降版安裝；每版必須遞增 `versionCode`。
- GitHub Release 只上傳 `app-release.apk`，不要上傳 debug APK、unsigned APK、mapping、keystore 或任何私鑰檔。
- 發佈後建議實機/平板下載 GitHub Release asset 安裝一次，確認下載檔案可正常安裝與啟動。

