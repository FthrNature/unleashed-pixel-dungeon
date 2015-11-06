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
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Ghost;
import com.shatteredpixel.pixeldungeonunleashed.items.food.MysteryMeat;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfExperience;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.Wand;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.LightningTrap;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.GreatCrabSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;

public class GreatCrab extends Crab {
    {
        name = "great crab";
        spriteClass = GreatCrabSprite.class;

        HP = HT = 30;
        defenseSkill = 0; //see damage()
        baseSpeed = 1f;

        EXP = 6;

        TYPE_ANIMAL = true;
        state = WANDERING;
    }

    private int moving = 0;

    @Override
    protected boolean getCloser( int target ) {
        //this is used so that the crab remains slower, but still detects the player at the expected rate.
        moving++;
        if (moving < 3) {
            return super.getCloser( target );
        } else {
            moving = 0;
            return true;
        }

    }

    @Override
    public void damage( int dmg, Object src ){
        //crab blocks all attacks originating from the hero or enemy characters or traps if it is alerted.
        //All direct damage from these sources is negated, no exceptions. blob/debuff effects go through as normal.
        if (enemySeen && (src instanceof Wand || src instanceof LightningTrap.Electricity || src instanceof Char)){
            GLog.n("The crab notices the attack and blocks with its massive claw.");
            sprite.showStatus( CharSprite.NEUTRAL, "blocked" );
        } else {
            super.damage( dmg, src );
        }
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);

        if (Dungeon.difficultyLevel != Dungeon.DIFF_ENDLESS) {
            Ghost.Quest.process();
        } else {
            Dungeon.level.drop(new PotionOfExperience(), pos).sprite.drop();
        }

        Dungeon.level.drop( new MysteryMeat(), pos );
        Dungeon.level.drop( new MysteryMeat(), pos ).sprite.drop();
    }

    @Override
    public String description() {
        return
                "This crab is gigantic, even compared to other sewer crabs. " +
                        "Its blue shell is covered in cracks and barnacles, showing great age. " +
                        "It lumbers around slowly, barely keeping balance with its massive claw.\n\n" +
                        "While the crab only has one claw, its size easily compensates. " +
                        "The crab holds the claw infront of itself whenever it sees a threat, shielding " +
                        "itself behind an impenetrable wall of carapace.";
    }
}
