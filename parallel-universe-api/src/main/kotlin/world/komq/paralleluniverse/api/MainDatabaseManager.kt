package world.komq.paralleluniverse.api

import org.intellij.lang.annotations.Language
import org.jetbrains.exposed.sql.Database
import world.komq.paralleluniverse.api.enums.DatabaseType

interface MainDatabaseManager {
    companion object: MainDatabaseManager by LibraryLoader.loadImplement(MainDatabaseManager::class.java)

    // Database Connect

    /***
     * 원하는 데이터베이스를 연결합니다. DatabaseManager#getDatabase를 이용해 다시 불러 올 수 있습니다.
     */
    fun connectDatabase(dbURL: String, dbDriver: String, username: String, password: String, databaseType: DatabaseType, databaseName: String? = null)

    // Get Database

    /***
     * 데이터베이스를 불러옵니다. DatabaseManager#connectNewDatabase를 이용해 연결한 데이터베이스를 불러 올 수 있습니다.
     *
     * @param databaseType 불러오고 싶은 데이터베이스의 타입 (LOBBY, GAMESRERVER, UNIVERSAL, OTHER)
     * @param databaseName 불러오고 싶은 데이터베이스의 이름; databaseType이 OTHER일 경우 사용됩니다.
     * @return 불러온 데이터베이스
     */
    fun getDatabase(databaseType: DatabaseType, databaseName: String? = null): Database?

    // Create Table

    /***
     * 데이터베이스에 필요한 Table을 생성합니다.
     * @param databaseType 불러오고 싶은 데이터베이스의 타입 (LOBBY, GAMESERVER, UNIVERSAL, OTHER)
     * @param values 테이블 생성에 사용할 명령; databaseType이 OTHER일 경우 사용됩니다.
     */
    fun createTable(databaseType: DatabaseType, tableName: String?, @Language("sql") values: String? = null)

    // Util

    /***
     * Boolean을 Int로 바꿉니다.
     *
     * @param bool Int로 변환하고 싶은 Boolean 값
     * @return 변환된 Int 값. true면 1, false면 0.
     */
    fun boolToInt(bool: Boolean): Int

    /***
     * Boolean을 Int로 바꿉니다.
     *
     * @param integer Boolean으로 변환하고 싶은 Int 값
     * @return 변환된 Boolean 값. 1이면 true, 0이면 false.
     */
    fun intToBool(integer: Int): Boolean
}