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
