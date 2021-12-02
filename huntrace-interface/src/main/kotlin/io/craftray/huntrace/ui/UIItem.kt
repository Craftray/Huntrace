package io.craftray.huntrace.ui

import org.bukkit.inventory.ItemStack

/**
 * @Author xbaimiao
 * @Date 2021/11/25 19:29
 */
data class UIItem(
    val key: Char,
    val itemStack: ItemStack,
    val commands: Commands
)