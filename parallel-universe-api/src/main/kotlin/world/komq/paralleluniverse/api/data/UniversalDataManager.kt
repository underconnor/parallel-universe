/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data

import net.kyori.adventure.text.Component
import world.komq.paralleluniverse.api.LibraryLoader
import world.komq.paralleluniverse.api.enums.AssignType
import world.komq.paralleluniverse.api.enums.RankType
import java.util.ArrayList
import java.util.UUID

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

interface UniversalDataManager {
    companion object: UniversalDataManager by LibraryLoader.loadImplement(UniversalDataManager::class.java)

    /// PlayerRanks ///

    /***
     * 원하는 플레이어의 랭크를 불러오거나 설정합니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param assignType 변수 변경 타입
     * @param newRankType 새로운 랭크 타입
     * @return 플레이어의 랭크 타입, 불러올 수 없거나 SET인 경우 null 반환
     */
    fun playerRanks(playerUUID: UUID, assignType: AssignType, newRankType: RankType? = null): RankType?

    /***
     * 원하는 랭크의 색을 불러옵니다.
     *
     * @param rankType 랭크 타입
     * @return 랭크에 해당하는 색상의 R, G, B의 Int값이 담긴 ArrayList<Int>
     */
    fun getRankPrefix(rankType: RankType, playerName: String?): Component

    /// PlayerCoins ///

    /***
     * 원하는 플레이어의 코인 수치를 더하거나, 빼거나, 불러오거나, 설정합니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param assignType 변수 변경 타입
     * @param amount 더하고 싶은 코인 수치 (Int)
     */
    fun playerCoins(playerUUID: UUID, assignType: AssignType, amount: Int? = null): Int?

    /// PlayerFriends ///

    /***
     * 원하는 플레이어의 친구 목록을 불러옵니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param assignType 변수 변경 타입
     * @param newFriendsList 새로운 친구 목록 ArrayList<String>
     * @return 플레이어의 친구 플레이어 이름 목록(ArrayList<String>), 불러올 수 없거나 SET인 경우 null 반환
     */
    fun playerFriends(playerUUID: UUID, assignType: AssignType, newFriendsList: ArrayList<String>? = null): ArrayList<String>?

    /// PlayerParties ///

    // TODO: Not yet implemented

//    fun addPlayerToParty()
//
//    fun removePlayerFromParty()
//
//    fun getPlayerParties()
//
//    fun removeParty()

    /// AdminSession ///

    /***
     * 관리자의 2FA 로그인 상태를 불러오거나 설정합니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param assignType 변수 변경 타입
     * @param newStatus 새로운 로그인 상태
     * @return 관리자의 2FA 로그인 상태 / 불러올 수 없거나 SET인 경우 null 반환
     */
    fun admin2FALoginStatus(playerUUID: UUID, assignType: AssignType, newStatus: Boolean? = null): Boolean?

    /***
     * 관리자의 마지막 로그인 날짜를 불러오거나 설정합니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param assignType 변수 변경 타입
     * @return 관리자의 마지막 로그인 날짜 / 불러올 수 없거나 SET인 경우 null 반환
     */
    fun adminLastLoginDate(playerUUID: UUID, assignType: AssignType): String?
}