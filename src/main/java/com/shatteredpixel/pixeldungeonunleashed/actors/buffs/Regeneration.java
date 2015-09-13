/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.pixeldungeonunleashed.actors.buffs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.ChaliceOfBlood;

public class Regeneration extends Buff {
	
	private static final float REGENERATION_DELAY = 2;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			if (target.HP < target.HT && !((Hero)target).isStarving()) {
				target.HP += 1;
				if (target.HP == target.HT){
					((Hero)target).resting = false;
				}
			}

			ChaliceOfBlood.chaliceRegen regenBuff = Dungeon.hero.buff( ChaliceOfBlood.chaliceRegen.class);

			float regenDelay = 15f;
			switch (Dungeon.difficultyLevel) {
				case Dungeon.DIFF_TUTOR:
				case Dungeon.DIFF_EASY:
					regenDelay = 12;
					break;
				case Dungeon.DIFF_HARD:
					regenDelay = 14;
					break;
				case Dungeon.DIFF_NTMARE:
					regenDelay = 15;
					break;
				default:
					regenDelay = 13;
					break;
			}
			if (regenBuff != null) {
				if (regenBuff.isCursed()) {
					regenDelay = regenDelay - REGENERATION_DELAY * 1.5f;
				} else {
					regenDelay = regenDelay - REGENERATION_DELAY - regenBuff.level() * 0.9f;
				}
			} else {
				regenDelay = regenDelay - REGENERATION_DELAY;
			}
			spend (Math.max(regenDelay, 4f));
		} else {
			diactivate();
		}
		
		return true;
	}
}
