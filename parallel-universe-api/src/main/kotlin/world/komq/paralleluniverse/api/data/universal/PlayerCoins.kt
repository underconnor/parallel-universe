package world.komq.paralleluniverse.api.data.universal

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object PlayerCoins: Table() {
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val coin: Column<Int> = integer("coin")
    override val primaryKey = PrimaryKey(uuid)
}