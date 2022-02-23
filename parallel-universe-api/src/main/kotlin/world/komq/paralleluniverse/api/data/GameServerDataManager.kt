package world.komq.paralleluniverse.api.data

import world.komq.paralleluniverse.api.LibraryLoader
import world.komq.paralleluniverse.api.enums.GameType

interface GameServerDataManager {
    companion object: GameServerDataManager by LibraryLoader.loadImplement(GameServerDataManager::class.java)

    /***
     * 현재 접속 가능한 서버들을 불러옵니다.
     * @param gameType 확인하고 싶은 서버의 게임 타입
     * @return 현재 접속 가능한 서버들의 이름 ArrayList
     */
    fun getAvailableServers(gameType: GameType): ArrayList<String>

    /***
     * 서버의 상태를 설정합니다.
     * @param gameType 설정하고 싶은 서버의 게임 타입
     * @param servername 설정하고 싶은 서버의 이름
     * @param isAvailable 현재 서버가 이용 가능한지에 대한 Boolean
     */
    fun setServerStatus(gameType: GameType, servername: String, isAvailable: Boolean)
}