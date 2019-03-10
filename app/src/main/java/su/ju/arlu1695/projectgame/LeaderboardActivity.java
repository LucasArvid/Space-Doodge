package su.ju.arlu1695.projectgame;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class LeaderboardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private Dialog leaderboardDialog;

    private ListView leaderBoardListView;
    private ArrayList<String> mLeaderBoardList = new ArrayList<>();
    private TextView tvLevelSelected;
    private TextView exitButton;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Constants.startMediaPlayer(0);

        getSupportActionBar().hide();

        leaderboardDialog = new Dialog(this);

        ListView listView = (ListView) findViewById(R.id.lw_leaderboard);
        listView.setAdapter(new ArrayAdapter<Constants.levelName>(
                this,
                android.R.layout.simple_list_item_1,
                Constants.levels
        ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                                showHighScores(position);
                                            }
                                        });
    }

    private void showHighScores (final int position) {

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("leaderboard");



        leaderboardDialog.setContentView(R.layout.leaderboard_popup);

        leaderBoardListView = (ListView) leaderboardDialog.findViewById(R.id.lw_level_scores);
        exitButton = (TextView) leaderboardDialog.findViewById(R.id.tv_exitButton);
        tvLevelSelected = (TextView) leaderboardDialog.findViewById(R.id.tv_levelSelected);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLeaderBoardList);
        leaderBoardListView.setAdapter(arrayAdapter);




        // Parse database for highscores on selected level.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mLeaderBoardList.clear();
                int count = 0; // top 50 count
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals("level"+(position+1))) {
                        for (DataSnapshot dsUsers : ds.getChildren()) {
                            count++;
                            String user = dsUsers.getValue().toString();
                            String score = dsUsers.getKey();
                            String format = String.format("%s: %s   %s:%s",
                                    getResources().getString(R.string.player),
                                    user,
                                    getResources().getString(R.string.score),
                                    score);
                            mLeaderBoardList.add(format);
                            Collections.reverse(mLeaderBoardList);
                            arrayAdapter.notifyDataSetChanged();
                            if(count == 50) // not placed in top 50
                                return;
                        }
                    }
                }
                tvLevelSelected.setText(String.format("Level %s Top50.",(position + 1)));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        leaderboardDialog.show();
    }

    public void popUpExitButtonCLicked(View view) {
        leaderboardDialog.dismiss();
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

