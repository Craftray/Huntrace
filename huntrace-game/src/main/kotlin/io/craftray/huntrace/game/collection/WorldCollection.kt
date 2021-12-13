package io.craftray.huntrace.game.collection

import org.bukkit.World
import kotlin.reflect.KProperty

class WorldCollection {
    var overworld: World by TypedWorldDelegate(World.Environment.NORMAL)

    var nether: World by TypedWorldDelegate(World.Environment.NETHER)

    var theEnd: World by TypedWorldDelegate(World.Environment.THE_END)

    operator fun contains(world: World) = world == overworld || world == nether || world == theEnd

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorldCollection

        if (overworld != other.overworld) return false
        if (nether != other.nether) return false
        if (theEnd != other.theEnd) return false

        return true
    }

    override fun hashCode(): Int {
        var result = overworld.hashCode()
        result = 31 * result + nether.hashCode()
        result = 31 * result + theEnd.hashCode()
        return result
    }

    override fun toString() = "WorldCollection(overworld=$overworld, nether=$nether, theEnd=$theEnd)"

    private inner class TypedWorldDelegate(val env: World.Environment) {
        lateinit var world: World

        operator fun getValue(thisRef: WorldCollection, property: KProperty<*>): World {
            check(::world.isInitialized) { "World \"${property.name}\" is not set" }
            return world
        }

        operator fun setValue(thisRef: WorldCollection, property: KProperty<*>, value: World) {
            check(!::world.isInitialized) { "World \"${property.name}\" already set" }
            check(value.environment == env) {
                "Environment of world \"${property.name}\" must be \"$env\" but it is ${value.environment}"
            }
            world = value
        }
    }
}