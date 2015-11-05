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
package com.shatteredpixel.pixeldungeonunleashed.items.scrolls;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Blindness;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.HeroClass;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.ShadowParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.ItemStatusHandler;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Scroll extends Item {

	private static final String TXT_BLINDED	= "You can't read a scroll while blinded";

	private static final String TXT_CURSED	= "Your cursed spellbook prevents you from invoking this scroll's magic! " +
											  "A scroll of remove curse might be strong enough to still work though...";
	
	public static final String AC_READ	= "READ";
	
	protected static final float TIME_TO_READ	= 1f;

	protected String initials;

	private static final Class<?>[] scrolls = {
		ScrollOfIdentify.class,
		ScrollOfMagicMapping.class,
		ScrollOfRecharging.class,
		ScrollOfRemoveCurse.class,
		ScrollOfTeleportation.class,
		ScrollOfUpgrade.class,
		ScrollOfRage.class,
		ScrollOfTerror.class,
		ScrollOfLullaby.class,
		ScrollOfMagicalInfusion.class,
		ScrollOfPsionicBlast.class,
		ScrollOfMirrorImage.class
	};
	private static final String[] runes =
		{"KAUNAN", "SOWILO", "LAGUZ", "YNGVI", "GYFU", "RAIDO", "ISAZ", "MANNAZ",
		"NAUDIZ", "BERKANAN", "ODAL", "TIWAZ", "URR", "BEORC", "PEORO", "JEAR"
		};
	private static final Integer[] images = {
		ItemSpriteSheet.SCROLL_KAUNAN,
		ItemSpriteSheet.SCROLL_SOWILO,
		ItemSpriteSheet.SCROLL_LAGUZ,
		ItemSpriteSheet.SCROLL_YNGVI,
		ItemSpriteSheet.SCROLL_GYFU,
		ItemSpriteSheet.SCROLL_RAIDO,
		ItemSpriteSheet.SCROLL_ISAZ,
		ItemSpriteSheet.SCROLL_MANNAZ,
		ItemSpriteSheet.SCROLL_NAUDIZ,
		ItemSpriteSheet.SCROLL_BERKANAN,
		ItemSpriteSheet.SCROLL_ODAL,
		ItemSpriteSheet.SCROLL_TIWAZ,
		ItemSpriteSheet.SCROLL_URR,
		ItemSpriteSheet.SCROLL_BEORC,
		ItemSpriteSheet.SCROLL_PEORO,
		ItemSpriteSheet.SCROLL_JEAR
	};
	
	private static ItemStatusHandler<Scroll> handler;
	
	private String rune;

	public boolean ownedByBook = false;
	
	{
		stackable = true;
		defaultAction = AC_READ;
	}
	
	@SuppressWarnings("unchecked")
	public static void initLabels() {
		handler = new ItemStatusHandler<Scroll>( (Class<? extends Scroll>[])scrolls, runes, images );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Scroll>( (Class<? extends Scroll>[])scrolls, runes, images, bundle );
	}
	
	public Scroll() {
		super();
		syncVisuals();
	}

	@Override
	public void syncVisuals(){
		image = handler.image( this );
		rune = handler.label( this );
	};
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {
			
			if (hero.buff( Blindness.class ) != null) {
				GLog.w( TXT_BLINDED );
			} else if (hero.buff(UnstableSpellbook.bookRecharge.class) != null
					&& hero.buff(UnstableSpellbook.bookRecharge.class).isCursed()
					&& !(this instanceof ScrollOfRemoveCurse)) {
				GLog.n( TXT_CURSED );
			} else {
				curUser = hero;
				curItem = detach( hero.belongings.backpack );
				doRead();
				if ((Dungeon.hero.heroClass == HeroClass.WARRIOR) && (Random.Int(8) == 0)) {
					GLog.w("As a Warrior, deciphering the strange runes causes you pain");
					switch (Dungeon.difficultyLevel) {
						case Dungeon.DIFF_TUTOR:
						case Dungeon.DIFF_EASY:
							Dungeon.hero.damage(Random.Int(1,3), this);
							break;
						case Dungeon.DIFF_HARD:
							Dungeon.hero.damage(Random.Int(2, (Dungeon.hero.HT / 8 + 3)), this);
							break;
						case Dungeon.DIFF_NTMARE:
							Dungeon.hero.damage(Random.Int(2, (Dungeon.hero.HT / 5 + 3)), this);
							break;
						default:
							Dungeon.hero.damage(Random.Int(1, (Dungeon.hero.HT / 12 + 3)), this);
							break;
					}
					Sample.INSTANCE.play( Assets.SND_DEATH );
					hero.sprite.emitter().burst( ShadowParticle.CURSE, 6 );
				}
			}
			
		} else {
		
			super.execute( hero, action );
			
		}
	}
	
	abstract protected void doRead();
	
	public boolean isKnown() {
		return handler.isKnown( this );
	}
	
	public void setKnown() {
		if (!isKnown() && !ownedByBook) {
			handler.know( this );
		}
		
		Badges.validateAllScrollsIdentified();
	}
	
	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}
	
	@Override
	public String name() {
		return isKnown() ? name : "scroll \"" + rune + "\"";
	}
	
	@Override
	public String info() {
		return isKnown() ?
			desc() :
			"This parchment is covered with indecipherable writing, and bears a title " +
			"of rune " + rune + ". Who knows what it will do when read aloud?";
	}

	public String initials(){
		return isKnown() ? initials : null;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return isKnown();
	}
	
	public static HashSet<Class<? extends Scroll>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Scroll>> getUnknown() {
		return handler.unknown();
	}
	
	public static boolean allKnown() {
		return handler.known().size() == scrolls.length;
	}
	
	@Override
	public int price() {
		return 15 * quantity;
	}
}
