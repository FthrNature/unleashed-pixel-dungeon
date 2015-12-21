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
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Fire;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Ghost;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.CurareDart;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.mechanics.Ballistica;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.GnollTricksterSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GnollTrickster extends Gnoll {
    {
        name = "gnoll trickster";
        spriteClass = GnollTricksterSprite.class;

        HP = HT = 20;
        defenseSkill = 5;
        atkSkill = 16;

        EXP = 5;

        state = WANDERING;

        loot = Generator.random(CurareDart.class);
        lootChance = 1f;
    }

    private int combo = 0;

    @Override
    protected boolean canAttack( Char enemy ) {
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE );
        if (!Level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos){
            combo++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        //The gnoll's attacks get more severe the more the player lets it hit them
        int effect = Random.Int(4)+combo;

        if (effect > 2) {

            if (effect >=6 && enemy.buff(Burning.class) == null){

                if (Level.flamable[enemy.pos])
                    GameScene.add(Blob.seed(enemy.pos, 4, Fire.class));
                Buff.affect(enemy, Burning.class).reignite( enemy );

            } else
                Buff.affect( enemy, Poison.class).set((effect-2) * Poison.durationFactor(enemy));

        }
        return damage;
    }

    @Override
    protected boolean getCloser( int target ) {
        combo = 0; //if he's moving, he isn't attacking, reset combo.
        if (enemy != null && Level.adjacent(pos, enemy.pos)) {
            return getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);

        if (Dungeon.difficultyLevel != Dungeon.DIFF_ENDLESS) {
            Ghost.Quest.process();
        } else {
            Dungeon.level.drop(new CurareDart(8), pos).sprite.drop();
        }
    }

    @Override
    public String description() {
        return
                "A strange looking creature, even by gnoll standards. It hunches forward with a wicked grin, " +
                        "almost cradling the satchel hanging over its shoulder. Its eyes are wide with a strange mix of " +
                        "fear and excitement.\n\n" +
                        "There is a large collection of poorly made darts in its satchel, they all seem to be " +
                        "tipped with various harmful substances.";
    }

    private static final String COMBO = "combo";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(COMBO, combo);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        combo = bundle.getInt( COMBO );
    }

}

