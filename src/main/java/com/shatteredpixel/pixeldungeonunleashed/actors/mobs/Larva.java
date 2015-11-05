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

import com.shatteredpixel.pixeldungeonunleashed.sprites.LarvaSprite;

public class Larva extends Mob {

    {
        name = "god's larva";
        spriteClass = LarvaSprite.class;

        HP = HT = 25;
        defenseSkill = 20;
        atkSkill = 30;
        dmgRed = 8;
        dmgMin = 15;
        dmgMax = 20;

        EXP = 0;

        state = HUNTING;
    }
    private String TXT_DESC = "The Larva is the offspring of an ancient god.";

    @Override
    public String description() {
        return TXT_DESC;

    }
}
