package su.ju.arlu1695.projectgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class Rectangle {

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    private int width;
    private int height;
    private int topPosX = 0;
    private int bottomPosX = 0;
    private int leftPosY = 0;
    private int rightPosY = 0;
    private Rect rectangle;

    public Rectangle() {
        rectangle = new Rect(leftPosY,topPosX,rightPosY,bottomPosX);
        width = Constants.SCREEN_WIDTH_COEFFICIENT;
        height = Constants.SCREEN_WIDTH_COEFFICIENT;

    }
    public Rectangle(int w, int h) {
        width = w;
        height = h;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0,255,0));
        canvas.drawRect(rectangle,paint);


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

    public void setWidth(int w) {
        width = w;
    }

    public void setHeight(int h) {
        height = h;
    }

    public void setLeftPosX(int xPosition, int yPosition) {
        topPosX = xPosition * Constants.SCREEN_WIDTH_COEFFICIENT;
        bottomPosX = topPosX + width;
        leftPosY = yPosition * Constants.SCREEN_HEIGHT_COEFFICIENT;
        rightPosY = leftPosY + height;
        rectangle.set(leftPosY,topPosX,rightPosY,bottomPosX);
    }


}
