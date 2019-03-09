package su.ju.arlu1695.projectgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.widget.Toast;

public class Obstacle implements GameObjects{

    private Rect rectangle;
    private Rect rectangleTwo;
    private int color;

    private Bitmap idleImg;


    public Obstacle(int obstacleHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        // left,top,right,bottom
        rectangle = new Rect(0, startY, startX, startY + obstacleHeight);
        rectangleTwo = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + obstacleHeight);

        // levelDesign
        loadLevelAssets();




    }

    public boolean collisionDetected(Player player) {
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangleTwo, player.getRectangle());
    }

    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
        rectangleTwo.top += y;
        rectangleTwo.bottom += y;
    }



    public Rect getRectangle() {
        return rectangle;
    }

    private void loadLevelAssets() {
        BitmapFactory bitmapFactory = new BitmapFactory();
        switch (Constants.LEVEL_SELECTED) {
            case 0:
                idleImg = bitmapFactory.decodeResource(Constants.GAME_CONTEXT.getResources(), R.drawable.dirtslice03);
                break;
            case 1:
                idleImg = bitmapFactory.decodeResource(Constants.GAME_CONTEXT.getResources(), R.drawable.grassslice03);
                break;
            case 2:
                idleImg = bitmapFactory.decodeResource(Constants.GAME_CONTEXT.getResources(), R.drawable.sandslice03);
                break;
            case 3:
                idleImg = bitmapFactory.decodeResource(Constants.GAME_CONTEXT.getResources(), R.drawable.tundraslice03);
                break;
            case 4:
                idleImg = bitmapFactory.decodeResource(Constants.GAME_CONTEXT.getResources(), R.drawable.metalslice03);
                break;

        }
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);
        canvas.drawRect(rectangleTwo,paint);

        canvas.drawBitmap(idleImg, null, rectangle, new Paint());
        canvas.drawBitmap(idleImg, null, rectangleTwo, new Paint());
    }
}
