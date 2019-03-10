package su.ju.arlu1695.projectgame;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static su.ju.arlu1695.projectgame.Util.getCurrentUserId;

public class GameLobbyActivity extends AppCompatActivity {

    // TV/IV displaying users
    private TextView tv_playerOne;
    private TextView tv_playerTwo;
    private TextView tv_levelSelected;

    private AppCompatImageView iv_playerOneReady;
    private AppCompatImageView iv_playerTwoReady;

    // Ready trigger
    private boolean playerOneReady;
    private boolean playerTwoReady;


    //Extras
    private String gameId;
    private String fromName;
    private String me;

    // level select stuff
    private int levelSelected;
    private Dialog levelSelectDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);
        getSupportActionBar().hide();

        levelSelectDialog = new Dialog(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Unpack Intent extras
        Bundle extrasBundle = getIntent().getExtras();
        gameId = extrasBundle.getString("gameId");
        me = extrasBundle.getString("me");
        fromName = extrasBundle.getString("fromName");

        playerOneReady = false;
        playerTwoReady = false;


        // Setup View dependent on challenger or challenged
        // Connect to Layout
        tv_playerOne = (TextView) findViewById(R.id.tv_current_user);
        tv_playerTwo = (TextView) findViewById(R.id.tv_opponent);
        tv_levelSelected = (TextView) findViewById(R.id.tv_game_lobby_level_selected);



        iv_playerOneReady = (AppCompatImageView) findViewById(R.id.iv_player_one_ready);
        iv_playerTwoReady = (AppCompatImageView) findViewById(R.id.iv_player_two_ready);

        // Challenger
        if (me.equals("challenger")) {
            tv_playerOne.setText(Constants.thisUser.getNickname());
            tv_playerTwo.setText(fromName);

            tv_levelSelected.setText("Please select a level");
            iv_playerOneReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!playerOneReady) {
                        FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child("playerOneReady").setValue("true");
                        iv_playerOneReady.setImageResource(R.drawable.checkbox_on_background);
                        playerOneReady = true;
                    }
                    else if(playerOneReady) {
                        FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child("playerOneReady").setValue("false");
                        iv_playerOneReady.setImageResource(R.drawable.checkbox_off_background);
                        playerOneReady = false;
                    }
                }
            });
        }

        // Being challenged
        else if(me.equals("challenged")) {
            tv_playerTwo.setText(Constants.thisUser.getNickname());
            tv_playerOne.setText(fromName);

            tv_levelSelected.setText(String.format("%s is choosing level", fromName));
            Button levelSelectButton = (Button) findViewById(R.id.b_level_select);
            Button levelStartButton = (Button) findViewById(R.id.b_level_start);
            levelStartButton.setVisibility(View.GONE);
            levelSelectButton.setVisibility(View.GONE);
            iv_playerTwoReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!playerTwoReady) {
                        FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child("playerTwoReady").setValue("true");
                        iv_playerTwoReady.setImageResource(R.drawable.checkbox_on_background);
                        playerTwoReady = true;
                    }
                    else if(playerTwoReady) {
                        FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child("playerTwoReady").setValue("false");
                        iv_playerTwoReady.setImageResource(R.drawable.checkbox_off_background);
                        playerTwoReady = false;
                    }
                }
            });
        }
        FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean r1,r2 = false;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Sync imageViews
                    if (dataSnapshot.child("playerOneReady").getValue().equals("true")) {
                        iv_playerOneReady.setImageResource(R.drawable.checkbox_on_background);
                        r1 = true;
                    }
                    else {
                        iv_playerOneReady.setImageResource(R.drawable.checkbox_off_background);
                        r1 = false;
                    }
                    if (dataSnapshot.child("playerTwoReady").getValue().equals("true")) {
                        iv_playerTwoReady.setImageResource(R.drawable.checkbox_on_background);
                        r2 = true;
                    }
                    else {
                        iv_playerTwoReady.setImageResource(R.drawable.checkbox_off_background);
                        r2 = false;
                    }

                    // Show selected level to all users.
                    String levelString = dataSnapshot.child("level").getValue().toString();
                    int levelInt = Integer.parseInt(levelString);
                    if(levelInt > 0) {
                        tv_levelSelected.setText(String.format("Level %s &s.",levelInt,Constants.GAME_CONTEXT.getResources().getString(R.string.has_been_chosen)));
                        Constants.LEVEL_SELECTED = (levelInt-1); // Level 1 = index 0;
                    }
                    // Players not ready
                    if ((dataSnapshot.child("startGame").getValue().equals("true")) && !(r1 && r2)) {
                        Button startGameButton = (Button) findViewById(R.id.b_level_start);
                        startGameButton.setText(Constants.GAME_CONTEXT.getResources().getString(R.string.waiting_for_players));
                    }
                    // Start game
                    if (dataSnapshot.child("startGame").getValue().equals("true") && r1 && r2) {
                        startActivity(new Intent(GameLobbyActivity.this, MainGameActivity.class)
                                .putExtra("mode","online")
                                .putExtra("gameId", gameId)
                                .putExtra("me",me)
                                .putExtra("fromName",fromName));
                        FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child("startGame").setValue("false");
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void levelSelectButtonClicked (View view) {

        // Simple level select for challenger
        levelSelectDialog.setContentView(R.layout.activity_levelselect);

        ListView listView = (ListView) levelSelectDialog.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<Constants.levelName>(
                this,
                android.R.layout.simple_list_item_1,
                Constants.levels
        ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 levelSelected = (position+1);
                 FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child("level").setValue(levelSelected);
                 levelSelectDialog.dismiss();
            }

        });
        levelSelectDialog.show();
    }

    public void levelStartButtonClicked (View view) {
        FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child("startGame").setValue("true");
    }
}
