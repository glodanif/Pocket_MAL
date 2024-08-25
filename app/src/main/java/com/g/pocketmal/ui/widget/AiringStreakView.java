package com.g.pocketmal.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.g.pocketmal.R;

public class AiringStreakView extends View {

    private Paint paint;
    private TextPaint textPaint;

    private String startDate = "?";
    private String finishDate = "?";

    private int streakCircleRadius;
    private int streakThickness;
    private int textPadding;
    private int textShift;

    public AiringStreakView(Context context) {
        super(context);
        init();
    }

    public AiringStreakView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AiringStreakView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        streakCircleRadius = getResources().getDimensionPixelSize(R.dimen.streak_circle_radius);
        streakThickness = getResources().getDimensionPixelSize(R.dimen.streak_thickness);
        textPadding = getResources().getDimensionPixelSize(R.dimen.streak_text_padding);
        textShift = getResources().getDimensionPixelSize(R.dimen.streak_text_shift);

        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.textColorSecondary});
        int accentColor = typedArray.getColor(0, -1);
        typedArray.recycle();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setColor(accentColor);

        int textColor = new TextView(getContext()).getTextColors().getDefaultColor();
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        textPaint.setTextSize(getResources().getDimension(R.dimen.streak_text_size));
        textPaint.setColor(textColor);
    }

    public void setDates(String startDate, String finishDate) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float textHeight = textPaint.descent() + textPaint.ascent() + textPadding / 2;
        int startDateWidth = (int) textPaint.measureText(startDate);
        int finishDateWidth = (int) textPaint.measureText(finishDate);

        if (!startDate.equals("?") && !finishDate.equals("?") && startDate.equals(finishDate)) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, streakCircleRadius, paint);
            canvas.drawText(startDate, getWidth() / 2 - startDateWidth - textShift - streakCircleRadius, (getHeight() - textHeight) / 2, textPaint);
            return;
        }

        int top = getHeight() / 2 - streakThickness / 2;
        int bottom = getHeight() / 2 + streakThickness / 2;

        int lineStart = streakCircleRadius + startDateWidth + textShift;
        int lineEnd = getWidth() - streakCircleRadius - finishDateWidth - textShift;

        canvas.drawRect(lineStart, top, lineEnd, bottom, paint);

        canvas.drawText(startDate, 0, (getHeight() - textHeight) / 2, textPaint);
        canvas.drawText(finishDate, getWidth() - finishDateWidth, (getHeight() - textHeight) / 2, textPaint);

        if (!"?".equals(startDate)) {
            canvas.drawCircle(streakCircleRadius + startDateWidth + textShift, getHeight() / 2, streakCircleRadius, paint);
        }
        if (!"?".equals(finishDate)) {
            canvas.drawCircle(getWidth() - streakCircleRadius - finishDateWidth - textShift, getHeight() / 2, streakCircleRadius, paint);
        }
    }
}
