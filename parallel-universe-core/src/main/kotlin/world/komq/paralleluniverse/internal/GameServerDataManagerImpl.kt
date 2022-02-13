package world.komq.paralleluniverse.internal

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import world.komq.paralleluniverse.api.MainDatabaseManager.Companion.boolToInt
import world.komq.paralleluniverse.api.PluginManager.plugin
import world.komq.paralleluniverse.api.data.GameServerDataManager
import world.komq.paralleluniverse.api.data.gameserver.Servers
import world.komq.paralleluniverse.api.enums.GameTypeEnum

@Suppress("UNUSED")
class GameServerDataManagerImpl: GameServerDataManager {
    override fun getAvailableServers(gameType: GameTypeEnum): ArrayList<String> {
        val type = gameType.toString().lowercase()
        val availableServers = ArrayList<String>()

        transaction {
            try {
                availableServers.clear()
                Servers.select {
                    Servers.gametype like "${type}%"
                    Servers.isavailable eq boolToInt(true)
                }.forEach {
                    availableServers.add(it[Servers.name])
                }
            } catch (e: NoSuchElementException) {
                plugin.logger.warning("[GameServerData] 게임 ${type}의 이용 가능한 서버가 없습니다.")
            }
        }

        return availableServers
    }

    override fun setServerStatus(name: String, gameType: GameTypeEnum, isAvailable: Boolean) {
        transaction {
            try {
                Servers.select {
                    Servers.gametype eq gameType.toString().lowercase()
                    Servers.name eq name
                }.single()
                Servers.update({ Servers.gametype eq gameType.toString().lowercase(); Servers.name eq name }) {
                     it[isavailable] = boolToInt(isAvailable)
                }
            } catch (e: NoSuchElementException) {
                Servers.insert {
                    it[Servers.name] = name
                    it[isavailable] = boolToInt(isAvailable)
                }
            }
        }
    }
}