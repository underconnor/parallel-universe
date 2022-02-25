/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data

import world.komq.paralleluniverse.api.LibraryLoader
import world.komq.paralleluniverse.api.enums.RankType
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
     * 원하는 플레이어의 랭크를 불러옵니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @return 플레이어의 랭크 타입, NONE이 반환 될 경우 오류 발생; 해당 플레이어에 대한 랭크 정보가 없음을 의미.
     */
    fun getPlayerRanks(playerUUID: UUID): RankType

    /***
     * 원하는 플레이어의 랭크를 설정합니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param rankType 새로 설정할 플레이어의 랭크 타입, NONE을 넣지 마세요!
     */
    fun setPlayerRanks(playerUUID: UUID, rankType: RankType)

    /// PlayerCoins ///

    /***
     * 원하는 플레이어의 코인 수치를 더합니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param amount 더하고 싶은 코인 수치 (Int)
     */
    fun addPlayerCoins(playerUUID: UUID, amount: Int)

    /***
     * 원하는 플레이어의 코인 수치를 뺍니다.
     * 
     * @param playerUUID 플레이어의 UUID
     * @param amount 빼고 싶은 코인 수치 (Int)
     */
    fun subtractPlayerCoins(playerUUID: UUID, amount: Int)

    /***
     * 원하는 플레이어의 코인 수치를 가져옵니다.
     * 
     * @param playerUUID 플레이어의 UUID
     * @return 현재 플레이어의 코인 수치 (Int) / 불러올 수 없는 경우 -1
     */
    fun getPlayerCoins(playerUUID: UUID): Int

    /***
     * 원하는 플레이어의 코인 수치를 설정합니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param amount 설정하고 싶은 코인 수치 (Int)
     */
    fun setPlayerCoins(playerUUID: UUID, amount: Int)

    /// PlayerFriends ///

    /***
     * 원하는 플레이어의 친구 목록을 불러옵니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @return 플레이어의 친구 플레이어 이름 목록 -> ArrayList<String>
     */
    fun getPlayerFriends(playerUUID: UUID): List<String>

    /***
     * 원하는 플레이어의 친구 목록을 설정합니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param friendsList 새로 설정할 플레이어의 친구 플레이어 이름 목록 -> ArrayList<String>
     */
    fun setPlayerFriends(playerUUID: UUID, friendsList: ArrayList<String>)

    /// PlayerParties ///

//    fun addPlayerToParty()
//
//    fun removePlayerFromParty()
//
//    /***
//     * 원하는 플레이어의 파티 정보를 불러옵니다.
//     *
//     * @param playerUUID 플레이어의 UUID
//     * @return 플레이어가 소속된 파티원 UUID 목록 -> ArrayList<String>
//     */
//    fun getPlayerParties(playerUUID: UUID): ArrayList<String>
//
//    fun removeParty()

    /// AdminSession ///

    /***
     * 관리자의 2FA 로그인 상태를 불러옵니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @return 관리자의 2FA 로그인 상태 / 불러올 수 없을 경우 null 반환
     */
    fun getAdmin2FALoginStatus(playerUUID: UUID): Boolean?

    /***
     * 관리자의 2FA 로그인 상태를 설정합니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @param status 설정하고 싶은 관리자의 2FA 로그인 상태
     */
    fun setAdmin2FALoginStatus(playerUUID: UUID, status: Boolean)

    /***
     * 관리자의 마지막 로그인 날짜를 불러옵니다.
     *
     * @param playerUUID 플레이어의 UUID
     * @return 관리자의 마지막 로그인 날짜 / 불러 올 수 없는 경우 빈 String 반환
     */
    fun getAdminLastLoginDate(playerUUID: UUID): String

    /***
     * 관리자의 마지막 로그인 날짜를 설정합니다.
     *
     * @param playerUUID 플레이어의 UUID
     */
    fun setAdminLastloginDate(playerUUID: UUID)
}