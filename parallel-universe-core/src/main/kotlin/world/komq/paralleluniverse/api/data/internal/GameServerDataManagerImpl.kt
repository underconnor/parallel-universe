package world.komq.paralleluniverse.api.data.internal

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import world.komq.paralleluniverse.api.DatabaseManager.Companion.boolToInt
import world.komq.paralleluniverse.api.DatabaseManager.Companion.getGameServerDatabase
import world.komq.paralleluniverse.api.PluginManager.plugin
import world.komq.paralleluniverse.api.data.GameServerDataManager
import world.komq.paralleluniverse.api.data.gameserver.Acxivity
import world.komq.paralleluniverse.api.data.gameserver.Fakepit
import world.komq.paralleluniverse.api.data.gameserver.Invixible
import world.komq.paralleluniverse.api.data.gameserver.Towel
import world.komq.paralleluniverse.api.enums.GameType

@Suppress("UNUSED")
class GameServerDataManagerImpl: GameServerDataManager {
    override fun getAvailableServers(gameType: GameType): ArrayList<String> {
        val type = gameType.toString().lowercase()
        val availableServers = ArrayList<String>()

        availableServers.clear()

        transaction(getGameServerDatabase()) {
            try {
                when (gameType) {
                    GameType.FAKEPIT -> {
                        Fakepit.select {
                            Fakepit.isavailable eq boolToInt(true)
                        }.forEach { availableServers.add(it[Fakepit.name]) }
                    }
                    GameType.ACXIVITY -> {
                        Acxivity.select {
                            Acxivity.isavailable eq boolToInt(true)
                        }.forEach { availableServers.add(it[Acxivity.name]) }
                    }
                    GameType.INVIXIBLE -> {
                        Invixible.select {
                            Invixible.isavailable eq boolToInt(true)
                        }.forEach { availableServers.add(it[Invixible.name]) }
                    }
                    GameType.TOWEL -> {
                        Towel.select {
                            Towel.isavailable eq boolToInt(true)
                        }.forEach { availableServers.add(it[Towel.name]) }
                    }
                }
            }
            catch (e: NoSuchElementException) {
                plugin.logger.warning("[GameServerData] 게임 ${type}의 이용 가능한 서버를 가져 올 수 없습니다.")
            }
        }

        return availableServers
    }

    override fun setServerStatus(gameType: GameType, servername: String, isAvailable: Boolean) {
        val type = gameType.toString().lowercase()

        transaction(getGameServerDatabase()) {
            try {
                when (gameType) {
                    GameType.FAKEPIT -> {
                        Fakepit.select {
                            Fakepit.name like "${type}%"
                        }
                        Fakepit.update({ Fakepit.name like "${type}%" }) {
                            it[isavailable] = boolToInt(isAvailable)
                        }
                    }
                    GameType.ACXIVITY -> {
                        Acxivity.select {
                            Acxivity.name like "${type}%"
                        }
                        Acxivity.update({ Acxivity.name like "${type}%" }) {
                            it[isavailable] = boolToInt(isAvailable)
                        }
                    }
                    GameType.INVIXIBLE -> {
                        Invixible.select {
                            Invixible.name like "${type}%"
                        }
                        Invixible.update({ Invixible.name like "${type}%" }) {
                            it[isavailable] = boolToInt(isAvailable)
                        }
                    }
                    GameType.TOWEL -> {
                        Towel.select {
                            Towel.name like "${type}%"
                        }
                        Towel.update({ Towel.name like "${type}%" }) {
                            it[isavailable] = boolToInt(isAvailable)
                        }
                    }
                }
            }
            catch (e: NoSuchElementException) {
                when (gameType) {
                    GameType.FAKEPIT -> {
                        Fakepit.insert {
                            it[name] = servername
                            it[isavailable] = boolToInt(isAvailable)
                        }
                    }
                    GameType.ACXIVITY -> {
                        Acxivity.insert {
                            it[name] = servername
                            it[isavailable] = boolToInt(isAvailable)
                        }
                    }
                    GameType.INVIXIBLE -> {
                        Invixible.insert {
                            it[name] = servername
                            it[isavailable] = boolToInt(isAvailable)
                        }
                    }
                    GameType.TOWEL -> {
                        Towel.insert {
                            it[name] = servername
                            it[isavailable] = boolToInt(isAvailable)
                        }
                    }
                }
            }
        }
    }
}