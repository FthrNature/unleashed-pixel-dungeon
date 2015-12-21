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

public class GrayWolfSprite extends MobSprite {

    public GrayWolfSprite() {
        super();

        texture( Assets.WOLF );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 9, 10, 11, 10 );

        run = new Animation( 10, true );
        run.frames( frames, 13, 14, 15 );

        attack = new Animation( 14, false );
        attack.frames( frames, 10, 12, 10 );

        die = new Animation( 10, false );
        die.frames( frames, 10, 16, 17 );

        play( idle );
    }
}