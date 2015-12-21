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

import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ZombieSprite;

import java.util.HashSet;

public class Zombie extends Mob {

    {
        name = "zombie";
        spriteClass = ZombieSprite.class;

        HP = HT = 22;
        defenseSkill = 9;
        atkSkill = 14;

        EXP = 5;
        maxLvl = 12;

        dmgMin = 3;
        dmgMax = 8;
        dmgRed = 4;
        TYPE_EVIL = true;
        TYPE_UNDEAD = true;
        TYPE_MINDLESS = true;
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    @Override
    public String description() {
        return
                "Zombies are the corpses of unlucky adventurers and inhabitants of the dungeon, " +
                        "animated by emanations of evil magic from the depths below.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
        IMMUNITIES.add( Death.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
