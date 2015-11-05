/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
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
