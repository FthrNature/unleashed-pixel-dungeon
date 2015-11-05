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
import com.shatteredpixel.pixeldungeonunleashed.Statistics;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Bestiary;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Necromancer;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfWealth;
import com.shatteredpixel.pixeldungeonunleashed.levels.painters.Painter;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.watabou.noosa.Scene;
import com.watabou.utils.Random;

public class OpenLevel extends Level {

    private int THEME = (Dungeon.depth / 6) % 5;
    {
        switch (THEME) {
            case 0: // this is a sewer level
                color1 = 0x48763c;
                color2 = 0x59994a;
                break;
            case 1: // this is a prison level
                color1 = 0x6a723d;
                color2 = 0x88924c;
                break;
            case 2: // this is a caves level
                color1 = 0x534f3e;
                color2 = 0xb9d661;
                break;
            case 3: // this is a city level
                color1 = 0x4b6636;
                color2 = 0xf2f2f2;
                break;
            default: // this is a halls level
                color1 = 0x801500;
                color2 = 0xa68521;
                break;
        }

        viewDistance = 8;
    }

    private static final int ROOM_LEFT		= WIDTH / 2 - 2;
    private static final int ROOM_RIGHT		= WIDTH / 2 + 2;
    private static final int ROOM_TOP		= HEIGHT / 2 - 2;
    private static final int ROOM_BOTTOM	= HEIGHT / 2 + 2;

    @Override
    public String tilesTex() {
        switch (THEME) {
            case 0: // sewers
                return Assets.TILES_SEWERS;
            case 1: // prison
                return Assets.TILES_PRISON;
            case 2: // caves
                return Assets.TILES_CAVES;
            case 3: // city
                return Assets.TILES_CITY;
            default: // halls
                return Assets.TILES_HALLS;
        }
    }

    @Override
    public String waterTex() {
        switch (THEME) {
            case 0: // sewers
                return Assets.WATER_SEWERS;
            case 1: // prison
                return Assets.WATER_PRISON;
            case 2: // caves
                return Assets.WATER_CAVES;
            case 3: // city
                return Assets.WATER_CITY;
            default: // halls
                return Assets.WATER_HALLS;
        }
    }

    @Override
    protected boolean build() {
        int topMost = Integer.MAX_VALUE;

        for (int i=0; i < 12; i++) {
            int left, right, top, bottom;
            if (Random.Int( 2 ) == 0) {
                left = Random.Int( 1, ROOM_LEFT - 3 );
                right = ROOM_RIGHT + 3;
            } else {
                left = ROOM_LEFT - 3;
                right = Random.Int( ROOM_RIGHT + 3, WIDTH - 1 );
            }
            if (Random.Int( 2 ) == 0) {
                top = Random.Int( 2, ROOM_TOP - 3 );
                bottom = ROOM_BOTTOM + 3;
            } else {
                top = ROOM_LEFT - 3;
                bottom = Random.Int( ROOM_TOP + 3, HEIGHT - 1 );
            }

            Painter.fill(this, left, top, right - left + 1, bottom - top + 1, Terrain.EMPTY);

            if (top < topMost) {
                topMost = top;
                exit = Random.Int( left, right ) + (top - 1) * WIDTH;
            }
        }

        for (int i = 0; i < 3; i++) {
            int left = Random.IntRange(6, WIDTH-6);
            int top = Random.IntRange(topMost + 5, HEIGHT-8);
            Painter.fill(this, left, top, 3, 3, Terrain.WALL);
        }
        for (int i = 0; i < 6; i++) {
            int left = Random.IntRange(6, WIDTH-6);
            int top = Random.IntRange(topMost + 4, HEIGHT-6);
            Painter.fill(this, left, top, 2, 2, Terrain.WALL);
        }

        map[exit] = Terrain.EXIT;

        boolean[] patch;
        switch (Random.Int(4)) {
            case 0:
                feeling = Feeling.BURNT;
                for (int i = 0; i < LENGTH; i++) {
                    if (map[i] == Terrain.EMPTY && Random.Int(6) == 0) {
                        map[i] = Terrain.EMBERS;
                    }
                }
                patch = Patch.generate(0.45f, 6);
                for (int i = 0; i < LENGTH; i++) {
                    if (map[i] == Terrain.EMPTY && patch[i]) {
                        map[i] = Terrain.WATER;
                    }
                }
                break;
            default:
                feeling = Feeling.GRASS;
                for (int i = 0; i < LENGTH; i++) {
                    if (map[i] == Terrain.EMPTY && Random.Int(6) == 0) {
                        map[i] = Terrain.HIGH_GRASS;
                    }
                }
                patch = Patch.generate(0.45f, 6);
                for (int i = 0; i < LENGTH; i++) {
                    if (map[i] == Terrain.EMPTY && patch[i]) {
                        map[i] = Terrain.WATER;
                    }
                }
        }

        entrance = Random.Int( ROOM_LEFT + 1, ROOM_RIGHT - 1 ) +
                Random.Int( ROOM_TOP + 1, ROOM_BOTTOM - 1 ) * WIDTH;
        map[entrance] = Terrain.ENTRANCE;


        return true;
    }

    @Override
    protected void decorate() {
        for (int i=WIDTH + 1; i < LENGTH - WIDTH; i++) {
            if (map[i] == Terrain.EMPTY) {
                int n = 0;
                if (map[i+1] == Terrain.WALL) {
                    n++;
                }
                if (map[i-1] == Terrain.WALL) {
                    n++;
                }
                if (map[i+WIDTH] == Terrain.WALL) {
                    n++;
                }
                if (map[i-WIDTH] == Terrain.WALL) {
                    n++;
                }
                if (Random.Int( 8 ) <= n) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }

        for (int i=0; i < LENGTH; i++) {
            if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) {
                map[i] = Terrain.WALL_DECO;
            }
        }


        int sign;
        do {
            sign = Random.Int( ROOM_LEFT, ROOM_RIGHT ) + Random.Int( ROOM_TOP, ROOM_BOTTOM ) * WIDTH;
        } while (sign == entrance);
        map[sign] = Terrain.SIGN;
    }

    @Override
    protected void createItems() {
        int nItems = 5;
        int bonus = 0;
        for (Buff buff : Dungeon.hero.buffs(RingOfWealth.Wealth.class)) {
            bonus += ((RingOfWealth.Wealth) buff).level;
        }
        //just incase someone gets a ridiculous ring, cap this at 80%
        bonus = Math.min(bonus, 9);
        while (Random.Float() < (0.35f + bonus*0.05f)) {
            nItems++;
        }

        if (Dungeon.difficultyLevel == Dungeon.DIFF_NTMARE) {
            nItems = nItems / 2;
        } else if (Dungeon.difficultyLevel <= Dungeon.DIFF_EASY) {
            nItems += 2;
        }

        for (int i=0; i < nItems; i++) {
            Heap.Type type = null;
            switch (Random.Int( 20 )) {
                case 0:
                    type = Heap.Type.SKELETON;
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    if (feeling == Feeling.BURNT) {
                        // burnt levels drop skeletons instead of chests
                        type = Heap.Type.SKELETON;
                    } else {
                        type = Heap.Type.CHEST;
                    }
                    break;
                case 5:
                    type = Heap.Type.MIMIC;
                    break;
                default:
                    type = Heap.Type.HEAP;
            }
            drop( Generator.random(), randomDestination() ).type = type;
        }
    }

    protected void createMobs() {
        int mobsToSpawn = nMobs();

        while (mobsToSpawn > 0) {
            Mob mob = Bestiary.mob(Dungeon.depth);
            mob.pos = randomDestination();
            mobsToSpawn--;
            mobs.add(mob);
        }

        if (Dungeon.depth == 11) {
            Necromancer necromancer = new Necromancer();
            necromancer.scaleMob();
            do {
                necromancer.pos = randomRespawnCell();
            } while (necromancer.pos == -1);
            mobs.add(necromancer);

        }
    }

    @Override
    public Actor respawner() {
        return new Actor() {

            {
                actPriority = 1; //as if it were a buff.
            }

            @Override
            protected boolean act() {
                int numMobs = nMobs();
                while (mobs.size() < numMobs) {

                    Mob mob = Bestiary.mutable( Dungeon.depth );
                    mob.state = mob.WANDERING;
                    mob.pos = randomRespawnCell();
                    if (Dungeon.hero.isAlive() && mob.pos != -1) {
                        GameScene.add(mob);
                        if (Statistics.amuletObtained) {
                            mob.beckon( Dungeon.hero.pos );
                        }
                    }
                }
                spend( Statistics.amuletObtained ? 25 : 45 );
                return true;
            }
        };
    }

    @Override
    public void addVisuals( Scene scene ) {
        switch (THEME) {
            case 0: // sewers
                SewerLevel.addVisuals( this, scene );
                break;
            case 1: // prison
                PrisonLevel.addVisuals( this, scene );
                break;
            case 2: // caves
                CavesLevel.addVisuals( this, scene );
                break;
            case 3: // city
                CityLevel.addVisuals( this, scene );
                break;
            default: // halls
                HallsLevel.addVisuals( this, scene );
                break;
        }
    }

    @Override
    public int nMobs() {
        switch (Dungeon.difficultyLevel) {
            case Dungeon.DIFF_TUTOR:
            case Dungeon.DIFF_EASY:
                return 4  + Dungeon.depth % 6 + Random.Int(4);
            case Dungeon.DIFF_HARD:
                return 6  + Dungeon.depth % 6 + Random.Int(5);
            case Dungeon.DIFF_NTMARE:
                return 7  + Dungeon.depth % 6 + Random.Int(6);
            default:
                return 5  + Dungeon.depth % 6 + Random.Int(4);
        }
    }
}