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

import com.watabou.noosa.TextureFilm;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;

public class LostSoulSprite extends MobSprite {

    public LostSoulSprite() {
        super();

        texture( Assets.LOSTSOUL );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 1, 2, 3 );

        run = new Animation( 12, true );
        run.frames( frames, 0, 1, 2, 3 );

        attack = new Animation( 12, false );
        attack.frames( frames, 0, 4, 5, 6 );

        die = new Animation( 10, false );
        die.frames( frames, 8, 9, 10, 11 );

        play( idle );
    }

    @Override
    public void die() {
        super.die();
        if (Dungeon.visible[ch.pos]) {
            emitter().burst( Speck.factory( Speck.BONE ), 6 );
        }
    }

    @Override
    public int blood() {
        return 0xFFcccccc;
    }
}
