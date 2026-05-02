package com.example.countnumber.data

enum class AppLanguage { CHINESE, ENGLISH, JAPANESE }

object AppStrings {
    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "動物數數"
        AppLanguage.ENGLISH -> "Animal Counting"
        AppLanguage.JAPANESE -> "どうぶつかぞえ"
    }

    fun start(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "開始！"
        AppLanguage.ENGLISH -> "Start!"
        AppLanguage.JAPANESE -> "スタート！"
    }

    fun settings(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "設定"
        AppLanguage.ENGLISH -> "Settings"
        AppLanguage.JAPANESE -> "せってい"
    }

    fun leaderboard(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "排行榜"
        AppLanguage.ENGLISH -> "Leaderboard"
        AppLanguage.JAPANESE -> "ランキング"
    }

    fun correct(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "答對了！"
        AppLanguage.ENGLISH -> "Correct!"
        AppLanguage.JAPANESE -> "せいかい！"
    }

    fun wrong(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "不對哦！"
        AppLanguage.ENGLISH -> "Wrong!"
        AppLanguage.JAPANESE -> "ざんねん！"
    }

    fun roundProgress(lang: AppLanguage, current: Int, total: Int) = when (lang) {
        AppLanguage.CHINESE -> "第 $current/$total 題"
        AppLanguage.ENGLISH -> "Round $current/$total"
        AppLanguage.JAPANESE -> "だい $current/$total もん"
    }

    fun howMany(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "有幾隻？"
        AppLanguage.ENGLISH -> "How many?"
        AppLanguage.JAPANESE -> "なんびき？"
    }

    fun rounds(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "題數"
        AppLanguage.ENGLISH -> "Rounds"
        AppLanguage.JAPANESE -> "もんすう"
    }

    fun maxCount(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "最多幾隻"
        AppLanguage.ENGLISH -> "Max Count"
        AppLanguage.JAPANESE -> "さいだいかず"
    }

    fun voice(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "語音"
        AppLanguage.ENGLISH -> "Voice"
        AppLanguage.JAPANESE -> "おんせい"
    }

    fun layout(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "排列方式"
        AppLanguage.ENGLISH -> "Layout"
        AppLanguage.JAPANESE -> "はいち"
    }

    fun grid(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "整齊格子"
        AppLanguage.ENGLISH -> "Grid"
        AppLanguage.JAPANESE -> "グリッド"
    }

    fun scattered(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "隨機散佈"
        AppLanguage.ENGLISH -> "Scattered"
        AppLanguage.JAPANESE -> "ランダム"
    }

    fun language(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "語言"
        AppLanguage.ENGLISH -> "Language"
        AppLanguage.JAPANESE -> "げんご"
    }

    fun back(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "返回"
        AppLanguage.ENGLISH -> "Back"
        AppLanguage.JAPANESE -> "もどる"
    }

    fun score(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "分數"
        AppLanguage.ENGLISH -> "Score"
        AppLanguage.JAPANESE -> "スコア"
    }

    fun correctCount(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "答對"
        AppLanguage.ENGLISH -> "Correct"
        AppLanguage.JAPANESE -> "せいかい"
    }

    fun wrongCount(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "答錯"
        AppLanguage.ENGLISH -> "Wrong"
        AppLanguage.JAPANESE -> "ふせいかい"
    }

    fun yourName(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "你的名字"
        AppLanguage.ENGLISH -> "Your Name"
        AppLanguage.JAPANESE -> "なまえ"
    }

    fun saveScore(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "儲存分數"
        AppLanguage.ENGLISH -> "Save Score"
        AppLanguage.JAPANESE -> "スコアをほぞん"
    }

    fun playAgain(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "再玩一次"
        AppLanguage.ENGLISH -> "Play Again"
        AppLanguage.JAPANESE -> "もういっかい"
    }

    fun home(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "回主畫面"
        AppLanguage.ENGLISH -> "Home"
        AppLanguage.JAPANESE -> "ホーム"
    }

    fun selectLanguage(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "選擇語言"
        AppLanguage.ENGLISH -> "Select Language"
        AppLanguage.JAPANESE -> "げんごをえらぶ"
    }

    fun on(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "開"
        AppLanguage.ENGLISH -> "On"
        AppLanguage.JAPANESE -> "オン"
    }

    fun off(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "關"
        AppLanguage.ENGLISH -> "Off"
        AppLanguage.JAPANESE -> "オフ"
    }

    fun tapToCount(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "點動物來數數！"
        AppLanguage.ENGLISH -> "Tap to count!"
        AppLanguage.JAPANESE -> "タップしてかぞえよう！"
    }

    fun date(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "日期"
        AppLanguage.ENGLISH -> "Date"
        AppLanguage.JAPANESE -> "にち"
    }

    fun clearLeaderboard(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "清除排行榜記錄"
        AppLanguage.ENGLISH -> "Clear Leaderboard"
        AppLanguage.JAPANESE -> "ランキングをリセット"
    }

    fun clearConfirm1(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "確定要清除所有記錄？"
        AppLanguage.ENGLISH -> "Clear all records?"
        AppLanguage.JAPANESE -> "全記録を消しますか？"
    }

    fun clearConfirm2(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "真的確定？清除後無法復原！"
        AppLanguage.ENGLISH -> "Really sure? Cannot be undone!"
        AppLanguage.JAPANESE -> "本当によいですか？元に戻せません！"
    }

    fun clearConfirmBtn(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "確定清除"
        AppLanguage.ENGLISH -> "Yes, Clear"
        AppLanguage.JAPANESE -> "はい、消す"
    }

    fun cancel(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "取消"
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.JAPANESE -> "キャンセル"
    }

    fun clearDone(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "已清除！"
        AppLanguage.ENGLISH -> "Cleared!"
        AppLanguage.JAPANESE -> "リセット完了！"
    }

    fun changeLanguage(lang: AppLanguage) = when (lang) {
        AppLanguage.CHINESE -> "切換語言"
        AppLanguage.ENGLISH -> "Change Language"
        AppLanguage.JAPANESE -> "げんごをかえる"
    }
}
