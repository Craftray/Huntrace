package io.craftray.huntrace.game

enum class GameResult {
    HUNTER_WIN,
    SURVIVOR_WIN,
    SURVIVOR_QUIT,
    HUNTER_QUIT
}

fun Game.matchResult(result: GameResult) = when (result) {
    GameResult.HUNTER_WIN -> this.hunterWin()
    GameResult.SURVIVOR_WIN -> this.survivorWin()
    GameResult.SURVIVOR_QUIT -> this.survivorQuit()
    GameResult.HUNTER_QUIT -> this.hunterQuit()
}

fun Game.hunterWin() {}

fun Game.survivorWin() {}

fun Game.survivorQuit() {}

fun Game.hunterQuit() {}