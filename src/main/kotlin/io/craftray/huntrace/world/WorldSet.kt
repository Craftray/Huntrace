package io.craftray.huntrace.world

import org.bukkit.World

// proxy for non-null type field
class WorldSet {
    private val worldSetImpl = WorldSetImpl()

    var overworld: World
        get() = worldSetImpl.overworld!!
        set(value) {
            worldSetImpl.overworld = value
        }

    var nether: World
        get() = worldSetImpl.nether!!
        set(value) {
            worldSetImpl.nether = value
        }

    var theEnd: World
        get() = worldSetImpl.theEnd!!
        set(value) {
            worldSetImpl.theEnd = value
        }

    override fun toString() = worldSetImpl.toString()

    override fun hashCode() = worldSetImpl.hashCode()

    @Suppress("ReplaceCallWithBinaryOperator")
    override fun equals(other: Any?) = worldSetImpl.equals(other)

    class WorldSetImpl {
        var overworld: World? = null
            get() = field ?: throw IllegalStateException("World \"overworld\" is not set")
            set(value) {
                if (field != null) throw IllegalStateException("World \"overworld\" already set")
                if (value!!.environment == World.Environment.NORMAL) field = value
                else throw IllegalArgumentException("Environment of world \"overworld\" must be \"NORMAL\" but it is ${value.environment}")
            }

        var nether: World? = null
            get() = field ?: throw IllegalStateException("World \"nether\" is not set")
            set(value) {
                if (field != null) throw IllegalStateException("World \"nether\" already set")
                if (value!!.environment == World.Environment.NETHER) field = value
                else throw IllegalArgumentException("Environment of world \"nether\" must be \"NETHER\" but it is ${value.environment}")
            }

        var theEnd: World? = null
            get() = field ?: throw IllegalStateException("World \"theEnd\" is not set")
            set(value) {
                if (field != null) throw IllegalStateException("World \"theEnd\" already set")
                if (value!!.environment == World.Environment.THE_END) field = value
                else throw IllegalArgumentException("Environment of world \"theEnd\" must be \"THE_END\" but it is ${value.environment}")
            }

        override fun toString() = "WorldSet(overworld=$overworld, nether=$nether, theEnd=$theEnd)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (this.javaClass != other?.javaClass) return false

            other as WorldSet

            if (overworld != other.overworld) return false
            if (nether != other.nether) return false
            if (theEnd != other.theEnd) return false

            return true
        }

        override fun hashCode(): Int {
            var result = overworld?.hashCode() ?: 0
            result = 31 * result + (nether?.hashCode() ?: 0)
            result = 31 * result + (theEnd?.hashCode() ?: 0)
            return result
        }
    }
}