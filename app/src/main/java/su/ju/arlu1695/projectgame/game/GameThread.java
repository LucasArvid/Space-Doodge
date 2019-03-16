package su.ju.arlu1695.projectgame.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import su.ju.arlu1695.projectgame.game.GameView;

public class GameThread extends Thread {
    private int FPS = 60;
    private double avgFPS;
    private SurfaceHolder surfaceHolder;
    private GameView game;
    private boolean running;
    public static Canvas canvas;
    public static final String EXTRA_LEVEL_INDEX = "levelSelectIndex";

    public GameThread(SurfaceHolder surfaceHolder, GameView game) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.game = game;

    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.game.update();
                    this.game.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime = totalTime + (System.nanoTime() - startTime);
            frameCount++;
            if(frameCount == FPS){
                System.out.println(totalTime);
                avgFPS = 1000 / ((totalTime / frameCount) / 1000000 );
                totalTime = 0;
                frameCount = 0;
                System.out.println(avgFPS);
            }

        }


    }

    public void setRunning(boolean isRunning) {
        running = isRunning;

    }

}
