package io.craftray.huntrace.game.event

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.GameResult
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class HuntraceGameEvent(async: Boolean) : Event(async) {
    constructor() : this(false)
}

/**
 * Thrown after a game finishes
 * @author Kylepoops
 */
class HuntraceGameFinishEvent(val game: Game, val result: GameResult) : HuntraceGameEvent(true) {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}

/**
 * Thrown after a game starts
 * @author Kylepoops
 */
class HuntraceGameStartEvent(val game: Game) : HuntraceGameEvent() {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}

/**
 * Thrown after a player joins a game
 * @author Kylepoops
 */
class HuntraceGameHunterQuitEvent(val game: Game, val player: Player) : HuntraceGameEvent(true) {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}

/**
 * Thrown after a compass is updated in the game
 * @author Kylepoops
 */
class HuntraceGameCompassUpdateEvent(
    val game: Game,
    val result: Result,
    val hunter: Player,
    val survivor: Player,
    val distance: Double? = null
) : HuntraceGameEvent(true) {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    enum class Result {
        SUCCESS_WITH_DISTANCE,
        SUCCESS,
        MISS,
        POOR_SIGNAL
    }


    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}