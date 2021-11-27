package io.craftray.huntrace.game.collection

import io.craftray.huntrace.Utils.literalDistanceOf
import io.craftray.huntrace.game.Game
import org.bukkit.entity.Player

class CompassTarget(val game: Game) {
    private val targetMap = mutableMapOf<Player, Player>()

    fun targetOf(player: Player): Player {
        return targetMap[player] ?: nearestSurvivorOf(player)
    }

    fun setTarget(player: Player, target: Player) {
        targetMap[player] = target
    }

    fun nearestSurvivorOf(hunter: Player): Player {
        if (game.state != Game.State.RUNNING && game.state != Game.State.PREPARING) {
            throw IllegalStateException("Game is not running")
        }
        val hunterLoc = hunter.location
        val nearest = game.survivors.minByOrNull { hunterLoc.literalDistanceOf(it.location) }
        return nearest ?: throw IllegalStateException("No survivor found")
    }
}