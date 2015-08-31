package com.shatteredpixel.pixeldungeonunleashed.sprites;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.watabou.noosa.TextureFilm;

public class SlimeRedSprite  extends MobSprite {
    public SlimeRedSprite() {
        super();

        texture(Assets.SLIME);

        TextureFilm frames = new TextureFilm(texture, 20, 14);

        idle = new Animation(10, true);
        idle.frames(frames, 14, 13, 22, 12, 13);

        run = new Animation(10, true);
        run.frames(frames, 15, 14, 13, 14);

        attack = new Animation(10, false);
        attack.frames(frames, 20, 21, 16, 21, 22);

        die = new Animation(10, false);
        die.frames(frames, 17, 18, 19);

        play(idle);
    }
}
