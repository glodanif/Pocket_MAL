package com.g.pocketmal.ui.legacy.widget;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.preference.ListPreference;
import androidx.annotation.NonNull;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MultiSelectListPreference extends ListPreference {

    private static final String SEPARATOR = "|";

    private boolean[] checkedEntryIndexes;

    public MultiSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiSelectListPreference(Context context) {
        super(context);
    }

    @Override
    public void setEntries(CharSequence[] entries) {
        super.setEntries(entries);
        updateCheckedEntryIndexes();
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        updateCheckedEntryIndexes();
    }

    @Override
    protected void onPrepareDialogBuilder(@NonNull Builder builder) {
        updateCheckedEntryIndexes();
        builder.setMultiChoiceItems(getEntries(), checkedEntryIndexes,
                (dialog, which, isChecked) -> {
                    checkedEntryIndexes[which] = isChecked;
                });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            CharSequence[] entryValues = getEntryValues();
            ArrayList<CharSequence> checkedValues = new ArrayList<>();
            for (int i = 0; i < entryValues.length; i++) {
                if (checkedEntryIndexes[i]) {
                    checkedValues.add(entryValues[i]);
                }
            }
            String val = toPersistedPreferenceValue(checkedValues
                    .toArray(new CharSequence[checkedValues.size()]));
            if (callChangeListener(val)) {
                setValue(val);
            }
        }
    }

    public static String[] fromPersistedPreferenceValue(String val) {
        return (val == null || val.isEmpty()) ? new String[0] : val.split("\\" + SEPARATOR);
    }

    public static String toPersistedPreferenceValue(CharSequence... entryKeys) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entryKeys.length; i++) {
            sb.append(entryKeys[i]);
            if (i < entryKeys.length - 1) {
                sb.append(SEPARATOR);
            }
        }
        return sb.toString();
    }

    public CharSequence[] getCheckedEntries() {
        CharSequence[] entries = getEntries();
        ArrayList<CharSequence> checkedEntries = new ArrayList<>();
        for (int i = 0; i < entries.length; i++) {
            if (checkedEntryIndexes[i]) {
                checkedEntries.add(entries[i]);
            }
        }
        return checkedEntries.toArray(new CharSequence[checkedEntries.size()]);
    }

    private void updateCheckedEntryIndexes() {
        CharSequence[] entryValues = getEntryValues();
        checkedEntryIndexes = new boolean[entryValues.length];
        String val = getValue();
        if (val != null) {
            Set<String> checkedEntryValues = new HashSet<>(Arrays.asList(fromPersistedPreferenceValue(val)));
            for (int i = 0; i < entryValues.length; i++) {
                checkedEntryIndexes[i] = checkedEntryValues.contains(entryValues[i].toString());
            }
        }
    }
}