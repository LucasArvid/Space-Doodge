package su.ju.arlu1695.projectgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Player implements GameObjects{

    private Rect rectangle;
    private int color;

    private Animation idle;
    private Animation walkRight;
    private Animation walkLeft;
    private AnimationHandler animationHandler;

    public Player (Rect rectangle, int color){
        this.rectangle = rectangle;
        this.color = color;

        // Animations
        BitmapFactory bitmapFactory = new BitmapFactory();
        Bitmap idleImg = bitmapFactory.decodeResource(Constants.GAME_CONTEXT.getResources(), R.drawable.aliengreen);
        Bitmap walkOne = bitmapFactory.decodeResource(Constants.GAME_CONTEXT.getResources(), R.drawable.aliengreen_walk1);
        Bitmap walkTwo = bitmapFactory.decodeResource(Constants.GAME_CONTEXT.getResources(), R.drawable.aliengreen_walk2);

        idle = new Animation(new Bitmap[]{idleImg}, 2);
        walkRight = new Animation(new Bitmap[]{walkOne,walkTwo}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1,1);
        walkOne = Bitmap.createBitmap(walkOne,0, 0, walkOne.getWidth(),  walkOne.getHeight(), m, false);
        walkTwo = Bitmap.createBitmap(walkTwo,0, 0, walkTwo.getWidth(),  walkTwo.getHeight(), m, false);

        walkLeft = new Animation(new Bitmap[]{walkOne,walkTwo}, 0.5f);

        animationHandler = new AnimationHandler(new Animation[]{idle,walkRight,walkLeft});
        // Animations end
    }


    public Rect getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rect rectangle) {
        this.rectangle = rectangle;
    }


    @Override
    public void draw(Canvas canvas) {
        /* Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint); */
        animationHandler.draw(canvas,rectangle);
    }

    @Override
    public void update() {
        animationHandler.update();
    }

    public void update(Point point) {
        float oldLeft = rectangle.left;
        //left,top,right,bottom
        rectangle.set(point.x - rectangle.width()/2,
                    point.y - rectangle.width()/2,
                    point.x + rectangle.width()/2,
                    point.y + rectangle.width()/2);

        int state = 0;
        if (rectangle.left - oldLeft > 5)
            state = 1;                              // moving right
        else if (rectangle.left - oldLeft < -5)
            state = 2;                              // moving left

        animationHandler.playAnimation(state);
        animationHandler.update();
    }
}
