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
package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Blob;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.StenchGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Ooze;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Ghost;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.FetidRatSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class FetidRat extends Rat {

    {
        name = "fetid rat";
        spriteClass = FetidRatSprite.class;

        HP = HT = 20;
        defenseSkill = 5;

        EXP = 4;

        TYPE_ANIMAL = true;
        state = WANDERING;
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int dr() {
        return 2;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Ooze.class);
        }

        return damage;
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {

        GameScene.add(Blob.seed(pos, 20, StenchGas.class));

        return super.defenseProc(enemy, damage);
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);

        if (Dungeon.difficultyLevel != Dungeon.DIFF_ENDLESS) {
            Ghost.Quest.process();
        } else {
            Dungeon.level.drop(new PotionOfParalyticGas(), pos).sprite.drop();
        }
    }

    @Override
    public String description() {
        return
                "Something is clearly wrong with this rat. Its greasy black fur and rotting skin are very " +
                        "different from the healthy rats you've seen previously. It's pale green eyes " +
                        "make it seem especially menacing.\n\n" +
                        "The rat carries a cloud of horrible stench with it, it's overpoweringly strong up close.\n\n" +
                        "Dark ooze dribbles from the rat's mouth, it eats through the floor but seems to dissolve in water.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( StenchGas.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
