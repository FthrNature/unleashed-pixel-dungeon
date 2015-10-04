/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
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
package com.shatteredpixel.pixeldungeonunleashed.items;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Ghost;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Wandmaker.Rotberry;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.*;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.*;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.Bag;
import com.shatteredpixel.pixeldungeonunleashed.items.food.Food;
import com.shatteredpixel.pixeldungeonunleashed.items.food.MysteryMeat;
import com.shatteredpixel.pixeldungeonunleashed.items.food.Pasty;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.*;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.*;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.*;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.*;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.*;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.*;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.*;
import com.shatteredpixel.pixeldungeonunleashed.plants.*;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Generator {

	public static enum Category {
		WEAPON	( 150,	Weapon.class ),
		ARMOR	( 100,	Armor.class ),
		POTION	( 500,	Potion.class ),
		SCROLL	( 400,	Scroll.class ),
		WAND	( 65,	Wand.class ),
		RING	( 30,	Ring.class ),
		ARTIFACT( 10,   Artifact.class),
		SEED	( 0,	Plant.Seed.class ),
		FOOD	( 0,	Food.class ),
		GOLD	( 500,	Gold.class );
		
		public Class<?>[] classes;
		public float[] probs;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
	};
	
	private static HashMap<Category,Float> categoryProbs = new HashMap<Generator.Category, Float>();
	private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{ 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1};

	static {
		
		Category.GOLD.classes = new Class<?>[]{
			Gold.class };
		Category.GOLD.probs = new float[]{ 1 };
		
		Category.SCROLL.classes = new Class<?>[]{
			ScrollOfIdentify.class,       // 26
			ScrollOfRemoveCurse.class,    // 15
			ScrollOfRecharging.class,     // 15
			ScrollOfMagicMapping.class,   // 15
			ScrollOfRage.class,           // 12
			ScrollOfMirrorImage.class,    // 10
			ScrollOfTeleportation.class,  // 10
			ScrollOfTerror.class,         //  8
			ScrollOfLullaby.class,        //  8
			ScrollOfPsionicBlast.class,   //  4
			ScrollOfUpgrade.class,        //  0 - uses special drop table
			ScrollOfMagicalInfusion.class //  0 - need special circumstances to get
		};
		Category.SCROLL.probs = new float[]{ 26, 15, 15, 15, 12, 10, 10, 8, 8, 4, 0, 0 };
		
		Category.POTION.classes = new Class<?>[]{
			PotionOfHealing.class,        // 40
			PotionOfMindVision.class,     // 20
			PotionOfToxicGas.class,       // 15
			PotionOfLiquidFlame.class,    // 15
			PotionOfPurity.class,         // 12
			PotionOfParalyticGas.class,   // 10
			PotionOfLevitation.class,     // 10
			PotionOfInvisibility.class,   // 10
			PotionOfFrost.class,          // 10
			PotionOfSlowness.class,       // 10
			PotionOfSpeed.class,          // 10
			PotionOfExperience.class,     //  4
			PotionOfStrength.class,       //  0 - uses special drop table
			PotionOfMight.class           //  0 - need special circumstances to get
		};
		Category.POTION.probs = new float[]{ 40, 20, 15, 15, 12, 10, 10, 10, 10, 10, 10, 4, 0, 0 };

		//TODO: add last ones when implemented
		Category.WAND.classes = new Class<?>[]{
			WandOfMagicMissile.class,
			WandOfLightning.class,
			WandOfDisintegration.class,
			WandOfFireblast.class,
			WandOfVenom.class,
			WandOfBlastWave.class,
			WandOfFrost.class,
			WandOfPrismaticLight.class,
			WandOfTransfusion.class,
			WandOfCorruption.class,
			WandOfRegrowth.class };
		Category.WAND.probs = new float[]{ 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3 };
		
		Category.WEAPON.classes = new Class<?>[]{
			Dagger.class,
			Knuckles.class,
			Quarterstaff.class,
			Spear.class,
			Mace.class,
			Sword.class,
			Longsword.class,
			BattleAxe.class,
			WarHammer.class,
			Glaive.class,
			ShortSword.class,     // set to 0 - warrior-only weapon
			Dart.class,           // set to 0 - special drop in high-grass
			Javelin.class,
			IncendiaryDart.class,
			CurareDart.class,
			Shuriken.class,
			Boomerang.class,      // set to 0 - huntress-only weapon
			Tamahawk.class };
		Category.WEAPON.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1 };
		
		Category.ARMOR.classes = new Class<?>[]{
			ClothArmor.class,
			LeatherArmor.class,
			MailArmor.class,
			ScaleArmor.class,
			PlateArmor.class };
		Category.ARMOR.probs = new float[]{ 1, 1, 1, 1, 1 };
		
		Category.FOOD.classes = new Class<?>[]{
			Food.class,
			Pasty.class,
			MysteryMeat.class };   // set to 0 - special drop
		Category.FOOD.probs = new float[]{ 4, 1, 0 };
			
		Category.RING.classes = new Class<?>[]{
			RingOfAccuracy.class,
			RingOfEvasion.class,
			RingOfElements.class,
			RingOfForce.class,
			RingOfFuror.class,
			RingOfHaste.class,
			RingOfMagic.class,    // DSM-xxxx test out putting this one back in...
			RingOfMight.class,
			RingOfSharpshooting.class,
			RingOfTenacity.class,
			RingOfWealth.class,
		    RingOfSating.class,
			RingOfSearching.class
		};
		Category.RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		Category.ARTIFACT.classes = new Class<?>[]{
			CapeOfThorns.class,         // set to 0 - special drop
			ChaliceOfBlood.class,
			CloakOfShadows.class,       // set to 0 - rogue-only item
			HornOfPlenty.class,
			MasterThievesArmband.class, // set to 0 - special drop
			SandalsOfNature.class,
			TalismanOfForesight.class,
			TimekeepersHourglass.class,
			UnstableSpellbook.class,
			AlchemistsToolkit.class,
			DriedRose.class, //starts with no chance of spawning, chance is set directly after beating ghost quest.
			LloydsBeacon.class,      // set to 0 - special drop
			EtherealChains.class,
			//BagOfDevouring.class
			};
		// note - duplicated down below in initArtifacts() - look into refactoring
		Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS;

		Category.SEED.classes = new Class<?>[]{
			Firebloom.Seed.class,
			Icecap.Seed.class,
			Sorrowmoss.Seed.class,
			Blindweed.Seed.class,
			Sungrass.Seed.class,
			Earthroot.Seed.class,
			Fadeleaf.Seed.class,
			Rotberry.Seed.class,       // set to 0 - quest item drop
			BlandfruitBush.Seed.class,
			Dreamfoil.Seed.class,
			Stormvine.Seed.class,
			Starflower.Seed.class};
		Category.SEED.probs = new float[]{ 12, 12, 12, 12, 12, 12, 12, 0, 4, 12, 12, 1 };
	}
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}
	
	public static Item random() {
		return random( Random.chances( categoryProbs ) );
	}
	
	public static Item random( Category cat ) {
		try {
			
			categoryProbs.put( cat, categoryProbs.get( cat ) / 2 );
			
			switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				return ((Item)cat.classes[Random.chances( cat.probs )].newInstance()).random();
			}
			
		} catch (Exception e) {

			return null;
			
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			
			return ((Item)cl.newInstance()).random();
			
		} catch (Exception e) {

			return null;
			
		}
	}

	public static Armor randomArmor(){
		int curStr = Hero.STARTING_STR + Dungeon.limitedDrops.strengthPotions.count;

		return randomArmor(curStr);
	}
	
	public static Armor randomArmor(int targetStr) {
		
		Category cat = Category.ARMOR;

		try {
			Armor a1 = (Armor) cat.classes[Random.chances(cat.probs)].newInstance();
			Armor a2 = (Armor) cat.classes[Random.chances(cat.probs)].newInstance();

			a1.random();
			a2.random();

			return Math.abs(targetStr - a1.STR) < Math.abs(targetStr - a2.STR) ? a1 : a2;
		} catch (Exception e) {
			return null;
		}
	}

	public static Weapon randomWeapon(){
		int curStr = Hero.STARTING_STR + Dungeon.limitedDrops.strengthPotions.count;

		return randomWeapon(curStr);
	}
	
	public static Weapon randomWeapon(int targetStr) {

		try {
			Category cat = Category.WEAPON;

			Weapon w1 = (Weapon)cat.classes[Random.chances( cat.probs )].newInstance();
			Weapon w2 = (Weapon)cat.classes[Random.chances( cat.probs )].newInstance();

			w1.random();
			w2.random();

			return Math.abs( targetStr - w1.STR ) < Math.abs( targetStr - w2.STR ) ? w1 : w2;
		} catch (Exception e) {
			return null;
		}
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		try {
			Category cat = Category.ARTIFACT;
			int i = Random.chances( cat.probs );

			//if no artifacts are left, return null
			if (i == -1){
				return null;
			}

			// after creation, ensure the spawn rate for this entry is 0 and that we added the name to the load/save drop list
			Artifact artifact = (Artifact)cat.classes[i].newInstance();
			if ((cat.probs[i] > 0) && (!spawnedArtifacts.contains(cat.classes[i].getSimpleName()))) {
				//remove the chance of spawning this artifact.
				cat.probs[i] = 0;
				spawnedArtifacts.add(cat.classes[i].getSimpleName());
			}

			artifact.random(); // is this artifact cursed?

			return artifact;

		} catch (Exception e) {
			return null;
		}
	}

	public static boolean removeArtifact(Artifact artifact) {
		if (spawnedArtifacts.contains(artifact.getClass().getSimpleName()))
			return false;

		Category cat = Category.ARTIFACT;
		// find our artifact in the array, ensure the spawn rate for this entry is 0 and that we added the name to the load/save drop list
		for (int i = 0; i < cat.classes.length; i++)
			if ((cat.classes[i].equals(artifact.getClass())) && (!spawnedArtifacts.contains(cat.classes[i].getSimpleName()))) {
				if (cat.probs[i] > 0){
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifact.getClass().getSimpleName());
					return true;
				} else
					return false;
			}

		return false;
	}

	//resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		// reset our drop list since we will be recreating it in restoreFromBundle()
		// this prevents possible issues when reloading a saved game
		spawnedArtifacts.clear();
		Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS;

		//checks for dried rose quest completion, adds the rose in accordingly.
		if (Ghost.Quest.processed) Category.ARTIFACT.probs[10] = 1;
	}

	private static ArrayList<String> spawnedArtifacts = new ArrayList<String>();

	private static final String ARTIFACTS = "artifacts";

	//used to store information on which artifacts have been spawned.
	public static void storeInBundle(Bundle bundle) {
		bundle.put( ARTIFACTS, spawnedArtifacts.toArray(new String[spawnedArtifacts.size()]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		initArtifacts();

		if (bundle.contains(ARTIFACTS)) {
			Category cat = Category.ARTIFACT;

			for (String artifact : spawnedArtifacts)
				for (int i = 0; i < cat.classes.length; i++)
					if ((cat.classes[i].getSimpleName().equals(artifact)) && (cat.probs[i] > 0)) {
						cat.probs[i] = 0;
						spawnedArtifacts.add(artifact);
					}
		}
	}
}
