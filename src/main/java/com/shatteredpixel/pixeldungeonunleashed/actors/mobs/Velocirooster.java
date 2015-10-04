package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.items.food.MysteryMeat;
import com.shatteredpixel.pixeldungeonunleashed.sprites.VelociroosterSprite;

public class Velocirooster extends Mob {

    {
        name = "velocirooster";
        spriteClass = VelociroosterSprite.class;

        HP = HT = 18;
        defenseSkill = 6;
        atkSkill = 11;
        dmgRed = 2;
        baseSpeed = 2f;
        dmgMin = 3;
        dmgMax = 6;

        EXP = 4;
        maxLvl = 11;

        loot = new MysteryMeat();
        lootChance = 0.167f;
        mobType = MOBTYPE_NIMBLE;
    }

    @Override
    public String defenseVerb() {
        return "Dodged";
    }

    @Override
    public String description() {
        return
                "The Velocirooster is a vicious cousin of the domesticated rooster." +
                " It races through the dungeon and attacks with razor sharp talons and a vicious beak." +
                " No one is sure how they got in the dungeon, but they seem at home here now.";
    }
}
