package su.ju.arlu1695.projectgame;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player extends Activity {

    private float posX;
    private float posY;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public Player() {
        posX = (screenWidth/2)-50;
        posY = screenHeight-screenHeight;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255,0,0));
        canvas.drawRect((posX),(posY),(posX+100),(posY+100),paint);
    }

    public void update() {
        posX += xVelocity;
        posY += yVelocity;
    }

    public float getPosXX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosX(float newPosX) {
        posX = newPosX;
    }

    public void setPosY(float newPosY) {
        posX = newPosY;
    }

}
