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
package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.watabou.utils.Random;

public class InfiniteBestiary {

    public enum Themes { THEME_SLIMES, THEME_UNDEAD, THEME_NATURAL, THEME_BANDITS, THEME_EARTH, THEME_AIR,
                  THEME_FIRE, THEME_WATER, THEME_SPIRIT, THEME_DEMONS, THEME_SEWERS, THEME_PRISON,
                  THEME_CAVES, THEME_CITY, THEME_HALLS
    };

    public static Themes currentTheme;

    public static void pickNewTheme() {
        // 1-5 = sewers, 6-10 = prison, 11-15 = caves, 16-20 = city, 21-25 = halls...
        int Tileset = (((Dungeon.depth -1) / 5) % 5);
        switch (Tileset) {
            case 0: // sewer levels
                switch (Random.Int(4)) {
                    case 0: currentTheme = Themes.THEME_SLIMES; break;
                    case 1: currentTheme = Themes.THEME_UNDEAD; break;
                    case 2: currentTheme = Themes.THEME_WATER; break;
                    default: currentTheme = Themes.THEME_SEWERS; break;
                }
                break;
            case 1: // prison levels
                switch (Random.Int(4)) {
                    case 0: currentTheme = Themes.THEME_UNDEAD; break;
                    case 1: currentTheme = Themes.THEME_BANDITS; break;
                    case 2: currentTheme = Themes.THEME_EARTH; break;
                    default: currentTheme = Themes.THEME_PRISON; break;
                }
                break;
            case 2: // caves levels
                switch (Random.Int(4)) {
                    case 0: currentTheme = Themes.THEME_NATURAL; break;
                    case 1: currentTheme = Themes.THEME_SLIMES; break;
                    case 2: currentTheme = Themes.THEME_AIR; break;
                    default: currentTheme = Themes.THEME_CAVES; break;
                }
                break;
            case 3: // city levels
                switch (Random.Int(4)) {
                    case 1: currentTheme = Themes.THEME_UNDEAD; break;
                    case 2: currentTheme = Themes.THEME_FIRE; break;
                    case 3: currentTheme = Themes.THEME_BANDITS; break;
                    default: currentTheme = Themes.THEME_SEWERS; break;
                }
                break;
            default: // halls levels
                switch (Random.Int(4)) {
                    case 0: currentTheme = Themes.THEME_DEMONS; break;
                    case 1: currentTheme = Themes.THEME_UNDEAD; break;
                    case 2: currentTheme = Themes.THEME_SPIRIT; break;
                    default: currentTheme = Themes.THEME_HALLS; break;
                }
                break;
        }
    }

    public static Mob mob( int depth ) {
        @SuppressWarnings("unchecked")
        Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );
        try {
            return cl.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static Mob mutable( int depth ) {
        @SuppressWarnings("unchecked")
        Class<? extends Mob> cl = (Class<? extends Mob>) mobClass(depth);

        if (Random.Int(30) == 0) {
            if (cl == Rat.class) {
                cl = Albino.class;
            } else if (cl == Thief.class) {
                cl = Bandit.class;
            } else if (cl == Brute.class) {
                cl = Shielded.class;
            } else if (cl == Monk.class) {
                cl = Senior.class;
            } else if (cl == Scorpio.class) {
                cl = Acidic.class;
            }
        }

        try {
            return cl.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    private static Class<?> mobClass( int depth ) {
        int mobChoice;
        int subDepth = depth % 5;

        if (subDepth == 1) mobChoice = Random.Int(2);
        else if (subDepth == 2) mobChoice = Random.Int(3);
        else if (subDepth == 2) mobChoice = Random.Int(3);
        else if (subDepth == 2) mobChoice = Random.Int(4);
        else mobChoice = Random.Int(4) + 1;

       Mob thisMob = null;

        if (currentTheme == null) {
            pickNewTheme();
        }

       switch (currentTheme) {
            case THEME_SLIMES:
                switch (mobChoice) {
                    case 0:  return SlimeBrown.class;
                    case 1:  return Slime.class;
                    case 2:  return GreenSnake.class;
                    case 3:  return SewerFly.class;
                    default: return SlimeRed.class;
                }
            case THEME_UNDEAD:
                switch (mobChoice) {
                    case 0:  return Zombie.class;
                    case 1:  return Skeleton.class;
                    case 2:  return Wraith.class;
                    case 3:  return Undead.class;
                    default: return LostSoul.class;
                }
            case THEME_NATURAL:
                switch (mobChoice) {
                    case 0:  return Rat.class;
                    case 1:  return GreenSnake.class;
                    case 2:  return Swarm.class;
                    case 3:  return Bat.class;
                    default: return Spinner.class;
                }
           case THEME_BANDITS:
               switch (mobChoice) {
                   case 0:  return Bandit.class;
                   case 1:  return Monk.class;
                   case 2:  return Thief.class;
                   case 3:  return Brute.class;
                   default: return Shielded.class;
               }
           case THEME_EARTH:
               switch (mobChoice) {
                   case 0:  return ClayGolem.class;
                   case 1:  return Gnoll.class;
                   case 2:  return SpiderBot.class;
                   case 3:  return Shaman.class;
                   default: return Golem.class;
               }
           case THEME_AIR:
               switch (mobChoice) {
                   case 0:  return Bat.class;
                   case 1:  return Velocirooster.class;
                   case 2:  return Swarm.class;
                   case 3:  return Wraith.class;
                   default: return Eye.class;
               }
           case THEME_FIRE:
               switch (mobChoice) {
                   case 0:  return SlimeRed.class;
                   case 1:  return Senior.class;
                   case 2:  return LostSoul.class;
                   case 3:  return Elemental.class;
                   default: return Succubus.class;
               }
           case THEME_WATER:
               switch (mobChoice) {
                   case 0:  return GreenSnake.class;
                   case 1:  return Swarm.class;
                   case 2:  return Crab.class;
                   case 3:  return Spinner.class;
                   default: return Succubus.class;
               }
           case THEME_SPIRIT:
               switch (mobChoice) {
                   case 0:  return Wraith.class;
                   case 1:  return Shaman.class;
                   case 2:  return LostSoul.class;
                   case 3:  return Warlock.class;
                   default: return Eye.class;
               }
           case THEME_DEMONS:
               switch (mobChoice) {
                   case 0:  return Zombie.class;
                   case 1:  return Skeleton.class;
                   case 2:  return Larva.class;
                   case 3:  return Succubus.class;
                   default: return Warlock.class;
               }
           case THEME_SEWERS:
               switch (mobChoice) {
                   case 0:  return Rat.class;
                   case 1:  return GreenSnake.class;
                   case 2:  return Gnoll.class;
                   case 3:  return SewerFly.class;
                   default: return Slime.class;
               }
           case THEME_PRISON:
               switch (mobChoice) {
                   case 0:  return Skeleton.class;
                   case 1:  return Shaman.class;
                   case 2:  return Spinner.class;
                   case 3:  return Assassin.class;
                   default: return Swarm.class;
               }
           case THEME_CAVES:
               switch (mobChoice) {
                   case 0:  return ClayGolem.class;
                   case 1:  return Bat.class;
                   case 2:  return Brute.class;
                   case 3:  return Shaman.class;
                   default: return Spinner.class;
               }
           case THEME_CITY:
               switch (mobChoice) {
                   case 0:  return Golem.class;
                   case 1:  return Elemental.class;
                   case 2:  return Monk.class;
                   case 3:  return Warlock.class;
                   default: return Succubus.class;
               }
           case THEME_HALLS:
           default:
               switch (mobChoice) {
                   case 0:  return Succubus.class;
                   case 1:  return Eye.class;
                   case 2:  return Scorpio.class;
                   case 3:  return LostSoul.class;
                   default: return Warlock.class;
               }
        }
    }
}