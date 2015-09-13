package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ZombieSprite;

import java.util.HashSet;

public class Zombie extends Mob {

    {
        name = "zombie";
        spriteClass = ZombieSprite.class;

        HP = HT = 22;
        defenseSkill = 9;
        atkSkill = 14;

        EXP = 5;
        maxLvl = 12;

        dmgMin = 3;
        dmgMax = 8;
        dmgRed = 4;
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    @Override
    public String description() {
        return
                "Zombies are the corpses of unlucky adventurers and inhabitants of the dungeon, " +
                        "animated by emanations of evil magic from the depths below.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
        IMMUNITIES.add( Death.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
