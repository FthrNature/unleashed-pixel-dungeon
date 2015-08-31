/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015 David Mitchell
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

// NOTE - the idea behind Load/Save functionality was originated in Soft Pixel Dungeon
package com.shatteredpixel.pixeldungeonunleashed.scenes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.GamesInProgress;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.ui.Archs;
import com.shatteredpixel.pixeldungeonunleashed.ui.RedButton;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndOptions;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndStory;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class LoadSaveScene extends PixelScene {

    private static final float BUTTON1_WIDTH = 34;
    private static final float BUTTON2_WIDTH = 55;
    private static final float BUTTON_HEIGHT = 20;
    private static final float BUTTON_PADDING = 3;

    private static final String TXT_TITLE	= "Save/Load ";

    private static final String TXT_LOAD	= "Load";
    private static final String TXT_SAVE	= "Save";
    private static final String TXT_SLOTNAME= "Game";

    private static final String HERO		= "hero";
    private static final String DEPTH		= "depth";
    private static final String DIFLEV      = "diflev";
    private static final String LEVEL		= "lvl";

    private static final String TXT_REALLY	= "Load";
    private static final String TXT_WARNING	= "Your current progress will be lost and Difficulty Levels may be changed.";
    private static final String TXT_YES		= "Yes, load " + TXT_SLOTNAME;
    private static final String TXT_NO		= "No, return to main menu";

    private static final String TXT_DPTH_LVL	= "Depth: %d, level: %d";

    private static final int CLR_WHITE	= 0xFFFFFF;


    @Override
    public void create() {

        super.create();

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize( w, h );
        add( archs );

        String showClassName = capitalizeWord(Dungeon.hero.heroClass.title());

        String diffLevel;
        String saveInstructions;
        switch (Dungeon.difficultyLevel) {
            case Dungeon.DIFF_TUTOR:
                diffLevel = "TUTORIAL";
                saveInstructions = "In Tutorial mode you may save anywhere.";
                break;
            case Dungeon.DIFF_EASY:
                diffLevel = "EASY";
                saveInstructions = "In Easy mode you may save anywhere.";
                break;
            case Dungeon.DIFF_HARD:
                diffLevel = "HARD";
                saveInstructions = "In Hard mode you may save after a boss level.";
                break;
            case Dungeon.DIFF_NTMARE:
                diffLevel = "NIGHTMARE";
                saveInstructions = "Games may not be saved in Nightmare mode.";
                break;
            default:
                diffLevel = "NORMAL";
                saveInstructions = "In Normal mode you may save at level entrance signs.";
                break;
        }
        BitmapText title = PixelScene.createText( TXT_TITLE + showClassName + " - " + diffLevel, 9 );
        title.hardlight( Window.TITLE_COLOR );
        title.measure();
        title.x = align( (w - title.width()) / 2 );
        title.y = BUTTON_PADDING;
        add( title );

        String currentProgress = "dead";
        GamesInProgress.Info info = GamesInProgress.check(Dungeon.hero.heroClass);
        if (info != null) {
            currentProgress = Utils.format(TXT_DPTH_LVL, info.depth, info.level);
        }
        BitmapText subTitle = PixelScene.createText( "Currently " + currentProgress, 6 );
        subTitle.hardlight(Window.TITLE_COLOR);
        subTitle.measure();
        subTitle.x = align( (w - title.width()) / 2 );
        subTitle.y =  (BUTTON_HEIGHT / 2) + BUTTON_PADDING ;
        add(subTitle);

        BitmapText saveInfo = PixelScene.createText( saveInstructions, 6 );
        saveInfo.hardlight( Window.TITLE_COLOR );
        saveInfo.measure();
        saveInfo.x = align( (w - saveInfo.width()) / 2 );
        saveInfo.y = BUTTON_PADDING + BUTTON_HEIGHT;
        add( saveInfo );



        int posY = (int) (BUTTON_HEIGHT + (BUTTON_PADDING * 3.5f));
        int posX2 = w - (int) (BUTTON2_WIDTH + BUTTON_PADDING);
        int posX = (int) (BUTTON1_WIDTH + (BUTTON_PADDING * 3));

        String[] classList = { "warrior", "mage", "rogue", "huntress" };
        String[] slotList = { "A", "B", "C", "D", "E" };

        for (String classInfo : classList) {
            if (Dungeon.hero.heroClass.title().equals(classInfo)) {

                for (String saveSlot : slotList) {
                    // add the row caption..
                    BitmapText buttonCapton1 = PixelScene.createText( TXT_SLOTNAME + " " + saveSlot, 9 );
                    buttonCapton1.hardlight( CLR_WHITE );
                    buttonCapton1.measure();
                    buttonCapton1.x = BUTTON_PADDING;
                    buttonCapton1.y = posY + (BUTTON_HEIGHT/3);
                    add( buttonCapton1 );

                    // add the save button..
                    if (Dungeon.hero.isAlive() &&
                            (Dungeon.difficultyLevel <= Dungeon.DIFF_EASY) ||
                            (Dungeon.difficultyLevel <= Dungeon.DIFF_HARD && Dungeon.level.isAdjacentTo(Dungeon.hero.pos, Terrain.SIGN))) {

                        GameButton btnSave = new GameButton( this, true, TXT_SAVE, "", classInfo, saveSlot );
                        add( btnSave );
                        btnSave.visible = true;
                        btnSave.setRect(posX, posY, BUTTON1_WIDTH, BUTTON_HEIGHT);
                    }

                    // add the load button if there are saved files to load..
                    String saveSlotFolder = Game.instance.getFilesDir().toString() + "/" + classInfo + saveSlot;

                    File backupFolder = new File(saveSlotFolder);
                    if (backupFolder.exists()) {
                        FileInputStream input;
                        try {
                            input = new FileInputStream(saveSlotFolder +"/" + classInfo +".dat");
                            Bundle bundle = Bundle.read( input );
                            input.close();
                            int savedDepth = bundle.getInt( DEPTH, 1 );
                            Bundle savedHero = bundle.getBundle( HERO );
                            int savedDif = bundle.getInt(DIFLEV);
                            int savedLevel = savedHero.getInt( LEVEL );
                            String savedProgress = Utils.format( TXT_DPTH_LVL, savedDepth, savedLevel );
                            String loadLevel;
                            switch (savedDif) {
                                case Dungeon.DIFF_TUTOR:
                                    loadLevel = TXT_LOAD + " TUTOR";
                                    break;
                                case Dungeon.DIFF_EASY:
                                    loadLevel = TXT_LOAD + " EASY";
                                    break;
                                case Dungeon.DIFF_HARD:
                                    loadLevel = TXT_LOAD + " HARD";
                                    break;
                                case Dungeon.DIFF_NTMARE:
                                    loadLevel = TXT_LOAD + " NTMARE";
                                    break;
                                default:
                                    loadLevel = TXT_LOAD + " NORMAL";
                                    break;
                            }
                            GameButton btnLoad1A = new GameButton( this, false, loadLevel , savedProgress, classInfo, saveSlot );

                            add( btnLoad1A );
                            btnLoad1A.visible = true;
                            btnLoad1A.setRect(posX2, posY, (int) (BUTTON2_WIDTH), BUTTON_HEIGHT);
                        } catch (FileNotFoundException e) {
                            //e.printStackTrace();
                        } catch (IOException e) {
                            //e.printStackTrace();
                        } catch (NullPointerException e ) {
                            //e.printStackTrace();
                        }
                    }
                    // move down the line now...
                    posY += BUTTON_HEIGHT + BUTTON_PADDING;
                }
            }
        }

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
        Game.switchScene( InterlevelScene.class );
    }

    protected static void exportGames(String classInfo, String saveSlot) {
        ArrayList<String> files = new ArrayList<String>();
        String saveSlotFolder = Game.instance.getFilesDir().toString() + "/" + classInfo + saveSlot;
        makeFolder(saveSlotFolder);

        for(String fileName : Game.instance.fileList()){
            if(isGameLevelFile(classInfo, fileName)){
                files.add(fileName);
            }
        }

        // remove previous saved game files..
        File backupFolder = new File(saveSlotFolder);

        for(File backupFile : backupFolder.listFiles()){
            if(isGameLevelFile(classInfo, backupFile.getName())){
                backupFile.delete();
            }
        }

        for (String fileName : files){
            try {
                FileInputStream in = Game.instance.openFileInput(fileName);
                OutputStream out = new FileOutputStream(saveSlotFolder + "/" + fileName);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
            catch(Exception e){
                e.printStackTrace();
                WndStory.showChapter("Failed to save file " + fileName);
                //Log.d("FAILED EXPORT", f);
            }
        }
        InterlevelScene.mode = InterlevelScene.Mode.SAVE;
        Game.switchScene( InterlevelScene.class );
    }

    private static boolean isGameLevelFile(String classInfo, String fileName) {
        return fileName.endsWith(".dat") && (fileName.startsWith(classInfo));
    }

    private static void makeFolder(String saveSlotFolder) {
        File dir = new File(saveSlotFolder);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    protected static void importGames(String classInfo, String saveSlot) {
        ArrayList<String> files = new ArrayList<String>();
        String saveSlotFolder = Game.instance.getFilesDir().toString() + "/" + classInfo + saveSlot;
        File backupFolder = new File(saveSlotFolder);

        for(File backupFile : backupFolder.listFiles()){
            if(isGameLevelFile(classInfo, backupFile.getName())){
                files.add(backupFile.getName());
            }
        }

        // remove in progress game files..
        for(String fileName : Game.instance.fileList()){
            if(fileName.startsWith("game_") || isGameLevelFile(classInfo, fileName)){
                Game.instance.deleteFile(fileName);
            }
        }


        for (String fileName : files){
            try {
                FileInputStream in = new FileInputStream(saveSlotFolder + "/" + fileName); //
                OutputStream out = Game.instance.openFileOutput(fileName, Game.MODE_PRIVATE );

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
            catch(Exception e){
                e.printStackTrace();
                WndStory.showChapter("Failed to load file " + fileName);
            }
        }
        InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
        Game.switchScene( InterlevelScene.class );
    }

    private static class GameButton extends RedButton {

        private static final int SECONDARY_COLOR	= 0xCACFC2;

        private BitmapText secondary;
        private Boolean isSave = true;
        private String classInfo = "";
        private String saveSlot = "";
        private LoadSaveScene loadSaveScene;

        public GameButton(LoadSaveScene loadSaveScene, Boolean isSave, String primary, String secondary, String classInfo, String saveSlot ) {
            super( primary );
            this.secondary( secondary );
            this.isSave = isSave;
            this.classInfo = classInfo;
            this.saveSlot = saveSlot;
            this.loadSaveScene = loadSaveScene;
        }
        @Override
        protected void onClick() {
            if (isSave) {
                exportGames(classInfo, saveSlot);
            } else {
                loadSaveScene.add( new WndOptions( TXT_REALLY + " " +saveSlot + " " + secondary.text() + "?", TXT_WARNING, TXT_YES + " " + saveSlot, TXT_NO ) {
                    @Override
                    protected void onSelect( int index ) {
                        if (index == 0) {
                            importGames(classInfo, saveSlot);
                        }
                    }
                } );
            }
        };

        @Override
        protected void createChildren() {
            super.createChildren();

            secondary = createText( 6 );
            secondary.hardlight( SECONDARY_COLOR );
            add( secondary );
        }

        @Override
        protected void layout() {
            super.layout();

            if (secondary.text().length() > 0) {
                text.y = y + (height - text.height() - secondary.baseLine()) / 2;

                secondary.x = align( x + (width - secondary.width()) / 2 );
                secondary.y = align( text.y + text.height() );
            } else {
                text.y = y + (height - text.baseLine()) / 2;
            }
        }

        public void secondary( String text ) {
            secondary.text( text );
            secondary.measure();
        }

    }
    public static String capitalizeWord(String oneWord)
    {
        return Character.toUpperCase(oneWord.charAt(0)) + oneWord.substring(1);
    }
}