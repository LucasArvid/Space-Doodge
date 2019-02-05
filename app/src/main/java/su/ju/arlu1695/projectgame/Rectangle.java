package su.ju.arlu1695.projectgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Rectangle {

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    private float width;
    private float height;
    private float topPosX = 0;
    private float bottomPosX = 0;
    private float leftPosY = 0;
    private float rightPosY = 0;
    private Random ran;

    public Rectangle() {
        width = Constants.SCREEN_WIDTH_COEFFICIENT;
        height = Constants.SCREEN_WIDTH_COEFFICIENT;
        ran = new Random();
        int randomNumber =  ran.nextInt(599) + 1;

    }
    public Rectangle(float w, float h) {
        width = w;
        height = h;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0,255,0));
        canvas.drawRect(leftPosY,topPosX,rightPosY,bottomPosX,paint);


    }

    public float getTopPosX() {
        return topPosX;
    }

    public float getBottomPosX() {
        return bottomPosX;
    }

    public float getRightPosY() {
        return rightPosY;
    }

    public float getLeftPosY() {
        return leftPosY;
    }

    public void setWidth(float w) {
        width = w;
    }

    public void setHeight(float h) {
        height = h;
    }

    public void setLeftPosX(float xPosition) {
        topPosX = xPosition * Constants.SCREEN_WIDTH_COEFFICIENT;
        bottomPosX = topPosX + width;
    }
    public void setLeftPosY(float yPosition) {
        leftPosY = yPosition * Constants.SCREEN_HEIGHT_COEFFICIENT;
        rightPosY = leftPosY + height;
    }

}
