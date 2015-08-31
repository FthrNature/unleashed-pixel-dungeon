package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Charm;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Cripple;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Light;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Vertigo;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfMagicalInfusion;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.mechanics.Ballistica;
import com.shatteredpixel.pixeldungeonunleashed.sprites.TinkererSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

/**
 * Created by david on 8/20/15.
 */
public class Tinkerer extends Mob {

    {
        name = "mad tinkerer";
        spriteClass = TinkererSprite.class;

        HP = HT = 70;
        defenseSkill = 20;
        atkSkill = 25;
        viewDistance = Light.DISTANCE;

        EXP = 11;
        maxLvl = 22;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(16, 20);
    }

    @Override
    public int dr() {
        return 5;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.THROWN_ITEM);
        return !Level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if ((!Level.adjacent(pos, enemy.pos)) && Random.Int( 2 ) == 0) {
            Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
            damage += Random.IntRange(4, 20);
        }

        return damage;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public String description() {
        return
                "This madman runs around the dungeon caring for his robotic creations.";
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        Dungeon.level.drop(new ScrollOfMagicalInfusion(), pos).sprite.drop();
        yell( "Avenge me my robots!" );
    }

    @Override
    public void notice() {
        super.notice();
        yell( "I will not let you harm my creations!" );
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    static {
        RESISTANCES.add( Death.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Vertigo.class );
        IMMUNITIES.add( Charm.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }}
