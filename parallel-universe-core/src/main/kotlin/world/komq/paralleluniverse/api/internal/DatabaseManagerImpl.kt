/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.internal

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import world.komq.paralleluniverse.api.DatabaseManager
import world.komq.paralleluniverse.api.LoggerObject.logger
import java.io.File
import java.nio.charset.StandardCharsets

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

@Suppress("UNUSED")
class DatabaseManagerImpl: DatabaseManager {
    private val komworldDatabaseConf = File("komworldDatabaseConf.txt")

    private var komworldDbURL: String = ""
    private var komworldDbUsername: String = ""
    private var komworldDbPassword: String = ""

    private var otherDb: Database? = null

    private val databaseMap = HashMap<String, Database?>()

    private fun checkConfig(): Boolean {
        val result = if (!komworldDatabaseConf.exists()) {
            komworldDatabaseConf.createNewFile()
            logger.severe("[DatabaseManager] 서버 루트 디렉터리에 있는 komworldDatabaseConf.txt를 수정해주세요. 그렇지 않을 경우 작동되지 않습니다.")
            logger.severe("1번째 줄은 URL, 2번째 줄은 사용자이름, 3번재 줄은 비밀번호입니다.")

            false
        }
        else {
            komworldDatabaseConf.readLines(StandardCharsets.UTF_8).isNotEmpty()
            true
        }

        if (komworldDatabaseConf.readLines(StandardCharsets.UTF_8).size == 3) {
            komworldDbURL = komworldDatabaseConf.readLines(StandardCharsets.UTF_8)[0]
            komworldDbUsername = komworldDatabaseConf.readLines(StandardCharsets.UTF_8)[1]
            komworldDbPassword = komworldDatabaseConf.readLines(StandardCharsets.UTF_8)[2]
        }

        return result
    }

    override fun getGameServerDatabase(): Database? {
        var db: Database? = null

        if (checkConfig()) {
            db = Database.connect("${if (komworldDbURL.endsWith("/")) komworldDbURL.removePrefix("/") else komworldDbURL}GameServerData", "com.mysql.cj.jdbc.Driver", komworldDbUsername, komworldDbPassword)
        }

        return db
    }

    override fun getUniversalDatabase(): Database? {
        var db: Database? = null

        if (checkConfig()) {
            db = Database.connect("${if (komworldDbURL.endsWith("/")) komworldDbURL.removePrefix("/") else komworldDbURL}UniversalData", "com.mysql.cj.jdbc.Driver", komworldDbUsername, komworldDbPassword)
        }

        return db
    }

    override fun getPlayerGameDatabase(): Database? {
        var db: Database? = null

        if (checkConfig()) {
            db = Database.connect("${if (komworldDbURL.endsWith("/")) komworldDbURL.removePrefix("/") else komworldDbURL}PlayerGameData", "com.mysql.cj.jdbc.Driver", komworldDbUsername, komworldDbPassword)
        }

        return db
    }

    override fun connectDatabase(dbURL: String, dbDriver: String, username: String, password: String, databaseName: String) {
        if (dbURL.endsWith("/")) dbURL.removeSuffix("/")

        otherDb = Database.connect("${dbURL}/${databaseName}", dbDriver, username, password)
        println("\"$databaseName\" database connected successfully!")
        databaseMap[databaseName] = otherDb
    }

    override fun getDatabase(databaseName: String?): Database? {
        return databaseMap[databaseName]
    }

    // Utils

    override fun boolToInt(bool: Boolean): Int {
        return when (bool) {
            true -> 1
            false -> 0
        }
    }

    override fun intToBool(integer: Int): Boolean {
        return when (integer) {
            1 -> true
            0 -> false
            else -> false
        }
    }

    override fun commit() {
        val gameServerDb = getGameServerDatabase()
        val universalDb = getUniversalDatabase()
        val playerGameDb = getPlayerGameDatabase()

        if (gameServerDb != null) {
            transaction(gameServerDb) {
                commit()
            }
        }
        else {
            logger.severe("[DatabaseManager] GameServerData 데이터베이스를 가져 올 수 없습니다.")
        }

        if (universalDb != null) {
            transaction(universalDb) {
                commit()
            }
        }
        else {
            logger.severe("[DatabaseManager] UniversalData 데이터베이스를 가져 올 수 없습니다.")
        }

        if (playerGameDb != null) {
            transaction(playerGameDb) {
                commit()
            }
        }
        else {
            logger.severe("[DatabaseManager] PlayerGameData 데이터베이스를 가져 올 수 없습니다.")
        }
        
        if (databaseMap.values.isNotEmpty()) {
            databaseMap.values.forEach {
                transaction(it) {
                    commit()
                }
            }
        }
    }
}