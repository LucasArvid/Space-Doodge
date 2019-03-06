package su.ju.arlu1695.projectgame;

import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimationHandler {

    private Animation[] animations;
    private int animIndex = 0;

    public AnimationHandler(Animation[] animations) {
        this.animations = animations;

    }

    public void playAnimation(int index) {
        for (int i = 0; i < animations.length; i++) {
            if (i == index) {
                if (!animations[index].isPlaying())
                    animations[i].playing();
            } else
                animations[i].stop();
        }
        animIndex = index;
    }

    public void draw(Canvas canvas, Rect rect) {
        if(animations[animIndex].isPlaying())
            animations[animIndex].draw(canvas, rect);
    }

    public void update() {
        if(animations[animIndex].isPlaying())
            animations[animIndex].update();
    }
}
