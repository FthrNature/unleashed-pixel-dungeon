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

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.items.food.MysteryMeat;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.Weapon;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Hunting extends Weapon.Enchantment {
    private static final String TXT_HUNTING	= "Hunting %s";

    private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xFF3333 );

    @Override
    public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
        int curDamage = 0;
        int level = Math.max( 0, weapon.level );

        if (defender.TYPE_ANIMAL) {
            curDamage += Random.Int(0, weapon.level + 3);
            defender.damage(curDamage, this);

            if (damage >= defender.HP && (Random.Int(level + 4) >= 3)) {
                Dungeon.level.drop( new MysteryMeat(), defender.pos ).sprite.drop();
            }
            return true;
        }
        return false;
    }

    @Override
    public String name( String weaponName) {
        return String.format( TXT_HUNTING, weaponName );
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return RED;
    }

    @Override
    public String enchDesc() {
        return "This weapon is designed to effectively butcher animals and turn them into tasty, tasty meat.";
    }
}
