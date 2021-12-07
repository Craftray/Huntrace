package io.craftray.huntrace.ui

import org.bukkit.inventory.ItemStack
import taboolib.module.ui.type.Basic

/**
 * @Author xbaimiao
 * @Date 2021/11/25 19:29
 */
class UISort(
    val source: List<String>
) {

    /**
     * UI有多少行
     */
    val line get() = source.size

    /**
     * 进行排序
     */
    fun sort(uiItems: List<UIItem>): Map<Int, ItemStack> {
        val allLine = source.let {
            val stringBuilder = StringBuilder()
            it.forEach { i -> stringBuilder.append(i) }
            stringBuilder.toString()
        }
        val map = HashMap<Int, ItemStack>()
        var solt = 0
        for (char in allLine) {
            for (uiItem in uiItems) {
                if (char == uiItem.key) {
                    map[solt] = uiItem.itemStack
                }
            }
            solt++
        }
        return map
    }

    fun sort(uiItems: List<UIItem>, basic: Basic) {
        sort(uiItems).forEach { (k, v) ->
            basic.set(k, v)
        }
    }
}