/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api

import org.jetbrains.exposed.sql.Database

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

interface DatabaseManager {
    companion object: DatabaseManager by LibraryLoader.loadImplement(DatabaseManager::class.java)

    // Database Connect

    /***
     * 원하는 데이터베이스를 연결합니다. DatabaseManager#getDatabase를 이용해 다시 불러 올 수 있습니다.
     */
    fun connectDatabase(dbURL: String, dbDriver: String, username: String, password: String, databaseName: String)

    // Get Database

    /***
     * GameServerData 데이터베이스를 불러옵니다.
     *
     * @return GameServerData 데이터베이스
     */
    fun getGameServerDatabase(): Database?

    /***
    * UniversalData 데이터베이스를 불러옵니다.
    *
    * @return UniversalData 데이터베이스
    */
    fun getUniversalDatabase(): Database?

    /***
    * PlayerGameData 데이터베이스를 불러옵니다.
    *
    * @return PlayerGameData 데이터베이스
    */
    fun getPlayerGameDatabase(): Database?

    /***
     * 특정 데이터베이스를 불러옵니다. DatabaseManager#connectNewDatabase를 이용해 연결한 데이터베이스를 불러 올 수 있습니다.
     *
     * @param databaseName 불러오고 싶은 데이터베이스의 이름; databaseType이 OTHER일 경우 사용됩니다.
     * @return 불러온 데이터베이스
     */
    fun getDatabase(databaseName: String? = null): Database?

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

    /***
     * 사용할 수 있는 데이터베이스들을 transaction을 열어 모두 commit합니다.
     *
     * JavaPlugin#onDisable에 넣는 것을 추천드립니다.
     */
    fun commit()
}