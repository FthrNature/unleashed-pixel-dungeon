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

import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Freezing;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.sprites.IceDemonSprite;

import java.util.HashSet;

public class IceDemon extends Mob {

    {
        name = "ice demon";
        spriteClass = IceDemonSprite.class;

        HP = HT = 110;
        defenseSkill = 26;
        atkSkill = 40;
        dmgRed = 18;
        dmgMin = 20;
        dmgMax = 35;
        EXP = 12;

        TYPE_EVIL = true;
        maxLvl = 29;
    }

    @Override
    public String description() {
        return
                "These little demons care only about serving their demon lord.  Ice runs in thier veins and "+
                "evil runs in their little hearts.";
    }


    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    static {
        RESISTANCES.add( Death.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Freezing.class );
        IMMUNITIES.add( Terror.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}

