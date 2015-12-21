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

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Chill;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Frost;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.WandOfFireblast;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Fire;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.sprites.LostSoulSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class LostSoul  extends Mob {

    {
        name = "lost soul";
        spriteClass = LostSoulSprite.class;

        HP = HT = 100;
        defenseSkill = 32;
        atkSkill = 46;
        dmgRed = 15;
        dmgMin = 22;
        dmgMax = 35;

        EXP = 12;
        maxLvl = 32;

        baseSpeed = 2f;
        flying = true;
        mobType = MOBTYPE_DEBUFF;
        TYPE_UNDEAD = true;
        TYPE_EVIL = true;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int( 3 ) == 0) {
            Buff.affect( enemy, Burning.class ).reignite( enemy );
        }

        return damage;
    }

    @Override
    public void add( Buff buff ) {
        if (buff instanceof Burning) {
            if (HP < HT) {
                HP++;
                sprite.emitter().burst( Speck.factory(Speck.HEALING), 1 );
            }
        } else if (buff instanceof Frost || buff instanceof Chill) {
            if (Level.water[this.pos])
                damage( Random.NormalIntRange( HT / 2, HT ), buff );
            else
                damage( Random.NormalIntRange( 1, HT * 2 / 3 ), buff );
        } else {
            super.add( buff );
        }
    }

    @Override
    public String description() {
        return
                "A fallen hero forced to spend eternity wandering halls and protecting the Chaos Mage.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Burning.class );
        IMMUNITIES.add( Fire.class );
        IMMUNITIES.add( WandOfFireblast.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
