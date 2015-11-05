package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.ResultDescriptions;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Blob;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Fire;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Amok;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Sleep;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Vertigo;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.mechanics.Ballistica;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.BurningFistSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;

import java.util.HashSet;

public class BurningFist extends Mob {

    {
        name = "burning fist";
        spriteClass = BurningFistSprite.class;

        HP = HT = 200;
        atkSkill = 36;
        defenseSkill = 25;
        dmgRed = 15;
        dmgMin = 20;
        dmgMax = 32;

        EXP = 0;

        state = WANDERING;
    }

    private String TXT_DESC = "The Burning Fist is an extension of an ancient god into our dimension.";

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    @Override
    public boolean attack( Char enemy ) {

        if (!Level.adjacent(pos, enemy.pos)) {
            spend( attackDelay() );

            if (hit( this, enemy, true )) {

                int dmg =  damageRoll();
                enemy.damage( dmg, this );

                enemy.sprite.bloodBurstA( sprite.center(), dmg );
                enemy.sprite.flash();

                if (!enemy.isAlive() && enemy == Dungeon.hero) {
                    Dungeon.fail( Utils.format(ResultDescriptions.UNIQUE, name) );
                    GLog.n(TXT_KILL, name);
                }
                return true;

            } else {

                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
                return false;
            }
        } else {
            return super.attack( enemy );
        }
    }

    @Override
    public boolean act() {

        for (int i=0; i < Level.NEIGHBOURS9.length; i++) {
            GameScene.add(Blob.seed(pos + Level.NEIGHBOURS9[i], 2, Fire.class));
        }

        return super.act();
    }

    @Override
    public String description() {
        return TXT_DESC;

    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    static {
        RESISTANCES.add( ToxicGas.class );
        RESISTANCES.add( Death.class );

    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Amok.class );
        IMMUNITIES.add( Sleep.class );
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Burning.class );
        IMMUNITIES.add( ScrollOfPsionicBlast.class );
        IMMUNITIES.add( Vertigo.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
