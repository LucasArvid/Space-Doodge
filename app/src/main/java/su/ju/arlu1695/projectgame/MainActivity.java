/*
 Level design assets credit : www.kenney.nl
 Usage allowed for personal and/or commercial projects.
 */
package su.ju.arlu1695.projectgame;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import su.ju.arlu1695.projectgame.activities.GameModeActivity;
import su.ju.arlu1695.projectgame.activities.LeaderboardActivity;
import su.ju.arlu1695.projectgame.activities.SettingsActivity;
import su.ju.arlu1695.projectgame.utils.Constants;
import su.ju.arlu1695.projectgame.R;

import static su.ju.arlu1695.projectgame.utils.Constants.CHANNEL_DESC;
import static su.ju.arlu1695.projectgame.utils.Constants.CHANNEL_ID;
import static su.ju.arlu1695.projectgame.utils.Constants.CHANNEL_NAME;


public class MainActivity extends AppCompatActivity {

    private String gameId;
    private String me;
    private int score;
    private String opponentScore;
    private String wonOrLost;
    private String mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // setup default Constants, grab device dimensions.
        Constants.setupContext(this);
        Constants.startMediaPlayer(R.raw.soft_and_furious_06_and_never_come_back);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        createNotificationChannel(); // Required for API 26+

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

    /* Create the NotificationChannel for API 26+ because
       the NotificationChannel class is new and not in the support library */
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = CHANNEL_DESC;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            channel.setShowBadge(true);

            // Register the channel with the system
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
