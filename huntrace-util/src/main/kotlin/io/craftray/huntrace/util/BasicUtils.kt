package io.craftray.huntrace.util

import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import kotlin.math.sqrt

/**
 * Basic utility functions across all modules
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object BasicUtils {
    const val OVERWORLD_TO_NETHER_MULTIPLIER = 0.125
    const val NETHER_TO_OVERWORLD_MULTIPLIER = 8.0

    val ItemStack.owningPlayer: Player?
        get() = (itemMeta as? SkullMeta)?.owningPlayer?.toPlayer()

    inline fun <T> MutableCollection<T>.pollEach(action: (T) -> Unit) {
        val iterator = iterator()
        for (element in iterator) {
            action(element)
            iterator.remove()
        }
    }

    fun skullOf(player: Player): ItemStack {
        val head = ItemStack(Material.PLAYER_HEAD)
        val meta = (head.itemMeta as SkullMeta).apply {
            owningPlayer = player
            displayName(Component.text(player.name))
        }
        return head.apply { itemMeta = meta }
    }

    fun OfflinePlayer.toPlayer() = this.name?.let { Bukkit.getPlayerExact(it) }

    // return a random boolean with specific chance
    fun randomBoolean(chance: Float) = Math.random() < chance

    fun Location.transformWorld(target: World): Location {
        val env = this.world.environment
        val result = this.clone()
        if (env == World.Environment.NORMAL && target.environment == World.Environment.NETHER) {
            result.x *= NETHER_TO_OVERWORLD_MULTIPLIER
            result.z *= NETHER_TO_OVERWORLD_MULTIPLIER
        } else if (env == World.Environment.NETHER && target.environment == World.Environment.NORMAL) {
            result.x *= OVERWORLD_TO_NETHER_MULTIPLIER
            result.z *= OVERWORLD_TO_NETHER_MULTIPLIER
        } else if ((env == World.Environment.NORMAL || env == World.Environment.NETHER) && target.environment == World.Environment.THE_END) {
            throw IllegalArgumentException("Cannot transform env THE_END with other dimensions")
        }

        return result.apply { world = target }
    }

    fun Location.literal2DDistanceOf(other: Location): Double {
        val otherLoc = other.transformWorld(this.world)
        val x = this.x - otherLoc.x
        val z = this.z - otherLoc.z
        return sqrt(x * x + z * z)
    }
}