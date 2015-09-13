package com.shatteredpixel.pixeldungeonunleashed.items.rings;

public class RingOfSating extends Ring {

    {
        name = "Ring of Sating";
    }

    @Override
    protected RingBuff buff( ) {
        return new Satiety();
    }

    @Override
    public String desc() {
        return isKnown() ?
                "This ring allows you to go longer without food, cursed rings have the opposite effect." :
                super.desc();
    }

    public class Satiety extends RingBuff {
    }
}
