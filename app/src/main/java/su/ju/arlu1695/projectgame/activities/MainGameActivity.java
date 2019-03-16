package su.ju.arlu1695.projectgame.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import su.ju.arlu1695.projectgame.utils.Constants;
import su.ju.arlu1695.projectgame.game.GameView;
import su.ju.arlu1695.projectgame.R;

public class MainGameActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver(broadcastReceiver, new IntentFilter("finish_game"));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Constants.stopMediaPlayer();
        Constants.startMediaPlayer(R.raw.soft_and_furious_05_falling_into_the_game);


        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        String gameId = intent.getStringExtra("gameId");
        String me = intent.getStringExtra("me");
        this.setContentView(new GameView(this, mode,gameId,me));
    }

    /*
        Used to finish the game activity from the game over UI.
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_game")) {
                finish();
                unregisterReceiver(broadcastReceiver);


            }
        }
    };




}