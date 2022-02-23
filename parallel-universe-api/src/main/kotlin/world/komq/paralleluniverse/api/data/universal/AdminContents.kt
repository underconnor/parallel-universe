package world.komq.paralleluniverse.api.data.universal

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object AdminContents: Table() {
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val tfastatus: Column<Int> = integer("tfastatus")
    val lastlogindate: Column<String> = text("lastlogindate").uniqueIndex()
    override val primaryKey = PrimaryKey(uuid)
}