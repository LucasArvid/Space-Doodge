package su.ju.arlu1695.projectgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.view.View.GONE;
import static su.ju.arlu1695.projectgame.Util.getCurrentUserId;

public class GameOverActivity extends AppCompatActivity {

    private String gameId;
    private String me;
    private int score;
    private String opponentScore;
    private String wonOrLost;

    private Button restartButton;

    private DatabaseReference firebaseRef;

    private TextView tv_score;

    private TextView tv_wonOrLoss;
    private TextView tv_hsUpdate;

    private ImageView iv_alien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        getSupportActionBar().hide();

        firebaseRef =  FirebaseDatabase.getInstance().getReference();

        Bundle extrasBundle = getIntent().getExtras();
        gameId = extrasBundle.getString("gameId");
        me = extrasBundle.getString("me");
        score = extrasBundle.getInt("score");
        wonOrLost = extrasBundle.getString("wonOrLost");
        opponentScore = extrasBundle.getString("opponentScore");

        tv_score = (TextView) findViewById(R.id.tv_my_score);

        tv_wonOrLoss = (TextView) findViewById(R.id.tv_game_won_lost);
        tv_hsUpdate = (TextView) findViewById(R.id.tv_highscore_update);

        restartButton = (Button) findViewById(R.id.b_gameOverRestart);

        iv_alien = (ImageView) findViewById(R.id.iv_gameover_alien);

        if(me.equals("solo"))
            handleSoloPlay();
        else if (me.equals("offline"))
            handleOfflinePlay();
        else
            handleOnlinePlay();


    }

    public void handleOfflinePlay() {
        tv_score.setText(String.format(
                "Final Score: %s",
                score
        ));
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

    public void handleSoloPlay() {
        tv_score.setText(String.format(
                "Final Score: %s",
                score
        ));
        final String format = String.format("level%d",
                (Constants.LEVEL_SELECTED+1)); // levels start from index 0, database starts from 1
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseRef.child("User").child(currentUser.getUid()).child("highscore")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(format).getValue(Integer.class) < score || dataSnapshot.child(format).getValue() == null ) {
                        firebaseRef.child("User").child(currentUser.getUid()).child("highscore").child(format).setValue(score);
                        firebaseRef.child("leaderboard").child(format).child(Integer.toString(score)).setValue(Constants.thisUser.getNickname());
                    }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }

    public void handleOnlinePlay() {
        boolean retry = true;
        while (retry) {
            try {
                Constants.thread.setRunning(false);
                Constants.thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

        FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child(me).child("Score").setValue(score);
        if(wonOrLost.equals("lost"))
            FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child(me).child("Dead").setValue("true");

        restartButton.setVisibility(GONE);

        tv_score.setText(String.format(
                "Final Score: %s",
                score
        ));


        if(wonOrLost.equals("won")) {
            tv_wonOrLoss.setText("Hurray, opponent died!");
            iv_alien.setImageResource(R.drawable.aliengreen);
        }
        else if (wonOrLost.equals("lost")) {
            tv_wonOrLoss.setText("Bitter defeat!");

        }
        tv_hsUpdate.setText("Highscore will be updated!");


    }

    public void gameOverExitButtonClicked(View view) {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
