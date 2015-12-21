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
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.AirElemental;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Shaman;
import com.shatteredpixel.pixeldungeonunleashed.effects.Lightning;
import com.watabou.noosa.TextureFilm;

public class AirElementalSprite extends MobSprite {

    public AirElementalSprite() {
        super();

        texture( Assets.ELEMENTAL2 );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 10, true );
        idle.frames( frames, 0, 1, 2 );

        run = new Animation( 12, true );
        run.frames( frames, 6, 7 );

        attack = new Animation( 15, false );
        attack.frames( frames, 1, 8, 1 );

        die = new Animation( 15, false );
        die.frames( frames, 3, 4, 5 );

        zap = attack.clone();

        play( idle );
    }

    public void zap( int pos ) {

        parent.add( new Lightning( ch.pos, pos, (AirElemental)ch ) );

        turnTo( ch.pos, pos );
        play( zap );
    }

    @Override
    public int blood() {
        return 0x0080FF;
    }
}
