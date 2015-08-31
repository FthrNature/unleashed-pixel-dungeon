package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import java.util.HashSet;

import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.Statistics;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Cripple;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Frost;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Roots;
import com.shatteredpixel.pixeldungeonunleashed.items.food.MysteryMeat;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.mechanics.Ballistica;
import com.shatteredpixel.pixeldungeonunleashed.sprites.SquidSprite;
import com.watabou.utils.Random;

public class Squid extends Mob {

    {
        name = "deep dweller";
        spriteClass = SquidSprite.class;
        dmgRed = Dungeon.depth;
        dmgMin = Dungeon.depth * 2;
        dmgMax = 4 + Dungeon.depth * 3;

        baseSpeed = 2f;

        EXP = 0;
    }

    public Squid() {
        super();

        HP = HT = 18 + Dungeon.depth * 6;
        defenseSkill = 14 + Dungeon.depth * 2;
        atkSkill = 24 + Dungeon.depth * 2;
    }

    @Override
    protected boolean act() {
        if (!Level.water[pos]) {
            die( null );
            return true;
        } else {
            //this causes squid to move away when a door is closed on them.
            Dungeon.level.updateFieldOfView( this );
            enemy = chooseEnemy();
            if (state == this.HUNTING &&
                    !(enemy != null && enemy.isAlive() && Level.fieldOfView[enemy.pos] && enemy.invisible <= 0)){
                state = this.WANDERING;
                int oldPos = pos;
                int i = 0;
                do {
                    i++;
                    target = Dungeon.level.randomDestination();
                    if (i == 100) return true;
                } while (!getCloser(target));
                moveSprite( oldPos, pos );
                return true;
            }

            return super.act();
        }
    }

    @Override
    public void die( Object cause ) {
        Dungeon.level.drop( new MysteryMeat(), pos ).sprite.drop();
        super.die(cause);
    }

    @Override
    protected boolean getCloser( int target ) {

        if (rooted) {
            return false;
        }

        int step = Dungeon.findPath( this, pos, target,
                Level.water,
                Level.fieldOfView );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean getFurther( int target ) {
        int step = Dungeon.flee(this, pos, target,
                Level.water,
                Level.fieldOfView);
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String description() {
        return
                "All tentacles and bite, this creature isn't normally found in dungeons. " +
                        "They were bred specifically to protect flooded treasure vaults.";
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (Level.adjacent( pos, enemy.pos )) {
            return true;
        } else {
            return false; // DSM-xxxx troubleshoot this next block...
            //Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);
            //return (attack.collisionPos == enemy.pos);
        }
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int( 4 ) == 0) {
            Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
        }

        return damage;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Burning.class );
        IMMUNITIES.add( Paralysis.class );
        IMMUNITIES.add( ToxicGas.class );
        IMMUNITIES.add( Roots.class );
        IMMUNITIES.add( Frost.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    /*
    @Override
    public boolean reset() {
        return true;
    }
   */
}
