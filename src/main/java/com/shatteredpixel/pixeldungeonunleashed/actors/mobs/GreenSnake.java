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

import com.shatteredpixel.pixeldungeonunleashed.sprites.GreenSnakeSprite;

public class GreenSnake extends Mob {

    {
        name = "dungeon snake";
        spriteClass = GreenSnakeSprite.class;

        HP = HT = 10;
        defenseSkill = 3;
        atkSkill = 8;
        dmgRed = 1;
        dmgMin = 1;
        dmgMax = 4;
        EXP = 2;

        TYPE_ANIMAL = true;
        maxLvl = 6;
    }

    @Override
    public String description() {
        return
                "Common dungeon snakes thrive in the dark and damp environments found in caves and sewers.";
    }
}

