package io.craftray.huntrace.game

import net.kyori.adventure.text.Component

enum class GameResult {
    HUNTER_WIN,
    SURVIVOR_WIN,
    SURVIVOR_QUIT,
    HUNTER_QUIT,
    ABORT
}

class GameResultMatcher(private val game: Game) {

    /**
     * Match and process the result of the game
     * @author Kylepoops
     * @param result The result
     */
    fun match(result: GameResult) = when (result) {
        GameResult.HUNTER_WIN -> this.hunterWin()
        GameResult.SURVIVOR_WIN -> this.survivorWin()
        GameResult.SURVIVOR_QUIT -> this.survivorQuit()
        GameResult.HUNTER_QUIT -> this.hunterQuit()
        GameResult.ABORT -> this.abort()
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

    private fun abort() {
        game.hunters.forEach { it.sendMessage(Component.text("abort")) }
        game.survivor.sendMessage(Component.text("abort"))
    }
}