package su.ju.arlu1695.projectgame.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import su.ju.arlu1695.projectgame.utils.Constants;
import su.ju.arlu1695.projectgame.R;
import su.ju.arlu1695.projectgame.utils.Util;

public class GameLobbyActivity extends AppCompatActivity {

    // TV/IV displaying users
    private TextView tv_playerOne;
    private TextView tv_playerTwo;
    private TextView tv_levelSelected;

    private AppCompatImageView iv_playerOneReady;
    private AppCompatImageView iv_playerTwoReady;

    private DatabaseReference gamesRef;

    // Ready trigger, default false
    private boolean playerOneReady = false;
    private boolean playerTwoReady = false;


    //Extras
    private String gameId;
    private String fromName;
    private String me;

    // level select stuff
    private int levelSelected;
    private Dialog levelSelectDialog;

    ValueEventListener vEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);
        getSupportActionBar().hide();

        levelSelectDialog = new Dialog(this); // level select pop up
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);



        gamesRef = FirebaseDatabase.getInstance().getReference().child("Games");

        // Unpack Intent extras
        Bundle extrasBundle = getIntent().getExtras();
        gameId = extrasBundle.getString("gameId");
        me = extrasBundle.getString("me");
        fromName = extrasBundle.getString("fromName");


        // Setup View dependent on challenger or challenged
        // Connect to Layout
        tv_playerOne = (TextView) findViewById(R.id.tv_current_user);
        tv_playerTwo = (TextView) findViewById(R.id.tv_opponent);
        tv_levelSelected = (TextView) findViewById(R.id.tv_game_lobby_level_selected);
        iv_playerOneReady = (AppCompatImageView) findViewById(R.id.iv_player_one_ready);
        iv_playerTwoReady = (AppCompatImageView) findViewById(R.id.iv_player_two_ready);

        // User is the challenger
        if (me.equals("challenger")) {
            tv_playerOne.setText(Constants.currentUser);
            tv_playerTwo.setText(fromName);

            tv_levelSelected.setText("Please select a level");
            iv_playerOneReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!playerOneReady) {
                        gamesRef.child(gameId).child("playerOneReady").setValue("true");
                        iv_playerOneReady.setImageResource(R.drawable.checkbox_on_background);
                        playerOneReady = true;
                    }
                    else if(playerOneReady) {
                        gamesRef.child(gameId).child("playerOneReady").setValue("false");
                        iv_playerOneReady.setImageResource(R.drawable.checkbox_off_background);
                        playerOneReady = false;
                    }
                }
            });
        }

        // User is being challenged
        else if(me.equals("challenged")) {
            tv_playerTwo.setText(Constants.currentUser);
            tv_playerOne.setText(fromName);

            tv_levelSelected.setText(String.format("%s %s", fromName, getResources().getString(R.string.is_chosing_level)));
            Button levelSelectButton = (Button) findViewById(R.id.b_level_select);
            Button levelStartButton = (Button) findViewById(R.id.b_level_start);
            levelStartButton.setVisibility(View.GONE);
            levelSelectButton.setVisibility(View.GONE);
            iv_playerTwoReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!playerTwoReady) {
                        gamesRef.child(gameId).child("playerTwoReady").setValue("true");
                        iv_playerTwoReady.setImageResource(R.drawable.checkbox_on_background);
                        playerTwoReady = true;
                    }
                    else if(playerTwoReady) {
                        gamesRef.child(gameId).child("playerTwoReady").setValue("false");
                        iv_playerTwoReady.setImageResource(R.drawable.checkbox_off_background);
                        playerTwoReady = false;
                    }
                }
            });
        }
        vEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean r1,r2;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.exists()) {
                        // Sync imageViews
                        if (dataSnapshot.child("playerOneReady").getValue().equals("true")) {
                            iv_playerOneReady.setImageResource(R.drawable.checkbox_on_background);
                            r1 = true;
                        } else {
                            iv_playerOneReady.setImageResource(R.drawable.checkbox_off_background);
                            r1 = false;
                        }
                        if (dataSnapshot.child("playerTwoReady").getValue().equals("true")) {
                            iv_playerTwoReady.setImageResource(R.drawable.checkbox_on_background);
                            r2 = true;
                        } else {
                            iv_playerTwoReady.setImageResource(R.drawable.checkbox_off_background);
                            r2 = false;
                        }

                        // Show selected level to all users.
                        String levelString = dataSnapshot.child("level").getValue().toString();
                        int levelInt = Integer.parseInt(levelString);
                        if (levelInt > 0) {
                            tv_levelSelected.setText(String.format("Level %s %s.", levelInt, getResources().getString(R.string.has_been_chosen)));
                            Constants.LEVEL_SELECTED = (levelInt - 1); // Level 1 = index 0;
                        }
                        // Players not ready, let challenger know that he cannot start yet.
                        if ((dataSnapshot.child("startGame").getValue().equals("true")) && !(r1 && r2)) {
                            Button startGameButton = (Button) findViewById(R.id.b_level_start);
                            startGameButton.setText(getResources().getString(R.string.waiting_for_players));
                        }
                        // Start game
                        if (dataSnapshot.child("startGame").getValue().equals("true") && r1 && r2) {
                            finish();
                            startActivity(new Intent(GameLobbyActivity.this, MainGameActivity.class)
                                    .putExtra("mode", "online")
                                    .putExtra("gameId", gameId)
                                    .putExtra("me", me)
                                    .putExtra("fromName", fromName));
                            gamesRef.child(gameId).child("startGame").setValue("false");
                            gamesRef.child(gameId).removeEventListener(this);
                            return;

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        /*
            Database listener to track whenever opponent is ready, and the level has been selected
         */
        gamesRef.child(gameId).addValueEventListener(vEventListener);


    }

    // Simple level select for challenger
    public void levelSelectButtonClicked (View view) {

        levelSelectDialog.setContentView(R.layout.activity_levelselect);

        ListView listView = (ListView) levelSelectDialog.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                Util.getLevelList(this)
        ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 levelSelected = (position+1);
                 gamesRef.child(gameId).child("level").setValue(levelSelected);
                 levelSelectDialog.dismiss();
            }

        });
        levelSelectDialog.show();
    }

    // Update database with start = true, this is read on the database listener
    public void levelStartButtonClicked (View view) {
        gamesRef.child(gameId).child("startGame").setValue("true");
    }

    // Ensure that the eventListener stops and the activity finishes if the user -
    // leaves the game lobby.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gamesRef.child(gameId).removeEventListener(vEventListener);
        finish();
    }


}
