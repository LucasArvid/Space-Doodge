package su.ju.arlu1695.projectgame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import su.ju.arlu1695.projectgame.utils.Constants;
import su.ju.arlu1695.projectgame.R;
import su.ju.arlu1695.projectgame.utils.Util;

public class LevelSelectActivity extends AppCompatActivity {

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelselect);
        getSupportActionBar().hide();

        Constants.startMediaPlayer(0);

        Intent intent = getIntent();
        final String me = intent.getExtras().getString("me");


        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.list_white_text,
                Util.getLevelList(this)

        ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Start selected level
                Intent intent = new Intent(LevelSelectActivity.this, MainGameActivity.class)
                        .putExtra("mode","solo")
                        .putExtra("gameId", "solo")
                        .putExtra("me", me);
                Constants.LEVEL_SELECTED = position;
                finish();
                startActivity(intent);
            }

        });

    }



    @Override
    public void onResume() {
        super.onResume();
        Constants.startMediaPlayer(0);
    }
}
