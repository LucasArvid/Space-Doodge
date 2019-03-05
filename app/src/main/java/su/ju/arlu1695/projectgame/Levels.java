/*
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

    public Levels(Context context) {

        this.context = context;
    }


    public void readLevelData(Obstacles obstacles,int selectedLevel) {
        InputStream is = context.getResources().openRawResource(R.raw.levels);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";

        try {
            // Find correct level.
            for (int i = 0; i <= selectedLevel; i++) {
                reader.readLine();
            }
            while ((line = reader.readLine()) != null) {
                Log.d("My Activity", "line: " + line);

                String[] tokens = line.split(",");
                // Give the obstacles the right cordinates.
                for (int i = 0; i <= (tokens.length-1); i+=2) {
                    obstacles.array[i/2].setLeftPosX(Integer.parseInt(tokens[i+1]),Integer.parseInt(tokens[i]));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
*/