package com.g.pocketmal.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class EventScrollView extends ScrollView {

    public interface OnScrollListener {
        void onScrollChanged(EventScrollView scrollView, int x, int y, int oldX, int oldY);
    }

    private OnScrollListener mOnScrollListener;

    public EventScrollView(Context context) {
        this(context, null, 0);
    }

    public EventScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

    public OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public void setOnScrollListener(OnScrollListener mOnEndScrollListener) {
        this.mOnScrollListener = mOnEndScrollListener;
    }

}
