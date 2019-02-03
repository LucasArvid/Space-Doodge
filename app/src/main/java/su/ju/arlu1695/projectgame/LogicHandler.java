package su.ju.arlu1695.projectgame;

public class LogicHandler {

    private Player player;
    private Obstacles obstacles;

    public LogicHandler (Player player, Obstacles obstacles) {

        this.player = player;
        this.obstacles = obstacles;

    }

    public boolean playerCollide() {
        for(int i = 0 ; i < obstacles.array.length; i++) {
            if (obstacles.intersects(player, obstacles.array[i]))
                return true;

        }
        return false;
    }
}
