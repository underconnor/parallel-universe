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

// PLEASE REMOVE SUPPRESS WHEN USING ALL CONTENTS.
@Suppress("UNUSED")
object PlayerParties: Table() {
    @Suppress
    val partyname: Column<String> = text("partyname").uniqueIndex()
    val partylist: Column<String> = text("partylist").uniqueIndex()
    override val primaryKey = PrimaryKey(partyname)
}