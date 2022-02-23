package world.komq.paralleluniverse.api.data.universal

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import world.komq.paralleluniverse.api.data.playergame.PlayerKit

object PlayerFriends: Table() {
    val uuid: Column<String> = PlayerKit.text("uuid").uniqueIndex()
    val friends: Column<String> = PlayerKit.text("friends").uniqueIndex()
    override val primaryKey = PrimaryKey(uuid)
}