/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2019 Vasyl Glodan
 * Copyright (c) 2015 greenfrvr
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.g.pocketmal.ui.legacy.widget.hashtagview;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.TextView;

import com.g.pocketmal.R;

/**
 * Object representing HashtagView item model.
 *
 * @param <T> custom data model
 */
class ItemData<T> implements Comparable<ItemData> {
    protected T data;

    protected View view;
    protected float width;
    protected boolean isSelected;

    public ItemData(T data) {
        this.data = data;
    }

    void setText(CharSequence charSequence) {
        ((TextView) view.findViewById(R.id.text)).setText(charSequence);
    }

    void displaySelection(int left, int leftSelected, int right, int rightSelected) {
        ((TextView) view.findViewById(R.id.text)).setCompoundDrawablesWithIntrinsicBounds(isSelected ? leftSelected : left, 0, isSelected ? rightSelected : right, 0);
        view.setSelected(isSelected);
        view.invalidate();
    }

    void select(int left, int leftSelected, int right, int rightSelected) {
        isSelected = !isSelected;
        displaySelection(left, leftSelected, right, rightSelected);
    }

    void decorateText(HashtagView.DataTransform<T> transformer) {
        if (transformer instanceof HashtagView.DataStateTransform) {
            if (isSelected) {
                setText(((HashtagView.DataStateTransform<T>) transformer).prepareSelected(data));
            } else {
                setText(transformer.prepare(data));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ItemData && this.data.equals(((ItemData) o).data);
    }

    @Override
    public String toString() {
        return String.format("Item data: title - %s, width - %f", data.toString(), width);
    }

    @Override
    public int compareTo(@NonNull ItemData another) {
        if (width == another.width) return 0;
        return width < another.width ? 1 : -1;
    }
}