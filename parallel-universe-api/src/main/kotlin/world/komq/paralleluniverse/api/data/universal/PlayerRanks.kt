/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data.universal

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

object PlayerRanks: Table() {
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val rank: Column<String> = text("rank").uniqueIndex()
    override val primaryKey = PrimaryKey(uuid)
}