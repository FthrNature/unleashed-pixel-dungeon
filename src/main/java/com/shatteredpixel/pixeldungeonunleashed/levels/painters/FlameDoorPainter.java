package com.shatteredpixel.pixeldungeonunleashed.levels.painters;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.EternalFlame;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfFrost;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Room;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.watabou.utils.Point;

public class FlameDoorPainter extends Painter {

    public static void paint( Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.EMPTY );

        Point c = room.center();
        int cx = c.x;
        int cy = c.y;

        Room.Door entrance = room.entrance();

        entrance.set( Room.Door.Type.ARCHWAY );
        level.addItemToSpawn(new PotionOfFrost());

        EternalFlame eternalFlame = new EternalFlame();
        eternalFlame.seed(entrance.y * Level.WIDTH + entrance.x, 1);
        level.blobs.put(EternalFlame.class, eternalFlame);

        if (entrance.x == room.left) {
            set( level, new Point( room.right-1, room.top+1 ), Terrain.STATUE );
            set( level, new Point( room.right-1, room.bottom-1 ), Terrain.STATUE );
            cx = room.right - 2;
        } else if (entrance.x == room.right) {
            set( level, new Point( room.left+1, room.top+1 ), Terrain.STATUE );
            set( level, new Point( room.left+1, room.bottom-1 ), Terrain.STATUE );
            cx = room.left + 2;
        } else if (entrance.y == room.top) {
            set( level, new Point( room.left+1, room.bottom-1 ), Terrain.STATUE );
            set( level, new Point( room.right-1, room.bottom-1 ), Terrain.STATUE );
            cy = room.bottom - 2;
        } else if (entrance.y == room.bottom) {
            set( level, new Point( room.left+1, room.top+1 ), Terrain.STATUE );
            set( level, new Point( room.right-1, room.top+1 ), Terrain.STATUE );
            cy = room.top + 2;
        }

        level.drop( prize( level ), cx + cy * Level.WIDTH ).type = Heap.Type.CHEST;
    }

    private static Item prize( Level level ) {

        Item prize = Generator.random(Generator.Category.RING);

        for (int i=0; i < 4; i++) {
            Item another = Generator.random( Generator.Category.RING );
            if (another.level > prize.level) {
                prize = another;
            }
        }

        return prize;
    }
}
