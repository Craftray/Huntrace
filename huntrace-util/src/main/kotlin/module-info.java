module huntrace.util {
    requires kotlin.stdlib;
    requires org.bukkit;
    requires net.kyori.adventure;
    requires huntrace.abstracts;

    exports io.craftray.huntrace.util;
    exports io.craftray.huntrace.util.io;
    exports io.craftray.huntrace.util.objects;
    exports io.craftray.huntrace.util.rule;
    exports io.craftray.huntrace.util.runnable;
}