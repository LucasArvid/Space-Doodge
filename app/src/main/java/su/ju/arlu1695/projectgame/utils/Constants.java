/*
This class holds all the constants that are used throughout the whole application
to avoid having to send or parse the database for the same values every time it is used in a new activity
 */

package su.ju.arlu1695.projectgame.utils;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

import su.ju.arlu1695.projectgame.game.GameThread;

public class Constants {

    // Device dimensions
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    /* Keeps track of the level selected, since its being used in alot of classes much deeper
       in the hierarchy from where it was first set.
     */
    public static int LEVEL_SELECTED;

    // Music player
    private static  MediaPlayer mediaPlayer;
    private static Context mainContext; // context for media player

    // Context used throughout game classes
    public static Context GAME_CONTEXT;

    // http address for notifications request from backend.
    public static final String FIREBASE_CLOUD_FUNCTIONS_BASE = "https://us-central1-icy-slide.cloudfunctions.net";

    // Channel constants needed for Notification channel on API 26+
    public static final String CHANNEL_ID = "icy_slide";
    public static final String CHANNEL_NAME = "Icy Slide";
    public static final String CHANNEL_DESC = "Icy Slide Notifications";

    // Local tracking of current user. Used for e.g. sending notifications
    public static String currentUser;

    // Booleans set in settings activity.
    public static boolean ALLOW_INVITES = true;
    public static boolean ALLOW_MUSIC = true;

    // Global connection to the GameThread running the gameView, used to join
    // the thread outside of the game activity.
    public static GameThread thread;


    // Level select array list.
    public static ArrayList<levelName> levels = new ArrayList<>();
    static {
        levels.add(new levelName("Level 1"));
        levels.add(new levelName("Level 2"));
        levels.add(new levelName("Level 3"));
        levels.add(new levelName("Level 4"));
        levels.add(new levelName("Level 5"));
    }


    public static class levelName {
        public String title;
        public levelName(String title) { this.title = title; }
        @Override
        public String toString() {
            return title;
        }
    }

    // filePath = 0 for simple resume
    public static void startMediaPlayer(int filePath) {
        if(!ALLOW_MUSIC)
            return;
        if (mediaPlayer == null && filePath != 0) {
            mediaPlayer = MediaPlayer.create(mainContext, filePath);
            mediaPlayer.isLooping();
        }
        mediaPlayer.start();

    }

    public static void stopMediaPlayer() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void pauseMediaPlayer() {
        if(mediaPlayer!= null)
            mediaPlayer.pause();
    }

    public static void setupContext(Context context) {
        mainContext = context;

    }
}
