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
package com.shatteredpixel.pixeldungeonunleashed.sprites;

import com.watabou.noosa.TextureFilm;
import com.shatteredpixel.pixeldungeonunleashed.Assets;

public class AcidicSprite extends ScorpioSprite {
	
	public AcidicSprite() {
		super();
		
		texture( Assets.SCORPIO );
		
		TextureFilm frames = new TextureFilm( texture, 18, 17 );
		
		idle = new Animation( 12, true );
		idle.frames( frames, 11, 11, 11, 11, 11, 11, 11, 11, 12, 13, 12, 13, 12, 13 );
		
		run = new Animation( 4, true );
		run.frames( frames, 16, 17 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 11, 14, 15 );
		
		zap = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, 11, 18, 19, 20, 21 );
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0xFF66FF22;
	}
}
