package world.komq.paralleluniverse.api.data.universal

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object PlayerParties: Table() {
    val partyname: Column<String> = text("partyname").uniqueIndex()
    val partylist: Column<String> = text("partylist").uniqueIndex()
    override val primaryKey = PrimaryKey(partyname)
}