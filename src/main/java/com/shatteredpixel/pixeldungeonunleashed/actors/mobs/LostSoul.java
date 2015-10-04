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

        HP = HT = 80;
        defenseSkill = 25;
        atkSkill = 40;
        dmgRed = 10;
        dmgMin = 18;
        dmgMax = 25;

        EXP = 12;
        maxLvl = 30;

        baseSpeed = 2f;
        flying = true;
        mobType = MOBTYPE_DEBUFF;
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
