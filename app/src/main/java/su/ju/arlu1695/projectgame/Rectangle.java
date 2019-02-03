package su.ju.arlu1695.projectgame;

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
    private float topPosY = 0;
    private float bottomPosY = 0;
    private Random ran;

    public Rectangle() {
        width = 100;
        height = 100;
        ran = new Random();
        int randomNumber =  ran.nextInt(599) + 1;
        setLeftPosX(randomNumber+100);
        setLeftPosY(randomNumber);

    }
    public Rectangle(float w, float h) {
        width = w;
        height = h;
    }

    public float getTopPosX() {
        return topPosX;
    }

    public float getBottomPosX() {
        return bottomPosX;
    }

    public float getBottomPosY() {
        return bottomPosY;
    }

    public float getTopPosY() {
        return topPosY;
    }

    public void setWidth(float w) {
        width = w;
    }

    public void setHeight(float h) {
        height = h;
    }

    public void setLeftPosX(float xPosition) {
        topPosX = xPosition;
        bottomPosX = topPosX + width;
    }
    public void setLeftPosY(float yPosition) {
        topPosY = yPosition;
        bottomPosY = topPosY + height;
    }

}
