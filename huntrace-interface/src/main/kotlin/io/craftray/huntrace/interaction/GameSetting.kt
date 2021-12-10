package io.craftray.huntrace.interaction

import io.craftray.huntrace.game.Game
import io.craftray.huntrace.game.GameBuilder
import io.craftray.huntrace.interaction.invitation.InvitationType
import io.craftray.huntrace.rule.CompassRule
import io.craftray.huntrace.rule.WorldRule
import org.bukkit.entity.Player

internal data class GameSetting(
    var hunters: MutableSet<Player> = mutableSetOf(),
    var survivors: MutableSet<Player> = mutableSetOf(),
    var compassRule: CompassRule = CompassRule(),
    var worldRule: WorldRule = WorldRule()
) {
    private lateinit var game: Game

    fun addPlayer(player: Player, type: InvitationType) {
        when (type) {
            InvitationType.HUNTER -> hunters.add(player)
            InvitationType.SURVIVOR -> survivors.add(player)
        }
    }

    fun isReady() = hunters.isNotEmpty() && survivors.isNotEmpty()

    fun build(): Game {
        if (!this::game.isInitialized) {
            this.game = GameBuilder()
                .withRule(compassRule)
                .withRule(worldRule)
                .withHunters(hunters)
                .withSurvivors(survivors)
                .build()
        }
        return game
    }
}