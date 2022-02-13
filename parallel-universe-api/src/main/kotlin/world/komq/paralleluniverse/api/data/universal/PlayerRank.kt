package world.komq.paralleluniverse.api.data.universal

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object PlayerRank: Table() {
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val ranktype: Column<String> = text("ranktype").uniqueIndex()
    override val primaryKey = PrimaryKey(uuid)
}