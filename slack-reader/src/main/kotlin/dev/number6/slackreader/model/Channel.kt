package dev.number6.slackreader.model

import java.util.*

class Channel(val id: String, val name: String) {

    override fun toString(): String {
        return "Channel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}'
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val channel = o as Channel
        return id == channel.id &&
                name == channel.name
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }

}