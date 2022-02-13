package world.komq.paralleluniverse.api.data

import world.komq.paralleluniverse.api.LibraryLoader
import java.util.*

interface LobbyDataManager {
    companion object: LobbyDataManager by LibraryLoader.loadImplement(LobbyDataManager::class.java)
    
    fun getVisibilityOption(targetPlayerUuid: UUID): Boolean?

    fun setVisibilityOption(targetPlayerUuid: UUID, value: Boolean)
}