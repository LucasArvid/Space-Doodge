package su.ju.arlu1695.projectgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    private GameThread thread;
    private Context context;

    private SceneHandler handler;


    public GameView(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(),this);

        handler = new SceneHandler();

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
