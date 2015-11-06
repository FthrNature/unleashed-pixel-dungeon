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

import com.shatteredpixel.pixeldungeonunleashed.items.food.MysteryMeat;
import com.shatteredpixel.pixeldungeonunleashed.sprites.VelociroosterSprite;

public class Velocirooster extends Mob {

    {
        name = "velocirooster";
        spriteClass = VelociroosterSprite.class;

        HP = HT = 18;
        defenseSkill = 6;
        atkSkill = 11;
        dmgRed = 2;
        baseSpeed = 2f;
        dmgMin = 3;
        dmgMax = 6;

        EXP = 4;
        maxLvl = 11;
        TYPE_ANIMAL = true;

        loot = new MysteryMeat();
        lootChance = 0.167f;
        mobType = MOBTYPE_NIMBLE;
    }

    @Override
    public String defenseVerb() {
        return "Dodged";
    }

    @Override
    public String description() {
        return
                "The Velocirooster is a vicious cousin of the domesticated rooster." +
                " It races through the dungeon and attacks with razor sharp talons and a vicious beak." +
                " No one is sure how they got in the dungeon, but they seem at home here now.";
    }
}
