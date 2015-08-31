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
package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Ghost;
import com.shatteredpixel.pixeldungeonunleashed.items.Gold;
import com.shatteredpixel.pixeldungeonunleashed.sprites.GnollSprite;
import com.watabou.utils.Random;

public class Gnoll extends Mob {
	
	{
		name = "gnoll scout";
		spriteClass = GnollSprite.class;
		
		HP = HT = 12;
		defenseSkill = 4;
		atkSkill = 11;
		dmgRed = 2;
		dmgMin = 2;
		dmgMax = 5;

		EXP = 2;
		maxLvl = 10;
		
		loot = Gold.class;
		lootChance = 0.5f;
	}
	
	@Override
	public String description() {
		return
			"Gnolls are hyena-like humanoids. They dwell in sewers and dungeons, venturing up to raid the surface from time to time. " +
			"Gnoll scouts are regular members of their pack, they are not as strong as brutes and not as intelligent as shamans.";
	}
}
