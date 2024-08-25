package com.g.pocketmal.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.g.pocketmal.R;

public class BorderCardView extends CardView {

    private static final int[] attr = new int[]{R.attr.cardBorderDrawable};

    public BorderCardView(Context context) {
        super(context);
        init();
    }

    public BorderCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BorderCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        TypedArray typedArray = getContext().obtainStyledAttributes(0, attr);
        Drawable drawable = typedArray.getDrawable(0);
        typedArray.recycle();

        if (drawable == null) {
            return;
        }

        ImageView background = new ImageView(getContext());
        background.setBackground(drawable);

        addView(background);
    }
}
