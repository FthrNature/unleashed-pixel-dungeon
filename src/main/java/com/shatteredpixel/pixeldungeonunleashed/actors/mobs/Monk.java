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

import java.util.HashSet;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Amok;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Imp;
import com.shatteredpixel.pixeldungeonunleashed.items.KindOfWeapon;
import com.shatteredpixel.pixeldungeonunleashed.items.food.Food;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Knuckles;
import com.shatteredpixel.pixeldungeonunleashed.sprites.MonkSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.Random;

public class Monk extends Mob {

	public static final String TXT_DISARM	= "%s has knocked the %s from your hands!";
	
	{
		name = "dwarf monk";
		spriteClass = MonkSprite.class;
		
		HP = HT = 70;
		defenseSkill = 30;
		atkSkill = 30;
		dmgMin = 12;
		dmgMax = 16;
		dmgRed = 2;

		EXP = 11;
		maxLvl = 26;
		mobType = MOBTYPE_NIMBLE;
		
		loot = new Food();
		lootChance = 0.083f;
	}

	static int chanceToDisarm = 10;

	@Override
	protected float attackDelay() {
		return 0.5f;
	}
	
	@Override
	public String defenseVerb() {
		return "parried";
	}
	
	@Override
	public void die( Object cause ) {
		Imp.Quest.process( this );
		
		super.die( cause );
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( chanceToDisarm ) == 0 && enemy == Dungeon.hero) {
			chanceToDisarm += 8;
			Hero hero = Dungeon.hero;
			KindOfWeapon weapon = hero.belongings.weapon;
			
			if (weapon != null && !(weapon instanceof Knuckles) && !weapon.cursed) {
				hero.belongings.weapon = null;
				Dungeon.quickslot.clearItem( weapon );
				weapon.updateQuickslot();
				Dungeon.level.drop( weapon, hero.pos ).sprite.drop();
				GLog.w( TXT_DISARM, name, weapon.name() );
			}
		} else {
			chanceToDisarm -= 1;
			if (chanceToDisarm < 5) {
				chanceToDisarm = 5;
			}
		}
		
		return damage;
	}
	
	@Override
	public String description() {
		return
			"These monks are fanatics, who devoted themselves to protecting their city's secrets from all aliens. " +
			"They don't use any armor or weapons, relying solely on the art of hand-to-hand combat.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
