package world.komq.paralleluniverse.plugin

import net.kyori.adventure.text.Component.text
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import world.komq.paralleluniverse.api.DatabaseManager.Companion.commit
import world.komq.paralleluniverse.api.PluginManager
import world.komq.paralleluniverse.api.PluginManager.plugin
import world.komq.paralleluniverse.api.PluginManager.server
import world.komq.paralleluniverse.api.data.UniversalDataManager
import world.komq.paralleluniverse.plugin.ParallelUniversePlugin.Companion.message

class ParallelUniversePlugin: JavaPlugin() {

    companion object {
        lateinit var instance: ParallelUniversePlugin
        private set

        lateinit var message: String
        private set
    }

    override fun onEnable() {
        instance = this
        plugin = this
        PluginManager.server = this.server
        message = "test"

        server.pluginManager.registerEvents(ParallelUniverseEvent(), this)
    }

    override fun onDisable() {
        commit()
    }
}

class ParallelUniverseEvent: Listener {
    private var task: BukkitTask? = null

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val p = e.player

        if (UniversalDataManager.getPlayerCoins(p.uniqueId) == -1) {
            UniversalDataManager.setPlayerCoins(p.uniqueId, 0)
        }

        task = server.scheduler.runTaskTimer(plugin, Runnable {
            server.broadcast(text(UniversalDataManager.getPlayerCoins(p.uniqueId)))
        }, 0L,0L)

        e.joinMessage(text(message))
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        server.scheduler.cancelTask(requireNotNull(task?.taskId))
        e.quitMessage(null)
    }
}