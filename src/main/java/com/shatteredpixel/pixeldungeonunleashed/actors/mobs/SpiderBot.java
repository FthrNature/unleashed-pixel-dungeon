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

import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Light;
import com.shatteredpixel.pixeldungeonunleashed.sprites.SpiderBotSprite;
import com.watabou.utils.Random;

public class SpiderBot extends Mob {

    {
        name = "metal spider";
        spriteClass = SpiderBotSprite.class;

        HP = HT = 60;
        defenseSkill = 12;
        atkSkill = 25;
        dmgRed = 8;
        dmgMin = 12;
        dmgMax = 16;


        viewDistance = Light.DISTANCE;

        EXP = 10;
        maxLvl = 20;
        mobType = MOBTYPE_DEBUFF;
        TYPE_MINDLESS = true;
    }

    @Override
    public String description() {
        return
                "A metal spider; This strange little contraption stumbles around the dungeon looking for prey.";
    }
}