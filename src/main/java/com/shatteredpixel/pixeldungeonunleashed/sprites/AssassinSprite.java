/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015 David Mitchell
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

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class AssassinSprite extends MobSprite {

    public AssassinSprite() {
        super();

        texture( Assets.MONK );

        TextureFilm frames = new TextureFilm( texture, 15, 14 );

        idle = new MovieClip.Animation( 12, true );
        idle.frames( frames, 35, 34, 35, 36 );

        run = new MovieClip.Animation( 15, true );
        run.frames( frames, 45, 46, 47, 48, 49, 50 );

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 37, 38, 37, 38 );

        die = new MovieClip.Animation( 15, false );
        die.frames( frames, 35, 41, 42, 42, 43, 44 );

        play( idle );
    }
}

