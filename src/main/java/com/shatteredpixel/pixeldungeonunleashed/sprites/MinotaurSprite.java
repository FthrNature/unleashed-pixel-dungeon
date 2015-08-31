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
