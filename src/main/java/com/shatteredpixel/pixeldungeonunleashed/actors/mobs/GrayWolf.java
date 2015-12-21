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

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.items.food.MysteryMeat;
import com.shatteredpixel.pixeldungeonunleashed.sprites.BrownWolfSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.GrayWolfSprite;

public class GrayWolf extends Mob {

    {
        name = "brown wolf";
        spriteClass = BrownWolfSprite.class;

        HP = HT = 110;
        defenseSkill = 26;
        atkSkill = 38;
        dmgRed = 18;
        dmgMin = 20;
        dmgMax = 35;
        EXP = 12;

        loot = new MysteryMeat();
        lootChance = 0.2f;

        TYPE_ANIMAL = true;
        maxLvl = 29;
    }

    @Override
    protected boolean act() {
        boolean justAlerted = alerted;
        boolean returnValue = super.act();

        if (justAlerted && (! Dungeon.level.mobs.isEmpty())) {
            for (Mob mob : Dungeon.level.mobs) {
                if ((mob instanceof BrownWolf || mob instanceof GrayWolf) && (mob != this)) {
                    mob.beckon(target);
                }
            }
        }

        return returnValue;
    }

    @Override
    public String description() {
        return
                "These giant silent wolves have free reign of the dungeons.  They are especially dangerous in packs.";
    }
}

