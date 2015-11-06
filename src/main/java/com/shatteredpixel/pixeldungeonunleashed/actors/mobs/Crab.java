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

import com.shatteredpixel.pixeldungeonunleashed.items.food.MysteryMeat;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CrabSprite;

public class Crab extends Mob {

	{
		name = "sewer crab";
		spriteClass = CrabSprite.class;
		
		HP = HT = 15;
		defenseSkill = 5;
		atkSkill = 11;
		dmgRed = 4;
		baseSpeed = 2f;
		dmgMin = 3;
		dmgMax = 6;

		EXP = 4;
		maxLvl = 11;
		
		loot = new MysteryMeat();
		lootChance = 0.167f;
		TYPE_ANIMAL = true;
		mobType = MOBTYPE_TOUGH;
	}
	
	@Override
	public String defenseVerb() {
		return "parried";
	}
	
	@Override
	public String description() {
		return
			"These huge crabs are at the top of the food chain in the sewers. " +
			"They are extremely fast and their thick carapace can withstand " +
			"heavy blows.";
	}
}
