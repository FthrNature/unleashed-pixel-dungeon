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
package com.shatteredpixel.pixeldungeonunleashed.items.armor.glyphs;

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Blob;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.GasesImmunity;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.Armor;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.Armor.Glyph;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Stench extends Glyph {

	private static final String TXT_STENCH	= "%s of stench";
	private static final String TXT_DESCRIPTION = "This armor emits a noxious stench to throw off an attacker.";

	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x22CC44 );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, armor.level );
		
		if (Level.adjacent( attacker.pos, defender.pos ) && Random.Int( level + 5 ) >= 4) {
			GameScene.add( Blob.seed( attacker.pos, 20, ToxicGas.class ) );
			if (Random.Int(4) < 3) {
				Buff.prolong(defender, GasesImmunity.class, GasesImmunity.DURATION);
			}

		}
		
		return damage;
	}

	@Override
	public String glyphDescription() { return TXT_DESCRIPTION; };

	@Override
	public String name( String weaponName) {
		return String.format( TXT_STENCH, weaponName );
	}
	
	@Override
	public Glowing glowing() {
		return GREEN;
	}

}
