package com.shatteredpixel.pixeldungeonunleashed.sprites;


import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.watabou.noosa.TextureFilm;

public class SlimeBrownSprite  extends MobSprite {
    public SlimeBrownSprite() {
        super();

        texture(Assets.SLIME);

        TextureFilm frames = new TextureFilm(texture, 20, 14);

        idle = new Animation(10, true);
        idle.frames(frames, 26, 25, 24, 24, 25);

        run = new Animation(10, true);
        run.frames(frames, 27, 29, 25, 26);

        attack = new Animation(10, false);
        attack.frames(frames, 32, 31, 28, 31, 34);

        die = new Animation(10, false);
        die.frames(frames, 29, 30, 31);

        play(idle);
    }
}
