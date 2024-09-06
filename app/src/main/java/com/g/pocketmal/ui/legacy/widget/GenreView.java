package com.g.pocketmal.ui.legacy.widget;

import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.g.pocketmal.R;

public class GenreView extends AppCompatTextView {

    public GenreView(Context context, int id, String name) {
        super(context);

        int margins = getResources().getDimensionPixelSize(R.dimen.hashtag_margin);
        int sidePadding = getResources().getDimensionPixelSize(R.dimen.hashtag_startend_padding);
        int topBottomPadding = getResources().getDimensionPixelSize(R.dimen.hashtag_topbottom_padding);

        setGravity(Gravity.CENTER);
        setTextColor(Color.WHITE);
        setBackground(getResources().getDrawable(R.drawable.blue_button));
        setPadding(sidePadding, topBottomPadding, sidePadding, topBottomPadding);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(margins, margins, margins, margins);
        setLayoutParams(params);

        setText(name);
        setTag(id);
    }
}
