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
package com.shatteredpixel.pixeldungeonunleashed.levels;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.DemonLord;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.IceDemon;
import com.shatteredpixel.pixeldungeonunleashed.effects.CellEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.SnowParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.SkeletonKey;
import com.shatteredpixel.pixeldungeonunleashed.levels.painters.Painter;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.watabou.noosa.Scene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class FrozenBossLevel extends Level {

    {
        color1 = 0x484876;
        color2 = 0x4b5999;

        viewDistance = 5;
    }

    private static final int ROOM_LEFT		= WIDTH / 2 - 1;
    private static final int ROOM_RIGHT		= WIDTH / 2 + 1;
    private static final int ROOM_TOP		= HEIGHT / 2 - 1;
    private static final int ROOM_BOTTOM	= HEIGHT / 2 + 1;

    private int stairs = -1;
    private boolean enteredArena = false;
    private boolean keyDropped = false;

    @Override
    public String tilesTex() {
        return Assets.TILES_FROZEN;
    }

    @Override
    public String waterTex() {
        return Assets.WATER_FROZEN;
    }

    private static final String STAIRS	= "stairs";
    private static final String ENTERED	= "entered";
    private static final String DROPPED	= "droppped";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( STAIRS, stairs );
        bundle.put( ENTERED, enteredArena );
        bundle.put( DROPPED, keyDropped );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        stairs = bundle.getInt( STAIRS );
        enteredArena = bundle.getBoolean( ENTERED );
        keyDropped = bundle.getBoolean( DROPPED );
    }

    @Override
    protected boolean build() {

        for (int i=0; i < 5; i++) {

            int top = Random.IntRange(2, ROOM_TOP - 1);
            int bottom = Random.IntRange( ROOM_BOTTOM + 1, 22 );
            Painter.fill(this, 2 + i * 4, top, 4, bottom - top + 1, Terrain.EMPTY);

            if (i == 2) {
                exit = (i * 4 + 3) + (top - 1) * WIDTH ;
            }

            for (int j=0; j < 4; j++) {
                if (Random.Int( 2 ) == 0) {
                    int y = Random.IntRange( top + 1, bottom - 1 );
                    map[i*4+j + y*WIDTH] = Terrain.WALL_DECO;
                }
            }
        }

        map[exit] = Terrain.LOCKED_EXIT;

        Painter.fill( this, ROOM_LEFT - 1, ROOM_TOP - 1,
                ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL );
        Painter.fill( this, ROOM_LEFT, ROOM_TOP,
                ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP + 1, Terrain.EMPTY );

        entrance = Random.Int( ROOM_LEFT + 1, ROOM_RIGHT - 1 ) +
                Random.Int( ROOM_TOP + 1, ROOM_BOTTOM - 1 ) * WIDTH;
        map[entrance] = Terrain.ENTRANCE;

        boolean[] patch = Patch.generate( 0.45f, 6 );
        for (int i=0; i < LENGTH; i++) {
            if (map[i] == Terrain.EMPTY && patch[i]) {
                map[i] = Terrain.WATER;
            }
        }

        return true;
    }

    @Override
    protected void decorate() {

        for (int i=0; i < LENGTH; i++) {
            if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
                map[i] = Terrain.EMPTY_DECO;
            }
        }
    }

    public Actor respawner() {
        return null;
    }

    @Override
    protected void createItems() {
    }

    @Override
    public int randomRespawnCell() {
        return -1;
    }

    @Override
    public void press( int cell, Char hero ) {

        super.press( cell, hero );

        if (!enteredArena && hero == Dungeon.hero && cell != entrance) {

            enteredArena = true;
            seal();

            for (int i=ROOM_LEFT-1; i <= ROOM_RIGHT + 1; i++) {
                doMagic( (ROOM_TOP - 1) * WIDTH + i );
                doMagic( (ROOM_BOTTOM + 1) * WIDTH + i );
            }
            for (int i=ROOM_TOP; i < ROOM_BOTTOM + 1; i++) {
                doMagic( i * WIDTH + ROOM_LEFT - 1 );
                doMagic( i * WIDTH + ROOM_RIGHT + 1 );
            }
            doMagic( entrance );
            GameScene.updateMap();

            Dungeon.observe();

            DemonLord boss = new DemonLord();
            if (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS) {
                boss.infiniteScaleMob(Dungeon.depth + 7);
            } else {
                boss.scaleMob();
            }
            do {
                boss.pos = Random.Int( LENGTH );
            } while (!passable[boss.pos] || Dungeon.visible[boss.pos]);
            GameScene.add(boss);

            for (int i = 0; i < 10; i++) {
                IceDemon mob = new IceDemon();
                do {
                    mob.pos = Random.Int( LENGTH );
                } while (!passable[mob.pos] || Dungeon.visible[mob.pos] || Actor.findChar( mob.pos ) != null);
                mob.state = mob.HUNTING;
                GameScene.add(mob);
            }

            stairs = entrance;
            entrance = -1;
        }
    }

    @Override
    protected void createMobs() {
    }

    private void doMagic( int cell ) {
        set( cell, Terrain.EMPTY_SP );
        CellEmitter.get(cell).start( SnowParticle.FACTORY, 0.1f, 3 );
    }

    @Override
    public Heap drop( Item item, int cell ) {

        if (!keyDropped && item instanceof SkeletonKey) {
            keyDropped = true;
            unseal();

            entrance = stairs;
            set( entrance, Terrain.ENTRANCE );
            GameScene.updateMap( entrance );
        }

        return super.drop( item, cell );
    }

    @Override
    public void addVisuals( Scene scene ) {
        FrozenLevel.addVisuals( this, scene );
    }
}

