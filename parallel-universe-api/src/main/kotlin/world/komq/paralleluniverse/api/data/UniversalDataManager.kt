package world.komq.paralleluniverse.api.data

import world.komq.paralleluniverse.api.LibraryLoader

interface UniversalDataManager {
    companion object: UniversalDataManager by LibraryLoader.loadImplement(UniversalDataManager::class.java)

    fun getPlayerRank()

    fun setPlayerRank()

    fun getPlayerStats()

    fun setPlayerStats()

    fun getPlayerKits()

    fun setPlayerKits()

    fun getPlayerDefaultKit()

    fun setPlayerDefaultKit()

    fun getPlayerParty()

    fun setPlayerParty()

    fun getPlayerFriends()

    fun setPlayerFriends()
}