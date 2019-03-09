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
import android.support.constraint.solver.widgets.Rectangle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    private GameThread thread;
    private Context context;

    private SceneHandler handler;

    private Player player;
    private Point playerPoint;
    private ObstacleHandler obstacleHandler;

    private Levels level;

    private String gameId;
    private String me;

    private String opponent;
    private String opponentScore = "0";

    private boolean playerMoving = false;
    private boolean gameOver = false;
    private long gameOverDelay;
    private boolean uiRunning = false;
    private String wonOrLost = "lost";


    public GameView(Context context, String mode, String gameId, String me) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);

        Constants.GAME_CONTEXT = context;

        thread = new GameThread(getHolder(),this);
        Constants.thread = this.thread;

        handler = new SceneHandler(mode,gameId,me);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        handler.draw(canvas);
    }


    public void update() {
        handler.update();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handler.recieveTouch(event);

        return true;
        // default return true
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


}
