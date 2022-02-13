package world.komq.paralleluniverse.api.data.universal

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object PlayerStats: Table() {
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val gametype: Column<String> = text("gametype").uniqueIndex()
    val coins: Column<Int> = integer("coins")
    override val primaryKey = PrimaryKey(uuid)
}