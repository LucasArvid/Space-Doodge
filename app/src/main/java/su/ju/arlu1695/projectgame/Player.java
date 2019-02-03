package su.ju.arlu1695.projectgame;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player extends Activity {

    private float topPosX;
    private float topPosY;
    private float bottomPosY;
    private float bottomPosX;

    private float velocityX;
    private float velocityY;

    private float width;
    private float height;


    private float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public Player() {

        width = 100;
        height = 100;
        velocityX = 5;
        velocityY = 5;
        setLeftPosX((screenWidth/2)-50);
        setLeftPosY(screenHeight-screenHeight);

    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255,0,0));
        canvas.drawRect((topPosX),(topPosY),(topPosX+width),(topPosY+height),paint);
    }

    public void update() {
        topPosX += velocityX;
        topPosY += velocityY;

    }

    public float getTopPosX() {
        return topPosX;
    }

    public float getBottomPosX() {
        return bottomPosX;
    }

    public float getTopPosY() {
        return topPosY;
    }

    public float getBottomPosY() {
        return bottomPosY;
    }

    public void setLeftPosX(float xPosition) {
        topPosX = xPosition;
        bottomPosX = topPosX + width;
    }
    public void setLeftPosY(float yPosition) {
        topPosY = yPosition;
        bottomPosY = topPosY + height;
    }


    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }


    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
        velocityY = 0;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
        velocityX = 0;
    }

}
