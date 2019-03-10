package su.ju.arlu1695.projectgame;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainGameActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


}