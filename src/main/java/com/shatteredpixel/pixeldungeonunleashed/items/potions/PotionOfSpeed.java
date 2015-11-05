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
package com.shatteredpixel.pixeldungeonunleashed.items.potions;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Slow;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Speed;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;

public class PotionOfSpeed  extends Potion {

    private static final float ALPHA	= 0.4f;

    {
        name = "Potion of Speed";
        initials = "Sp";
    }

    @Override
    public void shatter( int cell ) {
        for (Mob mob : Dungeon.level.mobs) {
            if (mob.pos == cell) {
                Buff.affect(mob, Speed.class, Speed.DURATION);
                GLog.w("The " + mob.description() + " speeds up!");
            }
        }

        super.shatter(cell);
    }

    @Override
    public void apply( Hero hero ) {
        setKnown();
        Buff.affect(hero, Speed.class, Speed.DURATION);
        GLog.i("You feel yourself moving faster!");
        // remove slow buff if attached
        Buff.detach(hero, Slow.class);
    }

    @Override
    public String desc() {
        return "This potion churns with raw energy.  Drinking this potion will make you move faster.";
    }

    @Override
    public int hungerMods() {
        return -5; // adds 5% to hunger levels
    }

    @Override
    public int price() {
        return isKnown() ? 50 * quantity : super.price();
    }
}
