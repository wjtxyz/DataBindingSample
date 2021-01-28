package com.xxx.databindingsample1.adapter;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

@BindingMethods(
        @BindingMethod(type = TextView.class, attribute = "myText", method = "setText")
)
public class TextViewBindingAdapter {

    @BindingAdapter(value = {"colorText", "colorStart", "colorEnd", "colorTextColor"}, requireAll = false)
    public static void setColorText(TextView textView, CharSequence colorText, int colorStart, int colorEnd, int colorTextColor) {
        if (colorText != null) {
            SpannableString spannableString = new SpannableString(colorText);
            spannableString.setSpan(new ForegroundColorSpan(colorTextColor), Math.min(Math.min(colorStart, colorEnd), colorText.length()), Math.min(Math.max(colorStart, colorEnd), colorText.length()), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        } else {
            textView.setText(colorText);
        }
    }
}
