package com.g.pocketmal.ui.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.g.pocketmal.R;

import java.util.List;

public class GenresLayout extends LinearLayout {

    public GenresLayout(Context context) {
        super(context);
    }

    public GenresLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GenresLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void populateText(List<GenreView> views) {

        int margins = getResources().getDimensionPixelSize(R.dimen.hashtag_startend_padding);

        removeAllViews();

        LinearLayout row = createRow();
        addView(row);

        int widthSoFar = 0;

        for (int i = 0; i < views.size(); i++) {

            GenreView child = views.get(i);

            child.measure(0, 0);
            widthSoFar += child.getMeasuredWidth() + margins * 2;

            if (widthSoFar >= getWidth()) {
                row = createRow();
                addView(row);
                widthSoFar = 0;
            }

            row.addView(child);
        }
    }

    private LinearLayout createRow() {
        LinearLayout row = new LinearLayout(getContext());
        row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        row.setGravity(Gravity.CENTER);
        row.setOrientation(LinearLayout.HORIZONTAL);
        return row;
    }
}
