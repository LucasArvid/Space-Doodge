package su.ju.arlu1695.projectgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import su.ju.arlu1695.projectgame.activities.GameModeActivity;
import su.ju.arlu1695.projectgame.activities.LeaderboardActivity;
import su.ju.arlu1695.projectgame.activities.SettingsActivity;
import su.ju.arlu1695.projectgame.utils.Constants;
import su.ju.arlu1695.projectgame.utils.Util;
/*
 Level design assets credit : www.kenney.nl
 Usage allowed for personal and/or commercial projects.
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Setup default Constants, grab device dimensions.
        Constants.setupContext(this);
        Constants.startMediaPlayer(R.raw.soft_and_furious_06_and_never_come_back);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;

        Util.setCurrentUserName(); // Enables notifications if already logged in on earlier use of app
        Util.createNotificationChannel(this); // Required for notifications if on API 26+

    }

    public void startGameButtonClicked(View view) {
        Intent intent = new Intent(this, GameModeActivity.class);
        startActivity(intent);
    }


    public void settingsButtonClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    public void leaderboardButtonClicked(View view) {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }


    @Override
    public void onPause() {
        super.onPause();
        Constants.pauseMediaPlayer();
    }


    @Override
    public void onResume() {
        super.onResume();
        Constants.startMediaPlayer(0);
    }


}
