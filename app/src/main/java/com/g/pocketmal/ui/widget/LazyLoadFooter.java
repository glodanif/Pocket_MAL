package com.g.pocketmal.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.g.pocketmal.R;

public class LazyLoadFooter extends FrameLayout {

    private ProgressBar progress;

    public LazyLoadFooter(Context context) {
        super(context);
        init();
    }

    public LazyLoadFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LazyLoadFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        View lazyLoadFooter = inflate(getContext(), R.layout.footer_lazy_load, null);
        progress = lazyLoadFooter.findViewById(R.id.pb_lazy_load_progress);

        addView(lazyLoadFooter);
    }

    public void setProgressVisible(boolean isVisible) {
        progress.setVisibility(isVisible ? VISIBLE : GONE);
    }
}
