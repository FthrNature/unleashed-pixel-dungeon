package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.sprites.SlimeBrownSprite;
import com.watabou.utils.Random;

public class SlimeBrown extends Mob {
    {
        name = "brown slime";
        spriteClass = SlimeBrownSprite.class;

        HP = HT = 14;
        defenseSkill = 4;
        atkSkill = 10;
        dmgRed = 3;

        EXP = 3;
        maxLvl = 10;

    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(3, 6);
    }

    @Override
    public String description() {
        return
                "Slimes look like icky little piles of goo, but they can pack a bite." +
                        "The brown slime is probably the weakest of the dungeon slimes.";
    }
}
