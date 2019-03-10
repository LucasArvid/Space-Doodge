/*
 Level design assets credit : www.kenney.nl
 Usage allowed for personal and/or commercial projects.
 */
package su.ju.arlu1695.projectgame;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import static su.ju.arlu1695.projectgame.Constants.CHANNEL_DESC;
import static su.ju.arlu1695.projectgame.Constants.CHANNEL_ID;
import static su.ju.arlu1695.projectgame.Constants.CHANNEL_NAME;
import static su.ju.arlu1695.projectgame.Util.getCurrentUserId;


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
        Constants.SCREEN_HEIGHT_COEFFICIENT = Constants.SCREEN_HEIGHT / 13;
        Constants.SCREEN_WIDTH_COEFFICIENT = Constants.SCREEN_WIDTH / 10;


        createNotificationChannel();


        //testOnline();

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
        // Create the NotificationChannel for API 26+ because
        // the NotificationChannel class is new and not in the support library
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

    private void testOnline(){
        startActivity(new Intent(this, GameLobbyActivity.class)
                .putExtra("mode", "online")
                .putExtra("me", "challenged")
                .putExtra("gameId", "Zjb3LfBbJcSeu4i0j6KJ6ckYNO72-ylP7GwE0xxOEn3UDQA46LaX0roX2")
                .putExtra("fromName", "Lucky"));

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
