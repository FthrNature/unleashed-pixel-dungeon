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

import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Donations;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Room;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.watabou.utils.Point;

public class AltarPainter extends Painter {

    public static void paint( Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.EMPTY );

        Room.Door entrance = room.entrance();

        Point altar = null;
        if (entrance.x == room.left) {
            altar = new Point( room.right-1, room.center().y );
        } else if (entrance.x == room.right) {
            altar = new Point( room.left+1, room.center().y );
        } else if (entrance.y == room.top) {
            altar = new Point( room.center().x, room.bottom-1 );
        } else if (entrance.y == room.bottom) {
            altar = new Point( room.center().x, room.top+1 );
        }
        set(level, altar, Terrain.ALTAR);

        Donations donation = new Donations();
        donation.seed(altar.x + Level.WIDTH * altar.y, 1 );
        level.blobs.put(Donations.class, donation );

        room.entrance().set(Room.Door.Type.ARCHWAY);
    }
}
