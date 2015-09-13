package com.shatteredpixel.pixeldungeonunleashed.sprites;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;

public class ClayGolemSprite extends MobSprite {

    public ClayGolemSprite() {
        super();

        texture( Assets.GOLEM );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 4, true );
        idle.frames( frames, 14, 15 );

        run = new Animation( 12, true );
        run.frames( frames, 16, 17, 18, 17 );

        attack = new Animation( 10, false );
        attack.frames( frames, 23, 24, 23 );

        die = new Animation( 15, false );
        die.frames( frames, 19, 20, 21, 22 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFF80706c;
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == die) {
            emitter().burst( ElmoParticle.FACTORY, 4 );
        }
        super.onComplete( anim );
    }
}
