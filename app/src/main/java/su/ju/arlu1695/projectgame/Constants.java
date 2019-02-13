package su.ju.arlu1695.projectgame;

import java.util.ArrayList;

public class Constants {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static int SCREEN_WIDTH_COEFFICIENT;
    public static int SCREEN_HEIGHT_COEFFICIENT;

    public static int LEVEL_SELECTED;

    public static final String FIREBASE_CLOUD_FUNCTIONS_BASE = "https://us-central1-icy-slide.cloudfunctions.net";

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
}
