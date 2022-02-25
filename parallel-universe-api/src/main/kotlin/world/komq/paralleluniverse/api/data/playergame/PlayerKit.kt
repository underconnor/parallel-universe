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

// PLEASE REMOVE SUPPRESS WHEN USING ALL CONTENTS.
@Suppress("UNUSED")
object PlayerKit: Table() {
    @Suppress
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val gametype: Column<String> = text("gametype").uniqueIndex()
    val kits: Column<String> = text("kits").uniqueIndex()
    override val primaryKey = PrimaryKey(uuid)
}