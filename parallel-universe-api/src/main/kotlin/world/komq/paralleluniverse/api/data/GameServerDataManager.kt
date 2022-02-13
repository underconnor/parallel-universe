package world.komq.paralleluniverse.api.data

import world.komq.paralleluniverse.api.LibraryLoader
import world.komq.paralleluniverse.api.enums.GameTypeEnum

interface GameServerDataManager {
    companion object: GameServerDataManager by LibraryLoader.loadImplement(GameServerDataManager::class.java)

    fun getAvailableServers(gameType: GameTypeEnum): ArrayList<String>

    fun setServerStatus(name: String, gameType: GameTypeEnum, isAvailable: Boolean)
}