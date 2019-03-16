package su.ju.arlu1695.projectgame.game.handlers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import su.ju.arlu1695.projectgame.game.data.Obstacle;
import su.ju.arlu1695.projectgame.game.data.Player;
import su.ju.arlu1695.projectgame.R;
import su.ju.arlu1695.projectgame.utils.Constants;

public class ObstacleHandler {

    private ArrayList<Obstacle> obstacleArray;
    private int obstacleGap;
    private int obstacleHeight;
    private int playerGap;
    private int color;

    private long startTime;
    private long initTime;

    private int score = 0;

    public ObstacleHandler (int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.obstacleGap = obstacleGap;
        this.playerGap = playerGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        obstacleArray = new ArrayList<>();

        startTime = initTime = System.currentTimeMillis();

        populateObstacleArray();

    }

    public boolean collisionDetected(Player player) {
        for (Obstacle ob : obstacleArray) {
            if (ob.collisionDetected(player))
                return true;
        }
        return false;
    }

    private void populateObstacleArray() {
        int currentY = -5 * Constants.SCREEN_HEIGHT/4;
        while(currentY < 0) {
            int xStart = (int)(Math.random()* (Constants.SCREEN_WIDTH - playerGap));
            obstacleArray.add(new Obstacle(obstacleHeight, color, xStart, currentY, playerGap));
            currentY += obstacleHeight + obstacleGap;
        }
    }

    /*
        Increment the obstacles downwards and make sure that the player gap is
        not generated outside the game screen.
        score++ when an obstacle has left the game screen
     */
    public void update() {
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float velocity = (float)(Math.sqrt(1 + (startTime - initTime)/2000.0))*Constants.SCREEN_HEIGHT/(10000.0f);
        for (Obstacle ob : obstacleArray ) {
            ob.incrementY(velocity * elapsedTime);
        }
        if (obstacleArray.get(obstacleArray.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int)(Math.random()* (Constants.SCREEN_WIDTH - playerGap));
            obstacleArray.add(0, new Obstacle(obstacleHeight, color, xStart,
                              obstacleArray.get(0).getRectangle().top - obstacleHeight - obstacleGap , playerGap));
            obstacleArray.remove(obstacleArray.size() - 1);
            score ++;
        }


    }

    public void draw(Canvas canvas) {
        for (Obstacle ob : obstacleArray)
            ob.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.WHITE);
        canvas.drawText(Constants.GAME_CONTEXT.getResources().getString(R.string.score)+ ": " + score,50, 100, paint);
    }

    public int getScore() {
        return score;
    }
}
