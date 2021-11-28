package io.craftray.huntrace.game.collection

import org.bukkit.World

// proxy for non-null type field
@Suppress("PropertyName")
class WorldCollection {
    private var _overworld: World? = null
        get() = field ?: throw IllegalStateException("World \"overworld\" is not set")
        set(value) {
            if (field != null) throw IllegalStateException("World \"overworld\" already set")
            if (value!!.environment == World.Environment.NORMAL) field = value
            else throw IllegalArgumentException("Environment of world \"overworld\" must be \"NORMAL\" but it is ${value.environment}")
        }

    private var _nether: World? = null
        get() = field ?: throw IllegalStateException("World \"nether\" is not set")
        set(value) {
            if (field != null) throw IllegalStateException("World \"nether\" already set")
            if (value!!.environment == World.Environment.NETHER) field = value
            else throw IllegalArgumentException("Environment of world \"nether\" must be \"NETHER\" but it is ${value.environment}")
        }

    private var _theEnd: World? = null
        get() = field ?: throw IllegalStateException("World \"theEnd\" is not set")
        set(value) {
            if (field != null) throw IllegalStateException("World \"theEnd\" already set")
            if (value!!.environment == World.Environment.THE_END) field = value
            else throw IllegalArgumentException("Environment of world \"theEnd\" must be \"THE_END\" but it is ${value.environment}")
        }

    var overworld: World
        get() = _overworld!!
        set(value) {
            _overworld = value
        }

    var nether: World
        get() = _nether!!
        set(value) {
            _nether = value
        }

    var theEnd: World
        get() = _theEnd!!
        set(value) {
            _theEnd = value
        }

    operator fun contains(world: World): Boolean {
        return world == overworld || world == nether || world == theEnd
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorldCollection

        if (_overworld != other._overworld) return false
        if (_nether != other._nether) return false
        if (_theEnd != other._theEnd) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _overworld?.hashCode() ?: 0
        result = 31 * result + (_nether?.hashCode() ?: 0)
        result = 31 * result + (_theEnd?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "WorldCollection(overworld=$_overworld, nether=$_nether, theEnd=$_theEnd)"
    }


}