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

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bleeding;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Cripple;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.Weapon;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.GrippingTrap;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite.Glowing;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Vicious  extends Weapon.Enchantment {

    private static final String TXT_VICIOUS	= "Vicious %s";
    private static ItemSprite.Glowing BLOODY = new ItemSprite.Glowing( 0x991111 );

    @Override
    public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
        int level = Math.max( 0, weapon.level );

        int curDamage = Random.Int(weapon.MIN, weapon.MAX);
        defender.damage( curDamage, this );

        if (Random.Int((level / 2) + 5) == 0) {
            int userDmg = Random.Int(0, damage);
            attacker.damage(userDmg, this);
            GLog.w("your weapon cuts your hands.");
            Sample.INSTANCE.play(Assets.SND_MELD);
            Buff.affect(attacker, Bleeding.class).set(Random.Int(3, 8));
            Buff.prolong(attacker, Cripple.class, Cripple.DURATION);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Glowing glowing() {
        return BLOODY;
    }

    @Override
    public String name( String weaponName) {
        return String.format( TXT_VICIOUS, weaponName );
    }

    @Override
    public String enchDesc() {
        return "Vicious weapons are covered in razor sharp spikes that do a great deal of damage to"+
        "your enemies, but they are difficult to use without injuring yourself.";
    }

}
