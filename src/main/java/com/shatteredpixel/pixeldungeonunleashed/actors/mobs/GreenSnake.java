package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;


import com.shatteredpixel.pixeldungeonunleashed.sprites.GreenSnakeSprite;

public class GreenSnake extends Mob {

    {
        name = "marsupial rat";
        spriteClass = GreenSnakeSprite.class;

        HP = HT = 10;
        defenseSkill = 3;
        atkSkill = 8;
        dmgRed = 1;
        dmgMin = 1;
        dmgMax = 4;
        EXP = 2;

        maxLvl = 6;
    }

    @Override
    public String description() {
        return
                "Common dungeon snakes thrive in the dark and damp environments found in caves and sewers.";
    }
}

