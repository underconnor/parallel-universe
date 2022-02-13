package world.komq.paralleluniverse.plugin

import org.bukkit.plugin.java.JavaPlugin
import world.komq.paralleluniverse.api.PluginManager.plugin

class ParallelUniversePlugin: JavaPlugin() {
    private val dbDriver = "org.mariadb.jdbc.driver"
    private val username = "mettaton"
    private val password = "kissthesexyrobot?"

    override fun onEnable() {
        plugin = this
    }
}