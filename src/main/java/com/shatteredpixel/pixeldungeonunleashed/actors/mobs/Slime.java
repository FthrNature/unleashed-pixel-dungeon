/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015 David Mitchell
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

import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.sprites.SlimeSprite;
import com.watabou.utils.Random;

public class Slime extends Mob {

    {
        name = "green slime";
        spriteClass = SlimeSprite.class;

        HP = HT = 18;
        defenseSkill = 6;
        atkSkill = 12;
        dmgRed = 5;

        EXP = 4;
        maxLvl = 11;

        loot = new PotionOfToxicGas();
        lootChance = 0.167f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(3, 7);
    }

    @Override
    public String description() {
        return
                "Slimes look like icky little piles of goo, but they can pack a bite." +
                        "Green slimes bash their prey around and then engulf them to eat.";
    }

}
