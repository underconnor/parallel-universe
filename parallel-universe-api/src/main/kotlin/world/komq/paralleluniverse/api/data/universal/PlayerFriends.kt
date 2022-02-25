/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data.universal

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import world.komq.paralleluniverse.api.data.playergame.PlayerKit

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

object PlayerFriends: Table() {
    val uuid: Column<String> = PlayerKit.text("uuid").uniqueIndex()
    val friends: Column<String> = PlayerKit.text("friends").uniqueIndex()
    override val primaryKey = PrimaryKey(uuid)
}