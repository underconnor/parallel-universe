package world.komq.paralleluniverse.api.data.gameserver

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Fakepit: Table() {
    val name: Column<String> = text("name").uniqueIndex()
    val isavailable: Column<Int> = integer("isavailable")
    override val primaryKey = PrimaryKey(name)
}