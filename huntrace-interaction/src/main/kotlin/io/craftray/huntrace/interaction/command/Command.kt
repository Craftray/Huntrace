package io.craftray.huntrace.interaction.command

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.specifier.Range
import io.craftray.huntrace.game.Game
import io.craftray.huntrace.interaction.GameSetting
import io.craftray.huntrace.interaction.invitation.Invitation
import io.craftray.huntrace.interaction.invitation.InvitationManager
import io.craftray.huntrace.interaction.invitation.InvitationType
import io.craftray.huntrace.interaction.text.InviteMessageBuilder
import io.craftray.huntrace.util.objects.Distance
import io.craftray.huntrace.util.objects.Spawnpoint
import io.craftray.huntrace.util.runnable.BukkitRunnableWrapper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.WorldType
import org.bukkit.entity.Player

/**
 * The command handler of the whole game.
 */
object Command {

    private val settingMap = HashMap<Player, GameSetting>()

    @CommandDescription("Main InGameTargetSelector of Huntrace")
    @CommandMethod("huntrace new")
    fun mainCommand(sender: Player) = when {
        settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You already have a game").color(NamedTextColor.RED))

        else -> {
            settingMap[sender] = GameSetting()
            sender.sendMessage(Component.text("[Huntrace] You have created a new game").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set the seed of a game")
    @CommandMethod("huntrace world seed <input>")
    fun seedCommand(
        sender: Player,
        @Argument("input", description = "the seed") input: String
    ) = when {
        !settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        else -> {
            settingMap[sender]!!.worldRule.seed = input
            sender.sendMessage(Component.text("[Huntrace] You have set the seed of the game: $input").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set the spawnpoint of the world")
    @CommandMethod("huntrace world spawnpoint <x> <y> <z>")
    fun spawnpointCommand(
        sender: Player,
        @Argument("x") x: Double,
        @Argument("y", description = "specific y") y: Double,
        @Argument("z") z: Double
    ) = when {
        !settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        else -> {
            Bukkit.getLogger().info(y.toString())
            settingMap[sender]!!.worldRule.spawnpoint = Spawnpoint.at(x, y, z)
            sender.sendMessage(Component.text("[Huntrace] You have set the spawnpoint of the game").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set the spawnpoint of the world to the highest location at the specific x and z")
    @CommandMethod("huntrace world spawnpoint <x> highest <z>")
    fun spawnpoint2DCommand(
        sender: Player,
        @Argument("x") x: Double,
        @Argument("z") z: Double
    ) = when {
        !settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        else -> {
            settingMap[sender]!!.worldRule.spawnpoint = Spawnpoint.at(x = x, z = z)
            sender.sendMessage(Component.text("[Huntrace] You have set the spawnpoint of the game").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set the maximum distance of tracking")
    @CommandMethod("huntrace compass distance <distance>")
    fun distanceCommand(
        sender: Player,
        @Argument("distance", description = "the distance") @Range(min = "1", max = "10000000") distance: Long
    ) = when {
        !settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        else -> {
            settingMap[sender]!!.compassRule.distanceLimit = Distance.limited(distance)
            sender.sendMessage(Component.text("[Huntrace] You have set the distance of the compass: $distance").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set whether the cross world tracking is enabled")
    @CommandMethod("huntrace compass crossworldtrack <boolean>")
    fun crossWorldTrackCommand(
        sender: Player,
        @Argument("boolean", description = "whether the cross world tracking is enabled") value: Boolean
    ) = when {
        !settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        else -> {
            settingMap[sender]!!.compassRule.crossWorldTrack = value
            sender.sendMessage(Component.text("[Huntrace] You have set the cross world tracking of the compass: $value").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set whether the deception of tracking is enabled")
    @CommandMethod("huntrace compass deception <boolean>")
    fun deceptionCommand(
        sender: Player,
        @Argument("boolean", description = "whether the deception of tracking is enabled") value: Boolean
    ) = when {
        !settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        else -> {
            settingMap[sender]!!.compassRule.deception = value
            sender.sendMessage(Component.text("[Huntrace] You have set the deception of the compass: $value").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set the update interval of compasses")
    @CommandMethod("huntrace compass interval <interval>")
    fun updateIntervalCommand(
        sender: Player,
        @Argument("interval", description = "the update interval") @Range(min = "20", max = "1000") interval: Long
    ) = when {
        !settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        else -> {
            settingMap[sender]!!.compassRule.updateInterval = interval
            sender.sendMessage(Component.text("[Huntrace] You have set the update interval of the compass: $interval").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set whether the structure will be generate")
    @CommandMethod("huntrace world structures <boolean>")
    fun structuresCommand(
        sender: Player,
        @Argument("boolean") input: Boolean
    ) = when {
        !settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        else -> {
            settingMap[sender]!!.worldRule.structures = input
            sender.sendMessage(Component.text("[Huntrace] You have set the structure of the game: $input").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Set the world type of a game")
    @CommandMethod("huntrace world type <type>")
    fun worldTypeCommand(
        sender: Player,
        @Argument("type") type: WorldType
    ) = when {
        !settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        else -> {
            settingMap[sender]!!.worldRule.type = type
            sender.sendMessage(Component.text("[Huntrace] You have set the world type of the game: $type").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Invite a hunter to the game")
    @CommandMethod("huntrace invite <type> <target>")
    fun inviteCommand(
        sender: Player,
        @Argument("type") type: InvitationType,
        @Argument("target") target: Player
    ) = when {
        !settingMap.containsKey(sender) -> sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        target in settingMap[sender]!!.hunters ->
            sender.sendMessage(Component.text("[Huntrace] $target is already in the game as a Hunter").color(NamedTextColor.RED))

        target in settingMap[sender]!!.survivors ->
            sender.sendMessage(Component.text("[Huntrace] $target is already in the game as a Survivor").color(NamedTextColor.RED))

        target == sender -> {
            settingMap[sender]!!.addPlayer(sender, type)
            sender.sendMessage(Component.text("[Huntrace] You have invited yourself to the game as a $type").color(NamedTextColor.GREEN))
        }

        else -> {
            InvitationManager.invitations.add(Invitation(sender, target, type))
            target.sendMessage(InviteMessageBuilder.of(sender))
            sender.sendMessage(Component.text("[Huntrace] You have invited ${target.name} as $type to the game").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Accept a invitation")
    @CommandMethod("huntrace invite accept <creator>")
    fun acceptCommand(
        sender: Player,
        @Argument("creator") creator: Player
    ) = when {
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
        sender: Player,
        @Argument("creator") creator: Player
    ) = when {
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

    @CommandDescription("Quit the game")
    @CommandMethod("huntrace quit")
    fun quitCommand(sender: Player) = when {
        Game.findGameByPlayerOrNull(sender)?.state != Game.State.RUNNING ->
            sender.sendMessage(Component.text("[Huntrace] You are not in a running game").color(NamedTextColor.RED))

        else -> {
            Game.getGameByPlayer(sender).quit(sender)
            sender.sendMessage(Component.text("[Huntrace] You have quit the game").color(NamedTextColor.GREEN))
        }
    }

    @CommandDescription("Start the Game")
    @CommandMethod("huntrace start")
    fun startCommand(sender: Player) = when {
        !settingMap.containsKey(sender) ->
            sender.sendMessage(Component.text("[Huntrace] You don't have a game").color(NamedTextColor.RED))

        settingMap[sender]?.isReady() == false ->
            sender.sendMessage(Component.text("[Huntrace] You don't have enough players").color(NamedTextColor.RED))

        else -> {
            BukkitRunnableWrapper.submit {
                settingMap[sender]!!.build().init().start()
                settingMap.remove(sender)
            }
            sender.sendMessage(Component.text("[Huntrace] Game has started").color(NamedTextColor.GREEN))
        }
    }
}