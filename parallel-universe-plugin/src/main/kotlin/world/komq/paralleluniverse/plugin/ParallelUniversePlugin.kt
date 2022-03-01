/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.plugin

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import world.komq.paralleluniverse.api.DatabaseManager.Companion.commit
import world.komq.paralleluniverse.api.data.UniversalDataManager.Companion.getAdminLastLoginDate
import world.komq.paralleluniverse.api.data.UniversalDataManager.Companion.setAdminLastloginDate
import world.komq.paralleluniverse.plugin.ParallelUniverseObject.plugin
import world.komq.paralleluniverse.plugin.ParallelUniverseObject.server

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

object ParallelUniverseObject {
    val plugin = ParallelUniversePlugin.instance

    val server = plugin.server
}

class ParallelUniversePlugin: JavaPlugin() {

    companion object {
        lateinit var instance: ParallelUniversePlugin
        private set
    }

    override fun onEnable() {
        instance = this

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

        if (getAdminLastLoginDate(p.uniqueId) == null) {
            plugin.logger.warning("${p.name}'s last login date is null.")
        }

        setAdminLastloginDate(p.uniqueId)
        plugin.logger.info("Login date has been set to : ${getAdminLastLoginDate(p.uniqueId)}.")
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onPlayerQuit(e: PlayerQuitEvent) {
        if (task != null) {
            server.scheduler.cancelTask(requireNotNull(task?.taskId))
        }
    }
}