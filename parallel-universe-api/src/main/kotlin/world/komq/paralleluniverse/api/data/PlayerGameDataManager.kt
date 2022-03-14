/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data

import world.komq.paralleluniverse.api.LibraryLoader
import world.komq.paralleluniverse.api.enums.GameType
import world.komq.paralleluniverse.api.enums.AssignType
import world.komq.paralleluniverse.api.enums.DataType
import java.util.*

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

interface PlayerGameDataManager {
    companion object: PlayerGameDataManager by LibraryLoader.loadImplement(PlayerGameDataManager::class.java)

    /***
     * 원하는 플레이어의 여태까지 특정 게임에서의 정수 데이터에 수치를 더하거나, 빼거나, 불러오거나, 설정합니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param gameType 설정하고 싶은 코인의 게임 타입
     * @param assignType 변수 변경 타입
     * @param amount 더하고 싶은 코인 수치 (Int)
     */
    fun modifyPlayerIntStatData(playerUUID: UUID, gameType: GameType, dataType: DataType, assignType: AssignType, amount: Int? = null): Int?
}