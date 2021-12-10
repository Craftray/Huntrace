package io.craftray.huntrace.interaction

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.meta.SimpleCommandMeta
import cloud.commandframework.paper.PaperCommandManager
import io.craftray.huntrace.interaction.command.Command
import io.craftray.huntrace.interaction.command.argument.EnumTypeArgumentParser
import io.craftray.huntrace.interaction.command.argument.InvitationTypeArgumentParser
import org.bukkit.WorldType
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

object InteractionBase {
    fun init(plugin: Plugin) {
        val manager = PaperCommandManager(
            plugin,
            CommandExecutionCoordinator.simpleCoordinator(),
            { it },
            { it }
        )
        InvitationTypeArgumentParser.register()
        EnumTypeArgumentParser.register(WorldType::class.java)
        AnnotationParser(manager, CommandSender::class.java) { SimpleCommandMeta.empty() }.parse(Command)
    }
}