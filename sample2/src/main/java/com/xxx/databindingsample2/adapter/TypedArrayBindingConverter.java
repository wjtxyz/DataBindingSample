package com.xxx.databindingsample2.adapter;

import android.content.res.TypedArray;
import android.util.TypedValue;

import androidx.databinding.BindingConversion;

import java.util.stream.IntStream;

import static android.util.TypedValue.TYPE_FIRST_INT;
import static android.util.TypedValue.TYPE_FLOAT;
import static android.util.TypedValue.TYPE_LAST_INT;
import static android.util.TypedValue.TYPE_REFERENCE;
import static android.util.TypedValue.TYPE_STRING;

public class TypedArrayBindingConverter {
    @BindingConversion
    public static Object[] toArray(TypedArray typedArray) {
        if (typedArray == null) {
            return null;
        }
        try {
            if (typedArray.length() > 0) {
                final TypedValue typedValue = typedArray.peekValue(0);
                if (typedValue.type >= TYPE_FIRST_INT && typedValue.type <= TYPE_LAST_INT) {
                    return IntStream.range(0, typedArray.length()).mapToObj(s -> typedArray.getInt(s, 0)).toArray(Integer[]::new);
                } else if (typedValue.type == TYPE_FLOAT) {
                    return IntStream.range(0, typedArray.length()).mapToObj(s -> typedArray.getFloat(s, 0f)).toArray(Float[]::new);
                } else if (typedValue.type == TYPE_REFERENCE) {
                    return IntStream.range(0, typedArray.length()).mapToObj(s -> typedArray.getResources().obtainTypedArray(typedArray.getResourceId(s, 0))).toArray(TypedArray[]::new);
                } else if (typedValue.type == TYPE_STRING && typedValue.resourceId != 0) {
                    return IntStream.range(0, typedArray.length()).mapToObj(s -> typedArray.getResourceId(s, 0)).toArray(Integer[]::new);
                }
            }
            return IntStream.range(0, typedArray.length()).mapToObj(typedArray::getString).toArray(String[]::new);
        } finally {
            typedArray.recycle();
        }
    }
}
