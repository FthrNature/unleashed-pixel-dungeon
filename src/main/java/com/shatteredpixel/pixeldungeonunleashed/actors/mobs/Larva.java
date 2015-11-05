package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.sprites.LarvaSprite;

public class Larva extends Mob {

    {
        name = "god's larva";
        spriteClass = LarvaSprite.class;

        HP = HT = 25;
        defenseSkill = 20;
        atkSkill = 30;
        dmgRed = 8;
        dmgMin = 15;
        dmgMax = 20;

        EXP = 0;

        state = HUNTING;
    }
    private String TXT_DESC = "The Larva is the offspring of an ancient god.";

    @Override
    public String description() {
        return TXT_DESC;

    }
}
