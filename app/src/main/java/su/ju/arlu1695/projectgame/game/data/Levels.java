/*
    This class grabs the data contained in the local raw resources csv file.
    This file contains the data for each level.
    Object height, object gap, player gap and bg color in rgb format.
 */
package su.ju.arlu1695.projectgame.game.data;



import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import su.ju.arlu1695.projectgame.R;
import su.ju.arlu1695.projectgame.utils.Constants;


public class Levels {

    private Context context;

    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int r,g,b;

    public Levels(Context context) {

        this.context = context;
    }


    public void readLevelData() {
        InputStream is = context.getResources().openRawResource(R.raw.levels);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";

        try {
            // Find correct level.
            for (int i = 0; i <= Constants.LEVEL_SELECTED; i++) {
                reader.readLine();
            }
            while ((line = reader.readLine()) != null) {
                Log.d("My Activity", "line: " + line);

                String[] tokens = line.split(",");

                // Store level data.
                playerGap = Integer.parseInt(tokens[0]);
                obstacleGap = Integer.parseInt(tokens[1]);
                obstacleHeight = Integer.parseInt(tokens[2]);

                r = Integer.parseInt(tokens[3]);
                g = Integer.parseInt(tokens[4]);
                b = Integer.parseInt(tokens[5]);

                break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getPlayerGap (){
        return playerGap;
    }


    public int getObstacleGap() {
        return obstacleGap;
    }


    public int getObstacleHeight() {
        return obstacleHeight;
    }


    public int getR() {
        return r;
    }


    public int getG() {
        return g;
    }


    public int getB() {
        return b;
    }


}
