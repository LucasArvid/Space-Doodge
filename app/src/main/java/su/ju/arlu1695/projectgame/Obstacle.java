package su.ju.arlu1695.projectgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Obstacle implements GameObjects{

    private Rect rectangle;
    private Rect rectangleTwo;
    private int color;


    public Obstacle(int obstacleHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        // left,top,right,bottom
        rectangle = new Rect(0, startY, startX, startY + obstacleHeight);
        rectangleTwo = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + obstacleHeight);


    }

    public boolean collisionDetected(Player player) {
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangleTwo, player.getRectangle());
    }

    /*
    public boolean intersects(Player player) {
        if (rectangle.contains(player.getRectangle().left, player.getRectangle().top)
            || rectangle.contains(player.getRectangle().right, player.getRectangle().top)
            || rectangle.contains(player.getRectangle().left, player.getRectangle().bottom)
            || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom))
        return true; // if
    return false; // else
    }
    */
    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
        rectangleTwo.top += y;
        rectangleTwo.bottom += y;
    }

    public Rect getRectangle() {
        return rectangle;
    }

    @Override
    public void update(Point point) {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);
        canvas.drawRect(rectangleTwo,paint);
    }
}
