package su.ju.arlu1695.projectgame;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Player extends Activity {

    private int topPosX;
    private int topPosY;
    private int bottomPosY;
    private int bottomPosX;

    private int velocityX;
    private int velocityY;

    private int width;
    private int height;

    private Rect player;


    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public Player() {
        player = new Rect();
        width = Constants.SCREEN_WIDTH_COEFFICIENT;
        height = Constants.SCREEN_WIDTH_COEFFICIENT;
        velocityX = 0;
        velocityY = 15;
        setLeftPosX(0);
        setLeftPosY(0);

    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255,0,0));
        canvas.drawRect(player,paint);
    }

    public void update() {

        player.set(topPosY,topPosX+ velocityX,bottomPosY,bottomPosX);
        player.set(topPosY + velocityY,topPosX,bottomPosY,bottomPosX);


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

    public void setLeftPosX(int xPosition) {
        topPosX = xPosition;
        bottomPosX = topPosX + width;
        player.set(topPosY,topPosX,bottomPosY,bottomPosX);
    }
    public void setLeftPosY(int yPosition) {
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

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
        velocityY = 0;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
        velocityX = 0;
    }

}
