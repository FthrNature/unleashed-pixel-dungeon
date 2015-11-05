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
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.Weapon;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Holy extends Weapon.Enchantment {
    private static final String TXT_HOLY	= "Holy %s";

    private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );

    @Override
    public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
        int curDamage = 0;
        int level = Math.max( 0, weapon.level );

        if (defender.TYPE_UNDEAD) {
            if (Random.Int(level + 4) >= 3) {
                curDamage += Random.Int(0, damage + weapon.level);
                defender.damage(curDamage, this);
                return true;
            }
        } else if (defender.TYPE_EVIL) {
            if (Random.Int(level + 4) >= 5) {
                curDamage += Random.Int(0, ((damage + weapon.level) / 2));
                defender.damage(curDamage, this);
                return true;
            }
        }
        return false;
    }

    @Override
    public String name( String weaponName) {
        return String.format( TXT_HOLY, weaponName );
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    @Override
    public String enchDesc() {
        return "Holy weapons inflict great pain upon the undead and truly evil of this world.";
    }
}