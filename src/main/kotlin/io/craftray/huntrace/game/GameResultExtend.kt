package io.craftray.huntrace.game

import net.kyori.adventure.text.Component

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

fun Game.hunterWin() {
    this.hunters.forEach { it.sendMessage(Component.text("hunter win")) }
    this.survivor.sendMessage(Component.text("hunter win"))
}

fun Game.survivorWin() {
    this.hunters.forEach { it.sendMessage(Component.text("survivor win")) }
    this.survivor.sendMessage(Component.text("survivor win"))
}

fun Game.survivorQuit() {
    this.hunters.forEach { it.sendMessage(Component.text("survivor quit")) }
    this.survivor.sendMessage(Component.text("survivor quit"))
}

fun Game.hunterQuit() {
    this.hunters.forEach { it.sendMessage(Component.text("hunter quit")) }
    this.survivor.sendMessage(Component.text("hunter quit"))
}