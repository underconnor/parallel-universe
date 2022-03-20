/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data.internal

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import world.komq.paralleluniverse.api.DatabaseManager.Companion.getPlayerGameDatabase
import world.komq.paralleluniverse.api.LoggerObject.logger
import world.komq.paralleluniverse.api.data.PlayerGameDataManager
import world.komq.paralleluniverse.api.data.playergame.PlayerStats
import world.komq.paralleluniverse.api.enums.AssignType
import world.komq.paralleluniverse.api.enums.DataType
import world.komq.paralleluniverse.api.enums.GameType
import java.util.*

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

@Suppress("UNUSED")
class PlayerGameDataManagerImpl: PlayerGameDataManager {
    private val db = getPlayerGameDatabase()

    override fun modifyPlayerIntStatData(playerUUID: UUID, gameType: GameType, dataType: DataType, assignType: AssignType, amount: Int?): Int? {
        val type = gameType.toString().uppercase()
        var requiredInt: Int? = null

        if (db != null) {
            transaction(db) {
                when (dataType) {
                    DataType.COIN -> {
                        when (assignType) {
                            AssignType.ADD -> {
                                try {
                                    PlayerStats.select {
                                        PlayerStats.uuid eq playerUUID.toString()
                                        PlayerStats.gametype eq type
                                    }.single().also { requiredInt = it[PlayerStats.coins] }
                                    PlayerStats.update({ PlayerStats.uuid eq playerUUID.toString() and (PlayerStats.gametype eq type) }) {
                                        val finalAmount = if (requiredInt != null) requiredInt?.plus(requireNotNull(amount)) else 0 + requireNotNull(amount)

                                        if (finalAmount != null) {
                                            it[coins] = finalAmount
                                        }
                                    }
                                } catch (e: NoSuchElementException) {
                                    PlayerStats.insert {
                                        it[uuid] = playerUUID.toString()
                                        it[gametype] = type
                                        it[coins] = 0 + requireNotNull(amount)
                                    }
                                }
                            }
                            AssignType.SUBTRACT -> {
                                try {
                                    PlayerStats.select {
                                        PlayerStats.uuid eq playerUUID.toString()
                                        PlayerStats.gametype eq type
                                    }.single().also { requiredInt = it[PlayerStats.coins] }
                                    PlayerStats.update({ PlayerStats.uuid eq playerUUID.toString() and (PlayerStats.gametype eq type) }) {
                                        val finalAmount = if (requiredInt != null) 0.coerceAtLeast(requireNotNull(requiredInt) - requireNotNull(amount)) else 0

                                        it[coins] = finalAmount
                                    }
                                } catch (e: NoSuchElementException) {
                                    PlayerStats.insert {
                                        val finalAmount = if (requiredInt != null) 0.coerceAtLeast(requireNotNull(requiredInt) - requireNotNull(amount)) else 0
                                        it[uuid] = playerUUID.toString()
                                        it[gametype] = type

                                        it[coins] = finalAmount
                                    }
                                }
                            }
                            AssignType.GET -> {
                                try {
                                    PlayerStats.select {
                                        PlayerStats.uuid eq playerUUID.toString()
                                        PlayerStats.gametype eq type
                                    }.single().also { requiredInt = it[PlayerStats.coins] }
                                } catch (e: NoSuchElementException) {
                                    logger.severe("[UniversalData] UUID \"${playerUUID}\"의 코인 수를 가져 올 수 없습니다.")
                                }
                            }
                            AssignType.SET -> {
                                try {
                                    PlayerStats.select {
                                        PlayerStats.uuid eq playerUUID.toString()
                                        PlayerStats.gametype eq type
                                    }.single()
                                    PlayerStats.update({ PlayerStats.uuid eq playerUUID.toString() and (PlayerStats.gametype eq type) }) {
                                        it[coins] = requireNotNull(amount)
                                    }
                                } catch (e: NoSuchElementException) {
                                    PlayerStats.insert {
                                        it[uuid] = playerUUID.toString()
                                        it[gametype] = type
                                        it[coins] = requireNotNull(amount)
                                    }
                                }
                            }
                        }
                    }
                    DataType.KILL -> {
                        when (assignType) {
                            AssignType.ADD -> {
                                try {
                                    PlayerStats.select {
                                        PlayerStats.uuid eq playerUUID.toString()
                                        PlayerStats.gametype eq type
                                    }.single().also { requiredInt = it[PlayerStats.kills] }
                                    PlayerStats.update({ PlayerStats.uuid eq playerUUID.toString() and (PlayerStats.gametype eq type) }) {
                                        val finalAmount = if (requiredInt != null) requiredInt?.plus(requireNotNull(amount)) else 0 + requireNotNull(amount)

                                        if (finalAmount != null) {
                                            it[kills] = finalAmount
                                        }
                                    }
                                } catch (e: NoSuchElementException) {
                                    PlayerStats.insert {
                                        it[uuid] = playerUUID.toString()
                                        it[gametype] = type
                                        it[kills] = 0 + requireNotNull(amount)
                                    }
                                }
                            }
                            AssignType.SUBTRACT -> {
                                try {
                                    PlayerStats.select {
                                        PlayerStats.uuid eq playerUUID.toString()
                                        PlayerStats.gametype eq type
                                    }.single().also { requiredInt = it[PlayerStats.kills] }
                                    PlayerStats.update({ PlayerStats.uuid eq playerUUID.toString() and (PlayerStats.gametype eq type) }) {
                                        val finalAmount = if (requiredInt != null) 0.coerceAtLeast(requireNotNull(requiredInt) - requireNotNull(amount)) else 0

                                        it[kills] = finalAmount
                                    }
                                } catch (e: NoSuchElementException) {
                                    PlayerStats.insert {
                                        it[uuid] = playerUUID.toString()
                                        it[gametype] = type
                                        val finalAmount = if (requiredInt != null) 0.coerceAtLeast(requireNotNull(requiredInt) - requireNotNull(amount)) else 0

                                        it[kills] = finalAmount
                                    }
                                }
                            }
                            AssignType.GET -> {
                                try {
                                    PlayerStats.select {
                                        PlayerStats.uuid eq playerUUID.toString()
                                        PlayerStats.gametype eq type
                                    }.single().also { requiredInt = it[PlayerStats.kills] }
                                } catch (e: NoSuchElementException) {
                                    logger.severe("[UniversalData] UUID \"${playerUUID}\"의 코인 수를 가져 올 수 없습니다.")
                                }
                            }
                            AssignType.SET -> {
                                try {
                                    PlayerStats.select {
                                        PlayerStats.uuid eq playerUUID.toString()
                                        PlayerStats.gametype eq type
                                    }.single()
                                    PlayerStats.update({ PlayerStats.uuid eq playerUUID.toString() and (PlayerStats.gametype eq type) }) {
                                        it[kills] = requireNotNull(amount)
                                    }
                                } catch (e: NoSuchElementException) {
                                    PlayerStats.insert {
                                        it[uuid] = playerUUID.toString()
                                        it[gametype] = type
                                        it[kills] = requireNotNull(amount)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            logger.severe("[PlayerGameData] PlayerGameData 데이터베이스를 가져 올 수 없습니다.")
        }

        return requiredInt
    }
}