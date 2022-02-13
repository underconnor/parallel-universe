package world.komq.paralleluniverse.api.data.lobby

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object VisibilityOption: Table() {
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val option: Column<Int> = integer("option")
    override val primaryKey = PrimaryKey(uuid)
}