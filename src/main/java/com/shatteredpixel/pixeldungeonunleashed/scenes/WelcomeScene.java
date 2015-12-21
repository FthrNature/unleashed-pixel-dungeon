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
package com.shatteredpixel.pixeldungeonunleashed.scenes;

import com.shatteredpixel.pixeldungeonunleashed.Chrome;
import com.shatteredpixel.pixeldungeonunleashed.ShatteredPixelDungeon;
import com.shatteredpixel.pixeldungeonunleashed.ui.Archs;
import com.shatteredpixel.pixeldungeonunleashed.ui.RedButton;
import com.shatteredpixel.pixeldungeonunleashed.ui.ScrollPane;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

//TODO: update this class with relevant info as new versions come out.
public class WelcomeScene extends PixelScene {

	private static final String TTL_Welcome = "Welcome!";

	private static final String TTL_Update = "v0.2.7: Let it Snow!";

	private static final String TTL_Future = "Wait What?";

	private static final String TXT_Welcome =
			"Unleashed Pixel Dungeon is a rework/expansion of Shattered Pixel Dungeon.\n\n"+
			"The latest round of updates finishes up our Difficulty Level Fixes!\n"+
			"To change difficulty levels click on the Out-of-Game Settings (little gears) in"+
			"the top left corner, difficulty will also change when you load games.\n\n"+
			"Enjoy";

	private static final String TXT_Update =
			"v02.7: Let it Snow!\n"+
					"- new frozen levels with mobs and a new boss\n"+
					"- graphics optimizations\n"+
					"- altars now toss out reward items\n"+
					"- mob rebalancing\n"+
					"- bug fixes\n"+
					"- and more bug fixes\n\n"+
			"v02.6: All the Cool Kids are doing it!\n"+
					"- look for the jewelry chain in the prison rooms\n"+
					"- items remain visible in the dungeon after they are seen\n"+
					"- new Holy and Hunting weapon enchantments\n"+
					"- enchantment description updates\n"+
					"- killing wraiths can now get you a reward\n"+
					"- slightly more special rooms per level\n"+
					"- food drops reduced slightly\n"+
					"- blinded creatures are now blind\n"+
					"- mindless mobs not affected by psionics\n"+
					"- graphics tweaks\n"+
					"- fix for the Rankings Screens\n"+
					"- Bug fixes\n\n"+
			"v02.5: A Balancing Act\n"+
					"- class rebalancing\n"+
					"     - Warrior can get psychic damage from reading scrolls\n"+
					"     - Mage wands charge faster, and know scroll of identify\n"+
					"     - Rogues get a +1 dagger, and know potion of toxic gas\n"+
					"     - Huntress starts with more HP and gets better drops\n"+
					"     - berserker fury now kicks in sooner\n"+
					"- mini-bosses are a little tougher\n"+
					"- open levels have less mobs and are a little less common\n"+
					"- ethereal chains tweaks\n"+
					"- better altar donation rewards\n"+
					"- bug fixes\n\n"+
			"v02.4: A Bug hunt!\n\n"+
			"v02.3: Badges and More!\n"+
					"- new room blocked with eternal flame\n"+
					"- fire damage scales slightly with depth\n"+
					"- new mini-bosses in infinite levels\n"+
					"- fixed some badge balance issues\n"+
					"- standardized graphics on some badges\n"+
					"- new badge just for Beta Testers\n"+
					"- donation rewards are now easier to reach\n"+
					"- throwing potions at mobs affect them\n"+
					"- ring of searching now affects search speed\n"+
					"- flavor text on tombstones\n"+
					"- Endless mode goes past depth 31\n"+
					"- stop the hero from ascending in Endless mode\n\n"+
			"v02.2: Endless Mode!\n"+
					"- Endless and Test modes added\n"+
					"- fixed some summoned mobs not scaling\n\n"+
			"v02.1: Rings and Potions and Mobs, oh my!\n"+
					"- rings are now listed in the catalogue\n"+
					"- potions of haste and slowness\n"+
					"- tweaked how potions affect hunger\n"+
					"- added dungeon snakes and the velocirooster\n\n"+
			"v02.0: Fleshing out the Game\n"+
					"- fix save file corruption\n"+
					"- Necromancer Mini-Boss, Zombies and Clay Golems\n"+
					"- hunger dmg now scales and prevents resting\n"+
					"- don't pick up dewdrops if not needed\n"+
					"- autotarget closest mobs\n"+
					"- shopkeeper flees when moved, may leave goods\n"+
					"- adjusted monk disarm logic\n"+
					"- bug fixes\n\n"+
			"v01.9: Squash Them Bugs\n"+
			"v01.8: Open Levels\n"+
			"v01.7: Enchantments and Glyphs\n"+
			"v01.6: Interim Build\n"+
            "v01.5: Content Push - It's all about the Mobs!\n"+
			"v01.4: Difficulty Levels\n";
	private static final String TXT_Future =
			"It seems that your current saves are from a future version of Unleashed Pixel Dungeon!\n\n"+
			"Either you're messing around with older versions of the app, or something has gone buggy.\n\n"+
			"Regardless, tread with caution! Your saves may contain things which don't exist in this version, "+
			"this could cause some very weird errors to occur.";

	@Override
	public void create() {
		super.create();

		final int gameversion = ShatteredPixelDungeon.version();

		BitmapTextMultiline title;
		BitmapTextMultiline text;

		if (gameversion == 0) {

			text = createMultiline(TXT_Welcome, 8);
			title = createMultiline(TTL_Welcome, 16);

		} else if (gameversion <= Game.versionCode) {

			text = createMultiline(TXT_Update, 6 );
			title = createMultiline(TTL_Update, 9 );

		} else {

			text = createMultiline( TXT_Future, 8 );
			title = createMultiline( TTL_Future, 16 );

		}

		int w = Camera.main.width;
		int h = Camera.main.height;

		int pw = w - 10;
		int ph = h - 50;

		title.maxWidth = pw;
		title.measure();
		title.hardlight(Window.SHPX_COLOR);

		title.x = align( (w - title.width()) / 2 );
		title.y = align( 8 );
		add( title );

		NinePatch panel = Chrome.get(Chrome.Type.WINDOW);
		panel.size( pw, ph );
		panel.x = (w - pw) / 2;
		panel.y = (h - ph) / 2;
		add( panel );

		ScrollPane list = new ScrollPane( new Component() );
		add( list );
		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop(),
				panel.innerWidth(),
				panel.innerHeight());
		list.scrollTo( 0, 0 );

		Component content = list.content();
		content.clear();

		text.maxWidth = (int) panel.innerWidth();
		text.measure();

		content.add(text);

		content.setSize( panel.innerWidth(), text.height() );

		RedButton okay = new RedButton("Okay!") {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.version(Game.versionCode);
				Game.switchScene(TitleScene.class);
			}
		};

		/*
		okay.setRect(text.x, text.y + text.height() + 5, 55, 18);
		add(okay);

		RedButton changes = new RedButton("Changes") {
			@Override
			protected void onClick() {
				parent.add(new WndChanges());
			}
		};

		changes.setRect(text.x + 65, text.y + text.height() + 5, 55, 18);
		add(changes);*/

		okay.setRect((w - pw) / 2, h - 22, pw, 18);
		add(okay);

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}
}


