package su.ju.arlu1695.projectgame;

import java.util.Random;

public class Rectangle {

    private float width;
    private float height;
    private float leftPosX = 0;
    private float rightPosX = 0;
    private float leftPosY = 0;
    private float rightPosY = 0;
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

    public float getLeftPosX() {
        return leftPosX;
    }

    public float getRightPosX() {
        return rightPosX;
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
        leftPosX = xPosition;
        rightPosX = leftPosX + width;
    }
    public void setLeftPosY(float yPosition) {
        leftPosY = yPosition;
        rightPosY = leftPosY + height;
    }

}
