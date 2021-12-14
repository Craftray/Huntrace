module huntrace.game {
    requires kotlin.stdlib;
    requires org.bukkit;
    requires huntrace.util;
    requires java.logging;
    requires Multiverse.Core;
    requires Multiverse.Inventories;
    requires Multiverse.NetherPortals;
    requires taboolib;
    requires huntrace.abstracts;

    exports io.craftray.huntrace.game;
    exports io.craftray.huntrace.game.event;
}