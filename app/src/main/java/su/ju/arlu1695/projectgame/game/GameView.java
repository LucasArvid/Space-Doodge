package su.ju.arlu1695.projectgame.game;

import android.content.Context;
import android.graphics.Canvas;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import su.ju.arlu1695.projectgame.game.handlers.SceneHandler;
import su.ju.arlu1695.projectgame.utils.Constants;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private Context context;

    // Scene handler for easier switching and managing of scenes.
    private SceneHandler handler;

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


}
