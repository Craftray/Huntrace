package io.craftray.huntrace.interaction

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.meta.SimpleCommandMeta
import cloud.commandframework.paper.PaperCommandManager
import io.craftray.huntrace.interaction.command.Command
import io.craftray.huntrace.interaction.command.argument.EnumTypeArgumentParser
import io.craftray.huntrace.interaction.invitation.InvitationType
import org.bukkit.WorldType
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

object InteractionBase {
    internal lateinit var plugin: Plugin

    fun init(plugin: Plugin) {
        this.plugin = plugin
        val manager = PaperCommandManager(
            plugin,
            CommandExecutionCoordinator.simpleCoordinator(),
            { it },
            { it }
        )
        EnumTypeArgumentParser.register(InvitationType::class.java)
        EnumTypeArgumentParser.register(WorldType::class.java)
        AnnotationParser(manager, CommandSender::class.java) { SimpleCommandMeta.empty() }.parse(Command)
    }
}