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
package com.shatteredpixel.pixeldungeonunleashed;

import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Amok;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Light;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.HeroClass;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Ghost;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Imp;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.pixeldungeonunleashed.items.Ankh;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.Potion;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.Ring;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.Scroll;
import com.shatteredpixel.pixeldungeonunleashed.levels.CavesBossLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.CavesLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.CityBossLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.CityLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.DeadEndLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.HallsBossLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.HallsLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.LastLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.LastShopLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.OpenLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.PrisonBossLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.PrisonLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.Room;
import com.shatteredpixel.pixeldungeonunleashed.levels.SewerBossLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.SewerLevel;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.scenes.StartScene;
import com.shatteredpixel.pixeldungeonunleashed.ui.QuickSlotButton;
import com.shatteredpixel.pixeldungeonunleashed.utils.BArray;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Dungeon {

	public static int transmutation;	// depth number for a well of transmutation
	public static int altarLevel;       // depth number for an altar
	public static int difficultyLevel = ShatteredPixelDungeon.getDifficulty();

	public final static int DIFF_TUTOR  = 10;
	public final static int DIFF_EASY   = 11;
	public final static int DIFF_NORM   = 12;
	public final static int DIFF_HARD   = 13;
	public final static int DIFF_NTMARE = 14;

	//enum of items which have limited spawns, records how many have spawned
	//could all be their own separate numbers, but this allows iterating, much nicer for bundling/initializing.
	public enum limitedDrops{
		//limited world drops
		strengthPotions,
		upgradeScrolls,
		arcaneStyli,

		//all unlimited health potion sources
		swarmHP,
		batHP,
		warlockHP,
		scorpioHP,
		cookingHP,
		//blandfruit, which can technically be an unlimited health potion source
		blandfruitSeed,

		//doesn't use Generator, so we have to enforce one armband drop here
		armband,

		//containers
		dewVial,
		seedBag,
		scrollBag,
		potionBag,
		wandBag;

		public int count = 0;

		//for items which can only be dropped once, should directly access count otherwise.
		public boolean dropped(){
			return count != 0;
		}
		public void drop(){
			count = 1;
		}
	}

	public static int challenges;

	public static Hero hero;
	public static Level level;

	public static QuickSlot quickslot = new QuickSlot();
	
	public static int depth = 1;
	public static int gold = 0;
	// Reason of death
	public static String resultDescription;
	
	public static HashSet<Integer> chapters;
	
	// Hero's field of view
	public static boolean[] visible = new boolean[Level.LENGTH];

	public static SparseArray<ArrayList<Item>> droppedItems;

	public static int version;

	// these following variables are for displaying tutorial-mode messages to the player
	// I don't want to store these variables in the bundle, because if they leave the
	// game and come back into a tutorial level game I want to repeat some of these to
	// the player to ensure they remember them.  There is probably a better way to do
	// this, but I am drawing a blank right now.
	public static boolean tutorial_mob_seen = (depth != 1);
	public static boolean tutorial_tactics_tip = (depth != 1);
	public static boolean tutorial_food_found = (depth != 1);
	public static boolean tutorial_sign_seen = (depth != 1);
	public static boolean tutorial_key_found = (depth != 1);
	public static boolean tutorial_altar_seen = (depth > 7);
	public static boolean tutorial_wellA_seen = (depth > 7);
	public static boolean tutorial_wellT_seen = (depth > 7);
	public static boolean tutorial_wellH_seen = (depth > 7);
	public static boolean tutorial_boss_found = (depth > 7);
	public static boolean tutorial_garden_found = (depth > 7);

	public static void init() {
		difficultyLevel = ShatteredPixelDungeon.getDifficulty();

		version = Game.versionCode;
		challenges = ShatteredPixelDungeon.challenges();

		Generator.initArtifacts();

		Actor.clear();
		Actor.resetNextID();
		
		PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );
		
		Scroll.initLabels();
		Potion.initColors();
		Ring.initGems();
		
		Statistics.reset();
		Journal.reset();

		quickslot.reset();
		QuickSlotButton.reset();
		
		depth = 0;
		gold = 0;

		if (difficultyLevel == DIFF_TUTOR) {
			tutorial_mob_seen = false;
			tutorial_tactics_tip = false;
			tutorial_food_found = false;
			tutorial_sign_seen = false;
			tutorial_key_found = false;
			tutorial_altar_seen = false;
			tutorial_wellA_seen = false;
			tutorial_wellT_seen = false;
			tutorial_wellH_seen = false;
			tutorial_boss_found = false;
			tutorial_garden_found = false;
		}

		droppedItems = new SparseArray<>();

		for (limitedDrops a : limitedDrops.values())
			a.count = 0;

		switch (difficultyLevel) {
			case DIFF_TUTOR:
			case DIFF_EASY:
				transmutation = Random.IntRange( 4, 10 );
				altarLevel = Random.IntRange(1, 3);
				break;
			case DIFF_HARD:
				transmutation = Random.IntRange( 6, 14 );
				altarLevel = Random.IntRange(3, 5);
				break;
			case DIFF_NTMARE:
				transmutation = Random.IntRange( 6, 15 );
				altarLevel = Random.IntRange(3, 6);
				break;
			case DIFF_NORM:
			default:
				transmutation = Random.IntRange( 6, 14 );
				altarLevel = Random.IntRange(2, 4);
				break;
		}

		chapters = new HashSet<>();
		
		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();
		
		Room.shuffleTypes();
		
		hero = new Hero();
		hero.live();
		
		Badges.reset();

		StartScene.curClass.initHero( hero );
	}

	public static boolean isChallenged( int mask ) {
		return (challenges & mask) != 0;
	}
	
	public static Level newLevel() {
		Dungeon.level = null;
		Actor.clear();
		
		depth++;
		if (depth > Statistics.deepestFloor) {
			Statistics.deepestFloor = depth;
			Statistics.completedWithNoKilling = Statistics.qualifiedForNoKilling;
		}
		
		Arrays.fill( visible, false );
		
		Level level;
		switch (depth) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			if (specialLevel(depth)) { // level 5 is gauranteed to be an Open Level
				level = new OpenLevel();
			} else {
				level = new SewerLevel();
			}
			break;
		case 6:
			level = new SewerBossLevel();
			break;
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
			if (specialLevel(depth) && Random.Int(3) == 0) {
				level = new OpenLevel();
			} else {
				level = new PrisonLevel();
			}
			break;
		case 12:
			level = new PrisonBossLevel();
			break;
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
			if (specialLevel(depth) && Random.Int(3) == 0) {
				level = new OpenLevel();
			} else {
				level = new CavesLevel();
			}
			break;
		case 18:
			level = new CavesBossLevel();
			break;
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
			if (specialLevel(depth) && Random.Int(3) == 0) {
				level = new OpenLevel();
			} else {
				level = new CityLevel();
			}
			break;
		case 24:
			level = new CityBossLevel();
			break;
		case 25:
			level = new LastShopLevel();
			break;
		case 26:
		case 27:
		case 28:
		case 29:
			if (specialLevel(depth) && Random.Int(3) == 0) {
				level = new OpenLevel();
			} else {
				level = new HallsLevel();
			}
			break;
		case 30:
			level = new HallsBossLevel();
			break;
		case Level.MAX_DEPTH:
			level = new LastLevel();
			break;
		default:
			level = new DeadEndLevel();
			Statistics.deepestFloor--;
		}
		
		level.create();
		
		Statistics.qualifiedForNoKilling = !bossLevel();
		
		return level;
	}
	
	public static void resetLevel() {
		Actor.clear();
		
		Arrays.fill(visible, false);
		
		level.reset();
		switchLevel( level, level.entrance );
	}
	
	public static boolean shopOnLevel() {
		return depth == 7 || depth == 13 || depth == 19;
	}
	
	public static boolean bossLevel() {
		return bossLevel( depth );
	}
	
	public static boolean bossLevel( int depth ) {
		// this level has a big boss fight on it
		return depth == 6 || depth == 12 || depth == 18 || depth == 24 || depth == 30;
	}

	public static boolean specialLevel( int depth ) {
		// this level is allowed to have a special layout
		return depth == 5 || depth == 10 || depth == 11 || depth == 15 || depth == 21 || depth == 27;
	}
	
	@SuppressWarnings("deprecation")
	public static void switchLevel( final Level level, int pos ) {
		Dungeon.level = level;
		Actor.init();
		
		Actor respawner = level.respawner();
		if (respawner != null) {
			Actor.add( level.respawner() );
		}

		hero.pos = pos != -1 ? pos : level.exit;
		
		Light light = hero.buff( Light.class );
		hero.viewDistance = light == null ? level.viewDistance : Math.max( Light.DISTANCE, level.viewDistance );
		observe();
		try {
			saveAll();
		} catch (IOException e) {
			/*This only catches IO errors. Yes, this means things can go wrong, and they can go wrong catastrophically.
			But when they do the user will get a nice 'report this issue' dialogue, and I can fix the bug.*/
		}
	}

	public static void dropToChasm( Item item ) {
		int depth = Dungeon.depth + 1;
		ArrayList<Item> dropped = Dungeon.droppedItems.get( depth );
		if (dropped == null) {
			Dungeon.droppedItems.put( depth, dropped = new ArrayList<>() );
		}
		dropped.add( item );
	}

	// these next few functions help to stagger when needed items drop, for instance in the following array:
	//    { 4, 2,   9, 4,   14, 6,   19, 8,   24, 9 } you should have 2 drops by level 4, 4 by level 9, 6 by 14...
    public static boolean posNeeded() {
		// adjusted slightly to account for larger dungeon size... still caps out at 9 upgrades by the end
		int[] quota = {5, 2,   11, 4,   17, 6,   23, 7,    29, 8};
		return chance( quota, limitedDrops.strengthPotions.count );
	}
	
	public static boolean souNeeded() {
		// adjusted slightly to account for larger dungeon, level caps and upgrade failures
		int[] quota = {6, 4,   12, 8,   18, 13,   24, 16,   29, 18};
		return chance( quota, limitedDrops.upgradeScrolls.count );
	}
	
	private static boolean chance( int[] quota, int number ) {
		
		for (int i=0; i < quota.length; i += 2) {
			int qDepth = quota[i];
			if (depth <= qDepth) {
				int qNumber = quota[i + 1];
				return Random.Float() < (float)(qNumber - number) / (qDepth - depth + 1);
			}
		}
		
		return false;
	}

	// guaranteed to drop at least one Arcane Styluses within your game
	public static boolean asNeeded() {
		return Random.Int( 12 * (1 + limitedDrops.arcaneStyli.count) ) < depth;
	}
	
	private static final String RG_GAME_FILE	= "rogue.dat";
	private static final String RG_DEPTH_FILE	= "rogue%d.dat";
	
	private static final String WR_GAME_FILE	= "warrior.dat";
	private static final String WR_DEPTH_FILE	= "warrior%d.dat";
	
	private static final String MG_GAME_FILE	= "mage.dat";
	private static final String MG_DEPTH_FILE	= "mage%d.dat";
	
	private static final String RN_GAME_FILE	= "huntress.dat";
	private static final String RN_DEPTH_FILE	= "huntress%d.dat";
	
	private static final String VERSION		= "version";
	private static final String CHALLENGES	= "challenges";
	private static final String DIFLEV      = "diflev";
	private static final String HERO		= "hero";
	private static final String GOLD		= "gold";
	private static final String DEPTH		= "depth";
	private static final String DROPPED     = "dropped%d";
	private static final String LEVEL		= "level";
	private static final String LIMDROPS    = "limiteddrops";
	private static final String DV			= "dewVial";
	private static final String WT			= "transmutation";
	private static final String ATLEV       = "altarLevel";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";

	public static String gameFile( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			return WR_GAME_FILE;
		case MAGE:
			return MG_GAME_FILE;
		case HUNTRESS:
			return RN_GAME_FILE;
		default:
			return RG_GAME_FILE;
		}
	}
	
	private static String depthFile( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			return WR_DEPTH_FILE;
		case MAGE:
			return MG_DEPTH_FILE;
		case HUNTRESS:
			return RN_DEPTH_FILE;
		default:
			return RG_DEPTH_FILE;
		}
	}
	
	public static void saveGame( String fileName ) throws IOException {
		try {
			Bundle bundle = new Bundle();

			version = Game.versionCode;
			bundle.put( VERSION, version );
			bundle.put( CHALLENGES, challenges );
			bundle.put( DIFLEV, difficultyLevel );
			bundle.put( HERO, hero );
			bundle.put( GOLD, gold );
			bundle.put(DEPTH, depth);

			for (int d : droppedItems.keyArray()) {
				bundle.put(String.format(DROPPED, d), droppedItems.get(d));
			}

			quickslot.storePlaceholders( bundle );

			bundle.put( WT, transmutation );
			bundle.put( ATLEV, altarLevel );

			int[] dropValues = new int[limitedDrops.values().length];
			for (limitedDrops value : limitedDrops.values())
				dropValues[value.ordinal()] = value.count;
			bundle.put ( LIMDROPS, dropValues );
			
			int count = 0;
			int ids[] = new int[chapters.size()];
			for (Integer id : chapters) {
				ids[count++] = id;
			}
			bundle.put(CHAPTERS, ids);
			
			Bundle quests = new Bundle();
			Ghost		.Quest.storeInBundle(quests);
			Wandmaker	.Quest.storeInBundle(quests);
			Blacksmith	.Quest.storeInBundle(quests);
			Imp			.Quest.storeInBundle(quests);
			bundle.put(QUESTS, quests);
			
			Room.storeRoomsInBundle(bundle);
			
			Statistics.storeInBundle(bundle);
			Journal.storeInBundle(bundle);
			Generator.storeInBundle(bundle);
			
			Scroll.save(bundle);
			Potion.save(bundle);
			Ring.save(bundle);

			Actor.storeNextID(bundle);
			
			Bundle badges = new Bundle();
			Badges.saveLocal( badges );
			bundle.put(BADGES, badges);
			
			OutputStream output = Game.instance.openFileOutput( fileName, Game.MODE_PRIVATE );
			Bundle.write(bundle, output);
			output.close();
			
		} catch (IOException e) {
			GamesInProgress.setUnknown( hero.heroClass );
		}
	}
	
	public static void saveLevel() throws IOException {
		Bundle bundle = new Bundle();
		bundle.put(LEVEL, level);
		
		OutputStream output = Game.instance.openFileOutput(
			Utils.format( depthFile( hero.heroClass ), depth ), Game.MODE_PRIVATE );
		Bundle.write( bundle, output );
		output.close();
	}
	
	public static void saveAll() throws IOException {
		if (hero.isAlive()) {
			
			Actor.fixTime();
			saveGame( gameFile( hero.heroClass ) );
			saveLevel();

			GamesInProgress.set( hero.heroClass, depth, hero.lvl, challenges != 0, difficultyLevel );

		} else if (WndResurrect.instance != null) {
			
			WndResurrect.instance.hide();
			Hero.reallyDie( WndResurrect.causeOfDeath );
			
		}
	}
	
	public static void loadGame( HeroClass cl ) throws IOException {
		loadGame(gameFile(cl), true);
	}

	public static void loadGame( String fileName ) throws IOException {
		loadGame(fileName, false);
	}
	
	public static void loadGame( String fileName, boolean fullLoad ) throws IOException {
		try {
			GLog.i("Attempting to load " + fileName);
			Bundle bundle = gameBundle(fileName);

			version = bundle.getInt(VERSION);

			Generator.reset();

			Actor.restoreNextID(bundle);

			quickslot.reset();
			QuickSlotButton.reset();

			Dungeon.challenges = bundle.getInt(CHALLENGES);
			Dungeon.difficultyLevel = bundle.getInt(DIFLEV, 12);
			ShatteredPixelDungeon.setDifficulty(Dungeon.difficultyLevel );

			Dungeon.level = null;
			Dungeon.depth = -1;

			if (fullLoad) {
				PathFinder.setMapSize(Level.WIDTH, Level.HEIGHT);
			}

			Scroll.restore(bundle);
			Potion.restore(bundle);
			Ring.restore(bundle);

			quickslot.restorePlaceholders(bundle);

			if (fullLoad) {
				transmutation = bundle.getInt(WT);
				altarLevel = bundle.getInt(ATLEV);

				int[] dropValues = bundle.getIntArray(LIMDROPS);
				for (limitedDrops value : limitedDrops.values())
					value.count = value.ordinal() < dropValues.length ? dropValues[value.ordinal()] : 0;
				if (bundle.getBoolean(DV)) limitedDrops.dewVial.drop();

				chapters = new HashSet<>();
				int ids[] = bundle.getIntArray(CHAPTERS);
				if (ids != null) {
					for (int id : ids) {
						chapters.add(id);
					}
				}

				Bundle quests = bundle.getBundle(QUESTS);
				if (!quests.isNull()) {
					Ghost.Quest.restoreFromBundle(quests);
					Wandmaker.Quest.restoreFromBundle(quests);
					Blacksmith.Quest.restoreFromBundle(quests);
					Imp.Quest.restoreFromBundle(quests);
				} else {
					Ghost.Quest.reset();
					Wandmaker.Quest.reset();
					Blacksmith.Quest.reset();
					Imp.Quest.reset();
				}

				Room.restoreRoomsFromBundle(bundle);
			}

			Bundle badges = bundle.getBundle(BADGES);
			if (!badges.isNull()) {
				Badges.loadLocal(badges);
			} else {
				Badges.reset();
			}

			hero = null;
			hero = (Hero) bundle.get(HERO);

			gold = bundle.getInt(GOLD);
			depth = bundle.getInt(DEPTH);

			Statistics.restoreFromBundle(bundle);
			Journal.restoreFromBundle(bundle);
			Generator.restoreFromBundle(bundle);

			droppedItems = new SparseArray<>();
			for (int i = 2; i <= Statistics.deepestFloor + 1; i++) {
				ArrayList<Item> dropped = new ArrayList<>();
				for (Bundlable b : bundle.getCollection(String.format(DROPPED, i))) {
					dropped.add((Item) b);
				}
				if (!dropped.isEmpty()) {
					droppedItems.put(i, dropped);
				}
			}
			if (fullLoad) {
				if (difficultyLevel == DIFF_TUTOR) {
					tutorial_mob_seen = (depth != 1);
					tutorial_tactics_tip = (depth != 1);
					tutorial_food_found = (depth != 1);
					tutorial_sign_seen = (depth != 1);
					tutorial_key_found = (depth != 1);
					tutorial_altar_seen = (depth > 7);
					tutorial_wellA_seen = (depth > 7);
					tutorial_wellT_seen = (depth > 7);
					tutorial_wellH_seen = (depth > 7);
					tutorial_boss_found = (depth > 7);
					tutorial_garden_found = (depth > 7);
				}
			}
		}
		catch (IOException ex) {
			GLog.i("Save File corrupt...\n\nthe gremlins have won this round!");
		}
	}
	
	public static Level loadLevel( HeroClass cl ) throws IOException {
		Dungeon.level = null;
		Actor.clear();
		
		InputStream input = Game.instance.openFileInput( Utils.format( depthFile( cl ), depth ) ) ;
		Bundle bundle = Bundle.read( input );
		input.close();
		
		return (Level)bundle.get( "level" );
	}
	
	public static void deleteGame( HeroClass cl, boolean deleteLevels ) {
		Game.instance.deleteFile( gameFile( cl ) );
		
		if (deleteLevels) {
			int depth = 1;
			while (Game.instance.deleteFile( Utils.format( depthFile( cl ), depth ) )) {
				depth++;
			}
		}
		
		GamesInProgress.delete( cl );
	}
	
	public static Bundle gameBundle( String fileName ) throws IOException {
		InputStream input = Game.instance.openFileInput( fileName );
		Bundle bundle = Bundle.read( input );
		input.close();
		
		return bundle;
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.depth = bundle.getInt( DEPTH );
		info.challenges = (bundle.getInt( CHALLENGES ) != 0);
		if (info.depth == -1) {
			info.depth = bundle.getInt( "maxDepth" );	// FIXME
		}
		info.difLev = bundle.getInt( "diflev", 12 ); // defaults to NORMAL mode

		Hero.preview( info, bundle.getBundle( HERO ) );
	}
	
	public static void fail( String desc ) {
		resultDescription = desc;
		if (hero.belongings.getItem( Ankh.class ) == null) {
			Rankings.INSTANCE.submit( false );
		}
	}
	
	public static void win( String desc ) {

		hero.belongings.identify();

		if (challenges != 0) {
			Badges.validateChampion();
		}

		resultDescription = desc;
		Rankings.INSTANCE.submit( true );
	}
	
	public static void observe() {

		if (level == null) {
			return;
		}
		
		level.updateFieldOfView( hero );
		System.arraycopy( Level.fieldOfView, 0, visible, 0, visible.length );
		
		BArray.or( level.visited, visible, level.visited );
		
		GameScene.afterObserve();
	}
	
	private static boolean[] passable = new boolean[Level.LENGTH];
	
	public static int findPath( Char ch, int from, int to, boolean pass[], boolean[] visible ) {
		
		if (Level.adjacent( from, to )) {
			return Actor.findChar( to ) == null && (pass[to] || Level.avoid[to]) ? to : -1;
		}
		
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
		}
		
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char)actor).pos;
				if (visible[pos]) {
					passable[pos] = false;
				}
			}
		}
		
		return PathFinder.getStep( from, to, passable );
		
	}
	
	public static int flee( Char ch, int cur, int from, boolean pass[], boolean[] visible ) {
		
		if (ch.flying) {
			BArray.or( pass, Level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
		}
		
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char)actor).pos;
				if (visible[pos]) {
					passable[pos] = false;
				}
			}
		}
		passable[cur] = true;
		
		return PathFinder.getStepBack( cur, from, passable );
		
	}

}
