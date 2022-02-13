package world.komq.paralleluniverse.internal

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import world.komq.paralleluniverse.api.MainDatabaseManager.Companion.boolToInt
import world.komq.paralleluniverse.api.MainDatabaseManager.Companion.getDatabase
import world.komq.paralleluniverse.api.MainDatabaseManager.Companion.intToBool
import world.komq.paralleluniverse.api.PluginManager.plugin
import world.komq.paralleluniverse.api.PluginManager.server
import world.komq.paralleluniverse.api.data.LobbyDataManager
import world.komq.paralleluniverse.api.data.lobby.VisibilityOption
import world.komq.paralleluniverse.api.data.lobby.VisibilityOption.option
import world.komq.paralleluniverse.api.enums.DatabaseType
import java.util.*

@Suppress("UNUSED")
class LobbyDataManagerImpl: LobbyDataManager {
    override fun getVisibilityOption(targetPlayerUuid: UUID): Boolean? {
        var result: Boolean? = null
        transaction(getDatabase(databaseType = DatabaseType.LOBBY)) {
            try {
                VisibilityOption.select {
                    VisibilityOption.uuid eq targetPlayerUuid.toString()
                }.single().also { result = intToBool(it[option]) }
            } catch (e: NoSuchElementException) {
                plugin.logger.warning("[LobbyDB] ${server.getPlayer(targetPlayerUuid)?.name}의 VisibilityOption 데이터가 없습니다.")
            }
        }
        return result
    }

    override fun setVisibilityOption(targetPlayerUuid: UUID, value: Boolean) {
        transaction(getDatabase(databaseType = DatabaseType.LOBBY)) {
            try {
                VisibilityOption.select {
                    VisibilityOption.uuid eq targetPlayerUuid.toString()
                }.single()
                VisibilityOption.update({ VisibilityOption.uuid eq targetPlayerUuid.toString() }) {
                    it[option] = boolToInt(value)
                }
            } catch (e: NoSuchElementException) {
                VisibilityOption.insert {
                    it[uuid] = targetPlayerUuid.toString()
                    it[option] = boolToInt(value)
                }
            }
        }
    }
}