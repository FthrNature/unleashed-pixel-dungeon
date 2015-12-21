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
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Freezing;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Chill;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Sleep;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Vertigo;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.SkeletonKey;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.DemonLordSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class DemonLord extends Mob {

    {
        name = "demon lord";
        spriteClass = DemonLordSprite.class;

        HP = HT = 180;
        defenseSkill = 35;
        atkSkill = 45;
        dmgRed = 22;
        dmgMin = 20;
        dmgMax = 50;
        EXP = 16;

        TYPE_EVIL = true;
        maxLvl = 34;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy instanceof Hero){
            if (Random.Int(3) == 0) {
                Buff.prolong(enemy, Chill.class, 4f);
            }
        }

        return damage;
    }


    @Override
    public String description() {
        return
                "A demon lord called from the pits of hell itself has come to claim this dungeon. "+
                "for years he was held in check by the power of the dwarf king, but now you have "+
                "let him loose on the world.";
    }

    @Override
    public void die( Object cause ) {

        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();
        super.die( cause );

        yell( "..." );
    }


    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    static {
        RESISTANCES.add( Death.class );
        RESISTANCES.add( Burning.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Freezing.class );
        IMMUNITIES.add( Sleep.class );
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( ScrollOfPsionicBlast.class );
        IMMUNITIES.add( Vertigo.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

}

