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
package com.shatteredpixel.pixeldungeonunleashed.items.potions;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bleeding;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Cripple;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Weakness;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;

public class PotionOfHealing extends Potion {

	{
		name = "Potion of Healing";
		initials = "He";

		bones = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		heal( Dungeon.hero );
		GLog.p( "Your wounds heal completely." );
	}
	
	public static void heal( Hero hero ) {

		switch (Dungeon.difficultyLevel) {
			case Dungeon.DIFF_NTMARE:
				hero.HP += (hero.HT * 0.5f);
				if (hero.HP > hero.HT) {
					hero.HP = hero.HT;
				}
				break;
			case Dungeon.DIFF_HARD:
				hero.HP += (hero.HT * 0.8f);
				if (hero.HP > hero.HT) {
					hero.HP = hero.HT;
				}
				break;
			default:
				hero.HP = hero.HT;
				break;
		}

		Buff.detach( hero, Poison.class );
		Buff.detach( hero, Cripple.class );
		Buff.detach(hero, Weakness.class);
		Buff.detach(hero, Bleeding.class);
		
		hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);
	}

	@Override
	public int hungerMods() {
		return 5; // reduces hunger by 5%
	}

	@Override
	public String desc() {
		return
			"An elixir that will instantly restore your health and cure poison.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
