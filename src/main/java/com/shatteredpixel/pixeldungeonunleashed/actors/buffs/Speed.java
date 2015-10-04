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

import com.shatteredpixel.pixeldungeonunleashed.ui.BuffIndicator;

public class Speed extends FlavourBuff {
	
	public static final float DURATION = 12f;

	@Override
	public int icon() {
		return BuffIndicator.HASTED;
	}

	@Override
	public String toString() {
		return "Hasted";
	}

	@Override
	public String desc() {
		return "Haste magic affects the target's rate of time, to them everything is moving super-slow.\n" +
				"\n" +
				"A hasted character performs all actions quicker than they would normally take.\n" +
				"\n" +
				"This haste will last for " + dispTurns() + ".";
	}

	//public static float duration( Char ch ) {
	//	Resistance r = ch.buff( Resistance.class );
	//	return r != null ? r.durationFactor() * DURATION : DURATION;
	//}
}
