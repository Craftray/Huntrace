package io.craftray.huntrace.world

import org.bukkit.World

class WorldSet {
    var overworld: World? = null
        set(value) {
            if (field != null) { throw IllegalStateException("World \"overworld\" already set") }
            if (value!!.environment == World.Environment.NORMAL) field = value
            else throw IllegalArgumentException("World \"overworld\" must be overworld but it is ${value.environment}")
        }
        get() = field ?: throw IllegalStateException("World \"overworld\" is not set")

    var nether: World? = null
        set(value) {
            if (field != null) { throw IllegalStateException("World \"nether\" already set") }
            if (value!!.environment == World.Environment.NETHER) field = value
            else throw IllegalArgumentException("World \"nether\" must be nether but it is ${value.environment}")
        }
        get() = field ?: throw IllegalStateException("World \"nether\" is not set")

    var theEnd: World? = null
        set(value) {
            if (field != null) { throw IllegalStateException("World \"theEnd\" already set") }
            if (value!!.environment == World.Environment.THE_END) field = value
            else throw IllegalArgumentException("World \"theEnd\" must be end but it is ${value.environment}")
        }
        get() = field ?: throw IllegalStateException("World \"theEnd\" is not set")

}