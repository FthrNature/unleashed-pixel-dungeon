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
package com.shatteredpixel.pixeldungeonunleashed.levels;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Bones;
import com.shatteredpixel.pixeldungeonunleashed.Challenges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.ChaosMage;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.FetidRat;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.GnollTrickster;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.GreatCrab;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.InfiniteBestiary;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Minotaur;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Tinkerer;
import com.shatteredpixel.pixeldungeonunleashed.items.DewVial;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfWealth;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.Scroll;
import com.shatteredpixel.pixeldungeonunleashed.levels.painters.Painter;
import com.shatteredpixel.pixeldungeonunleashed.levels.painters.ShopPainter;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.AlarmTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.FireTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.GrippingTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.LightningTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.ParalyticTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.PoisonTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.ToxicTrap;
import com.watabou.noosa.Scene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
public class InfiniteLevel  extends Level {

    private int THEME = (Dungeon.depth / 5) % 6;
    {
        switch (THEME) {
            case 0: // this is a sewer level
                color1 = 0x48763c;
                color2 = 0x59994a;
                break;
            case 1: // this is a prison level
                color1 = 0x6a723d;
                color2 = 0x88924c;
                break;
            case 2: // this is a caves level
                color1 = 0x534f3e;
                color2 = 0xb9d661;
                break;
            case 3: // this is a city level
                color1 = 0x4b6636;
                color2 = 0xf2f2f2;
                break;
            case 4: // this is a frozen level
                color1 = 0x484876;
                color2 = 0x4b5999;
                break;
            default: // this is a halls level
                color1 = 0x801500;
                color2 = 0xa68521;
                break;
        }

        viewDistance = 8;
    }

    @Override
    public String tilesTex() {
        switch (THEME) {
            case 0: // sewers
                return Assets.TILES_SEWERS;
            case 1: // prison
                return Assets.TILES_PRISON;
            case 2: // caves
                return Assets.TILES_CAVES;
            case 3: // city
                return Assets.TILES_CITY;
            case 4: // frozen
                return Assets.TILES_FROZEN;
            default: // halls
                return Assets.TILES_HALLS;
        }
    }

    @Override
    public String waterTex() {
        switch (THEME) {
            case 0: // sewers
                return Assets.WATER_SEWERS;
            case 1: // prison
                return Assets.WATER_PRISON;
            case 2: // caves
                return Assets.WATER_CAVES;
            case 3: // city
                return Assets.WATER_CITY;
            case 4: // frozen
                return Assets.WATER_FROZEN;
            default: // halls
                return Assets.WATER_HALLS;
        }
    }

    protected HashSet<Room> rooms;

    protected Room roomEntrance;
    protected Room roomExit;

    protected ArrayList<Room.Type> specials;

    public int secretDoors;

    protected void decorate() {

        for (int i=0; i < WIDTH; i++) {
            if (map[i] == Terrain.WALL &&
                    map[i + WIDTH] == Terrain.WATER &&
                    Random.Int( 4 ) == 0) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        for (int i=WIDTH; i < LENGTH - WIDTH; i++) {
            if (map[i] == Terrain.WALL &&
                    map[i - WIDTH] == Terrain.WALL &&
                    map[i + WIDTH] == Terrain.WATER &&
                    Random.Int( 2 ) == 0) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
            if (map[i] == Terrain.EMPTY) {

                int count =
                        (map[i + 1] == Terrain.WALL ? 1 : 0) +
                                (map[i - 1] == Terrain.WALL ? 1 : 0) +
                                (map[i + WIDTH] == Terrain.WALL ? 1 : 0) +
                                (map[i - WIDTH] == Terrain.WALL ? 1 : 0);

                if (Random.Int( 16 ) < count * count) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }

        placeSign();
    }

    @Override
    protected boolean build() {

        if (!initRooms()) {
            return false;
        }

        int distance;
        int retry = 0;
        int maxRoomRetry = 16;
        int minDistance = (int)Math.sqrt( rooms.size() );
        do {
            do {
                roomEntrance = Random.element(rooms);
            } while (roomEntrance.width() < 4 || roomEntrance.height() < 4);

            do {
                roomExit = Random.element( rooms );
            } while (roomExit == roomEntrance || roomExit.width() < 4 || roomExit.height() < 4);

            Graph.buildDistanceMap(rooms, roomExit);
            distance = roomEntrance.distance();

            if (retry++ > maxRoomRetry) {
                return false;
            }

        } while (distance < minDistance);

        roomEntrance.type = Room.Type.ENTRANCE;
        roomExit.type = Room.Type.EXIT;

        HashSet<Room> connected = new HashSet<>();
        connected.add( roomEntrance );

        Graph.buildDistanceMap( rooms, roomExit );
        List<Room> path = Graph.buildPath( rooms, roomEntrance, roomExit );

        Room room = roomEntrance;
        for (Room next : path) {
            room.connect( next );
            room = next;
            connected.add( room );
        }

        Graph.setPrice( path, roomEntrance.distance );

        Graph.buildDistanceMap( rooms, roomExit );
        path = Graph.buildPath( rooms, roomEntrance, roomExit );

        room = roomEntrance;
        for (Room next : path) {
            room.connect( next );
            room = next;
            connected.add( room );
        }

        int nConnected = (int)(rooms.size() * Random.Float( 0.5f, 0.7f )); // the higher this number is the more packed our map becomes
        while (connected.size() < nConnected) {

            Room cr = Random.element( connected );
            Room or = Random.element( cr.neigbours );
            if (!connected.contains( or )) {

                cr.connect( or );
                connected.add( or );
            }
        }

        if ((Dungeon.depth % 10) == 1) { // there is a shop on every 10th level: 1, 11, 21, 31, 41 ..
            Room shop = null;
            for (Room r : roomEntrance.connected.keySet()) {
                if (r.connected.size() == 1 && ((r.width()-1)*(r.height()-1) >= ShopPainter.spaceNeeded())) {
                    shop = r;
                    break;
                }
            }

            if (shop == null) {
                return false;
            } else {
                shop.type = Room.Type.SHOP;
            }
        }

        specials = new ArrayList<>( Room.SPECIALS );
        specials.remove( Room.Type.PIT); // we can't go up levels so this would be pointless

        if (Dungeon.isChallenged( Challenges.NO_ARMOR )){
            //no sense in giving an armor reward room on a run with no armor.
            specials.remove( Room.Type.CRYPT );
        }
        if (Dungeon.isChallenged( Challenges.NO_HERBALISM )){
            //sorry warden, no lucky sungrass or blandfruit seeds for you!
            specials.remove( Room.Type.GARDEN );
        }
        if (!assignRoomType())
            return false;

        paint();
        paintWater();
        paintGrass();
        placeTraps();

        if (feeling == Feeling.BURNT) {
            // go through and burn all the grass and unlocked doors
            for (int i=WIDTH+1; i < LENGTH-WIDTH-1; i++) {
                if (map[i] == Terrain.GRASS || map[i] == Terrain.HIGH_GRASS || map[i] == Terrain.DOOR) {
                    map[i] = Terrain.EMBERS;
                }
            }
        }

        if (Dungeon.depth == 1) {
            addItemToSpawn(new DewVial());
        }

        return true;
    }

    protected void placeSign(){
        while (true) {
            int pos = roomEntrance.random();
            if (pos != entrance && traps.get(pos) == null) {
                map[pos] = Terrain.SIGN;
                break;
            }
        }
    }

    protected boolean initRooms() {

        rooms = new HashSet<>();
        split( new Rect( 0, 0, WIDTH - 1, HEIGHT - 1 ) );

        if (rooms.size() < 8) {
            return false;
        }

        Room[] ra = rooms.toArray( new Room[0] );
        for (int i=0; i < ra.length-1; i++) {
            for (int j=i+1; j < ra.length; j++) {
                ra[i].addNeigbour( ra[j] );
            }
        }

        return true;
    }

    protected boolean assignRoomType() {

        int specialRooms = 0;
        if (Dungeon.depth < Dungeon.altarLevel) {
            // make sure that this room doesn't appear too early
            specials.remove( Room.Type.ALTAR );
        }

        if (feeling == Feeling.BURNT) {
            specials.remove( Room.Type.GARDEN );
        }

        for (Room r : rooms) {
            if (r.type == Room.Type.NULL &&
                    r.connected.size() == 1) {

                if (specials.size() > 0 &&
                        r.width() > 3 && r.height() > 3 &&
                        Random.Int( specialRooms * specialRooms + 2 ) < 2) {

                    if (Dungeon.depth % 10 == 2 && specials.contains( Room.Type.LABORATORY )) {

                        r.type = Room.Type.LABORATORY;

                    } else if (Dungeon.depth >= Dungeon.transmutation && specials.contains( Room.Type.MAGIC_WELL )) {

                        r.type = Room.Type.MAGIC_WELL;

                    } else if (Dungeon.depth >= Dungeon.altarLevel && specials.contains( Room.Type.ALTAR )) {

                        r.type = Room.Type.ALTAR;
                        Dungeon.altarLevel = Dungeon.depth - (Dungeon.depth % 10) + 15; // altars on levels 5, 15, 25...


                    } else {

                        int n = specials.size();
                        r.type = specials.get( Math.min( Random.Int( n ), Random.Int( n ) ) );
                        if (r.type == Room.Type.WEAK_FLOOR) {
                            weakFloorCreated = true;


                        }
                    }

                    Room.useType( r.type );
                    specials.remove( r.type );
                    specialRooms++;

                } else if (Random.Int( 2 ) == 0){

                    HashSet<Room> neigbours = new HashSet<>();
                    for (Room n : r.neigbours) {
                        if (!r.connected.containsKey( n ) &&
                                !Room.SPECIALS.contains( n.type ) &&
                                n.type != Room.Type.PIT) {

                            neigbours.add( n );
                        }
                    }
                    if (neigbours.size() > 1) {
                        r.connect( Random.element( neigbours ) );
                    }
                }
            }
        }

        int count = 0;
        for (Room r : rooms) {
            if (r.type == Room.Type.NULL) {
                int connections = r.connected.size();
                if (connections > 0) {
                    if (Random.Int(connections * connections) == 0) {
                        r.type = Room.Type.STANDARD;
                        count++;
                    } else {
                        r.type = Room.Type.TUNNEL;
                    }
                }
            }
        }

        while (count < 6) { // convert 6 random tunnels into standard rooms
            Room r = randomRoom( Room.Type.TUNNEL, 1 );
            if (r != null) {
                r.type = Room.Type.STANDARD;
                count++;
            }
        }

        return true;
    }

    protected void paintWater() {
        boolean[] lake = water();
        for (int i=0; i < LENGTH; i++) {
            if (map[i] == Terrain.EMPTY && lake[i]) {
                map[i] = Terrain.WATER;
            }
        }
    }

    protected void paintGrass() {
        boolean[] grass = grass();

        if (feeling == Feeling.GRASS || feeling == Feeling.BURNT) {

            for (Room room : rooms) {
                if (room.type != Room.Type.NULL && room.type != Room.Type.PASSAGE && room.type != Room.Type.TUNNEL) {
                    grass[(room.left + 1) + (room.top + 1) * WIDTH] = true;
                    grass[(room.right - 1) + (room.top + 1) * WIDTH] = true;
                    grass[(room.left + 1) + (room.bottom - 1) * WIDTH] = true;
                    grass[(room.right - 1) + (room.bottom - 1) * WIDTH] = true;
                }
            }
        }

        for (int i=WIDTH+1; i < LENGTH-WIDTH-1; i++) {
            if (map[i] == Terrain.EMPTY && grass[i]) {
                int count = 1;
                for (int n : NEIGHBOURS8) {
                    if (grass[i + n]) {
                        count++;
                    }
                }
                map[i] = (Random.Float() < count / 12f) ? Terrain.HIGH_GRASS : Terrain.GRASS;
            }
        }
    }

    protected boolean[] water() {
        return Patch.generate( feeling == Feeling.WATER ? 0.60f : 0.45f, 5 );
    }

    protected boolean[] grass() {
        return Patch.generate( (feeling == Feeling.GRASS || feeling == Feeling.BURNT) ? 0.60f : 0.40f, 4 );
    }

    protected void placeTraps() {

        int nTraps = nTraps();
        float[] trapChances = trapChances();

        for (int i=0; i < nTraps; i++) {

            int trapPos = Random.Int( LENGTH );

            if (map[trapPos] == Terrain.EMPTY) {
                map[trapPos] = Terrain.SECRET_TRAP;
                switch (Random.chances( trapChances )) {
                    case 0:
                        setTrap( new ToxicTrap().hide(), trapPos);
                        break;
                    case 1:
                        setTrap( new FireTrap().hide(), trapPos);
                        break;
                    case 2:
                        setTrap( new ParalyticTrap().hide(), trapPos);
                        break;
                    case 3:
                        setTrap( new PoisonTrap().hide(), trapPos);
                        break;
                    case 4:
                        setTrap( new AlarmTrap().hide(), trapPos);
                        break;
                    case 5:
                        setTrap( new LightningTrap().hide(), trapPos);
                        break;
                    case 6:
                        setTrap( new GrippingTrap().hide(), trapPos);
                        break;
                    case 7:
                        setTrap( new LightningTrap().hide(), trapPos);
                        break;
                }
            }
        }
    }

    protected int nTraps() {
        return Dungeon.depth <= 1 ? 0 : Random.Int( 1, rooms.size() + Math.max(Dungeon.depth, 20) );
    }

    protected float[] trapChances() {
        float[] chances = { 1, 1, 1, 1, 1, 1, 1, 1 };
        return chances;
    }

    protected int minRoomSize = 7;
    protected int maxRoomSize = 9;

    protected void split( Rect rect ) {

        int w = rect.width();
        int h = rect.height();

        if (w > maxRoomSize && h < minRoomSize) {

            int vw = Random.Int( rect.left + 3, rect.right - 3 );
            split( new Rect( rect.left, rect.top, vw, rect.bottom ) );
            split( new Rect( vw, rect.top, rect.right, rect.bottom ) );

        } else
        if (h > maxRoomSize && w < minRoomSize) {

            int vh = Random.Int( rect.top + 3, rect.bottom - 3 );
            split( new Rect( rect.left, rect.top, rect.right, vh ) );
            split( new Rect( rect.left, vh, rect.right, rect.bottom ) );

        } else
        if ((Math.random() <= (minRoomSize * minRoomSize / rect.square()) && w <= maxRoomSize && h <= maxRoomSize) || w < minRoomSize || h < minRoomSize) {

            rooms.add( (Room)new Room().set( rect ) );

        } else {

            if (Random.Float() < (float)(w - 2) / (w + h - 4)) {
                int vw = Random.Int( rect.left + 3, rect.right - 3 );
                split( new Rect( rect.left, rect.top, vw, rect.bottom ) );
                split( new Rect( vw, rect.top, rect.right, rect.bottom ) );
            } else {
                int vh = Random.Int( rect.top + 3, rect.bottom - 3 );
                split( new Rect( rect.left, rect.top, rect.right, vh ) );
                split( new Rect( rect.left, vh, rect.right, rect.bottom ) );
            }

        }
    }

    protected void paint() {

        for (Room r : rooms) {
            if (r.type != Room.Type.NULL) {
                placeDoors( r );
                r.type.paint( this, r );
            } else {
                if (feeling == Feeling.CHASM && Random.Int( 2 ) == 0) {
                    Painter.fill(this, r, Terrain.WALL);
                }
            }
        }

        for (Room r : rooms) {
            paintDoors( r );
        }
    }

    private void placeDoors( Room r ) {
        for (Room n : r.connected.keySet()) {
            Room.Door door = r.connected.get( n );
            if (door == null) {

                Rect i = r.intersect( n );
                if (i.width() == 0) {
                    door = new Room.Door(
                            i.left,
                            Random.Int( i.top + 1, i.bottom ) );
                } else {
                    door = new Room.Door(
                            Random.Int( i.left + 1, i.right ),
                            i.top);
                }

                r.connected.put( n, door );
                n.connected.put( r, door );
            }
        }
    }

    protected void paintDoors( Room r ) {
        for (Room n : r.connected.keySet()) {

            if (joinRooms( r, n )) {
                continue;
            }

            Room.Door d = r.connected.get( n );
            int door = d.x + d.y * WIDTH;

            switch (d.type) {
                case EMPTY:
                    map[door] = Terrain.EMPTY;
                    break;
                case TUNNEL:
                    map[door] =  tunnelTile();
                    break;
                case REGULAR:
                    if (Dungeon.depth <= 1) {
                        map[door] = Terrain.DOOR;
                    } else {
                        boolean secret = (Random.Int( 6 )) == 0;
                        map[door] = secret ? Terrain.SECRET_DOOR : Terrain.DOOR;
                        if (secret) {
                            secretDoors++;
                        }
                    }
                    break;
                case UNLOCKED:
                    map[door] = Terrain.DOOR;
                    break;
                case ARCHWAY:
                    map[door] = Terrain.ARCHWAY;
                    break;
                case HIDDEN:
                    map[door] = Terrain.SECRET_DOOR;
                    break;
                case BARRICADE:
                    map[door] = Random.Int( 3 ) == 0 ? Terrain.BOOKSHELF : Terrain.BARRICADE;
                    break;
                case LOCKED:
                    map[door] = Terrain.LOCKED_DOOR;
                    break;
            }
        }
    }

    protected boolean joinRooms( Room r, Room n ) {

        if (r.type != Room.Type.STANDARD || n.type != Room.Type.STANDARD) {
            return false;
        }

        Rect w = r.intersect( n );
        if (w.left == w.right) {

            if (w.bottom - w.top < 3) {
                return false;
            }

            if (w.height() == Math.max( r.height(), n.height() )) {
                return false;
            }

            if (r.width() + n.width() > maxRoomSize) {
                return false;
            }

            w.top += 1;
            w.bottom -= 0;

            w.right++;

            Painter.fill( this, w.left, w.top, 1, w.height(), Terrain.EMPTY );

        } else {

            if (w.right - w.left < 3) {
                return false;
            }

            if (w.width() == Math.max( r.width(), n.width() )) {
                return false;
            }

            if (r.height() + n.height() > maxRoomSize) {
                return false;
            }

            w.left += 1;
            w.right -= 0;

            w.bottom++;

            Painter.fill( this, w.left, w.top, w.width(), 1, Terrain.EMPTY );
        }

        return true;
    }

    @Override
    public int nMobs() {
        return 3 + Dungeon.depth % 5 + Random.Int(6);
    }

    @Override
    protected void createMobs() {
        if (Dungeon.depth % 5 == 1) {
            InfiniteBestiary.pickNewTheme();
        }

        //on floor 1, enough mobs are created so the player can get level 2.
        int mobsToSpawn = Dungeon.depth == 1 ? 13 : nMobs();

        HashSet<Room> stdRooms = new HashSet<>();
        for (Room room : rooms) {
            if (room.type == Room.Type.STANDARD) stdRooms.add(room);
        }
        Iterator<Room> stdRoomIter = stdRooms.iterator();

        while (mobsToSpawn > 0) {
            if (!stdRoomIter.hasNext())
                stdRoomIter = stdRooms.iterator();
            Room roomToSpawn = stdRoomIter.next();

            Mob mob = InfiniteBestiary.mob(Dungeon.depth);
            if (mob != null) {
                mob.infiniteScaleMob(Dungeon.depth);
                mob.pos = roomToSpawn.random();

                if (findMob(mob.pos) == null && Level.passable[mob.pos]) {
                    mobsToSpawn--;
                    mobs.add(mob);
                }
            }
        }

        Mob miniBoss = null;
        if (Dungeon.depth % 10 == 5) {
            switch (Random.Int(6)) {
                case 0:
                    miniBoss = new GnollTrickster();
                    break;
                case 1:
                    miniBoss = new GreatCrab();
                    break;
                case 2:
                    miniBoss = new Tinkerer();
                    break;
                case 3:
                    miniBoss = new Minotaur();
                    break;
                case 4:
                    miniBoss = new FetidRat();
                    break;
                default:
                    miniBoss = new ChaosMage();
                    break;
            }
            miniBoss.infiniteScaleMob(Dungeon.depth + 5);
            do {
                miniBoss.pos = randomRespawnCell();
            } while (miniBoss.pos == -1);
            mobs.add(miniBoss);
        }
    }

    @Override
    public int randomRespawnCell() {
        int count = 0;
        int cell = -1;

        while (true) {

            if (++count > 30) {
                return -1;
            }

            Room room = randomRoom(Room.Type.STANDARD, 10);
            if (room == null) {
                continue;
            }

            cell = room.random();
            if (!Dungeon.visible[cell] && Actor.findChar(cell) == null && Level.passable[cell]) {
                return cell;
            }

        }
    }

    @Override
    public int randomDestination() {

        int cell = -1;

        while (true) {

            Room room = Random.element(rooms);
            if (room == null) {
                continue;
            }

            cell = room.random();
            if (Level.passable[cell]) {
                return cell;
            }

        }
    }

    @Override
    protected void createItems() {

        int nItems = (Dungeon.depth == 1) ? 3 : 4;
        int bonus = 0;
        for (Buff buff : Dungeon.hero.buffs(RingOfWealth.Wealth.class)) {
            bonus += ((RingOfWealth.Wealth) buff).level;
        }
        //just incase someone gets a ridiculous ring, cap this at 80%
        bonus = Math.min(bonus, 9);
        while (Random.Float() < (0.35f + bonus*0.05f)) {
            nItems++;
        }

        for (int i=0; i < nItems; i++) {
            Heap.Type type = null;
            switch (Random.Int( 20 )) {
                case 0:
                    type = Heap.Type.SKELETON;
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    if (feeling == Feeling.BURNT) {
                        // burnt levels drop skeletons instead of chests
                        type = Heap.Type.SKELETON;
                    } else {
                        type = Heap.Type.CHEST;
                    }
                    break;
                case 5:
                    type = Dungeon.depth > 1 ? Heap.Type.MIMIC : Heap.Type.CHEST;
                    break;
                default:
                    type = Heap.Type.HEAP;
            }
            drop( Generator.random(), randomDropCell() ).type = type;
        }

        for (Item item : itemsToSpawn) {
            int cell = randomDropCell();
            if (item instanceof Scroll) {
                while ((map[cell] == Terrain.TRAP || map[cell] == Terrain.SECRET_TRAP)
                        && traps.get( cell ) instanceof FireTrap) {
                    cell = randomDropCell();
                }
            }
            drop( item, cell ).type = Heap.Type.HEAP;
        }

        Item item = Bones.get();
        if (item != null) {
            drop( item, randomDropCell() ).type = Heap.Type.REMAINS;
        }
    }

    protected Room randomRoom( Room.Type type, int tries ) {
        for (int i=0; i < tries; i++) {
            Room room = Random.element( rooms );
            if (room.type == type) {
                return room;
            }
        }
        return null;
    }

    public Room room( int pos ) {
        for (Room room : rooms) {
            if (room.type != Room.Type.NULL && room.inside( pos )) {
                return room;
            }
        }

        return null;
    }

    protected int randomDropCell() {
        while (true) {
            Room room = randomRoom( Room.Type.STANDARD, 1 );
            if (room != null) {
                int pos = room.random();
                if (passable[pos]) {
                    return pos;
                }
            }
        }
    }

    @Override
    public int pitCell() {
        for (Room room : rooms) {
            if (room.type == Room.Type.PIT) {
                return room.random();
            }
        }

        return super.pitCell();
    }

    @Override
    public void storeInBundle( Bundle bundle) {
        super.storeInBundle( bundle );
        bundle.put( "rooms", rooms );
    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);

        rooms = new HashSet<>( (Collection<Room>) ((Collection<?>) bundle.getCollection( "rooms" )) );
        for (Room r : rooms) {
            if (r.type == Room.Type.WEAK_FLOOR) {
                weakFloorCreated = true;
                break;
            }
        }
    }

    @Override
    public void addVisuals( Scene scene ) {
        switch (THEME) {
            case 0: // sewers
                SewerLevel.addVisuals( this, scene );
                break;
            case 1: // prison
                PrisonLevel.addVisuals( this, scene );
                break;
            case 2: // caves
                CavesLevel.addVisuals( this, scene );
                break;
            case 3: // city
                CityLevel.addVisuals( this, scene );
                break;
            case 4: // frozen
                FrozenLevel.addVisuals(this, scene);
                break;
            default: // halls
                HallsLevel.addVisuals( this, scene );
                break;
        }
    }
}
