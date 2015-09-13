package com.shatteredpixel.pixeldungeonunleashed.sprites;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.watabou.noosa.TextureFilm;

public class ZombieSprite  extends MobSprite {

    public ZombieSprite() {
        super();

        texture( Assets.ZOMBIE );

        TextureFilm frames = new TextureFilm( texture, 12, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 2, 3, 4, 5, 6 );

        attack = new Animation( 10, false );
        attack.frames( frames, 7, 8, 9, 8 );

        die = new Animation( 8, false );
        die.frames( frames, 1, 10, 11, 12 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0x99FF99;
    }
}
