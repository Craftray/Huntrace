package io.craftray.huntrace.interaction.command.argument

import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.InvalidSyntaxException
import io.craftray.huntrace.interaction.invitation.InvitationType
import java.util.*

class InvitationTypeArgumentParser : ArgumentParser<String, InvitationType> {
    override fun parse(
        context: CommandContext<String>,
        queue: Queue<String>
    ): ArgumentParseResult<InvitationType> {
        val input = queue.peek()
        return try {
            val type = InvitationType.valueOf(input.uppercase())
            ArgumentParseResult.success(type)
        } catch (ex: IllegalArgumentException) {
            ArgumentParseResult.failure(
                InvalidSyntaxException(
                    "survivor | hunter", context.sender,
                    context.currentArgument?.let { listOf(it) } ?: listOf()
                ).also { it.initCause(ex) }
            )
        }
    }
}