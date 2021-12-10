package io.craftray.huntrace.interaction.command.argument

import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.arguments.parser.StandardParserRegistry
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.InvalidSyntaxException
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import io.craftray.huntrace.interaction.invitation.InvitationType
import io.leangen.geantyref.TypeToken
import java.util.*
import kotlin.jvm.Throws

class InvitationTypeArgumentParser<C> : ArgumentParser<C, InvitationType> {
    override fun parse(
        context: CommandContext<C>,
        queue: Queue<String>
    ): ArgumentParseResult<InvitationType> {
        val input = queue.peek() ?: return ArgumentParseResult.failure(
            NoInputProvidedException(InvitationTypeArgumentParser::class.java, context)
        )
        return try {
            val type = InvitationType.valueOf(input.uppercase())
            ArgumentParseResult.success(type)
        } catch (ex: IllegalArgumentException) {
            ArgumentParseResult.failure(
                InvalidSyntaxException(
                    "survivor | hunter", context.sender as Any,
                    context.currentArgument?.let { listOf(it) } ?: listOf()
                ).also { it.initCause(ex) }
            )
        }
    }

    companion object {
        private var registered = false

        @Throws(IllegalStateException::class)
        fun register() {
            check(registered) { "InvitationTypeArgumentParser already registered" }
            StandardParserRegistry<String>().registerParserSupplier(TypeToken.get(String::class.java)) { InvitationTypeArgumentParser() }
            registered = true
        }
    }
}