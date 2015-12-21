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
package com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments;

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.Weapon;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Ancient extends Weapon.Enchantment  {
    public int numKills = 0;
    public int myLevel = 1;

    private static final String TXT_ANCIENT	= "Artifact %s";
    private static final String NUMKILLS = "numkills";

    private static ItemSprite.Glowing LTGRAY = new ItemSprite.Glowing( 0x888888 );

    @Override
    public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
        boolean specialAttack = false;
        int curDamage = 0;
        int defHP = defender.HP;

        int level = Math.max( 0, weapon.level );
        if (Random.Int( level + 3 ) >= 2) {
            curDamage = Random.Int(1, level + 2);
            defender.damage( curDamage, this );
            specialAttack = true;
        }
        else
        {
            curDamage = 0;
            specialAttack = false;
        }

        if ((weapon.level < weapon.levelCap) && ((curDamage + damage) >= defHP)) {
            // every 8 killing blows this weapon has a chance at an upgrade
            numKills++;
            if (numKills > 10) {
                int testValue = weapon.levelCap * weapon.levelCap;
                int failRate = testValue - (level * (level - 1));
                if (level < 3) {
                    GLog.p("The %s attunes itself to you.", weapon.name());
                    weapon.upgrade();
                    numKills = 0;
                } else if (Random.Int(testValue) < failRate) {
                    GLog.p("The %s attunes itself to you.", weapon.name());
                    weapon.upgrade();
                    numKills = 0;
                }
                else
                {
                    // it will take a little longer til we get this upgrade
                    numKills = 0;
                }
            }
        }

        return specialAttack;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );

        bundle.put( NUMKILLS, numKills );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        numKills = bundle.getInt( NUMKILLS );
    }

    @Override
    public String name( String weaponName) {
        return String.format( TXT_ANCIENT, weaponName );
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return LTGRAY;
    }

    @Override
    public String enchDesc() {
        return "Artifact weapons can not be upgraded, instead they attune themselves to the wielder "+
                "and grow in power through combat.";
    }
}
