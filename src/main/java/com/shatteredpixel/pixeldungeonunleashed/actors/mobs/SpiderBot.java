package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Light;
import com.shatteredpixel.pixeldungeonunleashed.sprites.SpiderBotSprite;
import com.watabou.utils.Random;

public class SpiderBot extends Mob {

    {
        name = "metal spider";
        spriteClass = SpiderBotSprite.class;

        HP = HT = 60;
        defenseSkill = 12;
        atkSkill = 25;
        dmgRed = 8;
        dmgMin = 12;
        dmgMax = 16;


        viewDistance = Light.DISTANCE;

        EXP = 10;
        maxLvl = 20;
        mobType = MOBTYPE_DEBUFF;
    }

    @Override
    public String description() {
        return
                "A metal spider; This strange little contraption stumbles around the dungeon looking for prey.";
    }
}