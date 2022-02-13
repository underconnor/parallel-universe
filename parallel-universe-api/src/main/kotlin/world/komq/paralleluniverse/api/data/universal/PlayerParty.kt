package world.komq.paralleluniverse.api.data.universal

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object PlayerParty: Table() {
    val uuid: Column<String> = text("uuid").uniqueIndex()
    val party: Column<String> = text("party").uniqueIndex()
    override val primaryKey = PrimaryKey(uuid)
}