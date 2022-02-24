package world.komq.paralleluniverse.api.internal

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import world.komq.paralleluniverse.api.DatabaseManager
import java.io.File
import java.nio.charset.StandardCharsets

@Suppress("UNUSED")
class DatabaseManagerImpl: DatabaseManager {
    private val komworldDatabaseConf = File("komworldDatabaseConf.txt")

    private var komworldDbURL: String = ""
    private var komworldDbUsername: String = ""
    private var komworldDbPassword: String = ""

    private var gameServerDb: Database? = null
    private var universalDb: Database? = null
    private var playerGameDb: Database? = null
    private var otherDb: Database? = null

    private val databaseMap = HashMap<String, Database?>()

    private fun checkConfig(): Boolean {
        val result = if (!komworldDatabaseConf.exists()) {
            komworldDatabaseConf.createNewFile()

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
        transaction(gameServerDb) {
            commit()
        }
        transaction(universalDb) {
            commit()
        }
        transaction(playerGameDb) {
            commit()
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