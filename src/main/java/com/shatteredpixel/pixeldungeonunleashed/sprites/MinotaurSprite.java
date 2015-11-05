/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
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
import com.watabou.noosa.TextureFilm;

public class MinotaurSprite extends MobSprite {

    public MinotaurSprite() {
        super();

        texture( Assets.MINOTAUR );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 4, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 4, 5, 6, 5 );

        attack = new Animation( 15, false );
        attack.frames( frames, 0, 3, 2, 3, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 7, 8, 9, 10 );

        play( idle );
    }
}
