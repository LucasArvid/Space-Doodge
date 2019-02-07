package su.ju.arlu1695.projectgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static su.ju.arlu1695.projectgame.GameThread.EXTRA_LEVEL_INDEX;

public class LevelSelect extends AppCompatActivity {

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelselect);

        getSupportActionBar().hide();
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<Constants.levelName>(
                this,
                android.R.layout.simple_list_item_1,
                Constants.levels
        ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LevelSelect.this, MainGameActivity.class);
                Constants.LEVEL_SELECTED = position;
                startActivity(intent);
            }

        });

    }
}
