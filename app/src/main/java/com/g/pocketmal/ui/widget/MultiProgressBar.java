package com.g.pocketmal.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.g.pocketmal.R;

import java.util.ArrayList;
import java.util.List;

public class MultiProgressBar extends View {

    private Paint rectanglePaint;

    private SparseArray<ProgressItem> progresses = new SparseArray<>();
    private int maxValue;

    public MultiProgressBar(Context context) {
        super(context);
        init();
    }

    public MultiProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        if(isInEditMode()){
            List<MultiProgressBar.ProgressItem> items = new ArrayList<>();
            items.add(new MultiProgressBar.ProgressItem(0, 35, getResources().getColor(R.color.in_progress_graph)));
            items.add(new MultiProgressBar.ProgressItem(1, 661, getResources().getColor(R.color.completed_graph)));
            items.add(new MultiProgressBar.ProgressItem(2, 13, getResources().getColor(R.color.on_hold_graph)));
            items.add(new MultiProgressBar.ProgressItem(3, 31, getResources().getColor(R.color.dropped_graph)));
            items.add(new MultiProgressBar.ProgressItem(4, 161, getResources().getColor(R.color.planned_graph)));

            setProgresses(items);
        }

        rectanglePaint = new Paint();
        rectanglePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        rectanglePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int viewWidth = getWidth();
        final int viewHeight = getHeight();

        final float ratio = viewWidth  / (float) maxValue;

        int leftOffset = 0;

        for (int i = 0; i < progresses.size(); i++) {

            ProgressItem progressItem = progresses.valueAt(i);

            rectanglePaint.setColor(progressItem.getColor());

            int rectWidth = (int) (ratio * progressItem.getValue());
            int rightEdge = i == progresses.size() - 1 ? viewWidth : leftOffset + rectWidth;

            canvas.drawRect(leftOffset, 0, rightEdge, viewHeight, rectanglePaint);

            leftOffset += rectWidth;
        }
    }

    public SparseArray<ProgressItem> getProgresses() {
        return progresses;
    }

    public void setProgresses(List<ProgressItem> progresses) {

        this.progresses = new SparseArray<>();
        this.maxValue = 0;

        for (ProgressItem progressItem : progresses) {
            this.progresses.put(progressItem.getId(), progressItem);
            this.maxValue += progressItem.getValue();
        }

        invalidate();
    }

    public void setProgress(int id, int newProgress) {
        ProgressItem item = this.progresses.get(id);
        this.maxValue = this.maxValue - item.getValue() + newProgress;
        item.setValue(newProgress);
        invalidate();
    }

    public void setColor(int id, int newColor) {
        ProgressItem item = this.progresses.get(id);
        item.setColor(newColor);
        invalidate();
    }

    public static class ProgressItem {

        private int id;
        private int value;
        private int color;

        public ProgressItem(int id, int value, int color) {
            this.id = id;
            this.value = value;
            this.color = color;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
