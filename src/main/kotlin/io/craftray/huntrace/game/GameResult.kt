package io.craftray.huntrace.game

import net.kyori.adventure.text.Component

enum class GameResult {
    HUNTER_WIN,
    SURVIVOR_WIN,
    SURVIVOR_QUIT,
    HUNTER_QUIT
}

class GameResultMatcher(private val game: Game) {

    fun match(result: GameResult) = when (result) {
        GameResult.HUNTER_WIN -> this.hunterWin()
        GameResult.SURVIVOR_WIN -> this.survivorWin()
        GameResult.SURVIVOR_QUIT -> this.survivorQuit()
        GameResult.HUNTER_QUIT -> this.hunterQuit()
    }

    private fun hunterWin() {
        game.hunters.forEach { it.sendMessage(Component.text("hunter win")) }
        game.survivor.sendMessage(Component.text("hunter win"))
    }

    private fun survivorWin() {
        game.hunters.forEach { it.sendMessage(Component.text("survivor win")) }
        game.survivor.sendMessage(Component.text("survivor win"))
    }

    private fun survivorQuit() {
        game.hunters.forEach { it.sendMessage(Component.text("survivor quit")) }
        game.survivor.sendMessage(Component.text("survivor quit"))
    }

    private fun hunterQuit() {
        game.hunters.forEach { it.sendMessage(Component.text("hunter quit")) }
        game.survivor.sendMessage(Component.text("hunter quit"))
    }
}