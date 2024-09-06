package com.g.pocketmal.ui.legacy.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.g.pocketmal.R;
import com.g.pocketmal.data.database.model.RelatedTitle;

@SuppressLint("ViewConstructor")
public class RelatedTitleView extends AppCompatTextView {

    private RelatedTitle relatedTitle;

    public RelatedTitleView(Context context, RelatedTitle relatedTitle) {
        super(context);
        this.relatedTitle = relatedTitle;
        init();
    }

    public RelatedTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelatedTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("ResourceType")
    private void init() {

        Resources resources = getResources();

        int sidePadding = (int) resources.getDimension(R.dimen.related_padding_side);
        int bigPadding = (int) resources.getDimension(R.dimen.related_big_padding);
        int smallPadding = (int) resources.getDimension(R.dimen.related_little_padding);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedValue typedValue = new TypedValue();
        int[] attr = new int[]{android.R.attr.textColorTertiary, androidx.appcompat.R.attr.colorAccent, android.R.attr.selectableItemBackground};
        TypedArray typedArray = getContext().obtainStyledAttributes(typedValue.data, attr);
        int textColor = typedArray.getColor(0, -1);
        int linkColor = typedArray.getColor(1, -1);
        Drawable background = typedArray.getDrawable(2);
        typedArray.recycle();

        setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        setBackground(background);

        setTextColor(relatedTitle.isLink() ? linkColor : textColor);
        setText(Html.fromHtml(relatedTitle.getName()));
        setPadding(sidePadding,
                relatedTitle.isLink() ? smallPadding : bigPadding,
                sidePadding, smallPadding);
    }
}
