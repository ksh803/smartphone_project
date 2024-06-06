package kr.ac.project.Fragment;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class DotDrawable extends Drawable {

    private final Paint paint;

    public DotDrawable(int color) {
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        int radius = Math.min(canvas.getWidth(), canvas.getHeight()) / 10;
        int x = canvas.getWidth() / 2;
        int y = canvas.getHeight() / 2;
        canvas.drawCircle(x, y, radius, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}