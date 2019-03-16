package su.ju.arlu1695.projectgame.game.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {

    private Bitmap[] frames;
    private int frameIndex;

    private float frameTime;
    private long lastFrame;

    private boolean isPlaying = false;

    public boolean isPlaying()  {
        return isPlaying;
    }

    public void playing() {
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

    public void stop() {
        isPlaying = false;
    }

    public Animation(Bitmap[] frames, float animSpeed) {
        this.frames = frames;
        frameIndex = 0;
        frameTime = animSpeed/frames.length;

        lastFrame = System.currentTimeMillis();
    }

    // Scales the rect object to the size of the art asset to avoid stretched art
    private void scaleRect(Rect rect) {
        float whRatio = (float)(frames[frameIndex].getWidth())/frames[frameIndex].getHeight();
        if(rect.width() > rect.height()) {
            rect.left = rect.right - (int)(rect.height() * whRatio);
        } else
            rect.top = rect.bottom - (int)(rect.width() * (1/whRatio));
    }


    public void update() {
        if(!isPlaying)
            return;
        if(System.currentTimeMillis() - lastFrame > frameTime*1000) {
            frameIndex++;
            frameIndex = frameIndex >= frames.length ? 0 : frameIndex;
            lastFrame = System.currentTimeMillis();
        }
    }

    public void draw(Canvas canvas, Rect destination) {
        if (!isPlaying)
            return;

        scaleRect(destination);

        canvas.drawBitmap(frames[frameIndex], null, destination, new Paint());
    }


}
