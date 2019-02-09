package su.ju.arlu1695.projectgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class GameModeActivity extends AppCompatActivity {
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        getSupportActionBar().hide();
    }

    public void soloButtonClicked(View view) {
        Intent intent = new Intent(this, LevelSelectActivity.class);
        startActivity(intent);
    }

   public void duelButtonClicked(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
