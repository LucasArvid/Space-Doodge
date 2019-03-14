package su.ju.arlu1695.projectgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameplaySceneOnline implements Scene {

    private Rect r = new Rect();

    private Player player;
    private Point playerPoint;
    private ObstacleHandler obstacleHandler;

    // Class containing level information parsed from raw csv resource
    private Levels level;

    private String gameId;
    private String me;

    private String opponent;
    private String opponentScore = "0";

    private boolean playerMoving = false;
    private boolean gameOver = false;
    private boolean uiRunning = false;

    private int START_POS_X = Constants.SCREEN_WIDTH/2;
    private int START_POS_Y = Constants.SCREEN_HEIGHT - 150;

    private String wonOrLost = "lost";


    public GameplaySceneOnline(String gameId, String me) {

        this.gameId = gameId;
        this.me = me;
        // Get level data
        level = new Levels(Constants.GAME_CONTEXT);
        level.readLevelData();
        // Instantiate Player
        player = new Player(new Rect(100,100,200,200), Color.rgb(255,0,0));
        playerPoint = new Point(START_POS_X,START_POS_Y);
        player.update(playerPoint);

        // Obstacle handler instantiated dependent on level.
        obstacleHandler = new ObstacleHandler(level.getPlayerGap(),level.getObstacleGap(), level.getObstacleHeight(), Color.BLACK);

        if (me.equals("challenged"))
            opponent = "challenger";
        else if (me.equals("challenger"))
            opponent = "challenged";

        if (!uiRunning) {
            FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child(opponent).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // Look for opponent death
                        if (dataSnapshot.child("Dead").getValue().equals("true")) {
                            opponentScore = dataSnapshot.child("Score").getValue().toString();
                            wonOrLost = "won";
                            gameOverUI();
                            gameOver = true;
                            return;


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }


    @Override
    public void terminate() {
        SceneHandler.ACTIVE_SCENE = 0;
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX(), (int)event.getY()))
                    playerMoving = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && playerMoving)
                    playerPoint.set((int)event.getX(),(int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                playerMoving = false;
                break;
        }
    }

    public void gameOverUI() {
        uiRunning = true;

        Constants.GAME_CONTEXT.startActivity(new Intent(Constants.GAME_CONTEXT, GameOverActivity.class)
                .putExtra("score", obstacleHandler.getScore())
                .putExtra("me", me)
                .putExtra("gameId", gameId)
                .putExtra("wonOrLost",wonOrLost)
                .putExtra("opponentScore",opponentScore));

    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawRGB(level.getR(),level.getG(),level.getB());
            player.draw(canvas);
            obstacleHandler.draw(canvas);
        }

    }

    @Override
    public void update() {
        if(!gameOver) {
            player.update(playerPoint);
            obstacleHandler.update();

            if (obstacleHandler.collisionDetected(player)) {
                gameOver = true;
                gameOverUI();
            }

        }
    }
}
