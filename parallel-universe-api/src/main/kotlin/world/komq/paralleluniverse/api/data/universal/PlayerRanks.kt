package world.komq.paralleluniverse.api.data.universal

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object PlayerRanks: Table() {
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val rank: Column<String> = text("rank").uniqueIndex()
    override val primaryKey = PrimaryKey(uuid)
}