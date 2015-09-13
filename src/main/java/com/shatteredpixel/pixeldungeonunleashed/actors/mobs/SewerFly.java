package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Corruption;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.effects.Pushing;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfHealing;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.levels.features.Door;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.FlySprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SewerFly extends Mob {

    {
        name = "swarm of sewer flies";
        spriteClass = FlySprite.class;

        HP = HT = 50;
        defenseSkill = 5;
        atkSkill = 12;
        dmgMin = 1;
        dmgMax = 4;

        maxLvl = 9;

        flying = true;

        loot = new PotionOfHealing();
        lootChance = 0.1f; //by default, see die()
    }

    private static final float SPLIT_DELAY	= 1f;

    int generation	= 0;

    private static final String GENERATION	= "generation";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( GENERATION, generation );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        generation = bundle.getInt( GENERATION );
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {

        if (HP >= damage + 2) {
            ArrayList<Integer> candidates = new ArrayList<>();
            boolean[] passable = Level.passable;

            int[] neighbours = {pos + 1, pos - 1, pos + Level.WIDTH, pos - Level.WIDTH};
            for (int n : neighbours) {
                if (passable[n] && Actor.findChar(n) == null) {
                    candidates.add( n );
                }
            }

            if (candidates.size() > 0) {

                SewerFly clone = split();
                clone.HP = (HP - damage) / 2;
                clone.pos = Random.element(candidates);
                clone.state = clone.HUNTING;

                if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
                    Door.enter(clone.pos);
                }

                GameScene.add(clone, SPLIT_DELAY);
                Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );

                HP -= clone.HP;
            }
        }

        return super.defenseProc(enemy, damage);
    }

    @Override
    public String defenseVerb() {
        return "evaded";
    }

    private SewerFly split() {
        SewerFly clone = new SewerFly();
        clone.generation = generation + 1;
        if (buff( Burning.class ) != null) {
            Buff.affect(clone, Burning.class).reignite( clone );
        }
        if (buff( Poison.class ) != null) {
            Buff.affect( clone, Poison.class ).set(2);
        }
        if (buff(Corruption.class ) != null) {
            Buff.affect( clone, Corruption.class);
        }
        return clone;
    }

    @Override
    public void die( Object cause ){
        //sets drop chance
        lootChance = 1f/((8 + Dungeon.limitedDrops.swarmHP.count ) * (generation+1) );
        super.die( cause );
    }

    @Override
    protected Item createLoot(){
        Dungeon.limitedDrops.swarmHP.count++;
        return super.createLoot();
    }

    @Override
    public String description() {
        return
                "Found only in sewers, these dangerous swarms of flies buzz angrily. Every non-magical attack " +
                        "will split it into two smaller but equally dangerous swarms.";
    }
}
