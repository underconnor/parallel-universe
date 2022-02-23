package world.komq.paralleluniverse.api.data

import world.komq.paralleluniverse.api.LibraryLoader

interface PlayerGameDataManager {
    companion object: PlayerGameDataManager by LibraryLoader.loadImplement(PlayerGameDataManager::class.java)
}