package su.ju.arlu1695.projectgame;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.annotation.RawRes;

import java.util.ArrayList;

public class Constants {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static int LEVEL_SELECTED;

    private static  MediaPlayer mediaPlayer;
    private static Context mainContext; // context for media player

    public static Context GAME_CONTEXT;

    // http address for notifications request from backend.
    public static final String FIREBASE_CLOUD_FUNCTIONS_BASE = "https://us-central1-icy-slide.cloudfunctions.net";

    // Channel constants needed for Notification channel on API 26+
    public static final String CHANNEL_ID = "icy_slide";
    public static final String CHANNEL_NAME = "Icy Slide";
    public static final String CHANNEL_DESC = "Icy Slide Notifications";

    // Local tracking of current user. Used for e.g. sending notifications
    public static User thisUser = new User();

    public static boolean ALLOW_INVITES = true;
    public static boolean ALLOW_MUSIC = true;
    public static boolean ALLOW_SOUND = true;

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
