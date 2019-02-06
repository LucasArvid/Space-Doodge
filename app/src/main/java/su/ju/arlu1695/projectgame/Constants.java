package su.ju.arlu1695.projectgame;

import java.util.ArrayList;

public class Constants {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static int SCREEN_WIDTH_COEFFICIENT;
    public static int SCREEN_HEIGHT_COEFFICIENT;

    public static ArrayList<levelName> levels = new ArrayList<>();
    static {
        levels.add(new levelName("Do A"));
        levels.add(new levelName("Do B"));
        levels.add(new levelName("Do C"));
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
