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

import java.util.HashSet;

import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.CapeOfThorns;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.LloydsBeacon;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Blob;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.effects.CellEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.ElmoParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.SkeletonKey;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.DM300Sprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.Random;

public class DM300 extends Mob {
	
	{
		name = "DM-300";
		spriteClass = DM300Sprite.class;
		
		HP = HT = 200;
		EXP = 30;
		defenseSkill = 18;
		atkSkill = 28;
		dmgRed = 10;
		dmgMin = 18;
		dmgMax = 24;

		loot = new CapeOfThorns().identify();
		lootChance = 0.333f;
	}
	
	@Override
	public boolean act() {
		
		GameScene.add( Blob.seed( pos, 30, ToxicGas.class ) );
		
		return super.act();
	}
	
	@Override
	public void move( int step ) {
		super.move( step );
		
		if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && HP < HT) {
			
			HP += Random.Int( 1, HT - HP );
			sprite.emitter().burst( ElmoParticle.FACTORY, 5 );
			
			if (Dungeon.visible[step] && Dungeon.hero.isAlive()) {
				GLog.n( "DM-300 repairs itself!" );
			}
		}
		
		int[] cells = {
			step-1, step+1, step-Level.WIDTH, step+Level.WIDTH,
			step-1-Level.WIDTH,
			step-1+Level.WIDTH,
			step+1-Level.WIDTH,
			step+1+Level.WIDTH
		};
		int cell = cells[Random.Int( cells.length )];
		
		if (Dungeon.visible[cell]) {
			CellEmitter.get( cell ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
			Camera.main.shake( 3, 0.7f );
			Sample.INSTANCE.play( Assets.SND_ROCKS );
			
			if (Level.water[cell]) {
				GameScene.ripple( cell );
			} else if (Dungeon.level.map[cell] == Terrain.EMPTY) {
				Level.set( cell, Terrain.EMPTY_DECO );
				GameScene.updateMap( cell );
			}
		}

		Char ch = Actor.findChar( cell );
		if (ch != null && ch != this) {
			Buff.prolong( ch, Paralysis.class, 2 );
		}
	}
	
	@Override
	public void die( Object cause ) {
		
		super.die( cause );
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey( Dungeon.depth  ), pos ).sprite.drop();
		
		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade();
			GLog.p("Your beacon grows stronger!");
		}
		
		yell( "Mission failed. Shutting down." );
	}
	
	@Override
	public void notice() {
		super.notice();
		yell( "Unauthorised personnel detected." );
	}
	
	@Override
	public String description() {
		return
			"This machine was created by the Dwarves several centuries ago. Later, Dwarves started to replace machines with " +
			"golems, elementals and even demons. Eventually it led their civilization to the decline. The DM-300 and similar " +
			"machines were typically used for construction and mining, and in some cases, for city defense.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( Death.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( ToxicGas.class );
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
