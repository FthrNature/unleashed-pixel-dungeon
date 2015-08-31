package com.shatteredpixel.pixeldungeonunleashed.sprites;

import com.shatteredpixel.pixeldungeonunleashed.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.shatteredpixel.pixeldungeonunleashed.Assets;

public class ChaosMageSprite extends MobSprite {

    public ChaosMageSprite() {
        super();

        texture( Assets.CHAOS );

        TextureFilm frames = new TextureFilm( texture, 12, 14 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        run = new Animation( 12, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        attack = new Animation( 12, false );
        attack.frames( frames, 8, 9, 10 );

        die = new Animation( 12, false );
        die.frames( frames, 11, 12, 13, 14 );

        play( idle );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == die) {
            emitter().burst( ElmoParticle.FACTORY, 4 );
        }
        super.onComplete( anim );
    }
}
