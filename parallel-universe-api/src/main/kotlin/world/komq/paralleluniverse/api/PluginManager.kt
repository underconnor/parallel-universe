package world.komq.paralleluniverse.api

import org.bukkit.plugin.Plugin

object PluginManager {
    lateinit var plugin: Plugin

    val server = plugin.server
}