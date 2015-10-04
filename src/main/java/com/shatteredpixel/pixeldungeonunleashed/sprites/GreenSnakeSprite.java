package com.shatteredpixel.pixeldungeonunleashed.sprites;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.watabou.noosa.TextureFilm;

public class GreenSnakeSprite extends MobSprite {

    public GreenSnakeSprite() {
        super();

        texture( Assets.SNAKE );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 5, 4, 3, 4, 5 );

        attack = new Animation( 14, false );
        attack.frames( frames, 0, 2, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 2, 6, 7 );

        play( idle );
    }
}
