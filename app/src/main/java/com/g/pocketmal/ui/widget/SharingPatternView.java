package com.g.pocketmal.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.g.pocketmal.R;

import static com.g.pocketmal.data.keyvalue.SharingPatternDispatcher.*;

public class SharingPatternView extends FrameLayout {

    private EditText value;

    private SharingPattern pattern;

    public SharingPatternView(Context context, SharingPattern pattern) {
        super(context);
        this.pattern = pattern;
        init();
    }

    public SharingPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SharingPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("InflateParams")
    private void init() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_sharing_pattern, null);

        value = view.findViewById(R.id.et_value);
        value.setText(pattern.getCurrentValue());

        ((TextView) view.findViewById(R.id.tv_name)).setText(pattern.getName());
        view.findViewById(R.id.tv_default_button)
                .setOnClickListener(v -> value.setText(pattern.getDefaultValue()));

        addView(view);
    }

    public String getKey() {
        return pattern.getKey();
    }

    public String getValue() {
        return value.getText().toString().trim();
    }
}
