package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.ResultDescriptions;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.SparkParticle;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.LightningTrap;
import com.shatteredpixel.pixeldungeonunleashed.mechanics.Ballistica;
import com.shatteredpixel.pixeldungeonunleashed.sprites.AirElementalSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class AirElemental extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 2f;

    private static final String TXT_LIGHTNING_KILLED = "%s's lightning bolt killed you...";

    {
        name = "air elemental";
        spriteClass = AirElementalSprite.class;

        HP = HT = 100;
        defenseSkill = 45;
        atkSkill = 35;
        dmgMin = 20;
        dmgMax = 45;
        dmgRed = 18;

        EXP = 9;
        maxLvl = 30;

        mobType = MOBTYPE_RANGED;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if (Level.distance(pos, enemy.pos) <= 1) {

            return super.doAttack( enemy );

        } else {

            boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemy.pos];
            if (visible) {
                ((AirElementalSprite)sprite).zap( enemy.pos );
            }

            spend( TIME_TO_ZAP );

            if (hit( this, enemy, true )) {
                int dmg = Random.Int(2, (dmgMax / 3));
                if (Level.water[enemy.pos] && !enemy.flying) {
                    dmg *= 1.5f;
                }
                enemy.damage( dmg, LightningTrap.LIGHTNING );

                enemy.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
                enemy.sprite.flash();

                if (enemy == Dungeon.hero) {

                    Camera.main.shake( 2, 0.3f );

                    if (!enemy.isAlive()) {
                        Dungeon.fail( Utils.format(ResultDescriptions.MOB, Utils.indefinite(name)) );
                        GLog.n(TXT_LIGHTNING_KILLED, name);
                    }
                }
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }

            return !visible;
        }
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public String description() {
        return
                "A whirlwind of destructive force called into being by ancient arcane magic.";
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    static {
        RESISTANCES.add( LightningTrap.Electricity.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }
}