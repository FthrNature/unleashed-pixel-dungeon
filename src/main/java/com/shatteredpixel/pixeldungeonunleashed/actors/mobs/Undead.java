package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.sprites.UndeadSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Undead extends Mob {

    public static int count = 0;

    {
        name = "undead dwarf";
        spriteClass = UndeadSprite.class;

        HP = HT = 28;
        defenseSkill = 15;
        atkSkill = 16;
        dmgMin = 12;
        dmgMax = 16;
        dmgRed = 5;

        EXP = 0;

        state = WANDERING;
    }

    @Override
    protected void onAdd() {
        count++;
        super.onAdd();
    }

    @Override
    protected void onRemove() {
        count--;
        super.onRemove();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int(5) == 0) {
            Buff.prolong(enemy, Paralysis.class, 1);
        }

        return damage;
    }

    @Override
    public void damage( int dmg, Object src ) {
        super.damage( dmg, src );
        if (src instanceof ToxicGas) {
            ((ToxicGas)src).clear( pos );
        }
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );

        if (Dungeon.visible[pos]) {
            Sample.INSTANCE.play( Assets.SND_BONES );
        }
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    @Override
    public String description() {
        return
                "These undead dwarves, risen by the will of the King of Dwarves, were members of his court. " +
                        "They appear as skeletons with a stunning amount of facial hair.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Death.class );
        IMMUNITIES.add( Paralysis.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
