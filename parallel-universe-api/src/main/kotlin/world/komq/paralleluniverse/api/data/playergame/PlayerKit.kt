package world.komq.paralleluniverse.api.data.playergame

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object PlayerKit: Table() {
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val gametype: Column<String> = text("gametype").uniqueIndex()
    val kits: Column<String> = text("kits").uniqueIndex()
    override val primaryKey = PrimaryKey(uuid)
}