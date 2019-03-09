
package su.ju.arlu1695.projectgame;



import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;


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
                // Give the obstacles the right cordinates.
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
