package com.g.pocketmal.ui.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class NumberInputFilter implements InputFilter {

    public static final int MAX_REWATCHING_NUMBER = 999;

    private int max;

    public NumberInputFilter(int max) {
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(max, input)) {
                return null;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean isInRange(int a, int b) {
        return a > 0 ? b >= 0 && b <= a : b >= a && b <= 0;
    }
}
