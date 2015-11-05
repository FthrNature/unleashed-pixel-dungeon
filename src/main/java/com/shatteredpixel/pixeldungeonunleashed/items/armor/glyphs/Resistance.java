/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.pixeldungeonunleashed.items.armor.glyphs;

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bleeding;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Blindness;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Cripple;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Hunger;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Weakness;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.ShadowParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.Armor;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Resistance extends Armor.Glyph {

    private static final String TXT_METABOLISM	= "%s of resistance";
    private static final String TXT_DESCRIPTION = "This armor can remove traces of toxins and mend the body at the cost of some personal energy.";

    private static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xCCAA44 );

    @Override
    public int proc( Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max( 0, armor.level );
        if (Random.Int(level + 4) >= 2) {
            boolean triggered = false;
            if ((defender.buff( Bleeding.class ) != null) || (defender.buff( Poison.class ) != null) ||
                (defender.buff( Cripple.class ) != null) || (defender.buff( Weakness.class ) != null) ||
                (defender.buff( Blindness.class ) != null) || (defender.buff( Paralysis.class ) != null)) {
                Buff.detach(defender, Bleeding.class);
                Buff.detach(defender, Poison.class);
                Buff.detach(defender, Cripple.class);
                Buff.detach(defender, Weakness.class);
                Buff.detach(defender, Blindness.class);
                Buff.detach(defender, Paralysis.class);

                Hunger hunger = defender.buff( Hunger.class );
                if (hunger != null && !hunger.isStarving()) {

                    hunger.reduceHunger( -Hunger.STARVING / 8 );
                    BuffIndicator.refreshHero();

                    defender.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
                }
            }
        }

        return damage;
    }

    @Override
    public String glyphDescription() { return TXT_DESCRIPTION; };

    @Override
    public String name( String weaponName) {
        return String.format( TXT_METABOLISM, weaponName );
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return YELLOW;
    }
}
