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
package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Sleep;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ClayGolemSprite;

import java.util.HashSet;

public class ClayGolem extends Mob {

    {
        name = "clay golem";
        spriteClass = ClayGolemSprite.class;

        HP = HT = 40;
        defenseSkill = 20;
        atkSkill = 15;
        dmgRed = 10;
        dmgMin = 8;
        dmgMax = 18;

        EXP = 8;
        maxLvl = 18;
        mobType = MOBTYPE_TOUGH;
        TYPE_MINDLESS = true;
    }

    @Override
    protected float attackDelay() {
        return 1.5f;
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    @Override
    public String description() {
        return
                "Clay Golems are the result of powerful chaotic magic reacting with the dungeon, "+
                "although not truly alive they move through the dungeon with intelligence and seek "+
                "only to destroy.";
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    static {
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Sleep.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
