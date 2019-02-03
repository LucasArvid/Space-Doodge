package su.ju.arlu1695.projectgame;

public class LogicHandler {

    private Player player;
    private Obstacles obstacles;
    private boolean MOVING;

    public LogicHandler (Player player, Obstacles obstacles) {

        this.player = player;
        this.obstacles = obstacles;
        MOVING = false;


    }

    public boolean playerCollide() {
        for(int i = 0 ; i < obstacles.array.length; i++) {
            if (obstacles.intersects(player, obstacles.array[i]))
                return true;

        }
        return false;
    }

    public boolean insideScreen() {
        if (    (player.getTopPosX()) >= 0 &&
                (player.getTopPosX() + player.getWidth()) <= Constants.SCREEN_WIDTH &&
                (player.getTopPosY()) >= 0 &&
                (player.getTopPosY() + player.getHeight() <= Constants.SCREEN_HEIGHT)
            ) {
            MOVING = true;
            return true;
        }
        else {
            if(MOVING) {
                if (player.getVelocityX() != 0)
                    player.setVelocityX(0);
                else if (player.getVelocityY() != 0)
                    player.setVelocityY(0);
                MOVING = false;
            }
            return false;
        }

    }

    public void setMoving (boolean moving) {
        MOVING = moving;
    }
}
