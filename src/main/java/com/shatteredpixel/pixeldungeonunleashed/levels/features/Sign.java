/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.pixeldungeonunleashed.levels.features;

import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.InfiniteBestiary;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.effects.CellEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.ElmoParticle;
import com.shatteredpixel.pixeldungeonunleashed.levels.DeadEndLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndMessage;

public class Sign {

	private static final String TXT_DEAD_END = "What are you doing here?!";
	private static final String TXT_ENDLESS  = "Level %d\n%s";

	private static final String[] TIPS = {
		"Almost all equipment has a strength requirement. Don't overestimate your strength, using equipment you can't " +
				"handle has big penalties!\n\nRaising your strength is not the only way to access better equipment, " +
				"you can also lower equipment strength requirements with Scrolls of Upgrade.\n\n\n" +
				"Items found in the dungeon will often be unidentified. Some items will have unknown effects, others " +
				"may be upgraded, or degraded and cursed! Unidentified items are unpredictable, so be careful!",
		"Charging forward recklessly is a great way to get killed.\n\n" +
				"Slowing down a bit to examine enemies and use the environment and items to your advantage can make a" +
				" big difference.\n\nThe dungeon is full of traps and hidden passageways as well, keep your eyes open!",
		"Your game auto-saves when you leave, but you can also Save your game into a save-slot when you are adjacent" +
			    " to one of these signs!\n\nYou can reload a saved game at any point through the action menu!",
		"Levelling up is important!\n\nBeing about the same level as the floor you are on is a good idea. " +
				"Hunger may keep you moving in search of more food, but don't be afraid to slow down a little and train." +
				"\n\n\nHunger and health are both resources, and using them well can mean starving yourself in order" +
				" to help conserve food, if you have some health to spare.",
		"When you level up you regain some of your health and sate some of your hunger.  Consider it a reward for" +
				" levelling!",

		"Note to all sewer maintenance & cleaning crews: TURN BACK NOW. Some sort of sludge monster has made its home" +
				" here and several crews have been lost trying to deal with it.\n\n" +
				"Approval has been given to seal off the lower sewers, this area has been condemned, LEAVE NOW.",

		"Pixel-Mart - all you need for successful adventure!",
		"The rogue isn't the only character that benefits from being sneaky. You can retreat to the other side of a " +
				"door to ambush a chasing opponent for a guaranteed hit!" +
				"\n\nAny attack on an unaware opponent is guaranteed to hit them.",
		"Identify your potions and scrolls as soon as possible. Don't put it off to the moment " +
				"when you actually need them.",
		"Being hungry doesn't hurt, but starving does hurt.",
		"Surprise attack has a better chance to hit. For example, you can ambush your enemy behind " +
				"a closed door when you know it is approaching.",

		"Don't let The Tengu out!",

		"Pixel-Mart. Spend money. Live longer.",
		"When you're attacked by several monsters at the same time, try to retreat behind a door.",
		"If you are burning, you can't put out the fire in the water while levitating.",
		"There is no sense in possessing more than one unblessed Ankh at the same time, " +
				"because you will lose them upon resurrecting.",
		"The more powerful the item the easier it is to be upgraded.\n\nThere is also a maximum" +
			    " enchantment bonus items can receive.  As you get closer to the maximum bonus" +
			    " the chance of failure increases.",

		"DANGER! Heavy machinery can cause injury, loss of limbs or death!",

		"Pixel-Mart. A safer life in dungeon.",
		"When you upgrade an enchanted weapon, there is a chance to destroy that enchantment.",
		"In a Well of Transmutation you can get an item, that cannot be obtained otherwise.",
		"The only way to enchant a weapon is by upgrading it with a Scroll of Magical Infusion.",
		"The minotaur likes to hide around corners, try luring him into the open.",

		"No weapons allowed in the presence of His Majesty!",
		"Beware, wolves tend to travel in packs.",
		"Deep dwellers have a special attack",
		"Most Badges can't be earned in the Easy difficulty level.",
		"Have you ever thrown a potion at a mob?",
		"Sign writers wanted, please apply at the Yog level!",
		"The Yog moved downstairs...",

		"Pixel-Mart. Special prices for demon hunters!",

		//hmm.. I wonder what this is?
		"Gur haqrnq znfgre jvyy evfr fbba",
		"N qrzba funyy pbzr gb grfg lbh",
		"Gur ebbz funyy snyy hcba lbhe urnq",
		"Gur qbbef gurzfryirf funyy qrprvir lbh"
	};
	
	private static final String TXT_BURN =
		"As you try to read the sign it bursts into greenish flames.";
	
	public static void read( int pos ) {
		if (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS) {
			GameScene.show( new WndMessage( Utils.format(TXT_ENDLESS, Dungeon.depth, InfiniteBestiary.currentTheme)) );
			return;
		}
		
		if (Dungeon.level instanceof DeadEndLevel) {
			
			GameScene.show( new WndMessage( TXT_DEAD_END ) );
			
		} else {
			
			int index = Dungeon.depth - 1;

			if (index < TIPS.length) {
				GameScene.show( new WndMessage( TIPS[index] ) );

				if (index >= 31) {

					Level.set( pos, Terrain.EMBERS );
					GameScene.updateMap( pos );
					GameScene.discoverTile( pos, Terrain.SIGN );

					GLog.w( TXT_BURN );

					CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
					Sample.INSTANCE.play(Assets.SND_BURNING);
				}

			}
		}
	}
}
