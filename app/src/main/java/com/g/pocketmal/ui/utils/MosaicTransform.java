package com.g.pocketmal.ui.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

public class MosaicTransform implements Transformation {

    private final static float MAX_RADIUS = .07f;

    private float radius;

    public MosaicTransform() {
        this(MAX_RADIUS);
    }

    public MosaicTransform(float radius) {
        this.radius = radius;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap blurredBitmap = getMosaicsBitmap(source, radius);
        source.recycle();
        return blurredBitmap;
    }

    @Override
    public String key() {
        return "MosaicTransform (radius=" + radius + ")";
    }

    private Bitmap getMosaicsBitmap(Bitmap bmp, double percent) {

        int bmpW = bmp.getWidth();
        int bmpH = bmp.getHeight();
        Bitmap resultBmp = Bitmap.createBitmap(bmpW, bmpH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBmp);
        Paint paint = new Paint();
        double unit = percent == 0 ? bmpW : 1 / percent;
        double resultBmpW = bmpW / unit;
        double resultBmpH = bmpH / unit;
        for (int i = 0; i < resultBmpH; i++) {
            for (int j = 0; j < resultBmpW; j++) {
                int pickPointX = (int) (unit * (j + .5));
                int pickPointY = (int) (unit * (i + .5));
                int color = pickPointX >= bmpW || pickPointY >= bmpH ?
                        bmp.getPixel(bmpW / 2, bmpH / 2) : bmp.getPixel(pickPointX, pickPointY);
                paint.setColor(color);
                canvas.drawRect((int) (unit * j), (int) (unit * i),
                        (int) (unit * (j + 1)), (int) (unit * (i + 1)), paint);
            }
        }
        canvas.setBitmap(null);

        return resultBmp;
    }
}