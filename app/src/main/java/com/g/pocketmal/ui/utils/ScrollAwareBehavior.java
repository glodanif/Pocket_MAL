package com.g.pocketmal.ui.utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.g.pocketmal.R;

public class ScrollAwareBehavior extends CoordinatorLayout.Behavior<TextView> {

    private Animation openAnimation;
    private Animation closeAnimation;

    private boolean isAnimationRunning = false;
    private boolean isOpened = true;

    public ScrollAwareBehavior(Context context, AttributeSet attrs) {
        init(context);
    }

    private void init(Context context) {

        openAnimation = AnimationUtils.loadAnimation(context, R.anim.popup_open_sliding);
        openAnimation.setFillAfter(true);
        closeAnimation = AnimationUtils.loadAnimation(context, R.anim.popup_close_sliding);
        closeAnimation.setFillAfter(true);

        openAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimationRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationRunning = false;
                isOpened = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimationRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationRunning = false;
                isOpened = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean onStartNestedScroll(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull final TextView child,
                                       @NonNull final View directTargetChild, @NonNull final View target, final int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(@NonNull final CoordinatorLayout coordinatorLayout,
                               @NonNull final TextView child, @NonNull final View target,
                               final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if (isAnimationRunning) {
            return;
        }

        if (dyConsumed > 0 && isOpened) {
            child.startAnimation(closeAnimation);
        } else if (dyConsumed < 0 && !isOpened) {
            child.startAnimation(openAnimation);
        }
    }
}