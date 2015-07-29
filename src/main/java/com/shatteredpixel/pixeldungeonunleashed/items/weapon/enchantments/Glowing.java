package com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments;

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Light;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.Weapon;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.Random;

public class Glowing extends Weapon.Enchantment {

    private static final String TXT_GLOWING	= "Glowing %s";

    private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );

    @Override
    public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
        int curDamage = 0;
        int level = Math.max( 0, weapon.level );

        if (Random.Int(level + 4) >= 3) {
            Buff.affect( attacker, Light.class, (level + 5f) );
            GLog.i("Your " + weapon.name() + " glows brightly.");
            curDamage = Random.Int(0, level);
            defender.damage(curDamage, this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String name( String weaponName) {
        return String.format( TXT_GLOWING, weaponName );
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }
}
