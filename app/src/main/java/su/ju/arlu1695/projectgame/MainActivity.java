package su.ju.arlu1695.projectgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_HEIGHT_COEFFICIENT = Constants.SCREEN_HEIGHT / 13;
        Constants.SCREEN_WIDTH_COEFFICIENT = Constants.SCREEN_WIDTH / 10;

    }

    public void startGameButtonClicked(View view) {
        Intent intent = new Intent(this, GameMode.class);
        startActivity(intent);
    }

    public void settingsButtonClicked(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void leaderboardButtonClicked(View view) {
        Intent intent = new Intent(this, Leaderboard.class);
        startActivity(intent);
    }



}
