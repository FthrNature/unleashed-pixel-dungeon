package com.shatteredpixel.pixeldungeonunleashed.items.potions;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Slow;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Speed;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;

public class PotionOfSlowness  extends Potion {

    private static final float ALPHA	= 0.4f;

    {
        name = "Potion of Slowness";
        initials = "Sl";
    }

    @Override
    public void shatter( int cell ) {
        for (Mob mob : Dungeon.level.mobs) {
            if (mob.pos == cell) {
                Buff.affect(mob, Slow.class, Slow.DURATION);
                GLog.i("The " + mob.description() + " slows down");
            }
        }

        super.shatter(cell);
    }

        @Override
    public void apply( Hero hero ) {
        setKnown();
        Buff.affect(hero, Slow.class, Slow.DURATION);
        GLog.w("You feel yourself moving slower!");
        // remove haste buff if attached
        Buff.detach(hero, Speed.class);
    }

    @Override
    public String desc() {
        return "This potion runs like molasses.  Drinking this potion will make you move slower.";
    }

    @Override
    public int hungerMods() {
        return 10; // reduces hunger by 10%
    }

    @Override
    public int price() {
        return isKnown() ? 20 * quantity : super.price();
    }
}
