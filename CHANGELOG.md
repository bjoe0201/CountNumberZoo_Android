# Changelog

All notable changes to **動物數數 / Animal Counting / どうぶつかぞえ** are documented here.

Format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).
Versioning follows [Semantic Versioning](https://semver.org/).

---

## [1.0.3] — 2026-05-02

### Added
- **退出測試按鈕** — 遊戲畫面「有幾隻？」列最右側新增退出按鈕（支援中 / EN / JA）
- **AlertDialog 二次確認** — 點擊退出後彈出置中確認視窗，防止誤觸；可點選「繼續遊戲」取消

---

## [1.0.2] — 2026-05-02

### Added
- **Home screen animals enlarged** — doubled the size of floating animals for better visibility (72.sp instead of 36.sp)
- **Interactive tip text** — "Tap animals to hear pronunciation" hint at the bottom of home screen in 3 languages (Chinese / English / Japanese)

### Improved
- Better visual appeal with larger, more prominent floating animals
- Enhanced user guidance for home screen interactions

---

## [1.0.1] — 2026-05-02

### Fixed
- Improved UI stability and performance across all screens
- Enhanced animal grid layout responsiveness
- Optimized TTS timing sequences

### Changed
- Fine-tuned feedback overlay animations
- Improved leaderboard display formatting

---

## [1.0.0] — 2026-05-02

### Added
- **Language Selection Screen** — choose Chinese / English / Japanese on first launch; changeable anytime from Home
- **Home Screen** — 20 animated animals floating in background; Start / Settings / Leaderboard / Change Language buttons
- **Settings Screen** — rounds (3–20), max animal count (5–100), voice on/off, grid/scattered layout, language switcher
- **Game Screen** — animals auto-size to fill screen via BoxWithConstraints; tap animals to count with TTS; 5 answer buttons
- **Answer TTS sequence** — tapping an answer speaks the selected number first, then correct/wrong feedback after 900 ms; auto-advances after 1500 ms
- **Correct / Wrong feedback overlay** — ⭐ green overlay for correct, 😢 red overlay with correct answer for wrong
- **Result Screen** — score display, correct/wrong stats, emoji animal name picker (up to 8 emojis from 20 animals, repeatable, backspace support)
- **Leaderboard** — top-10 persisted via DataStore Preferences; shows emoji name, score, date
- **20 animals** — 🐶🐱🐰🐻🐼🐷🐮🐸🐵🦁🐯🐔🐧🐳🐘🦒🐑🐴🦋🐢 with names in Chinese, English, Japanese
- **Multi-language TTS** — Traditional Chinese / English / Japanese voice using Android TextToSpeech
- **Child-friendly theme** — forced light mode, bright pastel palette, large fonts, rounded shapes
- **Version label** — `v1.0.0` displayed in bottom-right corner of Home Screen
- MIT open-source license
