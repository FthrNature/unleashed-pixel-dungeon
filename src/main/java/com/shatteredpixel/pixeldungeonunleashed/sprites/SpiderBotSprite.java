package com.shatteredpixel.pixeldungeonunleashed.sprites;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.watabou.noosa.TextureFilm;

public class SpiderBotSprite extends MobSprite {
    public SpiderBotSprite() {
        super();

        texture(Assets.SPIDERBOT);

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 10, true );
        idle.frames( frames, 0, 0, 0, 0, 0, 1, 0, 1 );

        run = new Animation( 15, true );
        run.frames( frames, 0, 2, 0, 3 );

        attack = new Animation( 12, false );
        attack.frames( frames, 0, 4, 5, 0 );

        die = new Animation( 12, false );
        die.frames( frames, 6, 7, 8, 9 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFFCCCCCC;
    }
}