package world.komq.paralleluniverse.internal

import org.intellij.lang.annotations.Language
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import world.komq.paralleluniverse.api.MainDatabaseManager
import world.komq.paralleluniverse.api.enums.DatabaseType

@Suppress("UNUSED")
class MainDatabaseManagerImpl: MainDatabaseManager {
    private var lobbyDb: Database? = null
    private var gameServerDb: Database? = null
    private var universalDb: Database? = null
    private var otherDb: Database? = null

    private val databaseMap = HashMap<String, Database?>()
    
    override fun connectDatabase(dbURL: String, dbDriver: String, username: String, password: String, databaseType: DatabaseType, databaseName: String?) {

        if (dbURL.endsWith("/")) dbURL.removeSuffix("/")

        when (databaseType) {
            DatabaseType.LOBBY -> {
                lobbyDb = Database.connect("${dbURL}/LobbyData", dbDriver, username, password)
                println("\"LobbyData\" database connected successfully!")
            }
            DatabaseType.GAMESERVER -> {
                gameServerDb = Database.connect("${dbURL}/GameServerData", dbDriver, username, password)
                println("\"GameServerData\" database connected successfully!")
            }
            DatabaseType.UNIVERSAL -> {
                universalDb = Database.connect("${dbURL}/UniversalData", dbDriver, username, password)
                println("\"UniversalData\" database connected successfully!")
            }
            DatabaseType.OTHER -> {
                otherDb = Database.connect("${dbURL}/${databaseName}", dbDriver, username, password)
                println("\"$databaseName\" database connected successfully!")
                databaseMap[databaseName.toString()] = otherDb
            }
        }
    }

    override fun getDatabase(databaseType: DatabaseType, databaseName: String?): Database? {
        val data: Database? = when (databaseType) {
            DatabaseType.LOBBY -> lobbyDb
            DatabaseType.GAMESERVER -> gameServerDb
            DatabaseType.UNIVERSAL -> universalDb
            DatabaseType.OTHER -> databaseMap[databaseName]
        }

        return data
    }

    // Tables

    override fun createTable(databaseType: DatabaseType, tableName: String?, @Language("sql") values: String?) {
        when (databaseType) {
            DatabaseType.LOBBY -> {
                if (lobbyDb != null) {
                    transaction(lobbyDb) {
                        exec("CREATE TABLE IF NOT EXISTS VisibilityOption (uuid    VARCHAR(255) NOT NULL UNIQUE, value    INTEGER NOT NULL, PRIMARY KEY(uuid));")
                    }
                }
            }
            DatabaseType.GAMESERVER -> {
                if (gameServerDb != null) {
                    transaction(gameServerDb) {
                        exec("CREATE TABLE IF NOT EXISTS Servers (name    VARCHAR(255) NOT NULL UNIQUE, gametype    VARCHAR(255) NOT NULL UNIQUE, isavailable    INTEGER NOT NULL, PRIMARY KEY(name);")
                    }
                }
            }
            DatabaseType.UNIVERSAL -> {
                if (universalDb != null) {
                    transaction(universalDb) {
                        exec("CREATE TABLE IF NOT EXISTS PlayerRank (uuid    VARCHAR(255) NOT NULL UNIQUE, ranktype    VARCHAR(255) NOT NULL, PRIMARY KEY(uuid));")
                        exec("CREATE TABLE IF NOT EXISTS PlayerStats (uuid    VARCHAR(255) NOT NULL UNIQUE, gametype    VARCHAR(255) NOT NULL, coins    INTEGER NOT NULL, PRIMARY KEY(uuid));")
                        exec("CREATE TABLE IF NOT EXISTS PlayerKit (uuid    VARCHAR(255) NOT NULL UNIQUE, gametype    VARCHAR(255) NOT NULL, kits    VARCHAR(255) NOT NULL, PRIMARY KEY(uuid));")
                        exec("CREATE TABLE IF NOT EXISTS PlayerParty (uuid    VARCHAR(255) NOT NULL UNIQUE, party    VARCHAR(255) NOT NULL, PRIMARY KEY(uuid));'")
                        exec("CREATE TABLE IF NOT EXISTS PlayerFriends (uuid    VARCHAR(255) NOT NULL UNIQUE, friends    VARCHAR(255) NOT NULL, PRIMARY KEY(uuid));")
                    }
                }
            }
            DatabaseType.OTHER -> {
                if (otherDb != null) {
                    transaction(otherDb) {
                        if (tableName != null && values != null) {
                            exec("CREATE TABLE IF NOT EXISTS $tableName (${values});")
                        }
                    }
                }
            }
        }
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
}