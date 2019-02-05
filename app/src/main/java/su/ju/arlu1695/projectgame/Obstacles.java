package su.ju.arlu1695.projectgame;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Obstacles extends Activity {

    Rectangle[] array;
    private int numberOfObstacles;

    public Obstacles(int numberOfObstacles) {
        this.numberOfObstacles = numberOfObstacles;
        array = new Rectangle[numberOfObstacles];
        for (int i=0; i < numberOfObstacles; i++) {
            Rectangle r = new Rectangle();
            array[i] = r;


        }

    }


    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0,255,0));
        for (int i = 0; i < numberOfObstacles; i++) {
            array[i].draw(canvas);
        }
    }

    public boolean intersects(Player player, Rectangle rectangle) {
        return ((player.getTopPosY()+player.getHeight()) <= (rectangle.getBottomPosX()+rectangle.getHeight()) &&
                (rectangle.getBottomPosX()+rectangle.getHeight()) <= (player.getBottomPosX()+player.getHeight()) &&
                (player.getTopPosX()+player.getWidth()) <= (rectangle.getRightPosY()+rectangle.getWidth()) &&
                (rectangle.getTopPosX()+rectangle.getWidth()) <= (player.getBottomPosY()+player.getWidth()));
    }

    public int getNumberOfObstacles() {

        return numberOfObstacles;
    }


    public Rectangle[] getArray() {
        return array;
    }

    public void setArray(Rectangle[] array) {
        this.array = array;
    }



}
