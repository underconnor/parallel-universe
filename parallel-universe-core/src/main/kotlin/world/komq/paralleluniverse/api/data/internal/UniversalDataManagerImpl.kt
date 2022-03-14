/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data.internal

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import world.komq.paralleluniverse.api.DatabaseManager.Companion.boolToInt
import world.komq.paralleluniverse.api.DatabaseManager.Companion.getUniversalDatabase
import world.komq.paralleluniverse.api.DatabaseManager.Companion.intToBool
import world.komq.paralleluniverse.api.LoggerObject.logger
import world.komq.paralleluniverse.api.data.UniversalDataManager
import world.komq.paralleluniverse.api.data.universal.AdminContents
import world.komq.paralleluniverse.api.data.universal.PlayerCoins
import world.komq.paralleluniverse.api.data.universal.PlayerFriends
import world.komq.paralleluniverse.api.data.universal.PlayerRanks
import world.komq.paralleluniverse.api.data.universal.PlayerRanks.rank
import world.komq.paralleluniverse.api.enums.AssignType
import world.komq.paralleluniverse.api.enums.RankType
import java.time.LocalDateTime
import java.util.*

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

@Suppress("UNUSED")
class UniversalDataManagerImpl: UniversalDataManager {
    private val db = getUniversalDatabase()

    override fun playerRanks(playerUUID: UUID, assignType: AssignType, newRankType: RankType?): RankType? {
        var rankType: RankType? = null

        if (db != null) {
            transaction(db) {
                when (assignType) {
                    AssignType.GET -> {
                        try {
                            PlayerRanks.select {
                                PlayerRanks.uuid eq playerUUID.toString()
                            }.single().also { rankType = RankType.valueOf(it[rank].uppercase()) }
                        } catch (e: NoSuchElementException) {
                            logger.severe("[UniversalData] UUID \"${playerUUID}\"의 랭크를 가져 올 수 없습니다.")
                        }
                    }
                    AssignType.SET -> {
                        try {
                            PlayerRanks.select {
                                PlayerRanks.uuid eq playerUUID.toString()
                            }.single()
                            PlayerRanks.update({ PlayerRanks.uuid eq playerUUID.toString() }) {
                                it[rank] = newRankType.toString().uppercase()
                            }
                        } catch (e: NoSuchElementException) {
                            PlayerRanks.insert {
                                it[uuid] = playerUUID.toString()
                                it[rank] = newRankType.toString().uppercase()
                            }
                        }
                    }
                    else -> {
                        logger.severe("[UniversalData] 잘못된 AssingType을 설정하셨습니다. (GET/SET 중 하나를 선택해주세요.)")
                    }
                }
            }
        }
        else {
            logger.severe("[UniversalData] UniversalData 데이터베이스를 가져 올 수 없습니다.")
        }

        return rankType
    }

    override fun getRankPrefix(rankType: RankType, playerName: String?): Component {
        var prefix = text("")

        when (rankType) {
            RankType.DEFAULT -> {
                prefix = if (playerName != null) {
                    text("[DEFAULT] $playerName").color(color(0xAAAAAA))
                } else {
                    text("[DEFAULT] ").color(color(0xAAAAAA))
                }
            }
            RankType.VIP -> {
                prefix = if (playerName != null) {
                    text("[VIP] $playerName").color(color(0xFFAA00))
                }
                else {
                    text("[VIP] ").color(color(0xFFAA00))
                }
            }
            RankType.MVP -> {
                prefix = if (playerName != null) {
                    text("[MVP] $playerName").color(color(0x55FFFF))
                }
                else {
                    text("[MVP] ").color(color(0x55FFFF))
                }
            }
            RankType.YOUTUBER -> {
                prefix = if (playerName != null) {
                    text("[YOUTUBE] $playerName").color(color(0xFF5555))
                }
                else {
                    text("[YOUTUBE] ").color(color(0xFF5555))
                }
            }
            RankType.DEV -> {
                prefix = if (playerName != null) {
                    text("[DEV] $playerName").color(color(0xFF69B4))
                }
                else {
                    text("[DEV] ").color(color(0xFF69B4))
                }
            }
            RankType.KOMQ -> {
                prefix = if (playerName != null) {
                    text("[KOMQ] $playerName").color(color(0xDC4F09))
                }
                else {
                    text("[KOMQ] ").color(color(0xDC4F09))
                }
            }
        }

        return prefix
    }

    override fun playerCoins(playerUUID: UUID, assignType: AssignType, amount: Int?): Int? {
        var coins: Int? = null

        if (db != null) {
            transaction(db) {
                when (assignType) {
                    AssignType.ADD -> {
                        try {
                            PlayerCoins.select {
                                PlayerCoins.uuid eq playerUUID.toString()
                            }.single().also { coins = it[PlayerCoins.coin] }
                            PlayerCoins.update({ PlayerCoins.uuid eq playerUUID.toString() }) {
                                it[coin] = requireNotNull(coins) + requireNotNull(amount)
                            }
                        } catch (e: NoSuchElementException) {
                            PlayerCoins.insert {
                                it[uuid] = playerUUID.toString()
                                it[coin] = 0 + requireNotNull(amount)
                            }
                        }
                    }
                    AssignType.SUBTRACT -> {
                        try {
                            PlayerCoins.select {
                                PlayerCoins.uuid eq playerUUID.toString()
                            }.single().also { coins = it[PlayerCoins.coin] }
                            PlayerCoins.update({ PlayerCoins.uuid eq playerUUID.toString() }) {
                                it[coin] = 0.coerceAtLeast(requireNotNull(coins) - requireNotNull(amount))
                            }
                        }
                        catch (e: NoSuchElementException) {
                            PlayerCoins.insert {
                                it[uuid] = playerUUID.toString()
                                it[coin] = 0.coerceAtLeast(requireNotNull(coins) - requireNotNull(amount))
                            }
                        }
                    }
                    AssignType.GET -> {
                        try {
                            PlayerCoins.select {
                                PlayerCoins.uuid eq playerUUID.toString()
                            }.single().also { coins = it[PlayerCoins.coin] }
                        }
                        catch (e: NoSuchElementException) {
                            logger.severe("[UniversalData] UUID \"${playerUUID}\"의 코인 수를 가져 올 수 없습니다.")
                        }
                    }
                    AssignType.SET -> {
                        try {
                            PlayerCoins.select {
                                PlayerCoins.uuid eq playerUUID.toString()
                            }.single()
                            PlayerCoins.update({ PlayerCoins.uuid eq playerUUID.toString() }) {
                                it[coin] = requireNotNull(amount)
                            }
                        } catch (e: NoSuchElementException) {
                            PlayerCoins.insert {
                                it[uuid] = playerUUID.toString()
                                it[coin] = requireNotNull(amount)
                            }
                        }
                    }
                }
            }
        }
        else {
            logger.severe("[UniversalData] UniversalData 데이터베이스를 가져 올 수 없습니다.")
        }

        return coins
    }

    override fun playerFriends(playerUUID: UUID, assignType: AssignType, newFriendsList: ArrayList<String>?): ArrayList<String>? {
        var friendsList: ArrayList<String>? = null

        if (db != null) {
            transaction(db) {
                when (assignType) {
                    AssignType.GET -> {
                        try {
                            PlayerFriends.select {
                                PlayerFriends.uuid eq playerUUID.toString()
                            }.single().also {
                                friendsList = arrayListOf(it[PlayerFriends.friends].split(",").toString().removePrefix("[").removeSuffix("]"))
                            }
                        } catch (e: NoSuchElementException) {
                            logger.severe("[UniversalData] UUID \"${playerUUID}\"의 친구 목록을 가져 올 수 없습니다.")
                        }
                    }
                    AssignType.SET -> {
                        try {
                            PlayerFriends.select {
                                PlayerFriends.uuid eq playerUUID.toString()
                            }.single()
                            PlayerFriends.update({ PlayerFriends.uuid eq playerUUID.toString() }) {
                                it[friends] = newFriendsList.toString().removePrefix("[").removeSuffix("]")
                            }
                        } catch (e: NoSuchElementException) {
                            PlayerFriends.insert {
                                it[uuid] = playerUUID.toString()
                                it[friends] = newFriendsList.toString().removePrefix("[").removeSuffix("]")
                            }
                        }
                    }
                    else -> {
                        logger.severe("[UniversalData] 잘못된 AssingType을 설정하셨습니다. (GET/SET 중 하나를 선택해주세요.)")
                    }
                }
            }
        }
        else {
            logger.severe("[UniversalData] UniversalData 데이터베이스를 가져 올 수 없습니다.")
        }

        return friendsList
    }

    override fun admin2FALoginStatus(playerUUID: UUID, assignType: AssignType, newStatus: Boolean?): Boolean? {
        var status: Boolean? = null

        if (db != null) {
            transaction(db) {
                when (assignType) {
                    AssignType.GET -> {
                        try {
                            AdminContents.select {
                                AdminContents.uuid eq playerUUID.toString()
                            }.single().also { status = intToBool(it[AdminContents.tfastatus]) }
                        } catch (e: NoSuchElementException) {
                            logger.severe("[UniversalData] 관리자 UUID \"${playerUUID}\"의 2FA 로그인 상태를 가져 올 수 없습니다.")
                        }
                    }
                    AssignType.SET -> {
                        try {
                            AdminContents.select {
                                AdminContents.uuid eq playerUUID.toString()
                            }.single()
                            AdminContents.update({ AdminContents.uuid eq playerUUID.toString() }) {
                                it[tfastatus] = boolToInt(requireNotNull(newStatus))
                            }
                        } catch (e: NoSuchElementException) {
                            AdminContents.insert {
                                it[uuid] = playerUUID.toString()
                                it[tfastatus] = boolToInt(requireNotNull(newStatus))
                            }
                        }
                    }
                    else -> {
                        logger.severe("[UniversalData] 잘못된 AssingType을 설정하셨습니다. (GET/SET 중 하나를 선택해주세요.)")
                    }
                }
            }
        }
        else {
            logger.severe("[UniversalData] UniversalData 데이터베이스를 가져 올 수 없습니다.")
        }

        return status
    }

    override fun adminLastLoginDate(playerUUID: UUID, assignType: AssignType): String? {
        var date: String? = null
        val localDateTime = LocalDateTime.now()
        val newDate = "${localDateTime.year}-${localDateTime.monthValue}-${localDateTime.dayOfMonth} ${localDateTime.hour}:${localDateTime.minute}:${localDateTime.second}"

        if (db != null) {
            transaction(db) {
                when (assignType) {
                    AssignType.GET -> {
                        try {
                            AdminContents.select {
                                AdminContents.uuid eq playerUUID.toString()
                            }.single().also { date = it[AdminContents.lastlogindate] }
                        } catch (e: NoSuchElementException) {
                            logger.severe("[UniversalData] 관리자 UUID \"${playerUUID}\"의 마지막 로그인 날짜를 가져 올 수 없습니다.")
                        }
                    }
                    AssignType.SET -> {
                        try {
                            AdminContents.select {
                                AdminContents.uuid eq playerUUID.toString()
                            }.single()
                            AdminContents.update({ AdminContents.uuid eq playerUUID.toString() }) {
                                it[lastlogindate] = newDate
                            }
                        } catch (e: NoSuchElementException) {
                            AdminContents.insert {
                                it[uuid] = playerUUID.toString()
                                it[lastlogindate] = newDate
                            }
                        }
                    }
                    else -> {
                        logger.severe("[UniversalData] 잘못된 AssingType을 설정하셨습니다. (GET/SET 중 하나를 선택해주세요.)")
                    }
                }
            }
        }
        else {
            logger.severe("[UniversalData] UniversalData 데이터베이스를 가져 올 수 없습니다.")
        }

        return date
    }
}