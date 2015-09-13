package com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.items.Gold;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.Weapon;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Midas  extends Weapon.Enchantment  {

    private static final String TXT_MIDAS	= "Midas %s";

    private static ItemSprite.Glowing GOLDEN = new ItemSprite.Glowing( 0xFFD700 );

    @Override
    public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
        int level = weapon.level;
        if (damage >= defender.HP) {
            if (level >= 0) {
                int goldValue = Random.IntRange(level + 3, level + 10);
                Dungeon.level.drop(new Gold(goldValue), defender.pos);
                defender.sprite.showStatus(CharSprite.GOLDEN, Integer.toString(goldValue));

                Sample.INSTANCE.play(Assets.SND_GOLD);
            }
            return true;
        } else if (Random.Int(level + 3) >= 4) {
            int goldValue = Random.IntRange(1, level + 2);
            Dungeon.level.drop(new Gold(goldValue), defender.pos);
            defender.sprite.showStatus(CharSprite.GOLDEN, Integer.toString(goldValue));
            Sample.INSTANCE.play(Assets.SND_GOLD);
            return true;
        }

        return false;
    }

    @Override
    public String name( String weaponName) {
        return String.format( TXT_MIDAS, weaponName );
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GOLDEN;
    }

    @Override
    public String enchDesc() {
        return "Midas weapons cause your enemies to bleed gold; a quick cut might give a handful "+
        "of coins, but to truly grow rich you want your enemies to bleed... a lot.";
    }
}
