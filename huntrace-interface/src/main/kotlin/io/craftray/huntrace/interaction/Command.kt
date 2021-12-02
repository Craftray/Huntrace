package io.craftray.huntrace.interaction

import org.bukkit.entity.Player
import taboolib.common.platform.Awake
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.command

/**
 * @Author xbaimiao
 * @Date 2021/11/26 9:31
 */
object Command {

    @Awake
    fun cmd() {
        command(
            name = "huntrace",
            permissionDefault = PermissionDefault.TRUE
        ) {
            execute<Player> { sender, context, argument ->
                //CreateGame.open(sender)
            }
        }
    }

}