package su.ju.arlu1695.projectgame;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameplayScene implements Scene {

    private Rect r = new Rect();

    private Player player;
    private Point playerPoint;
    private ObstacleHandler obstacleHandler;

    private String me;

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
        gameOver = false;
        player.update(playerPoint);
        obstacleHandler = new ObstacleHandler(level.getPlayerGap(),level.getObstacleGap(), level.getObstacleHeight(), Color.BLACK);
        playerPoint = new Point(START_POS_X,START_POS_Y);
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
                if(gameOver && System.currentTimeMillis() - gameOverDelay >= 1000){
                        resetGame();
                }
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

    // andreas1724, copied from StackOverFlow
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
                gameOverUI();
                gameOverDelay = System.currentTimeMillis();
            }

        }
    }
}
