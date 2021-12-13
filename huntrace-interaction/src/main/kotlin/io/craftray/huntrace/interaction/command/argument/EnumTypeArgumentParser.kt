package io.craftray.huntrace.interaction.command.argument

import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.arguments.parser.StandardParserRegistry
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.InvalidSyntaxException
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import io.leangen.geantyref.TypeToken
import java.util.*

class EnumTypeArgumentParser<C, T : Enum<*>>(private val type: Class<T>) : ArgumentParser<C, T> {
    override fun parse(
        context: CommandContext<C>,
        queue: Queue<String>
    ): ArgumentParseResult<T> {
        val input = queue.peek() ?: return ArgumentParseResult.failure(
            NoInputProvidedException(EnumTypeArgumentParser::class.java, context)
        )
        return try {
            @Suppress("TYPE_MISMATCH_WARNING")
            val type = java.lang.Enum.valueOf(type, input.uppercase()) as T
            ArgumentParseResult.success(type)
        } catch (_: IllegalArgumentException) {
            ArgumentParseResult.failure(
                InvalidSyntaxException(
                    type.enumConstants.joinToString(", "),
                    context.sender as Any,
                    context.currentArgument?.let { listOf(it) } ?: listOf()
                ) // .also { it.initCause(ex) }
            )
        }
    }

    companion object {
        fun <T : Enum<*>> register(type: Class<T>) {
            StandardParserRegistry<String>().registerParserSupplier(TypeToken.get(String::class.java)) { EnumTypeArgumentParser(type) }
        }
    }
}