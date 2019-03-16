package su.ju.arlu1695.projectgame.game.handlers;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

import su.ju.arlu1695.projectgame.game.scenes.GameplayScene;
import su.ju.arlu1695.projectgame.game.scenes.GameplaySceneOnline;
import su.ju.arlu1695.projectgame.interfaces.Scene;

public class SceneHandler {

    private ArrayList<Scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE;

    public SceneHandler(String mode, String gameId,String me) {
        ACTIVE_SCENE = 0;

        if (mode.equals("online"))
            scenes.add(new GameplaySceneOnline(gameId,me));
        else
            scenes.add(new GameplayScene(me));
    }

    public void recieveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).recieveTouch(event);
    }

    public void update() {
        scenes.get(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }

}
