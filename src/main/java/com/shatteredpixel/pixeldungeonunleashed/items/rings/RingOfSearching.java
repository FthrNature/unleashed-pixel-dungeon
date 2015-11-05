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