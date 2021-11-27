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
    distance: Double? = null
) : HuntraceGameEvent(true) {

    private val _distance = distance


    init {
        if (result != Result.SUCCESS_WITH_DISTANCE && distance != null) {
            throw IllegalArgumentException("Distance is only available when result is SUCCESS_WITH_DISTANCE")
        }
        if (result == Result.SUCCESS_WITH_DISTANCE && distance == null) {
            throw IllegalArgumentException("Distance is required when result is SUCCESS_WITH_DISTANCE")
        }
    }

    val distance: Double
        get() {
            if (result != Result.SUCCESS_WITH_DISTANCE) {
                throw IllegalStateException("Distance is only available for SUCCESS_WITH_DISTANCE compass updates")
            }
            return _distance!!
        }



    override fun getHandlers(): HandlerList {
        return handlerList
    }

    enum class Result {
        SUCCESS_WITH_DISTANCE,
        SUCCESS,
        MISS,
        DECEPTION
    }


    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}