/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data.playergame

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

object PlayerStats: Table() {
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val gametype: Column<String> = text("gametype").uniqueIndex()
    val coins: Column<Int> = integer("coins")
    val kills: Column<Int> = integer("kills")
    override val primaryKey = PrimaryKey(uuid)
}