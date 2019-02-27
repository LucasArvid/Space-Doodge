package su.ju.arlu1695.projectgame;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;

import static su.ju.arlu1695.projectgame.Constants.CHANNEL_DESC;
import static su.ju.arlu1695.projectgame.Constants.CHANNEL_ID;
import static su.ju.arlu1695.projectgame.Constants.CHANNEL_NAME;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.setDefaultSettings();
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_HEIGHT_COEFFICIENT = Constants.SCREEN_HEIGHT / 13;
        Constants.SCREEN_WIDTH_COEFFICIENT = Constants.SCREEN_WIDTH / 10;
        createNotificationChannel();

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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = CHANNEL_DESC;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                                              NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            channel.setShowBadge(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviours after this
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}
