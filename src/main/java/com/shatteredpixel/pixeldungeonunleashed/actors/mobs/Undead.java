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

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.sprites.UndeadSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Undead extends Mob {

    public static int count = 0;

    {
        name = "undead dwarf";
        spriteClass = UndeadSprite.class;

        HP = HT = 28;
        defenseSkill = 15;
        atkSkill = 16;
        dmgMin = 12;
        dmgMax = 16;
        dmgRed = 5;

        EXP = 0;
        TYPE_EVIL = true;
        TYPE_UNDEAD = true;
        TYPE_MINDLESS = true;

        state = WANDERING;
    }

    @Override
    protected void onAdd() {
        count++;
        super.onAdd();
    }

    @Override
    protected void onRemove() {
        count--;
        super.onRemove();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int(5) == 0) {
            Buff.prolong(enemy, Paralysis.class, 1);
        }

        return damage;
    }

    @Override
    public void damage( int dmg, Object src ) {
        super.damage( dmg, src );
        if (src instanceof ToxicGas) {
            ((ToxicGas)src).clear( pos );
        }
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );

        if (Dungeon.visible[pos]) {
            Sample.INSTANCE.play( Assets.SND_BONES );
        }
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    @Override
    public String description() {
        return
                "These undead dwarves, risen by the will of the King of Dwarves, were members of his court. " +
                        "They appear as skeletons with a stunning amount of facial hair.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Death.class );
        IMMUNITIES.add( Paralysis.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
