/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.pixeldungeonunleashed.levels.painters;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Gold;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.AnkhChain;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.IronKey;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Room;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.watabou.utils.Random;

public class PrisonPainter extends Painter {

    public static void paint( Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.EMPTY );

        Room.Door entrance = room.entrance();

        // one skeleton in the room will contain a special drop of some type
        int remains = room.random();
        level.drop( Generator.random( Random.oneOf(
                Generator.Category.RING,
                Generator.Category.WEAPON,
                Generator.Category.ARMOR
        ) ), remains).type = Heap.Type.SKELETON;

        // drop 1-3 additional skeletons in the room
        int n = Random.IntRange( 3, 4 );
        int pos = room.random();
        for (int i=0; i <= n; i++) {
            // keep looking til we find an empty spot for our skeleton
            while (level.map[pos] != Terrain.EMPTY) {
                pos = room.random();
            }
            level.drop(new Gold().random(), pos).type = Heap.Type.SKELETON;
        }

        if (Random.Int(2)==0 && !Dungeon.limitedDrops.ankhChain.dropped()){
            do {
                pos = room.random();
            } while (level.heaps.get(pos) != null);
            level.drop(new AnkhChain(), pos);
            Dungeon.limitedDrops.ankhChain.drop();
        }

        // lock the door and place the key somewhere on the level
        entrance.set( Room.Door.Type.LOCKED );
        level.addItemToSpawn( new IronKey( Dungeon.depth ) );
    }
}
