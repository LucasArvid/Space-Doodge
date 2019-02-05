package su.ju.arlu1695.projectgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private Player player;
    private GameThread thread;
    private Obstacles obstacles;
    private Levels levels;
    private Context context;
    private LogicHandler logicHandler;

    private enum Direction{
        UP,DOWN,LEFT,RIGHT
    }

    public GameView(Context context) {
        super(context);
        this.context = context;

        getHolder().addCallback(this);

        thread = new GameThread(getHolder(),this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        player = new Player();
        obstacles = new Obstacles(7);
        levels = new Levels(context);
        levels.readLevelData(obstacles, 0); // selectedLevel >= 1 !!
        logicHandler = new LogicHandler(player, obstacles);
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
        if (canvas != null) {
            canvas.drawColor(Color.BLUE);
            player.draw(canvas);
            obstacles.draw(canvas);

        }

    }

    private void movePlayer(Direction direction){



            switch (direction) {
                case UP:
                    player.setVelocityY(-10);
                    break;
                case DOWN:
                    player.setVelocityY(10);
                    break;
                case LEFT:
                    player.setVelocityX(-10);
                    break;
                case RIGHT:
                    player.setVelocityX(10);
                    break;
            }

    }

    public void update() {
        logicHandler.insideScreen();
        player.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            return true;

        if(event.getAction() == MotionEvent.ACTION_MOVE){
            float x = event.getX();
            float y = event.getY();

            float playerX = player.getTopPosX();
            float playerY = player.getTopPosY();

            float dx = x -playerX;
            float dy = y - playerY;

            float absDx = Math.abs(dx);
            float absDy = Math.abs(dy);

            if(absDx > absDy) {
                // move in x-direction
                if(dx > 0)
                    movePlayer(Direction.RIGHT);

                else
                    movePlayer(Direction.LEFT);
                ;
            }
            else{
                // move in y-direction
                if(dy > 0)
                    movePlayer(Direction.DOWN);

                else
                    movePlayer(Direction.UP);

            }
        }
        return true;
    }
}
