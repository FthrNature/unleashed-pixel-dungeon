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
package com.shatteredpixel.pixeldungeonunleashed.items.rings;

import com.shatteredpixel.pixeldungeonunleashed.items.Item;

public class RingOfSearching  extends Ring {

    {
        name = "Ring of Searching";
        levelCap = 0;
        cursed = false;
        level = 0;
    }

    @Override
    public Item upgrade( int n ) {
        this.level = 0;

        updateQuickslot();

        return this;
    }

    @Override
    public Item upgrade() {
        return this;
    }

    @Override
    protected RingBuff buff( ) {
        return new EasySearch();
    }

    @Override
    public String desc() {
        return isKnown() ?
                "When wearing this ring brings out of place things into focus "+
                 "making searches easier. "+
                 "A degraded ring makes it tougher to find things." :
                super.desc();
    }

    public class EasySearch extends RingBuff {
    }
}