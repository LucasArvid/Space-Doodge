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
            canvas.drawRect((array[i].getLeftPosX()), (array[i].getRightPosX()), (array[i].getLeftPosY()), (array[i].getRightPosY()), paint);
        }
    }

    public void update(){

    }

    public int getNumberOfObstacles() {
        return numberOfObstacles;
    }
}
