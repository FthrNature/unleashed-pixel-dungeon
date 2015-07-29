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

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.Armor;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.Armor.Glyph;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Displacement extends Glyph {

	private static final String TXT_DISPLACEMENT	= "%s of displacement";
	private static final String TXT_DESCRIPTION = "This armor can teleport the wearer away from harm.";

	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x66AAFF );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage ) {

		if (Dungeon.bossLevel()) {
			return damage;
		}
		
		int nTries = (armor.level < 0 ? 1 : armor.level + 1) * 5;
		for (int i=0; i < nTries; i++) {
			int pos = Random.Int( Level.LENGTH );
			if (Dungeon.visible[pos] && Level.passable[pos] && Actor.findChar( pos ) == null) {
				
				ScrollOfTeleportation.appear( defender, pos );
				Dungeon.level.press( pos, defender );
				Dungeon.observe();

				break;
			}
		}
		
		return damage;
	}

	@Override
	public String glyphDescription() { return TXT_DESCRIPTION; };

	@Override
	public String name( String weaponName) {
		return String.format( TXT_DISPLACEMENT, weaponName );
	}

	@Override
	public Glowing glowing() {
		return BLUE;
	}
}
