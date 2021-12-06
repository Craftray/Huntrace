package io.craftray.huntrace.game.collection

import org.bukkit.World

// proxy for non-null type field
@Suppress("PropertyName")
class WorldCollection {
    private var _overworld: World? = null
        get() = checkNotNull(field) { "World \"overworld\" is not set" }
        set(value) {
            check(field == null) { "World \"overworld\" already set" }
            require(value!!.environment == World.Environment.NORMAL) {
                "Environment of world \"overworld\" must be \"NORMAL\" but it is ${value.environment}"
            }
            field = value
        }

    private var _nether: World? = null
        get() = checkNotNull(field) { "World \"nether\" is not set" }
        set(value) {
            check(field == null) { "World \"nether\" already set" }
            require(value!!.environment == World.Environment.NETHER) {
                "Environment of world \"nether\" must be \"NETHER\" but it is ${value.environment}"
            }
            field = value
        }

    private var _theEnd: World? = null
        get() = checkNotNull(field) { "World \"theEnd\" is not set" }
        set(value) {
            check(field == null) { "World \"theEnd\" already set" }
            require(value!!.environment == World.Environment.THE_END) {
                "Environment of world \"theEnd\" must be \"THE_END\" but it is ${value.environment}"
            }
            field = value
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

    operator fun contains(world: World) = world == overworld || world == nether || world == theEnd

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

    override fun toString() = "WorldCollection(overworld=$_overworld, nether=$_nether, theEnd=$_theEnd)"


}