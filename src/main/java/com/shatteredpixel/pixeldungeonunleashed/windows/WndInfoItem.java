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
package com.shatteredpixel.pixeldungeonunleashed.windows;

import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.Artifact;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.Ring;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.Wand;
import com.watabou.noosa.BitmapTextMultiline;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap.Type;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.scenes.PixelScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.ui.ItemSlot;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;

public class WndInfoItem extends Window {
	
	private static final String TTL_CHEST           = "Chest";
	private static final String TTL_LOCKED_CHEST	= "Locked chest";
	private static final String TTL_CRYSTAL_CHEST	= "Crystal chest";
	private static final String TTL_TOMB			= "Tomb";
	private static final String TTL_SKELETON		= "Skeletal remains";
	private static final String TTL_REMAINS 		= "Heroes remains";
	private static final String TXT_WONT_KNOW		= "You won't know what's inside until you open it!";
	private static final String TXT_NEED_KEY		= TXT_WONT_KNOW + " But to open it you need a golden key.";
	private static final String TXT_INSIDE			= "You can see %s inside, but to open the chest you need a golden key.";
	private static final String TXT_OWNER	=
		"This ancient tomb may contain something useful, " +
		"but its owner will most certainly object to checking.";
	private static final String TXT_SKELETON =
		"This is all that's left of some unfortunate adventurer. " +
		"Maybe it's worth checking for any valuables.";
	private static final String TXT_REMAINS =
		"This is all that's left from one of your predecessors. " +
		"Maybe it's worth checking for any valuables.";
	
	private static final float GAP	= 2;
	
	private static final int WIDTH = 120;
	
	public WndInfoItem( Heap heap ) {
		
		super();
		
		if (heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE) {
			
			Item item = heap.peek();
			
			int color = TITLE_COLOR;
			if (item.levelKnown && item.level > 0) {
				color = ItemSlot.UPGRADED;
			} else if (item.levelKnown && item.level < 0) {
				color = ItemSlot.DEGRADED;
			}
			fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
			
		} else {
			
			String title;
			String info;
			
			if (heap.type == Type.CHEST || heap.type == Type.MIMIC) {
				title = TTL_CHEST;
				info = TXT_WONT_KNOW;
			} else if (heap.type == Type.TOMB) {
				title = TTL_TOMB;
				int thisTomb = heap.pos % 25;
				switch (thisTomb) {
					case 0:
						info = "'Gnoll Trickster, humanitarian, hero... left behind wife and 12 children.'\n\n" + TXT_OWNER;
						break;
					case 1:
						info = "'Here lies Fred, once a hero, now he's dead'\n\n" + TXT_OWNER;
						break;
					case 2:
						info = "'Jokes over. Let me out now!'\n\n" + TXT_OWNER;
						break;
					case 3:
						info = "'Here lies Bob, he died from not forwarding that text message to 10 people.'\n\n" + TXT_OWNER;
						break;
					case 4:
						info = "'Here lies Tom, he loved bacon.  Oh, and his wife and kids too.'\n\n" + TXT_OWNER;
						break;
					case 5:
						info = "'Here lies my husband Tom, now I know where he is every night.'\n\n" + TXT_OWNER;
						break;
					case 6:
						info = "'I told you I was sick!'\n\n" + TXT_OWNER;
						break;
					case 7:
						info = "'Here lies Carl.  The second fastest draw in the west.'\n\n" + TXT_OWNER;
						break;
					case 8:
						info = "'Here lies Rick, he forgot to use the door trick.'\n\n" + TXT_OWNER;
						break;
					case 9:
						info = "'This was not in my job description'\n\n" + TXT_OWNER;
						break;
					case 10:
						info = "'Here lies mister Jones, he should have paid off those Mafia loans.'\n\n" + TXT_OWNER;
						break;
					case 11:
						info = "'I made some good deals and I made some bad ones.  I really went in the hole with this one.'\n\n" + TXT_OWNER;
						break;
					case 12:
						info = "'Those weren't goldfish'\n\n" + TXT_OWNER;
						break;
					case 13:
						info = "'That was not a potion of healing.'\n\n" + TXT_OWNER;
						break;
					case 14:
						info = "'I came here without being consulted and I leave without my consent.'\n\n" + TXT_OWNER;
						break;
					case 15:
						info = "'DM-299 died doing what he loved.  Killing humans.'\n\n" + TXT_OWNER;
						break;
					case 16:
						info = "'Here lies Magda.  If you are reading this I must be dead.'\n\n" + TXT_OWNER;
						break;
					case 17:
						info = "'... well this sucks.'\n\n" + TXT_OWNER;
						break;
					case 18:
						info = "'Waldo'\n\n" + TXT_OWNER;
						break;
					case 19:
						info = "'but it was just a flesh wound.'\n\n" + TXT_OWNER;
						break;
					case 20:
						info = "'Let me out now!'\n\n" + TXT_OWNER;
						break;
					case 21:
						info = "'Yul B. Next'\n\n" + TXT_OWNER;
						break;
					case 22:
						info = "'He died like he lived, screaming and crying in the corner.'\n\n" + TXT_OWNER;
						break;
					case 23:
						info = "'Barry A. Live - here he lies, but he never died!'\n\n" + TXT_OWNER;
						break;
					case 24:
						info = "'That potion of levitation didn't last as long as I'd like it to.'\n\n" + TXT_OWNER;
						break;
					default:
						info = TXT_OWNER;
						break;
				}
			} else if (heap.type == Type.SKELETON) {
				title = TTL_SKELETON;
				info = TXT_SKELETON;
			} else if (heap.type == Type.REMAINS) {
				title = TTL_REMAINS;
				info = TXT_REMAINS;
			} else if (heap.type == Type.CRYSTAL_CHEST) {
				title = TTL_CRYSTAL_CHEST;
				if (heap.peek() instanceof Artifact)
					info = Utils.format( TXT_INSIDE, "an artifact" );
				else if (heap.peek() instanceof Wand)
					info = Utils.format( TXT_INSIDE, "a wand" );
				else if (heap.peek() instanceof Ring)
					info = Utils.format( TXT_INSIDE, "a ring" );
				else
					info = Utils.format( TXT_INSIDE, Utils.indefinite( heap.peek().name() ) );
			} else {
				title = TTL_LOCKED_CHEST;
				info = TXT_NEED_KEY;
			}
			
			fillFields( heap.image(), heap.glowing(), TITLE_COLOR, title, info );
			
		}
	}
	
	public WndInfoItem( Item item ) {
		
		super();
		
		int color = TITLE_COLOR;
		if (item.levelKnown && item.level > 0) {
			color = ItemSlot.UPGRADED;
		} else if (item.levelKnown && item.level < 0) {
			color = ItemSlot.DEGRADED;
		}
		
		fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
	}
	
	private void fillFields( int image, ItemSprite.Glowing glowing, int titleColor, String title, String info ) {
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( image, glowing ) );
		titlebar.label( Utils.capitalize( title ), titleColor );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		BitmapTextMultiline txtInfo = PixelScene.createMultiline( info, 6 );
		txtInfo.maxWidth = WIDTH;
		txtInfo.measure();
		txtInfo.x = titlebar.left();
		txtInfo.y = titlebar.bottom() + GAP;
		add( txtInfo );
		
		resize( WIDTH, (int)(txtInfo.y + txtInfo.height()) );
	}
}
