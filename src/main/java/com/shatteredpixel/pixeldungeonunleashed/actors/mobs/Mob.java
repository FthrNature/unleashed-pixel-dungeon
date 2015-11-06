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
package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Challenges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.Statistics;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Amok;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Corruption;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Sleep;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.HeroClass;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.HeroSubClass;
import com.shatteredpixel.pixeldungeonunleashed.effects.Surprise;
import com.shatteredpixel.pixeldungeonunleashed.effects.Wound;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfAccuracy;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfWealth;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level.Feeling;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndMessage;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public abstract class Mob extends Char {
	// these flags tell the infinite mode how to scale the mobs
	public final int MOBTYPE_NORMAL = 0;
	public final int MOBTYPE_NIMBLE = 1;
	public final int MOBTYPE_TOUGH  = 2;
	public final int MOBTYPE_RANGED = 3;
	public final int MOBTYPE_DEBUFF = 4;
	public final int MOBTYPE_SPECIAL = 5;

	{
		actPriority = 2; //hero gets priority over mobs.
	}
	
	private static final String	TXT_DIED	= "You hear something died in the distance";
	
	protected static final String TXT_NOTICE1	= "?!";
	protected static final String TXT_RAGE		= "#$%^";
	protected static final String TXT_EXP		= "%+dEXP";

	public AiState SLEEPING     = new Sleeping();
	public AiState HUNTING		= new Hunting();
	public AiState WANDERING	= new Wandering();
	public AiState FLEEING		= new Fleeing();
	public AiState PASSIVE		= new Passive();
	public AiState state = SLEEPING;
	
	public Class<? extends CharSprite> spriteClass;
	
	protected int target = -1;

	public int defenseSkill = 0;
	public int atkSkill = 1;
	public int dmgRed = 0;
	public int dmgMin = 1;
	public int dmgMax = 3;
	public int mobType = MOBTYPE_NORMAL;

	public boolean TYPE_ANIMAL = false;
	public boolean TYPE_EVIL = false;
	public boolean TYPE_UNDEAD = false;

	protected int EXP = 1;
	protected int maxLvl = Hero.MAX_LEVEL;
	
	protected Char enemy;
	protected boolean enemySeen;
	protected boolean alerted = false;

	protected static final float TIME_TO_WAKE_UP = 1f;
	
	public boolean hostile = true;
	public boolean ally = false;
	
	private static final String STATE	= "state";
	private static final String SEEN	= "seen";
	private static final String TARGET	= "target";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle(bundle);

		if (state == SLEEPING) {
			bundle.put( STATE, Sleeping.TAG );
		} else if (state == WANDERING) {
			bundle.put( STATE, Wandering.TAG );
		} else if (state == HUNTING) {
			bundle.put( STATE, Hunting.TAG );
		} else if (state == FLEEING) {
			bundle.put( STATE, Fleeing.TAG );
		} else if (state == PASSIVE) {
			bundle.put( STATE, Passive.TAG );
		}
		bundle.put( SEEN, enemySeen );
		bundle.put( TARGET, target );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle(bundle);

		String state = bundle.getString( STATE );

		if (state.equals( Sleeping.TAG )) {
			this.state = SLEEPING;
		} else if (state.equals( Wandering.TAG )) {
			this.state = WANDERING;
		} else if (state.equals( Hunting.TAG )) {
			this.state = HUNTING;
		} else if (state.equals( Fleeing.TAG )) {
			this.state = FLEEING;
		} else if (state.equals( Passive.TAG )) {
			this.state = PASSIVE;
		}

		enemySeen = bundle.getBoolean( SEEN );

		target = bundle.getInt( TARGET );
	}
	
	public CharSprite sprite() {
		CharSprite sprite = null;
		try {
			sprite = spriteClass.newInstance();
		} catch (Exception e) {
		}
		return sprite;
	}

	public void scaleMob() {
		switch (Dungeon.difficultyLevel) {
			case Dungeon.DIFF_TUTOR:
			case Dungeon.DIFF_EASY:
				scaleMob(-1);
				break;
			case Dungeon.DIFF_HARD:
				scaleMob(1);
				break;
			case Dungeon.DIFF_NTMARE:
				scaleMob(2);
				break;
		}
	}

	public void infiniteScaleMob(int depth) {
		this.maxLvl = depth + 3;
		if (mobType == MOBTYPE_NORMAL) {
			// LEV   HT/HP   DEF   ATK   DR   DAMAGE         XP
			//  1      8      2     8     0    1-4   (2.5)    1
			// 10     44     11    17     2    1-13   (7)     3
			// 20     84     21    27     4    1-23  (12)     5
			// 30    124     31    37     6    1-33  (17)     7
			this.HT = (int) (4 + (depth * 4));
			this.HP = this.HT;
			this.defenseSkill = 1 + depth;
			this.atkSkill = 7 + depth;
			this.dmgRed = (depth / 5);
			this.dmgMin = 1;
			this.dmgMax = 3 + depth;
			this.EXP = 1 + (depth/5);
		} else if (mobType == MOBTYPE_NIMBLE) {
			// LEV   HT/HP   DEF   ATK   DR   DAMAGE         XP
			//  1      8      8     8     0    1-7   (2.5)    3
			// 10     44     20    17     1    1-13   (7)     5
			// 20     84     33    27     3    1-23  (12)     8
			// 30    124     47    37     5    1-33  (17)    10
			this.HT = (int) (4 + (depth * 4));
			this.HP = this.HT;
			this.defenseSkill = 7 + (4 * depth / 3);
			this.atkSkill = 7 + depth;
			this.dmgRed = (depth / 6);
			this.dmgMin = 1;
			this.dmgMax = 3 + depth;
			this.EXP = 3 + (depth / 4);
		} else if (mobType == MOBTYPE_TOUGH) {
			// LEV   HT/HP   DEF   ATK   DR   DAMAGE         XP
			//  1     12      1     6     1    1-7    (4)     3
			// 10     66      6    12     4    3-16  (9.5)    5
			// 20    126     11    19     7    6-26  (16)     8
			// 30    186     16    26    11    8-36  (22)    10
			this.HT = (int) (6 + (depth * 6));
			this.HP = this.HT;
			this.defenseSkill = 1 + (depth / 2);
			this.atkSkill = 6 + (2 * depth / 3);
			this.dmgRed = 1 + (depth / 3);
			this.dmgMin = 1 + (depth / 4);
			this.dmgMax = 6 + depth;
			this.EXP = 3 + (depth / 4);
		} else if (mobType == MOBTYPE_RANGED) {
			// LEV   HT/HP   DEF   ATK   DR   DAMAGE         XP
			//  1      8      1     8     0    1-7   (2.5)    3
			// 10     44      6    17     1    1-13   (7)     5
			// 20     84     11    27     3    1-23  (12)     8
			// 30    124     16    37     5    1-33  (17)    10
			this.HT = (int) (4 + (depth * 4));
			this.HP = this.HT;
			this.defenseSkill = 1 + (depth / 2);
			this.atkSkill = 7 + depth;
			this.dmgRed = (depth / 6);
			this.dmgMin = 1;
			this.dmgMax = 3 + depth;
			this.EXP = 3 + (depth / 4);
		} else if (mobType == MOBTYPE_DEBUFF) {
			// LEV   HT/HP   DEF   ATK   DR   DAMAGE         XP
			//  1      8      2     8     0    1-4   (2.5)    1
			// 10     44     11    17     2    1-13   (7)     3
			// 20     84     21    27     4    1-23  (12)     5
			// 30    124     31    37     6    1-33  (17)     7
			this.HT = (int) (4 + (depth * 4));
			this.HP = this.HT;
			this.defenseSkill = 1 + depth;
			this.atkSkill = 7 + depth;
			this.dmgRed = (depth / 5);
			this.dmgMin = 1;
			this.dmgMax = 3 + depth;
			this.EXP = 3 + (depth / 4);
		} else if (mobType == MOBTYPE_SPECIAL) {
			// LEV   HT/HP   DEF   ATK   DR   DAMAGE         XP
			//  1     10      8     8     0    1-7    (4)     3
			// 10     55     20    17     2    3-13   (8)     6
			// 20    105     33    27     5    6-23 (14.5)    9
			// 30    155     47    37     7    8-33 (20.5)   13
			this.HT = (int) (5 + (depth * 5));
			this.HP = this.HT;
			this.defenseSkill = 7 + (4 * depth / 3);
			this.atkSkill = 7 + depth;
			this.dmgRed = (depth / 4);
			this.dmgMin = 1 + (depth / 4);
			this.dmgMax = 3 + depth;
			this.EXP = 3 + (depth / 3);
		}
	}

	public void scaleMob(int scaling) {
		this.HT = (int) (this.HT * (1.0f + (scaling * .2f)));
		this.HP = this.HT;
		this.defenseSkill = (int) (this.defenseSkill * (1.0f + (scaling * .1f)));
		this.atkSkill = (int) (this.atkSkill * (1.0f + (scaling * .15f)));
		this.dmgRed = (int) (this.dmgRed * (1.0f + (scaling * .02f)));
	}

	@Override
	protected boolean act() {
		
		super.act();
		
		boolean justAlerted = alerted;
		alerted = false;
		
		sprite.hideAlert();
		
		if (paralysed) {
			enemySeen = false;
			spend( TICK );
			return true;
		}
		
		enemy = chooseEnemy();
		
		boolean enemyInFOV = enemy != null && enemy.isAlive() && Level.fieldOfView[enemy.pos] && enemy.invisible <= 0;

		return state.act( enemyInFOV, justAlerted );
	}
	
	protected Char chooseEnemy() {

		Terror terror = buff( Terror.class );
		if (terror != null) {
			Char source = (Char)Actor.findById( terror.object );
			if (source != null) {
				return source;
			}
		}

		//resets target if: the target is dead, the target has been lost (wandering)
		//or if the mob is amoked/corrupted and targeting the hero (will try to target something else)
		if ( enemy != null &&
				!enemy.isAlive() || state == WANDERING ||
				((buff( Amok.class ) != null || buff(Corruption.class) != null) && enemy == Dungeon.hero ))
			enemy = null;

		//if there is no current target, find a new one.
		if (enemy == null) {

			HashSet<Char> enemies = new HashSet<>();

			//if the mob is amoked or corrupted...
			if ( buff(Amok.class) != null || buff(Corruption.class) != null) {

				//try to find an enemy mob to attack first.
				for (Mob mob : Dungeon.level.mobs)
					if (mob != this && Level.fieldOfView[mob.pos] && mob.hostile)
							enemies.add(mob);
				if (enemies.size() > 0) return Random.element(enemies);

				//try to find ally mobs to attack second.
				for (Mob mob : Dungeon.level.mobs)
					if (mob != this && Level.fieldOfView[mob.pos] && mob.ally)
						enemies.add(mob);
				if (enemies.size() > 0) return Random.element(enemies);

				//if there is nothing, go for the hero, unless corrupted, then go for nothing.
				if (buff(Corruption.class) != null) return null;
				else return Dungeon.hero;

			//if the mob is not amoked...
			} else {

				//try to find ally mobs to attack.
				for (Mob mob : Dungeon.level.mobs)
					if (mob != this && Level.fieldOfView[mob.pos] && mob.ally)
						enemies.add(mob);

				//and add the hero to the list of targets.
				enemies.add(Dungeon.hero);

				//target one at random.
				return Random.element(enemies);

			}

		} else
			return enemy;
	}

	protected boolean moveSprite( int from, int to ) {

		if (sprite.isVisible() && (Dungeon.visible[from] || Dungeon.visible[to])) {
			sprite.move( from, to );
			return true;
		} else {
			sprite.place( to );
			return true;
		}
	}
	
	@Override
	public void add( Buff buff ) {
		super.add(buff);
		if (buff instanceof Amok) {
			if (sprite != null) {
				sprite.showStatus( CharSprite.NEGATIVE, TXT_RAGE );
			}
			state = HUNTING;
		} else if (buff instanceof Terror) {
			state = FLEEING;
		} else if (buff instanceof Sleep) {
			state = SLEEPING;
			this.sprite().showSleep();
			postpone( Sleep.SWS );
		}
	}
	
	@Override
	public void remove( Buff buff ) {
		super.remove(buff);
		if (buff instanceof Terror) {
			if (sprite != null) {
				sprite.showStatus(CharSprite.NEGATIVE, TXT_RAGE);
			}
			state = HUNTING;
		}
	}
	
	protected boolean canAttack( Char enemy ) {
		return Level.adjacent( pos, enemy.pos );
	}
	
	protected boolean getCloser( int target ) {
		
		if (rooted) {
			return false;
		}
		
		int step = Dungeon.findPath( this, pos, target,
			Level.passable,
			Level.fieldOfView );
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean getFurther( int target ) {
		int step = Dungeon.flee(this, pos, target,
				Level.passable,
				Level.fieldOfView);
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updateSpriteState() {
		super.updateSpriteState();
		if (Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) != null)
			sprite.add( CharSprite.State.PARALYSED );
	}

	@Override
	public void move( int step ) {
		super.move(step);
		
		if (!flying) {
			Dungeon.level.mobPress( this );
		}
	}
	
	protected float attackDelay() {
		return 1f;
	}
	
	protected boolean doAttack( Char enemy ) {
		
		boolean visible = Dungeon.visible[pos];
		
		if (visible) {
			sprite.attack( enemy.pos );
		} else {
			attack( enemy );
		}
				
		spend( attackDelay() );
		
		return !visible;
	}
	
	@Override
	public void onAttackComplete() {
		attack( enemy );
		super.onAttackComplete();
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		if (enemySeen && !paralysed) {
			int defenseSkill = this.defenseSkill;
			int penalty = 0;
			for (Buff buff : enemy.buffs(RingOfAccuracy.Accuracy.class)) {
				penalty += ((RingOfAccuracy.Accuracy) buff).level;
			}
			if (penalty != 0 && enemy == Dungeon.hero)
				defenseSkill *= Math.pow(0.75, penalty);
			return defenseSkill;
		} else {
			return 0;
		}
	}

	@Override
	public int attackSkill( Char target ) {
		return atkSkill;
	}

	@Override
	public int dr() {
		return dmgRed;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(dmgMin, dmgMax);
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {
		if (!enemySeen && enemy == Dungeon.hero) {
			if (((Hero)enemy).subClass == HeroSubClass.ASSASSIN) {
				damage *= 1.34f;
				Wound.hit(this);
			} else {
				Surprise.hit(this);
			}
		}

		//become aggro'd by a corrupted enemy
		if (enemy.buff(Corruption.class) != null) {
			aggro(enemy);
			target = enemy.pos;
			if (state == SLEEPING || state == WANDERING)
				state = HUNTING;
		}

		return damage;
	}

	public void aggro( Char ch ) {
		enemy = ch;
	}

	@Override
	public void damage( int dmg, Object src ) {

		Terror.recover( this );

		if (state == SLEEPING) {
			state = WANDERING;
		}
		alerted = true;
		
		super.damage( dmg, src );
	}
	
	
	@Override
	public void destroy() {
		
		super.destroy();
		
		Dungeon.level.mobs.remove( this );
		
		if (Dungeon.hero.isAlive()) {
			
			if (hostile) {
				Statistics.enemiesSlain++;
				Badges.validateMonstersSlain();
				Statistics.qualifiedForNoKilling = false;
				
				if (Dungeon.level.feeling == Feeling.DARK) {
					Statistics.nightHunt++;
				} else {
					Statistics.nightHunt = 0;
				}
				Badges.validateNightHunter();
			}
			
			if (Dungeon.hero.lvl <= maxLvl && EXP > 0) {
				Dungeon.hero.sprite.showStatus( CharSprite.POSITIVE, TXT_EXP, EXP );
				Dungeon.hero.earnExp( EXP );
			}

			if ((Dungeon.difficultyLevel == Dungeon.DIFF_TUTOR) && (!Dungeon.tutorial_tactics_tip)) {
				Dungeon.tutorial_tactics_tip = true;
				GameScene.show(new WndMessage("Well done, keep in mind that some simple strategies can help you live " +
					"longer; try positioning yourself so that only one enemy can approach at a time, some negative potions " +
					"can be thrown at enemies to affect them, when an enemy steps through a doorway and sees you for the " +
					"first time gives you a surprise attack against them.  As you go on you will discover other tricks and " +
					"strategies to help you."));
			}

		}
	}
	
	@Override
	public void die( Object cause ) {
		
		super.die( cause );

		float lootChance = this.lootChance;
		int bonus = 0;
		for (Buff buff : Dungeon.hero.buffs(RingOfWealth.Wealth.class)) {
			bonus += ((RingOfWealth.Wealth) buff).level;
		}
		if (Dungeon.hero.heroClass == HeroClass.HUNTRESS) {
			bonus += 1;
		}

		lootChance *= Math.pow(1.1, bonus);
		
		if (Random.Float() < lootChance && Dungeon.hero.lvl <= maxLvl + 2) {
			Item loot = createLoot();
			if (loot != null)
				Dungeon.level.drop( loot , pos ).sprite.drop();
		}
		
		if (Dungeon.hero.isAlive() && !Dungeon.visible[pos]) {
			GLog.i( TXT_DIED );
		}
	}
	
	protected Object loot = null;
	protected float lootChance = 0;
	
	@SuppressWarnings("unchecked")
	protected Item createLoot() {
		Item item;
		if (loot instanceof Generator.Category) {

			item = Generator.random( (Generator.Category)loot );

		} else if (loot instanceof Class<?>) {

			item = Generator.random( (Class<? extends Item>)loot );

		} else {

			item = (Item)loot;

		}
		return item;
	}
	
	public boolean reset() {
		return false;
	}
	
	public void beckon( int cell ) {
		
		notice();
		
		if (state != HUNTING) {
			state = WANDERING;
		}
		target = cell;
	}

	public String description() {
		return "Real description is coming soon!";
	}
	
	public void notice() {
		if (sprite != null) {
			sprite.showAlert();
		}
	}

	public void yell( String str ) {
		GLog.n( "%s: \"%s\" ", name, str );
	}

	//returns true when a mob sees the hero, and is currently targeting them.
	public boolean focusingHero() {
		return enemySeen && (target == Dungeon.hero.pos);
	}

	public interface AiState {
		boolean act( boolean enemyInFOV, boolean justAlerted );
		String status();
	}

	private class Sleeping implements AiState {

		public static final String TAG	= "SLEEPING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && Random.Int( distance( enemy ) + enemy.stealth() + (enemy.flying ? 2 : 0) ) == 0) {

				enemySeen = true;

				notice();
				state = HUNTING;
				target = enemy.pos;

				if (Dungeon.isChallenged( Challenges.SWARM_INTELLIGENCE )) {
					// there is a slight chance that there are no mobs on this level...
					if (! Dungeon.level.mobs.isEmpty()) {
						for (Mob mob : Dungeon.level.mobs) {
							if (mob != Mob.this) {
								mob.beckon(target);
							}
						}
					}
				}

				spend( TIME_TO_WAKE_UP );

				if ((Dungeon.difficultyLevel == Dungeon.DIFF_TUTOR) && (!Dungeon.tutorial_mob_seen)) {
					Dungeon.tutorial_mob_seen = true;
					GameScene.show(new WndMessage("Up ahead is your first enemy, you can assign ranged weapons or wands to " +
						"your Quick-Slot(s) next to your backpack icon on the lower right side of the screen; Your Quick-Slot " +
						"can auto-aim on the closest mob if you double tap it.  You can also attack enemies in melee (close " +
						"combat) by approaching them and then either clicking on the enemy or on the red icon that will appear " +
						"above your Quick-Slots."));
				}

			} else {

				enemySeen = false;

				spend(TICK);

			}
			return true;
		}

		@Override
		public String status() {
			return Utils.format( "This %s is sleeping", name );
		}
	}

	private class Wandering implements AiState {

		public static final String TAG	= "WANDERING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && (justAlerted || Random.Int( distance( enemy ) / 2 + enemy.stealth() ) == 0)) {

				enemySeen = true;

				notice();
				state = HUNTING;
				target = enemy.pos;

			} else {

				enemySeen = false;

				int oldPos = pos;
				if (target != -1 && getCloser( target )) {
					spend( 1 / speed() );
					return moveSprite( oldPos, pos );
				} else {
					target = Dungeon.level.randomDestination();
					spend( TICK );
				}

			}
			return true;
		}

		@Override
		public String status() {
			return Utils.format( "This %s is wandering", name );
		}
	}

	private class Hunting implements AiState {

		public static final String TAG	= "HUNTING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				return doAttack( enemy );

			} else {

				if (enemyInFOV) {
					target = enemy.pos;
				}

				int oldPos = pos;
				if (target != -1 && getCloser( target )) {

					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else {

					spend( TICK );
					state = WANDERING;
					target = Dungeon.level.randomDestination();
					return true;
				}
			}
		}

		@Override
		public String status() {
			return Utils.format( "This %s is hunting", name );
		}
	}

	protected class Fleeing implements AiState {

		public static final String TAG	= "FLEEING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			if (enemyInFOV) {
				target = enemy.pos;
			}

			int oldPos = pos;
			if (target != -1 && getFurther( target )) {

				spend( 1 / speed() );
				return moveSprite( oldPos, pos );

			} else {

				spend( TICK );
				nowhereToRun();

				return true;
			}
		}

		protected void nowhereToRun() {
		}

		@Override
		public String status() {
			return Utils.format( "This %s is fleeing", name );
		}
	}

	private class Passive implements AiState {

		public static final String TAG	= "PASSIVE";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = false;
			spend( TICK );
			return true;
		}

		@Override
		public String status() {
			return Utils.format( "This %s is passive", name );
		}
	}
}

