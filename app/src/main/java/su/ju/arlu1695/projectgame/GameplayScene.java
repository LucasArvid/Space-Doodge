package su.ju.arlu1695.projectgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GameplayScene implements Scene {

    private Rect r = new Rect();

    private Player player;
    private Point playerPoint;
    private ObstacleHandler obstacleHandler;

    private boolean playerMoving = false;
    private boolean gameOver = false;
    private long gameOverDelay;

    public GameplayScene() {
        // Instantiate Player
        player = new Player(new Rect(100,100,200,200), Color.rgb(255,0,0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleHandler = new ObstacleHandler(200,350, 75, Color.BLACK);
    }

    public void resetGame() {
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        obstacleHandler = new ObstacleHandler(200,350, 75, Color.BLACK);
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
                if(gameOver && System.currentTimeMillis() - gameOverDelay >= 2000){
                    resetGame();
                    gameOver = false;
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

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawRGB(173, 245, 255);
            player.draw(canvas);
            obstacleHandler.draw(canvas);
        }

        if (gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.BLUE);
            drawCenterText(canvas,paint,"Game Over");
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
                gameOverDelay = System.currentTimeMillis();
            }

        }
    }
}
