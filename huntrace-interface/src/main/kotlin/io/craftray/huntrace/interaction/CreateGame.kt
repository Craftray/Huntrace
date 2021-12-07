package io.craftray.huntrace.interaction

/*
import io.craftray.huntrace.ui.Commands
import io.craftray.huntrace.ui.UI
import io.craftray.huntrace.ui.UIItem
import io.craftray.huntrace.ui.UISort
import org.bukkit.entity.Player
import taboolib.library.xseries.XMaterial
import taboolib.module.chat.colored
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Basic
import taboolib.platform.util.buildItem
*/

/**
 * @Author xbaimiao
 * @Date 2021/11/25 19:18
 */
/*
object CreateGame : UI {

    /**
     * create a io.craftray.huntrace.game
     * @param player Player
     */
    fun open(player: Player) {
        player.openMenu<Basic>(title) {
            sort.sort(this@CreateGame.items, this)
            onClick {
                it.isCancelled = true
            }
        }
    }

    override val sort: UISort
        get() = UISort(Main.createGui.getStringList("sort"))

    override val title: String
        get() = Main.createGui.getString("title").colored()

    override val items: List<UIItem>
        get() = let {
            val list = ArrayList<UIItem>()
            Main.createGui.getConfigurationSection("items").let { config ->
                config.getKeys(false).forEach {
                    val item = buildItem(XMaterial.matchXMaterial(config.getString("$it.id")).orElse(XMaterial.PAPER)) {
                        this.name = config.getString("$it.name").colored()
                        this.lore.addAll(config.getStringList("$it.lore").colored())
                    }
                    list.add(UIItem(it[0], item, Commands()))
                }
            }
            list
        }

}

 */