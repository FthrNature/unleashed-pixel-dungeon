package com.shatteredpixel.pixeldungeonunleashed.sprites;

import com.watabou.noosa.TextureFilm;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;

public class SquidSprite extends MobSprite {

    // private int cellToAttack;

    public SquidSprite() {
        super();

        texture( Assets.SQUID );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 1, 2, 1 );

        run = new Animation( 8, true );
        run.frames( frames, 0, 1, 2, 1 );

        attack = new Animation( 14, false );
        attack.frames( frames, 3, 4, 5, 6, 5, 4, 3 );

        die = new Animation( 6, false );
        die.frames( frames, 7, 8, 9 );

        play( idle );
    }

    /*
    @Override
    public void attack( int cell ) {
        if (!Level.adjacent(cell, ch.pos)) {

            cellToAttack = cell;
            turnTo( ch.pos , cell );
            play( zap );

        } else {

            super.attack( cell );

        }
    }
    */

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete( anim );

        /*
        if (anim == zap) {
            idle();

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset(ch.pos, cellToAttack, new Dart(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    });
        } else
         */
        if (anim == attack) {
            GameScene.ripple( ch.pos );
        }
    }
}
