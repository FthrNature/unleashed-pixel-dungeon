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
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Amok;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bleeding;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Blindness;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Charm;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Cripple;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Light;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Slow;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Vertigo;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.ShadowParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfExperience;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfMight;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.WandOfDisintegration;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Leech;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ChaosMageSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.MinotaurSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.HashSet;

public class ChaosMage extends Mob {
    {
        name = "chaos mage";
        spriteClass = ChaosMageSprite.class;

        HP = HT = 125;
        defenseSkill = 28;
        atkSkill = 40;
        dmgRed = 18;
        dmgMin = 20;
        dmgMax = 35;

        EXP = 15;
        maxLvl = 30;

        viewDistance = Light.DISTANCE;

        baseSpeed = 2f;
        TYPE_EVIL = true;
        state = HUNTING;
        mobType = MOBTYPE_RANGED;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy instanceof Hero){
            corrupt((Hero) enemy);
        }

        if (damage > 0) {
            int healingAmt = Random.Int(0, damage);
            HP = Math.min(HT, HP + healingAmt);
            sprite.emitter().burst(ShadowParticle.UP, 2);
        }

        return damage;
    }

    @Override
    public String description() {
        return
                "A powerful mage gone crazy with power, he wishes only to increase his power.";
    }

    protected boolean corrupt( Hero hero ) {
        switch (Random.Int(7)) {
            case 0:
                Buff.affect(hero, Vertigo.class);
                Buff.prolong(hero, Vertigo.class, 4);
                return true;
            case 1:
                Buff.affect(hero, Poison.class).set(3 * Poison.durationFactor(hero));
                return true;
            case 2:
                Buff.affect(hero, Burning.class).reignite(hero);
                return true;
            case 3:
                Buff.affect(hero, Blindness.class);
                Buff.prolong(hero, Blindness.class, 4);
                return true;
            case 4:
                Buff.affect(hero, Slow.class);
                Buff.prolong(hero, Slow.class, 6);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        Dungeon.level.drop(new PotionOfExperience(), pos).sprite.drop();
    }

    @Override
    public void notice() {
        super.notice();
        GLog.i("The Chaos Mage cackles with glee");
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    static {
        RESISTANCES.add( Death.class );
        RESISTANCES.add( Leech.class );
        RESISTANCES.add( WandOfDisintegration.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Vertigo.class );
        IMMUNITIES.add( Charm.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
