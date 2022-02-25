/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data.internal

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import world.komq.paralleluniverse.api.DatabaseManager.Companion.boolToInt
import world.komq.paralleluniverse.api.DatabaseManager.Companion.intToBool
import world.komq.paralleluniverse.api.DatabaseManager.Companion.getUniversalDatabase
import world.komq.paralleluniverse.api.PluginManager.plugin
import world.komq.paralleluniverse.api.data.UniversalDataManager
import world.komq.paralleluniverse.api.data.universal.AdminContents
import world.komq.paralleluniverse.api.data.universal.PlayerCoins
import world.komq.paralleluniverse.api.data.universal.PlayerFriends
import world.komq.paralleluniverse.api.data.universal.PlayerRanks
import world.komq.paralleluniverse.api.data.universal.PlayerRanks.rank
import world.komq.paralleluniverse.api.enums.RankType
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

@Suppress("UNUSED")
class UniversalDataManagerImpl: UniversalDataManager {
    override fun getPlayerRanks(playerUUID: UUID): RankType {
        var rankType = RankType.NONE

        transaction(getUniversalDatabase()) {
            try {
                PlayerRanks.select {
                    PlayerRanks.uuid eq playerUUID.toString()
                }.single().also { rankType = RankType.valueOf(it[rank].uppercase()) }
            }
            catch (e: NoSuchElementException) {
                plugin.logger.severe("[UniversalData] 플레이어 \"${plugin.server.getPlayer(playerUUID)?.name}\"의 랭크를 가져 올 수 없습니다.")
            }
        }

        return rankType
    }

    override fun setPlayerRanks(playerUUID: UUID, rankType: RankType) {
        transaction(getUniversalDatabase()) {
            try {
                PlayerRanks.select {
                    PlayerRanks.uuid eq playerUUID.toString()
                }.single()
                PlayerRanks.update({ PlayerRanks.uuid eq playerUUID.toString() }) {
                    it[rank] = rankType.toString().uppercase()
                }
            }
            catch (e: NoSuchElementException) {
                PlayerRanks.insert {
                    it[uuid] = playerUUID.toString()
                    it[rank] = rankType.toString().uppercase()
                }
            }
        }
    }

    override fun addPlayerCoins(playerUUID: UUID, amount: Int) {
        var originalCoins = 0

        transaction(getUniversalDatabase()) {
            try {
                PlayerCoins.select {
                    PlayerCoins.uuid eq playerUUID.toString()
                }.single().also { originalCoins = it[PlayerCoins.coin] }
                PlayerCoins.update({ PlayerCoins.uuid eq playerUUID.toString() }) {
                    it[coin] = originalCoins + amount
                }
            }
            catch (e: NoSuchElementException) {
                PlayerCoins.insert {
                    it[uuid] = playerUUID.toString()
                    it[coin] = originalCoins + amount
                }
            }
        }
    }

    override fun subtractPlayerCoins(playerUUID: UUID, amount: Int) {
        var originalCoins = 0

        transaction(getUniversalDatabase()) {
            try {
                PlayerCoins.select {
                    PlayerCoins.uuid eq playerUUID.toString()
                }.single().also { originalCoins = it[PlayerCoins.coin] }
                PlayerCoins.update({ PlayerCoins.uuid eq playerUUID.toString() }) {
                    it[coin] = 0.coerceAtLeast(originalCoins - amount)
                }
            }
            catch (e: NoSuchElementException) {
                PlayerCoins.insert {
                    it[uuid] = playerUUID.toString()
                    it[coin] = 0.coerceAtLeast(originalCoins - amount)
                }
            }
        }
    }

    override fun getPlayerCoins(playerUUID: UUID): Int {
        var originalCoins = -1

        transaction(getUniversalDatabase()) {
            try {
                PlayerCoins.select {
                    PlayerCoins.uuid eq playerUUID.toString()
                }.single().also { originalCoins = it[PlayerCoins.coin] }
            }
            catch (e: NoSuchElementException) {
                plugin.logger.severe("[UniversalData] 플레이어 \"${plugin.server.getPlayer(playerUUID)?.name}\"의 코인 수를 가져 올 수 없습니다.")
            }
        }

        return originalCoins
    }

    override fun setPlayerCoins(playerUUID: UUID, amount: Int) {
        transaction(getUniversalDatabase()) {
            try {
                PlayerCoins.select {
                    PlayerCoins.uuid eq playerUUID.toString()
                }.single()
                PlayerCoins.update({ PlayerCoins.uuid eq playerUUID.toString() }) {
                    it[coin] = amount
                }
            }
            catch (e: NoSuchElementException) {
                PlayerCoins.insert {
                    it[uuid] = playerUUID.toString()
                    it[coin] = amount
                }
            }
        }
    }

    override fun getPlayerFriends(playerUUID: UUID): List<String> {
        var friendsList: List<String> = listOf()

        transaction(getUniversalDatabase()) {
            try {
                PlayerFriends.select {
                    PlayerFriends.uuid eq playerUUID.toString()
                }.single().also {
                    friendsList = it[PlayerFriends.friends].split(",")
                }
            }
            catch (e: NoSuchElementException) {
                plugin.logger.severe("[UniversalData] 플레이어 \"${plugin.server.getPlayer(playerUUID)?.name}\"의 친구 목록을 가져 올 수 없습니다.")
            }
        }

        return friendsList
    }

    override fun setPlayerFriends(playerUUID: UUID, friendsList: ArrayList<String>) {
        transaction(getUniversalDatabase()) {
            try {
                PlayerFriends.select {
                    PlayerFriends.uuid eq playerUUID.toString()
                }.single()
                PlayerFriends.update({ PlayerFriends.uuid eq playerUUID.toString() }) {
                    it[friends] = friendsList.toString().removePrefix("[").removeSuffix("]")
                }
            }
            catch (e: NoSuchElementException) {
                PlayerFriends.insert {
                    it[uuid] = playerUUID.toString()
                    it[friends] = friendsList.toString().removePrefix("[").removeSuffix("]")
                }
            }
        }
    }

    override fun getAdmin2FALoginStatus(playerUUID: UUID): Boolean? {
        var status: Boolean? = null

        transaction(getUniversalDatabase()) {
            try {
                AdminContents.select {
                    AdminContents.uuid eq playerUUID.toString()
                }.single().also { status = intToBool(it[AdminContents.tfastatus]) }
            }
            catch (e: NoSuchElementException) {
                plugin.logger.severe("[UniversalData] 관리자 플레이어 \"${plugin.server.getPlayer(playerUUID)?.name}\"의 2FA 로그인 상태를 가져 올 수 없습니다.")
            }
        }

        return status
    }

    override fun setAdmin2FALoginStatus(playerUUID: UUID, status: Boolean) {
        transaction(getUniversalDatabase()) {
            try {
                AdminContents.select {
                    AdminContents.uuid eq playerUUID.toString()
                }.single()
                AdminContents.update({ AdminContents.uuid eq playerUUID.toString() }) {
                    it[tfastatus] = boolToInt(status)
                }
            }
            catch (e: NoSuchElementException) {
                AdminContents.insert {
                    it[uuid] = playerUUID.toString()
                    it[tfastatus] = boolToInt(status)
                }
            }
        }
    }

    override fun getAdminLastLoginDate(playerUUID: UUID): String {
        var date = ""

        transaction(getUniversalDatabase()) {
            try {
                AdminContents.select {
                    AdminContents.uuid eq playerUUID.toString()
                }.single().also { date = it[AdminContents.lastlogindate] }
            }
            catch (e: NoSuchElementException) {
                plugin.logger.severe("[UniversalData] 관리자 플레이어 \"${plugin.server.getPlayer(playerUUID)?.name}\"의 마지막 로그인 날짜를 가져 올 수 없습니다.")
            }
        }

        return date
    }

    override fun setAdminLastloginDate(playerUUID: UUID) {
        val currentDate = currentTimeMillis()
        val format = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
        val finalDate = format.format(currentDate)

        transaction(getUniversalDatabase()) {
            try {
                AdminContents.select {
                    AdminContents.uuid eq playerUUID.toString()
                }.single()
                AdminContents.update({ AdminContents.uuid eq playerUUID.toString() }) {
                    it[lastlogindate] = finalDate
                }
            }
            catch (e: NoSuchElementException) {
                AdminContents.insert {
                    it[uuid] = playerUUID.toString()
                    it[lastlogindate] = finalDate
                }
            }
        }
    }
}