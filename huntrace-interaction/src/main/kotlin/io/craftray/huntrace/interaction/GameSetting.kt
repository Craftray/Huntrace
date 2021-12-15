package io.craftray.huntrace.interaction

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.GameBuilder
import io.craftray.huntrace.interaction.invitation.InvitationType
import io.craftray.huntrace.util.rule.CompassRule
import io.craftray.huntrace.util.rule.WorldRule
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import org.bukkit.entity.Player

/**
 * Wrapped the GameBuilder again.
 */
internal data class GameSetting(
    var hunters: MutableSet<Player> = ObjectLinkedOpenHashSet(),
    var survivors: MutableSet<Player> = ObjectLinkedOpenHashSet(),
    var compassRule: CompassRule = CompassRule(),
    var worldRule: WorldRule = WorldRule()
) {
    private lateinit var game: Game

    /**
     * Add a player to the setting by type
     * @param player The player to add
     * @param type The type that the player will be added to
     * @exception IllegalStateException If the player is already in the setting
     */
    fun addPlayer(player: Player, type: InvitationType) {
        if (player in hunters || player in survivors) {
            error("Player ${player.name} is already in the game")
        }
        when (type) {
            InvitationType.HUNTER -> hunters.add(player)
            InvitationType.SURVIVOR -> survivors.add(player)
        }
    }

    /**
     * Check whether the setting is fully initialized
     * @return true if the setting is fully initialized
     */
    fun isReady() = hunters.isNotEmpty() && survivors.isNotEmpty()

    /**
     * Build the game by this setting
     * @return the built game
     * @exception IllegalStateException if the setting is not fully initialized
     * @exception IllegalStateException if the game is already built
     */
    fun build(): Game {
        check(isReady()) { "GameSetting is not ready" }
        check(!this::game.isInitialized) { "Game has already been built" }
        return GameBuilder()
            .withRule(compassRule)
            .withRule(worldRule)
            .withHunters(hunters)
            .withSurvivors(survivors)
            .build()
    }
}