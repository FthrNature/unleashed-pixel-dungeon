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

import com.shatteredpixel.pixeldungeonunleashed.sprites.SlimeBrownSprite;
import com.watabou.utils.Random;

public class SlimeBrown extends Mob {
    {
        name = "brown slime";
        spriteClass = SlimeBrownSprite.class;

        HP = HT = 14;
        defenseSkill = 4;
        atkSkill = 10;
        dmgRed = 3;
        dmgMin = 3;
        dmgMax = 6;

        EXP = 3;
        maxLvl = 10;

    }

    @Override
    public String description() {
        return
                "Slimes look like icky little piles of goo, but they can pack a bite." +
                        "The brown slime is probably the weakest of the dungeon slimes.";
    }
}
