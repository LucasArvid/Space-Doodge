/*  ------------------------------------------------
    This scene is only reached if the user started a
    offline or solo game.
    ------------------------------------------------ */
package su.ju.arlu1695.projectgame.game.scenes;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import su.ju.arlu1695.projectgame.game.data.Levels;
import su.ju.arlu1695.projectgame.game.handlers.ObstacleHandler;
import su.ju.arlu1695.projectgame.R;
import su.ju.arlu1695.projectgame.game.handlers.SceneHandler;
import su.ju.arlu1695.projectgame.activities.GameOverActivity;
import su.ju.arlu1695.projectgame.game.data.Player;
import su.ju.arlu1695.projectgame.interfaces.Scene;
import su.ju.arlu1695.projectgame.utils.Constants;


public class GameplayScene implements Scene {

    private Rect r = new Rect();

    private Player player;
    private Point playerPoint;
    private ObstacleHandler obstacleHandler;

    private String me; // me contains "offline" or "solo", identifying what game mode the user started

    private Levels level;

    private boolean playerMoving = false;
    private boolean gameOver = false;
    private long gameOverDelay;
    private boolean uiRunning = false;

    private int START_POS_X = Constants.SCREEN_WIDTH/2;
    private int START_POS_Y = Constants.SCREEN_HEIGHT - 150;



    public GameplayScene(String me) {
        // Get level data
        level = new Levels(Constants.GAME_CONTEXT);
        level.readLevelData();

        // Instantiate Player
        player = new Player(new Rect(100,100,200,200), Color.rgb(255,0,0));
        playerPoint = new Point(START_POS_X,START_POS_Y);
        player.update(playerPoint);

        this.me = me;

        // Obstacle handler instantiated dependent on level.
        obstacleHandler = new ObstacleHandler(level.getPlayerGap(),level.getObstacleGap(), level.getObstacleHeight(), Color.BLACK);
    }

    public void resetGame() {
        player.update(playerPoint);
        obstacleHandler = new ObstacleHandler(level.getPlayerGap(),level.getObstacleGap(), level.getObstacleHeight(), Color.BLACK);
        playerPoint = new Point(START_POS_X,START_POS_Y);
        gameOver = false;
        playerMoving = false;
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
                // Allows the player to restart the game 1+ seconds after death
                if(gameOver && System.currentTimeMillis() - gameOverDelay >= 1000)
                        resetGame();
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


    public void gameOverActivity() {
        uiRunning = true;

        Constants.GAME_CONTEXT.startActivity(new Intent(Constants.GAME_CONTEXT, GameOverActivity.class)
                .putExtra("score", obstacleHandler.getScore())
                .putExtra("me", me));

    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawRGB(level.getR(),level.getG(),level.getB());
            player.draw(canvas);
            obstacleHandler.draw(canvas);
        }

        if (gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.WHITE);
            drawCenterText(canvas,paint,Constants.GAME_CONTEXT.getResources().getString(R.string.tap_to_restart));
        }
    }

    // andreas1724, taken from StackOverFlow. Draws a text in the center of the gameView.
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void update() {
        if(!gameOver) {
            player.update(playerPoint);
            obstacleHandler.update();

            if (obstacleHandler.collisionDetected(player)) {
                gameOver = true;
                gameOverActivity();
                gameOverDelay = System.currentTimeMillis();
            }

        }
    }
}
