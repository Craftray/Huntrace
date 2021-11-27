package io.craftray.huntrace.game.event

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.GameResult
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class HuntraceGameEvent(val game: Game, async: Boolean) : Event(async) {
    constructor(game: Game) : this(game, false)
}

/**
 * Thrown after a game finishes
 * @author Kylepoops
 */
class HuntraceGameFinishEvent(game: Game, val result: GameResult) : HuntraceGameEvent(game, true) {
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
class HuntraceGameStartEvent(game: Game) : HuntraceGameEvent(game) {
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
class HuntraceGameHunterQuitEvent(game: Game, val player: Player) : HuntraceGameEvent(game, true) {
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
    game: Game,
    val result: Result,
    val hunter: Player,
    val target: Player = game.compassTarget.targetOf(hunter)
) : HuntraceGameEvent(game, true) {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    enum class Result {
        SUCCESS,
        MISS,
        DECEPTION
    }


    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}

/**
 * Thrown after a player means to select a target
 * @author Kylepoops
 */
class HuntraceGameSelectTargetEvent(
    game: Game,
    val hunter: Player,
    val target: Player
) : HuntraceGameEvent(game) {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}