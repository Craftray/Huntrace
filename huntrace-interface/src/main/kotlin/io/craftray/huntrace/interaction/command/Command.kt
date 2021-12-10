package io.craftray.huntrace.interaction.command

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import io.craftray.huntrace.Utils.bukkitRunnableOf
import io.craftray.huntrace.interaction.GameSetting
import io.craftray.huntrace.interaction.InteractionBase
import io.craftray.huntrace.interaction.invitation.Invitation
import io.craftray.huntrace.interaction.invitation.InvitationManager
import io.craftray.huntrace.interaction.invitation.InvitationType
import io.craftray.huntrace.interaction.text.InviteMessageBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.WorldType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Command {
    private val settingMap = HashMap<Player, GameSetting>()

    @CommandDescription("Main InGameTargetSelector of Huntrace")
    @CommandMethod("huntrace new")
    fun mainCommand(sender: CommandSender) {
        if (sender !is Player) {
            sender.sendMessage(Component.text("[Huntrace] Game can only be created by player").color(NamedTextColor.RED))
            return
        } else if (settingMap.containsKey(sender)) {
            sender.sendMessage(Component.text("[Huntrace] You already have a game").color(NamedTextColor.RED))
            return
        } else {
            settingMap[sender] = GameSetting()
            sender.sendMessage(Component.text("[Huntrace] You have created a new game").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set the seed of a game")
    @CommandMethod("huntrace world seed <input>")
    fun seedCommand(
        sender: CommandSender,
        @Argument("input") input: String
    ) {
        if (sender !is Player) {
            sender.sendMessage(Component.text("[Huntrace] Game can only be configured by player").color(NamedTextColor.RED))
        } else if (!settingMap.containsKey(sender)) {
            sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))
        } else {
            settingMap[sender]!!.worldRule.seed = input
            sender.sendMessage(Component.text("[Huntrace] You have set the seed of the game: $input").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set the world type of a game")
    @CommandMethod("huntrace world type <type>")
    fun worldTypeCommand(
        sender: CommandSender,
        @Argument("type") type: WorldType
    ) {
        if (sender !is Player) {
            sender.sendMessage(Component.text("[Huntrace] Game can only be configured by player").color(NamedTextColor.RED))
        } else if (!settingMap.containsKey(sender)) {
            sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))
        } else {
            settingMap[sender]!!.worldRule.type = type
            sender.sendMessage(Component.text("[Huntrace] You have set the world type of the game: $type").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Invite a hunter to the game")
    @CommandMethod("huntrace invite <type> <target>")
    fun inviteCommand(
        sender: CommandSender,
        @Argument("type") type: InvitationType,
        @Argument("target") target: Player
    ) {
        if (sender !is Player) {
            sender.sendMessage(Component.text("[Huntrace] game can only be configured by player").color(NamedTextColor.RED))
        } else if (!settingMap.containsKey(sender)) {
            sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))
        } else {
            InvitationManager.invitations.add(Invitation(sender, target, type))
            target.sendMessage(InviteMessageBuilder.of(sender))
            sender.sendMessage(Component.text("[Huntrace] You have invited ${target.name} as $type to the game").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Accept a invitation")
    @CommandMethod("huntrace invite accept <creator>")
    fun acceptCommand(
        sender: CommandSender,
        @Argument("creator") creator: Player
    ) = when {
        sender !is Player -> sender.sendMessage(Component.text("[Huntrace] invitation can only be accepted by player"))

        !settingMap.containsKey(creator) -> sender.sendMessage(Component.text("[Huntrace] ${creator.name} doesn't have a game"))

        !InvitationManager.contains(creator, sender) ->
            sender.sendMessage(Component.text("[Huntrace] You don't have any invitation from ${creator.name}").color(NamedTextColor.RED))

        else -> {
            val type = InvitationManager.get(creator, sender)?.type!!
            InvitationManager.remove(creator, sender)
            settingMap[creator]!!.addPlayer(sender, type)
            sender.sendMessage(
                Component.text("[Huntrace] You have accepted the invitation from ${creator.name}").color(NamedTextColor.GREEN)
            )
            creator.sendMessage(
                Component.text("[Huntrace] ${sender.name} has accepted your invitation").color(NamedTextColor.GREEN)
            )
        }
    }

    @CommandDescription("Deny a invitation")
    @CommandMethod("huntrace invite deny <creator>")
    fun denyCommand(
        sender: CommandSender,
        @Argument("creator") creator: Player
    ) = when {
        sender !is Player -> sender.sendMessage(Component.text("[Huntrace] invitation can only be denied by player"))

        !settingMap.containsKey(creator) -> sender.sendMessage(Component.text("[Huntrace] ${creator.name} doesn't have a game"))

        !InvitationManager.contains(creator, sender) ->
            sender.sendMessage(Component.text("[Huntrace] You don't have any invitation from ${creator.name}").color(NamedTextColor.RED))

        else -> {
            InvitationManager.remove(creator, sender)
            sender.sendMessage(
                Component.text("[Huntrace] You have denied the invitation from ${creator.name}").color(NamedTextColor.RED)
            )
            creator.sendMessage(
                Component.text("[Huntrace] ${sender.name} has denied your invitation").color(NamedTextColor.RED)
            )
        }
    }

    @CommandDescription("Start the Game")
    @CommandMethod("huntrace start")
    fun startCommand(sender: CommandSender) = when {
        sender !is Player -> sender.sendMessage(Component.text("[Huntrace] Game can only be started by player"))

        !settingMap.containsKey(sender) ->
            sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        settingMap[sender]?.isReady() == false ->
            sender.sendMessage(Component.text("[Huntrace] You don't have enough players").color(NamedTextColor.RED))

        else -> {
            bukkitRunnableOf {
                settingMap[sender]!!.build().init().start()
            }.runTask(InteractionBase.plugin)
            sender.sendMessage(Component.text("[Huntrace] Game has started").color(NamedTextColor.GREEN))
        }
    }
}