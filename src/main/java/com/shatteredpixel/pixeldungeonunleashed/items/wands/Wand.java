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
package com.shatteredpixel.pixeldungeonunleashed.items.wands;

import java.util.ArrayList;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.HeroClass;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfMagic;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.MagesStaff;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Invisibility;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.effects.MagicMissile;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.Bag;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfMagic.Magic;
import com.shatteredpixel.pixeldungeonunleashed.mechanics.Ballistica;
import com.shatteredpixel.pixeldungeonunleashed.scenes.CellSelector;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.ui.QuickSlotButton;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public abstract class Wand extends Item {

	private static final int USAGES_TO_KNOW    = 20;

	public static final String AC_ZAP	= "ZAP";

	private static final String TXT_FIZZLES		= "your wand fizzles; it must not have enough charge.";
	private static final String TXT_SELF_TARGET	= "You can't target yourself";

	private static final String TXT_IDENTIFY    = "You are now familiar with your %s.";

	private static final float TIME_TO_ZAP	= 1f;
	
	public int maxCharges = initialCharges();
	public int curCharges = maxCharges;
	public float partialCharge = 0f;
	
	protected Charger charger;
	
	private boolean curChargeKnown = false;

	protected int usagesToKnow = USAGES_TO_KNOW;

	protected int collisionProperties = Ballistica.MAGIC_BOLT;

	
	{
		defaultAction = AC_ZAP;
		usesTargeting = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (curCharges > 0 || !curChargeKnown) {
			actions.add( AC_ZAP );
		}

		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_ZAP )) {
			
			curUser = hero;
			curItem = this;
			GameScene.selectCell( zapper );
			
		} else {
			
			super.execute(hero, action);
			
		}
	}
	
	protected abstract void onZap( Ballistica attack );

	public abstract void onHit( MagesStaff staff, Char attacker, Char defender, int damage);

	@Override
	public boolean collect( Bag container ) {
		if (super.collect( container )) {
			if (container.owner != null) {
				charge( container.owner );
			}
			return true;
		} else {
			return false;
		}
	}
	
	public void charge( Char owner ) {
		if (charger == null) charger = new Charger();
		charger.attachTo( owner );
	}

	public void charge( Char owner, float chargeScaleFactor ){
		charge( owner );
		charger.setScaleFactor(chargeScaleFactor);
	}

	@Override
	public void onDetach( ) {
		stopCharging();
	}

	public void stopCharging() {
		if (charger != null) {
			charger.detach();
			charger = null;
		}
	}
	
	public int level() {
		return level;
	}
	
	@Override
	public Item identify() {

		curChargeKnown = true;
		super.identify();
		
		updateQuickslot();
		
		return this;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder( super.toString() );
		
		String status = status();
		if (status != null) {
			sb.append( " (" + status +  ")" );
		}
		
		return sb.toString();
	}
	
	@Override
	public String info() {
		return (cursed && cursedKnown) ?
				desc() + "\n\nThis wand is cursed, making its magic chaotic and random." :
				desc();
	}
	
	@Override
	public boolean isIdentified() {
		return super.isIdentified() && curChargeKnown;
	}
	
	@Override
	public String status() {
		if (levelKnown) {
			return (curChargeKnown ? curCharges : "?") + "/" + maxCharges;
		} else {
			return null;
		}
	}

	@Override
	public Item upgrade( int n ) {
		cursed = false;
		cursedKnown = true;

		this.level = n;
		updateLevel();
		curCharges = maxCharges;
		updateQuickslot();

		return this;
	}

	@Override
	public Item upgrade() {

		if (upgradeSucceds()) {
			super.upgrade();
			updateLevel();
			curCharges = Math.min( curCharges + 1, maxCharges );
			updateQuickslot();
		} else {
			GLog.n("The magic failed to bind to your wand.");
		}

		return this;
	}
	
	@Override
	public Item degrade() {
		super.degrade();
		
		updateLevel();
		updateQuickslot();
		
		return this;
	}
	
	public void updateLevel() {
		maxCharges = Math.min( initialCharges() + level, 10 );
		curCharges = Math.min( curCharges, maxCharges );
        if (curCharges < 1) {
            curCharges = 1;
        }
	}
	
	protected int initialCharges() {
		return 2;
	}

	protected int chargesPerCast() {
		return 1;
	}
	
	protected void fx( Ballistica bolt, Callback callback ) {
		MagicMissile.whiteLight( curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	public void staffFx( MagesStaff.StaffParticle particle ){
		particle.color(0xFFFFFF); particle.am = 0.3f;
		particle.setLifespan( 1f);
		particle.speed.polar( Random.Float(PointF.PI2), 2f );
		particle.setSize( 1f, 2.5f );
		particle.radiateXY(1f);
	}

	protected void wandUsed() {
		usagesToKnow -= cursed ? 1 : chargesPerCast();
		curCharges -= cursed ? 1 : chargesPerCast();
		if (!isIdentified() && usagesToKnow <= 0) {
			identify();
			GLog.w( TXT_IDENTIFY, name() );
		} else {
			if (curUser.heroClass == HeroClass.MAGE) levelKnown = true;
			updateQuickslot();
		}

		curUser.spendAndNext( TIME_TO_ZAP );
	}
	
	@Override
	public Item random() {
		int n = 0;

		if (Random.Int(2) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}

		upgrade(n);
		if (Random.Float() < 0.3f) {
			cursed = true;
			cursedKnown = false;
		}

		return this;
	}
	
	@Override
	public int price() {
		int price = 75;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level > 0) {
				price *= (level + 1);
			} else if (level < 0) {
				price /= (1 - level);
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	private static final String UNFAMILIRIARITY        = "unfamiliarity";
	private static final String CUR_CHARGES			= "curCharges";
	private static final String CUR_CHARGE_KNOWN	= "curChargeKnown";
	private static final String PARTIALCHARGE 		= "partialCharge";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, usagesToKnow );
		bundle.put( CUR_CHARGES, curCharges );
		bundle.put( CUR_CHARGE_KNOWN, curChargeKnown );
		bundle.put( PARTIALCHARGE , partialCharge );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((usagesToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			usagesToKnow = USAGES_TO_KNOW;
		}
		curCharges = bundle.getInt( CUR_CHARGES );
		curChargeKnown = bundle.getBoolean( CUR_CHARGE_KNOWN );
		partialCharge = bundle.getFloat( PARTIALCHARGE );
	}
	
	protected static CellSelector.Listener zapper = new  CellSelector.Listener() {

		@Override
		public void onSelect( Integer target ) {
			try {
				if (target != null) {

					final Wand curWand = (Wand) Wand.curItem;

					final Ballistica shot = new Ballistica(curUser.pos, target, curWand.collisionProperties);
					int cell = shot.collisionPos;

					if (target == curUser.pos || cell == curUser.pos) {
						GLog.i(TXT_SELF_TARGET);
						return;
					}

					curUser.sprite.zap(cell);

					//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
					if (Actor.findChar(target) != null)
						QuickSlotButton.target(Actor.findChar(target));
					else
						QuickSlotButton.target(Actor.findChar(cell));

					if (curWand.curCharges >= (curWand.cursed ? 1 : curWand.chargesPerCast())) {

						curUser.busy();

						if (curWand.cursed) {
							CursedWand.cursedZap(curWand, curUser, new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT));
							if (!curWand.cursedKnown) {
								curWand.cursedKnown = true;
								GLog.n("This " + curItem.name() + " is cursed!");
							}
						} else {
							curWand.fx(shot, new Callback() {
								public void call() {
									curWand.onZap(shot);
									curWand.wandUsed();
								}
							});
						}

						Invisibility.dispel();

					} else {

						GLog.w(TXT_FIZZLES);

					}

				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		@Override
		public String prompt() {
			return "Choose a location to zap";
		}
	};
	
	protected class Charger extends Buff {
		
		private static final float BASE_CHARGE_DELAY = 10f;
		private static final float SCALING_CHARGE_ADDITION = 40f;
		private static final float NORMAL_SCALE_FACTOR = 0.875f;

		private static final float CHARGE_BUFF_BONUS = 0.25f;

		float scalingFactor = NORMAL_SCALE_FACTOR;
		
		@Override
		public boolean attachTo( Char target ) {
			super.attachTo(target);
			
			return true;
		}
		
		@Override
		public boolean act() {
			if (curCharges < maxCharges)
				gainCharge();
			
			if (partialCharge >= 1 && curCharges < maxCharges) {
				partialCharge--;
				curCharges++;
				updateQuickslot();
			}
			
			spend( TICK ); // check each turn
			
			return true;
		}

		private void gainCharge(){
			float prevCharge = partialCharge;
			int missingCharges = maxCharges - curCharges;

			float scalingChargeAddition = SCALING_CHARGE_ADDITION;
			if (Dungeon.hero.heroClass == HeroClass.MAGE) {
				scalingChargeAddition = 35.0f;
			}
			float turnsToCharge = (float) (BASE_CHARGE_DELAY + (scalingChargeAddition * Math.pow(scalingFactor, missingCharges)));
			partialCharge += 1f/turnsToCharge;

			// a scroll of recharging gives a .25 charge per turn
			ScrollOfRecharging.Recharging bonus = target.buff(ScrollOfRecharging.Recharging.class);
			if (bonus != null && bonus.remainder() > 0f){
				partialCharge += CHARGE_BUFF_BONUS * bonus.remainder();
			}

			float bonus2 = 0f;
			for (Buff buff : target.buffs( RingOfMagic.Magic.class )) {
				bonus2 += (((RingOfMagic.Magic)buff).level + 1) * .005f;
			}
			partialCharge += bonus2;
			if (prevCharge > partialCharge) {
				partialCharge = prevCharge + .005f;
			}

		}

		private void setScaleFactor(float value){
			this.scalingFactor = value;
		}
	}
}
