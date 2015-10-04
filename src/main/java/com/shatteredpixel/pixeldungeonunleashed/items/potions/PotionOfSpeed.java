package com.shatteredpixel.pixeldungeonunleashed.items.potions;

import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Slow;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Speed;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;

public class PotionOfSpeed  extends Potion {

    private static final float ALPHA	= 0.4f;

    {
        name = "Potion of Speed";
        initials = "Sp";
    }

    @Override
    public void apply( Hero hero ) {
        setKnown();
        Buff.affect(hero, Speed.class, Speed.DURATION);
        GLog.i("You feel yourself moving faster!");
        // remove slow buff if attached
        Buff.detach(hero, Slow.class);
    }

    @Override
    public String desc() {
        return "This potion churns with raw energy.  Drinking this potion will make you move faster.";
    }

    @Override
    public int hungerMods() {
        return -5; // adds 5% to hunger levels
    }

    @Override
    public int price() {
        return isKnown() ? 50 * quantity : super.price();
    }
}
