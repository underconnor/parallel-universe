/*
 * Copyright (c) 2022 <Name>
 *
 *  Licensed under the General Public License, Version 3.0. (https://opensource.org/licenses/gpl-3.0/)
 */

package world.komq.paralleluniverse.api.data

import world.komq.paralleluniverse.api.LibraryLoader

/***
 * @author <Name>
 */

interface PlayerGameDataManager {
    companion object: PlayerGameDataManager by LibraryLoader.loadImplement(PlayerGameDataManager::class.java)
}