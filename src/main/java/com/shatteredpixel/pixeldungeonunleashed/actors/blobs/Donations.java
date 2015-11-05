/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015 David Mitchell
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

package com.shatteredpixel.pixeldungeonunleashed.actors.blobs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.Journal;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Awareness;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bless;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Vertigo;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.effects.BlobEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfHealing;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.Scroll;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.Weapon;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Ancient;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Glowing;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Luck;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndMessage;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Donations extends Blob {
    protected int pos;

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        for (int i=0; i < LENGTH; i++) {
            if (cur[i] > 0) {
                pos = i;
                break;
            }
        }
    }

    @Override
    protected void evolve() {
        volume = off[pos] = cur[pos];

        if (Dungeon.visible[pos]) {
            Journal.add( Journal.Feature.ALTAR );
            if (Dungeon.difficultyLevel == Dungeon.DIFF_TUTOR && !Dungeon.tutorial_altar_seen) {
                Dungeon.tutorial_altar_seen = true;
                GameScene.show(new WndMessage("An Altar room allows you to donate items in the hopes of " +
                    "divine interention.  More expensive donations provide better rewards, including some difficult " +
                    "to obtain enchanted weapons, artifacts and instant level-ups.  Your donated loot is cumulative."));
            }
        }
    }

    @Override
    public void seed( int cell, int amount ) {
        cur[pos] = 0;
        pos = cell;
        volume = cur[pos] = amount;
    }

    public static void donate( int cell ) {
        Hero hero = Dungeon.hero;
        Heap heap = Dungeon.level.heaps.get( cell );
        if (heap != null) {
            // don't allow accidental donations of important items such as keys or quest items
            // and don't insult the gods with insignificant donations
            if (heap.peek().unique || heap.peek().price() < 5 || (heap.peek() instanceof MissileWeapon)) {
                GLog.p("The Gods refuse your offering.");
                // throw the item off of the Altar to avoid a redonation loop
                int newPlace;
                do {
                    newPlace = cell + Level.NEIGHBOURS8[Random.Int(8)];
                } while (!Level.passable[newPlace] && !Level.avoid[newPlace]);
                Dungeon.level.drop(heap.pickUp(), newPlace).sprite.drop(cell);

                return;
            }

            if (heap.peek() instanceof Scroll) {
                hero.donatedLoot += (heap.peek().price() * heap.peek().quantity() * 3);
            } else {
                hero.donatedLoot += (heap.peek().price() * heap.peek().quantity());
            }
            GLog.i("DONATED LOOT: " + hero.donatedLoot); // DSM-xxxx
            if (heap.peek().cursed) {
                // the Gods do not like to receive cursed goods and will punish the hero for this
                Buff.affect(hero, Burning.class).reignite(hero);
                Buff.affect(hero, Paralysis.class);
                Buff.prolong(hero, Vertigo.class, 5f);
                GLog.w("The Gods are displeased with your donation!");
                GLog.i("DONATED LOOT: " + hero.donatedLoot + ", attempted to donate a cursed item."); // DSM-xxxx

                // the hero may not use this altar again during this run
                hero.donatedLoot = 0;
            } else if (hero.donatedLoot >= 300) {
                // in order to get here you either have to donate a lot of goods all at once,
                // or you have been doing a lot of donations and collecting the lower rewards
                GLog.p("The Gods are very pleased and reward you!");
                if (Random.Int(3) == 0) {
                    GLog.p("The Gods flood your mind with visions of battles past.");
                    hero.earnExp(hero.maxExp());
                    hero.donatedLoot -= 300;
                    GLog.i("DONATED LOOT: " + hero.donatedLoot + ", (-300 for level up)"); // DSM-xxxx
                } else {
                    GLog.p("You are rewarded with an ancient Artifact!");
                    Generator.random( Generator.Category.ARTIFACT ).collect();
                    hero.donatedLoot -= 300;
                    GLog.i("DONATED LOOT: " + hero.donatedLoot + ", (-300 for artifact)"); // DSM-xxxx
                }
            } else if (hero.donatedLoot >= 125) {
                if (Random.Int(3) == 0) {
                    // upgrade an item
                    Weapon wpn = (Weapon) Generator.random(Generator.Category.WEAPON);
                    try {
                        switch (Random.Int(3)) {
                            case 0:
                                wpn.enchant(Glowing.class.newInstance()).collect();
                                break;
                            case 1:
                                wpn.enchant(Ancient.class.newInstance()).collect();
                                break;
                            default:
                                wpn.enchant(Luck.class.newInstance()).collect();
                                break;
                        }
                    } catch (InstantiationException e) {
                        wpn.enchant().collect();;
                    } catch (IllegalAccessException e) {
                        wpn.enchant().collect();;
                    }
                    hero.donatedLoot -= 125;
                    GLog.i("DONATED LOOT: " + hero.donatedLoot + ", (-125 for enchanted weapon)"); // DSM-xxxx
                }
            } else if (hero.donatedLoot >= 40) {
                // some type of reward may be given to the hero, if so reduce the total donation value
                if (hero.HT < hero.HP) {
                    GLog.p("The Gods heal your wounds.");
                    PotionOfHealing.heal(hero);
                    hero.donatedLoot -= 35;
                    GLog.i("DONATED LOOT: " + hero.donatedLoot + ", (-35 for healing)"); // DSM-xxxx
                } else if ((! hero.buffs().contains(Awareness.class)) && (Random.Int(6) == 0)) {
                    GLog.p("The Gods reveal secrets of your surroundings.");
                    Buff.affect(hero, Awareness.class, Awareness.DURATION * 2);
                    Dungeon.observe();
                    hero.donatedLoot -= 30;
                    GLog.i("DONATED LOOT: " + hero.donatedLoot + ", (-30 for awareness buff)"); // DSM-xxxx
                } else if ((! hero.buffs().contains(Bless.class)) && (Random.Int(6) == 0)){
                    GLog.p("The Gods bless you.");
                    hero.belongings.uncurseEquipped();
                    Buff.affect(hero, Bless.class);
                    Buff.prolong(hero, Bless.class, 120f);
                    hero.donatedLoot -= 40;
                    GLog.i("DONATED LOOT: " + hero.donatedLoot + ", (-40 for blessing buff)"); // DSM-xxxx

                } else {
                    GLog.p("The Gods seem happy... next donation level is 125"); // DSM-xxxx
                }
            } else {
                GLog.p("The Gods seem happy... next donation level is 300"); // DSM-xxxx
            }

            heap.donate();
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(Speck.factory(Speck.LIGHT), 0.2f, 0);
    }
}