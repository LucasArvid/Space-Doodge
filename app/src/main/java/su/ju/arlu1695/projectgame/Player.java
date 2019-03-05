package su.ju.arlu1695.projectgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Player implements GameObjects{

    private Rect rectangle;
    private int color;

    public Player (Rect rectangle, int color){
        this.rectangle = rectangle;
        this.color = color;
    }


    public Rect getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rect rectangle) {
        this.rectangle = rectangle;
    }


    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);
    }

    @Override
    public void update(Point point) {
        //left,top,right,bottom
        rectangle.set(point.x - rectangle.width()/2,
                    point.y - rectangle.width()/2,
                    point.x + rectangle.width()/2,
                    point.y + rectangle.width()/2);

    }
}
