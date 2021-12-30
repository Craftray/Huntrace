package io.craftray.huntrace.game.collection

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.util.literal2DDistanceOf
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import org.bukkit.entity.Player

class HunterTargetCollection(val game: Game) {
    private val targetMap = Object2ObjectLinkedOpenHashMap<Player, Player>()

    /**
     * Get the target of given hunter
     * @author Kylepoops
     */
    fun targetOf(player: Player) = targetMap[player] ?: nearestSurvivorOf(player)

    /**
     * Set the target of given hunter
     * @author Kylepoops
     */
    fun setTarget(player: Player, target: Player) {
        targetMap[player] = target
    }

    /**
     * Get the nearest survivor of given hunter
     * @author Kylepoops
     * @exception IllegalStateException if hunter is not in game
     * @exception IllegalStateException if no survivors are found
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun nearestSurvivorOf(hunter: Player): Player {
        check(game.state == Game.State.RUNNING || game.state == Game.State.PREPARING) { "Game is not running" }
        val hunterLoc = hunter.location
        val nearest = game.survivors.minByOrNull { hunterLoc.literal2DDistanceOf(it.location) }
        return checkNotNull(nearest) { "No survivor found" }
    }
}