/*
 * Copyright (c) 2022 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data.gameserver

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/***
 * @author BaeHyeonWoo
 *
 * "Until my feet are crushed,"
 * "Until I can get ahead of myself."
 */

object Fakepit: Table() {
    val name: Column<String> = text("name").uniqueIndex()
    val isavailable: Column<Int> = integer("isavailable")
    override val primaryKey = PrimaryKey(name)
}